package com.sn.gameelectricity.app.network;


import com.sn.gameelectricity.app.App;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpFactory {

    private volatile static HttpFactory mHttpFactory;
    private OkHttpClient mOkHttpClient;
    private static List<Interceptor> okHttpInterceptors = new ArrayList<>(6);

    //连接超时时间
    private static final int CONNECT_TIME_OUT = 10;
    //读写超时时间
    private static final int IO_TIME_OUT = 30;

    private HttpFactory() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .readTimeout(IO_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(IO_TIME_OUT, TimeUnit.SECONDS)
//                .dns(new HttpDns())
//                .addInterceptor(BaseInterceptor.HeaderInterceptor())
                .addInterceptor(BaseInterceptor.LogInterceptor())
//                .addInterceptor(new TokenOutInterceptor())
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(BaseInterceptor.UserAgentInterceptor(App.instance));
        if (okHttpInterceptors != null && okHttpInterceptors.size() > 0) {
            for (Interceptor interceptor : okHttpInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        mOkHttpClient = builder.build();

    }

    //必须在Application里调用，不然无效
    public static void addOkhttpInterceptor(Interceptor interceptor) {
        okHttpInterceptors.add(interceptor);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static HttpFactory getInstance() {
        if (mHttpFactory == null) {
            synchronized (HttpFactory.class) {
                if (mHttpFactory == null) {
                    mHttpFactory = new HttpFactory();
                }
            }
        }
        return mHttpFactory;
    }

    //通用http请求入口
    public ApiService APINew(String baseUrl) {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())//添加rxjava转换器
                .client(mOkHttpClient)
                .build();
        return mRetrofit.create(ApiService.class);
    }

    public <T> ObservableTransformer<T, T> setThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    private SSLSocketFactory setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
