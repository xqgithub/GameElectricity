package com.sn.gameelectricity.app.util.permission;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.permissionx.guolindev.PermissionX;

/**
 * @ProjectName: GK-Android
 * @Package: org.sugram.foundation.permission
 * @ClassName: PermissionRequestManager
 * @Description: 权限请求
 * @Author: czhen
 * @CreateDate: 2020/10/14 15:50
 */
public class PermissionRequestManager {

    public static RequestBuilder request(FragmentActivity activity, String... permissions) {
        return new RequestBuilder(PermissionX.init(activity).permissions(permissions), activity);
    }

    public static RequestBuilder request(Fragment fragment, String... permissions) {
        return new RequestBuilder(PermissionX.init(fragment).permissions(permissions), fragment.requireContext());
    }
}
