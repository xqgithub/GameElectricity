package com.sn.gameelectricity.app.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.DrawableRes;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.BitmapFactory.decodeFile;

public class BitmapUtil {
    private static final String TAG = "ThumbnailUtil";

    public static boolean saveBitmapToFile(Bitmap bitmap, String destPath) {
        return saveBitmapToFile(bitmap, destPath, 100);
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String destPath, int quality) {
        return saveBitmapToFile(bitmap, destPath, quality, getBitmapFormat(bitmap));
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String destPath, int quality, Bitmap.CompressFormat format) {
        if (bitmap == null || bitmap.isRecycled()) {
//            LogUtil.w(TAG, "bitmap == null || bitmap.isRecycled()");
            return false;
        }
        File destFile = new File(destPath);
        File tmpFile = new File(destPath + ".tmp");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tmpFile);
            if (bitmap.compress(format, quality, out)) {
                return tmpFile.renameTo(destFile);
            }
        } catch (IOException e) {
//            LogUtil.w(TAG, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
//                    LogUtil.w(TAG, e);
                }
            }
        }
        return false;
    }

    private static Bitmap.CompressFormat getBitmapFormat(Bitmap bitmap) {
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
    }

    /**
     * 获取bitmap后缀
     *
     * @return
     */
    public static String getBitmapSuffix(Bitmap bitmap) {
        if (getBitmapFormat(bitmap) == Bitmap.CompressFormat.PNG) {
            return ".png";
        } else {
            return ".jpeg";
        }
    }

    // 回收图片资源
    public static void recycler(Bitmap bmp) {
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
    }

    /**
     * 按照指定大小加载bitmap
     *
     * @param path      图片路径
     * @param dstWidth  目标宽度
     * @param dstHeight 目标高度
     * @return
     */
    public static Bitmap decodeFromFile(String path, int dstWidth, int dstHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(path, options);
        options.inSampleSize = computeInSampleSize(options, dstWidth, dstHeight);
        options.inJustDecodeBounds = false;
        return decodeFile(path, options);
    }

    /**
     * 从资源文件中加载bitmap
     */
    public static Bitmap decodeFromResource(Resources resources, @DrawableRes int resId) {
        return decodeFromResource(resources, resId, 100, 100);
    }

    public static Bitmap decodeFromResource(Resources resources, @DrawableRes int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = computeInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    private static int computeInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 获取图片宽高
     *
     * @param path 图片路径
     * @return
     */
    public static int[] getBitmapSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(path, options);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

}
