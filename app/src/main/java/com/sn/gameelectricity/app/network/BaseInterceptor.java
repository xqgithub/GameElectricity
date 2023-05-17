package com.sn.gameelectricity.app.network;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class BaseInterceptor {

    private static final String USER_AGENT_HEADER_NAME = "User-Agent";

    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                LogUtils.e("321", "" + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    public static Interceptor HeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request mRequest = chain.request();
                //在这里你可以做一些想做的事
                //比如添加header  待扩展。。。。
                return chain.proceed(mRequest);
            }
        };
    }


    public static Interceptor UserAgentInterceptor(final Context context) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
//                String ua = CommonUtil.getSysUserAgent(context) + CommonUtil.getDefaultUserAgent(context);
//                //防止有中文
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0, length = ua.length(); i < length; i++) {
//                    char c = ua.charAt(i);
//                    if (c <= '\u001f' || c >= '\u007f') {
//                        sb.append(String.format("\\u%04x", (int) c));
//                        sb.append(c);
//                    } else {
//                    }
//                }
//                ua = sb.toString();
                String appToken = TokenInterceptorHelper.getInstance().getAppToken(context);
                LogUtils.e("appToken", appToken);
                Request originalRequest = chain.request();
                Request requestWithUserAgent = originalRequest.newBuilder()
                        .addHeader("app-authc", appToken)   //加入token
                        .build();

                return chain.proceed(requestWithUserAgent);
            }
        };
    }
}
