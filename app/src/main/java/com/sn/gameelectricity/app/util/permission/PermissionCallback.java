package com.sn.gameelectricity.app.util.permission;

import java.util.List;

/**
 * @ProjectName: GK-Android
 * @Package: org.sugram.foundation.permission
 * @ClassName: PermissionCallback
 * @Description: java类作用描述
 * @Author: czhen
 * @CreateDate: 2020/10/14 16:15
 */
public interface PermissionCallback {

    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList);
}
