package com.sn.gameelectricity.app.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nickyang on 2018/3/21.
 */

public class FileProvider7 {

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    private static Uri getUriForFile24(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".android7.fileprovider", file);

        return fileUri;
    }


    public static void setIntentData(Context context, Intent intent, String type, File file, boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    public static void setIntentData(Context context, Intent intent, File file, boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.putExtra(Intent.EXTRA_STREAM, getUriForFile(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
    }

    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        setIntentData(context, intent, "application/vnd.android.package-archive", apkFile, true);
        context.startActivity(intent);
        //如果不加，最后不会提示完成、打开。
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static FileInfo getFileInfoFormContentUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme.equals("file")) {
            String filePath = uri.getPath();
            File file = new File(filePath);
            if (!file.exists()) return null;

            FileInfo fileInfo = new FileInfo();
            fileInfo.fullPath = file.getAbsolutePath();
            fileInfo.name = file.getName();
            fileInfo.size = file.length();
            return fileInfo;
        } else if (scheme.equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (null == cursor) {
                return null;
            }
            FileInfo file = new FileInfo();
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            if (cursor.moveToFirst()) {
                file.fullPath = uri.toString();
                file.name = cursor.getString(nameIndex);
                file.size = cursor.getLong(sizeIndex);
                cursor.close();
                if (TextUtils.isEmpty(file.name)) { //有可能取不到文件名字
                    String filePath = getPath(context, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        File f = new File(filePath);
                        if (f.exists()) {
                            file.name = f.getName();
                        }
                    }
                }

                return file;
            }
        }
        return null;
    }

    public static class FileInfo {
        public String fullPath; // 全路径和名称
        public String name;  //文件的名字，7.0上获取分享文件的路径没有意义，只能通过uri访问到文件
        public long size;    //文件的大小
    }

    /**
     * @param context 上下文对象
     * @param uri     当前相册照片的Uri
     * @return 解析后的Uri对应的String
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        return getPath(context, uri, true);
    }

    /**
     * @param context 上下文对象
     * @param uri     当前相册照片的Uri
     * @return 解析后的Uri对应的String
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri, boolean pathHeadEnable) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String pathHead = "file:///";
        // 1. DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // 1.1 ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return (pathHeadEnable ? pathHead : "") + Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // 1.2 DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {//vivo
                    return id.substring(4);
                }

                if (id.contains(":")) {
                    id = id.split(":")[1];
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return (pathHeadEnable ? pathHead : "") + path;
                        }
                    } catch (Exception e) {
                    }
                }

                String fileName = FileUtil.getFileName(context, uri);
                File cacheDir = FileUtil.getDocumentCacheDir(context);
                File file = FileUtil.generateFileName(fileName, cacheDir);
                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    FileUtil.saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
            }
            // 1.3 MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else if ("document".equals(type)) {
                    contentUri = MediaStore.Files.getContentUri("external");
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return (pathHeadEnable ? pathHead : "") + getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // 2. MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {//判断是否是google相册图片
                return uri.getLastPathSegment();
            } else if (isGooglePlayPhotosUri(uri)) {//判断是否是Google相册图片
                return uri.toString();
            } else if ("com.tencent.mtt.fileprovider".equals(uri.getAuthority())) {//判断是否是tencent fileprovider
                //  Path: /QQBrowser/Android/data/com.tencent.mm/MicroMsg/Download/新建文本文档.html
                return "/storage/emulated/0/" + uri.getPath().replace("/QQBrowser/", "");
            } else if ("com.ss.android.lark.common.fileprovider".equals(uri.getAuthority())) {
                return "/storage/emulated/0/" + uri.getPath().replace("/external_files/", "");
            } else if ("com.huawei.hidisk.fileprovider".equals(uri.getAuthority())) {//判断是否是 huawei fileprovider
                //  Path: /root/storage/emulated/0/Tencent/MicroMsg/Download/1_📥 需求池(1).xlsx
                return uri.getPath().replace("/root", "");
            } else {//其他类似于media这样的图片，和android4.4以下获取图片path方法类似
                return getFilePath_below19(context, uri);
            }
        }
        // 3. 判断是否是文件形式 File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.content/...
     **/
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.contentprovider/0/1/mediakey:/local%3A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1075342619
     **/
    public static boolean isGooglePlayPhotosUri(Uri uri) {
        return "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority());
    }

    /**
     * Google相册图片获取路径
     **/
    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将图片流读取出来保存到手机本地相册中
     **/
    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * 获取小于api19时获取相册中图片真正的uri
     * 对于路径是：content://media/external/images/media/33517这种的，需要转成/storage/emulated/0/DCIM/Camera/IMG_20160807_133403.jpg路径，也是使用这种方法
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePath_below19(Context context, Uri uri) {
        //这里开始的第二部分，获取图片的路径：低版本的是没问题的，但是sdk>19会获取不到
        Cursor cursor = null;
        String path = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是android多媒体数据库的封装接口，具体的看Android文档
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            //获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
            path = cursor.getString(column_index);
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 获取视频的时长
     *
     * @param context
     * @param uri
     * @return
     */
    public static long getVideoDuration(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = new String[]{MediaStore.Video.Media.DURATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isMediaDocument(uri)) {
                String selection = "_id=?";
                String documentId = DocumentsContract.getDocumentId(uri);
                String[] selectionArgs = new String[]{documentId.split(":")[1]};
                cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
            } else {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
            }

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getLong(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0L;
    }

    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".provider";
    }
}
