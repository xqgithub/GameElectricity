package com.sn.gameelectricity.app.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.sn.gameelectricity.app.App;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IsInstallApp {

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 检测是否安装支付宝
     *
     * @return * @param context
     */
    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;

    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible() {
        final PackageManager packageManager = App.instance.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvaolable() {
        final PackageManager packageManager = App.instance.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断新浪微博APP是否安装
     *
     * @return
     */
    public static boolean isWeiBoInstalled() {
        PackageManager pm = App.instance.getPackageManager();
        //系统应用uid从1000开始，用户应用uid从10000(FIRST_APPLICATION_UID)开始，直接合并查询
        for (int i = 10000; i <= 11000; i++) {
            try {
                String[] apps = pm.getPackagesForUid(i);
                if (apps != null) {
                    for (String app : apps) {
                        PackageInfo info = pm.getPackageInfo(app, 0);
                        if (info != null && info.packageName.equals("com.sina.weibo")) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}