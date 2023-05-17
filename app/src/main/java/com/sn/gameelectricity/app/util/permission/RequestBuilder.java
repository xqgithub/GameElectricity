package com.sn.gameelectricity.app.util.permission;

import android.Manifest;
import android.content.Context;
import android.view.View;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.permissionx.guolindev.request.PermissionBuilder;
import com.sn.gameelectricity.app.util.SharedpreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: GK-Android
 * @Package: org.sugram.foundation.permission
 * @ClassName: RequestBuilder
 * @Description: java类作用描述
 * @Author: czhen
 * @CreateDate: 2020/10/14 16:09
 */
public class RequestBuilder {
    private static final long TIME = 604800000;

    private PermissionBuilder mPermissionBuilder;
    private Context mContext;

    public RequestBuilder(PermissionBuilder permissionBuilder, Context context) {
        this.mPermissionBuilder = permissionBuilder;
        this.mContext = context;
    }


    public RequestBuilder toSetting() {
        mPermissionBuilder.onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
            @Override
            public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                scope.showRequestReasonDialog(new PermissionExplainRequestDialog(mContext, deniedList));
            }
        });
        mPermissionBuilder.onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                if (deniedList.contains(Manifest.permission.READ_CONTACTS) || deniedList.contains(Manifest.permission.READ_PHONE_STATE)) {
                    long time = SharedpreferencesUtil.getLong(mContext, deniedList.toString(), 0);
                    if (System.currentTimeMillis() - time >= TIME) {
                        scope.showForwardToSettingsDialog(new PermissionRationaleDialog(mContext, deniedList));
                        SharedpreferencesUtil.putLong(mContext, deniedList.toString(), System.currentTimeMillis());
                    }
                } else {
                    scope.showForwardToSettingsDialog(new PermissionRationaleDialog(mContext, deniedList));
                }
            }
        });

        return this;
    }

    public RequestBuilder toSetting(final String message, final String positiveText, final String negativeText) {
        mPermissionBuilder.onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                scope.showForwardToSettingsDialog(deniedList, message, positiveText, negativeText);

            }
        });
        return this;
    }


    public RequestBuilder toSetting(final String message, final String positiveText) {
        mPermissionBuilder.onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                scope.showForwardToSettingsDialog(deniedList, message, positiveText);
            }
        });
        return this;
    }

    public void request(final PermissionCallback callback) {
        request(callback, false);
    }

    public void request(final PermissionCallback callback, boolean initiative) {
        final List<String> deniedList = new ArrayList<>();
        for (String permission : mPermissionBuilder.normalPermissions) {
            if (!PermissionX.isGranted(mContext, permission)) {
                deniedList.add(permission);
            }
        }

        if (initiative) {
            long time = SharedpreferencesUtil.getLong(mContext, deniedList.toString(), 0);
            if (System.currentTimeMillis() - time >= TIME) {
                SharedpreferencesUtil.putLong(mContext, deniedList.toString(), System.currentTimeMillis());
                request(callback, deniedList);
            } else {
                callback.onResult(deniedList.isEmpty(), new ArrayList<String>(), deniedList);
            }
        } else {
            request(callback, deniedList);
        }
    }

    private void request(final PermissionCallback callback, final List<String> deniedList) {
        if (deniedList.size() > 0) {
            PermissionRequestBeforeDialog dialog = new PermissionRequestBeforeDialog(mContext, deniedList, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestInternal(callback);
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onResult(false, deniedList, deniedList);
                    }
                }
            });
            dialog.show();
        } else {
            requestInternal(callback);
        }
    }

    public void requestInternal(final PermissionCallback callback) {
        mPermissionBuilder.request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (callback != null) {
                    callback.onResult(allGranted, grantedList, deniedList);
                }
            }
        });
    }
}
