package com.sn.gameelectricity.app.network;

import android.content.Intent;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sn.gameelectricity.app.App;
import com.sn.gameelectricity.app.util.CacheUtil;
import com.sn.gameelectricity.ui.activity.MainActivity;
import com.sn.gameelectricity.ui.activity.login.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            try {
                String result = buffer.clone().readString(StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(result);
                int code = jsonObject.getInt("code");
                LogUtils.e("88888888888888=1111111===" + code);
                if (code == 9 || code == 8 || code == 805371949) {
                    ToastUtils.showShort("登录身份失效，请重新登录");
                    CacheUtil.INSTANCE.setIsLogin(false);
                    ActivityUtils.finishActivity(MainActivity.class);
                    Intent intent = new Intent(App.instance, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.instance.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
 