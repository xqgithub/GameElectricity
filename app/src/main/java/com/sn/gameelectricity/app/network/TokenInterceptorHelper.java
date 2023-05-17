package com.sn.gameelectricity.app.network;

import android.content.Context;
import android.text.TextUtils;

import com.sn.gameelectricity.app.util.SharedpreferencesUtil;


public class TokenInterceptorHelper {
    private static TokenInterceptorHelper INSTANCE;

    private static final String DEFAULT_APP_TOKEN = "Nzc2MzM4NDg0NDE";

    private String appToken = DEFAULT_APP_TOKEN;

    private TokenInterceptorHelper() {

    }

    public static TokenInterceptorHelper getInstance() {
        TokenInterceptorHelper tempInstance = INSTANCE;
        if (null == tempInstance) {
            synchronized (TokenInterceptorHelper.class) {
                tempInstance = INSTANCE;
                if (null == tempInstance) {
                    INSTANCE = new TokenInterceptorHelper();
                    tempInstance = INSTANCE;
                }
            }
        }
        return tempInstance;
    }

    public void saveAppToken(Context context, String appToken) {
        this.appToken = appToken;
        SharedpreferencesUtil.putToken(context, appToken);
    }

    public String getAppToken(Context context) {
        if (TextUtils.equals(appToken, DEFAULT_APP_TOKEN) || TextUtils.isEmpty(appToken)) {
            appToken = SharedpreferencesUtil.getString(context, "appToken", DEFAULT_APP_TOKEN);
        }
        return appToken;
    }

    public void clearAppToken() {
        appToken = DEFAULT_APP_TOKEN;
    }
}
