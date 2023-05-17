package com.sn.gameelectricity.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import com.sn.gameelectricity.app.util.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 迁移过来的图库压缩工具类
 * Created by Seaky
 */

public class PictureUtil {


    public static void getImageFileWithPNG(Context context, String key, String filePath, String destDirPath, boolean thumb) {
        File file = new File(destDirPath, key);
        Bitmap bm = getSmallBitmap(filePath, thumb);
        try {
            BitmapUtils.writeBitmapToUri(context,
                    bm,
                    Uri.fromFile(file),
                    Bitmap.CompressFormat.PNG, 60);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据路径获的图片并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, boolean thumb) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        if (thumb) {
            options.inSampleSize = calculateInSampleSize(options, 320, 480);
        } else {
            options.inSampleSize = calculateInSampleSize(options, 960, 1440);
        }


        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap localBitmap1 = BitmapFactory.decodeFile(filePath, options);
        int j = readPictureDegree(filePath);
        Bitmap localBitmap2 = null;
        // 旋转图片
        if ((localBitmap1 != null) && (j != 0)) {
            localBitmap2 = rotaingImageView(j, localBitmap1);
            localBitmap1.recycle();
            localBitmap1 = null;
            return localBitmap2;
        }

        return localBitmap1;
    }

    // 判断图片角度
    public static int readPictureDegree(String paramString) {
        int i = 0;
        try {
            ExifInterface localExifInterface = new ExifInterface(paramString);
            int j = localExifInterface.getAttributeInt("Orientation", 1);
            switch (j) {
                case 6:
                    i = 90;
                    break;
                case 3:
                    i = 180;
                    break;
                case 8:
                    i = 270;
                case 4:
                case 5:
                case 7:
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return i;
    }

    // 旋转图片
    public static Bitmap rotaingImageView(int paramInt, Bitmap paramBitmap) {
        Matrix localMatrix = new Matrix();
        localMatrix.postRotate(paramInt);
        Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0,
                paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix,
                true);
        return localBitmap;
    }
}
