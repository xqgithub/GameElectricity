package com.sn.gameelectricity.app.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;


import com.sn.gameelectricity.app.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SGFileLoader {
    private static final String TAG = "SGFileLoader";
    private static final String SEPARATOR = File.separator;
    private static volatile SGFileLoader Instance = null;
    private String rootDir;
    private DecimalFormat mFormatter;

    public static SGFileLoader getInstance() {
        SGFileLoader localInstance = Instance;
        if (localInstance == null) {
            synchronized (SGFileLoader.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new SGFileLoader();
                }
            }
        }
        return localInstance;
    }

    private SGFileLoader() {
    }

    // 检测文件路径是否已经创建好
    private File checkPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    // 判断对应路径文件是否存在
    public boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    // 根据dialogId及类型type获取根目录Path
    public String getMediaDirectoryPath(long dialogId, int dirType) {
        String dir = getDirName(dirType);

        if (TextUtils.isEmpty(dir)) {
            return null;
        }

        String path = getUserRoot() + encryptPath(String.valueOf(dialogId)) + SEPARATOR + dir + SEPARATOR;
        checkPath(path);
        return path;
    }

    //获取临时文件存放目录 目录为Xianliao/Cache/Temporary
    public String getTemporaryDir(int dirType) {
        String dir = getDirName(dirType);

        if (TextUtils.isEmpty(dir)) {
            return null;
        }

        String path = getCacheDirectoryPath() + dir + SEPARATOR;
        checkPath(path);
        return path;
    }


    // 根据dialogId及类型type获取根目录File
    public File getMediaDirectory(long dialogId, int dirType) {
        String path = getMediaDirectoryPath(dialogId, dirType);
        if (path == null) {
            return null;
        }
        return checkPath(path);
    }

    // 获取指定dialogId & type & originalObjectKey & extension 对应的文件路径
    public String getMediaFilePath(long dialogId, int dirType, String objectkey) {
        String path = getMediaDirectoryPath(dialogId, dirType);
        if (FileParams.DIR_MEDIA_DOCUMENT == dirType) {
            path = path + objectkey;
        } else {
            path = path + encryptPath(objectkey);
        }
        return path;
    }

    // 获取文件目录路径
    public String getDirectoryPath(int dirType) {
        String dir = getDirName(dirType);

        if (TextUtils.isEmpty(dir)) {
            return null;
        }

        String path = getRootDir() + dir + SEPARATOR;
        checkPath(path);
        return path;
    }

    // 通过dirType获取文件名称
    public String getDirName(int dirType) {
        String dir = null;
        switch (dirType) {
            case FileParams.DIR_CACHE:
                dir = "Cache";
                break;
            case FileParams.DIR_AVATAR:
                dir = "ContactUser";
                break;
            case FileParams.DIR_MEDIA_AUDIO:
                dir = "Audio";
                break;
            case FileParams.DIR_MEDIA_DOCUMENT:
                dir = "Document";
                break;
            case FileParams.DIR_MEDIA_IMAGE_ORIGINAL:
                dir = "Image";
                break;
            case FileParams.DIR_MEDIA_IMAGE_SMALL:
                dir = "Thumbnail";
                break;
            case FileParams.DIR_MEDIA_VIDEO:
                dir = "Video";
                break;
            case FileParams.DIR_SAVE:
                dir = "Save";
                break;
            case FileParams.DIR_DOWNLOAD:
                dir = "Download";
                break;
            case FileParams.DIR_EXPRESSION:
                dir = "Expression";
                break;
            case FileParams.DIR_TEMPORARY:
                dir = "Temporary";
                break;
            case FileParams.DIR_USER:
                dir = "User";
                break;
            case FileParams.DIR_MEDIA_CHATRECORD:
                dir = "ChatRecord";
                break;
        }

        if (dir != null
                && dirType != FileParams.DIR_SAVE
                && dirType != FileParams.DIR_DOWNLOAD
                && dirType != FileParams.DIR_EXPRESSION) {
            // 下载 & 保存 , 表情文件夹不加密路径
            dir = encryptPath(dir);
        }
        return dir;
    }

    public static class FileParams {
        // 多媒体文件类型
        public static final byte FILE_TYPE_ORIGINAL = 1;
        public static final byte FILE_TYPE_SMALL = 2;

        // 文件Category类别
        public static final byte CATEGORY_IMAGE = 1;
        public static final byte CATEGORY_AUDIO = 2;
        public static final byte CATEGORY_VIDEO = 4;
        public static final byte CATEGORY_LOCATION = 5;
        public static final byte CATEGORY_CHATRECORD = 6;
        public static final byte CATEGORY_FILE = 8;
        public static final byte CATEGORY_GIF = 9;
        public static final byte CATEGORY_THIRD_IMG = 10;

        // 文件目录
        public static final int DIR_MEDIA_IMAGE_ORIGINAL = 0;
        public static final int DIR_MEDIA_IMAGE_SMALL = 1;
        public static final int DIR_MEDIA_AUDIO = 2;
        public static final int DIR_MEDIA_VIDEO = 3;
        public static final int DIR_MEDIA_DOCUMENT = 4;
        public static final int DIR_CACHE = 5;
        public static final int DIR_AVATAR = 6; // 头像
        public static final int DIR_SAVE = 7; // 保存目录
        public static final int DIR_DOWNLOAD = 8; // 下载目录
        public static final int DIR_EXPRESSION = 9; //表情目录
        public static final int DIR_TEMPORARY = 10; //临时文件存放目录 比如刚拍的图片 视频等
        public static final int DIR_USER = 11; // 存放用户数据
        public static final int DIR_MEDIA_CHATRECORD = 12; // 存放聊天记录数据

        // 文件后缀
        public static final String SUFFIX_GIFIMAGE = ".gif";
        public static final String SUFFIX_IMAGE = ".jpg";
        public static final String SUFFIX_AUDIO = ".ogg";
        public static final String SUFFIX_VIDEO = ".mp4";
    }

    //获取表情目录
    public String getExpressionDirectoryPath() {
        return getDirectoryPath(FileParams.DIR_EXPRESSION);
    }

    // 获取用户目录
    public String getUserAvatarDirectoryPath() {
        return getDirectoryPath(FileParams.DIR_AVATAR);
    }

    // 获取临时缓存目录
    public String getCacheDirectoryPath() {
        return getDirectoryPath(FileParams.DIR_CACHE);
    }

    // 获取根路径
    public String getRootDir() {
        if (TextUtils.isEmpty(rootDir)) {
            boolean isExternal = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (isExternal) {
                // sdcard/Sugram/
                rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + SEPARATOR + "Sugram" + SEPARATOR;
            } else {
                // data/data/package/files/
                rootDir = App.instance.getFilesDir().getAbsolutePath() + SEPARATOR;
            }

            checkPath(rootDir);

            if (isExternal) {
                boolean isExist = isFileExists(rootDir);
                if (!isExist) {
                    rootDir = App.instance.getFilesDir().getAbsolutePath() + SEPARATOR;
                    checkPath(rootDir);
                }
            }
        }

        return rootDir;
    }

    // 返回缓存图片路径
    public String getCacheImagePath(boolean needDelete) {
        String path = getCacheDirectoryPath() + encryptPath("image");
        if (needDelete) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        return path;
    }

    // 返回缓存图片路径
    public String getCacheImagePath(boolean needDelete, String fileName) {
        String path = getCacheDirectoryPath() + encryptPath(fileName);
        if (needDelete) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
        return path;
    }

    // 获取用户目录
    public String getUserRoot() {
        // A用户目录：root/user/A/
        return getRootDir() + getDirName(FileParams.DIR_USER) + SEPARATOR + encryptPath(String.valueOf("sn")) + SEPARATOR;
    }

    // 复制文件,并解密
    public void copyFileAsyncAndDecrypt(final String srcPath, final String destPath, final String decryptKey) {
        copyFileAndDecrypt(srcPath, destPath, decryptKey).subscribe();
    }

    // 复制文件,并加密
    public void copyFileAsyncAndEncrypt(final String srcPath, final String destPath, final String encryptKey) {
        copyFile(srcPath, destPath, true, encryptKey).subscribe();
    }

    // 复制文件，并解密
    public Observable<Boolean> copyFileAndDecrypt(final String srcPath, final String destPath, final String decryptKey) {
        return copyFile(srcPath, destPath, false, decryptKey);
    }

    /**
     * 复制文件，并加密或解密
     *
     * @param encrypt true, 加密， false 解密
     */
    private Observable<Boolean> copyFile(final String srcPath, final String destPath, final boolean encrypt, final String encryptKey) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                FileInputStream source = null;
                FileOutputStream destination = null;

                try {
                    File srcFile = new File(srcPath);
                    if (!srcFile.exists()) {
                        e.onNext(false);
                        return;
                    }

                    File tempFile = new File(destPath + "_temp");
                    if (!tempFile.exists()) {
                        tempFile.createNewFile();
                    }

                    File destFile = new File(destPath);

                    source = new FileInputStream(srcFile);
                    destination = new FileOutputStream(tempFile);
                    destination.getChannel().transferFrom(source.getChannel(), 0, source.getChannel().size());
                    if (!TextUtils.isEmpty(encryptKey)) {
                        if (encrypt) { //加密
                            IsaacCipher.encrypt(encryptKey, tempFile, destFile);
                        } else {
                            IsaacCipher.decrypt(encryptKey, tempFile, destFile);
                        }
                        tempFile.delete();
                    } else {
                        tempFile.renameTo(destFile);
                    }
                    e.onNext(true);
                } catch (Exception exception) {
                    e.onNext(false);
                } finally {
                    try {
                        if (source != null) {
                            source.close();
                        }
                        if (destination != null) {
                            destination.close();
                        }
                    } catch (Exception exception) {
                    }

                }
            }
        }).subscribeOn(Schedulers.io());

        return observable;
    }

//    // 复制文件
//    public void copyFileAndDecrypt(final String srcPath, final String destPath) {
//        copyFileAndDecrypt(srcPath, destPath, null);
//    }

    // 通过路径获取文件后缀 etc: .jpg
    public String getExtensionFromPath(String path) {
        String extension = "";
        if (TextUtils.isEmpty(path)) {
            return extension;
        }

        int index = path.lastIndexOf(".");
        if (index == -1) {
            return extension;
        } else {
            extension = path.substring(index, path.length());
        }
        return extension;
    }

    // 删除指定dialog的文件目录
    public void deleteDialogDir(final long dialogId) {
//        deleteDialogDir(UserConfigCenter.getInstance().getClientUserUin(), dialogId);
    }

    public void deleteDialogDir(final long userId, final long dialogId) {
        // A用户目录：root/user/A/
        final String userRootDir = getRootDir() + getDirName(FileParams.DIR_USER) + SEPARATOR + encryptPath(String.valueOf(userId)) + SEPARATOR;
//        EventDispatcher.getInstance().postRunnable(new Runnable() {
//            @Override
//            public void run() {
//                String dirPath = userRootDir + Base64Utils.encrypt(dialogId + "");
//                File file = new File(dirPath);
//                boolean isExist = file.exists();
//                if (!isExist) {
//                    return;
//                }
//
//                deleteDir(file);
//            }
//        });
    }

    // 递归删除指定文件目录
    public boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return true;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null) {
                return false;
            }
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    // 删除文件
    public void deleteFile(File f) {
        if (f == null) {
            return;
        }

        if (f.exists()) {
            try {
                f.delete();
            } catch (Exception e) {
            }
        }
    }

    // 获取文件目录大小 单位：byte
    public long getDirSize(File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                for (File f : childFiles) {
                    size += getDirSize(f);
                }
            } else {
                size = file.length();
            }
        }
        return size;
    }

    /***
     * 格式化文件大小
     * @param fileSize 单位：字节
     * @return
     */
    public String formatFileSize(long fileSize) {
        if (null == mFormatter) {
            mFormatter = new DecimalFormat("#.##");
        }

        double size;
        if ((size = fileSize) < 1024) {
            // B
            return size + "B";
        } else if ((size = size / 1024) < 1024) {
            // KB
            return (int) size + "KB";
        } else if ((size = size / 1024) < 1024) {
            // MB
            return mFormatter.format(size) + "MB";
        } else {
            // GB
            return mFormatter.format(size / 1024) + "GB";
        }
    }

    // 清除旧用户数据 保留头像和缓存目录
    public void cleanUpDataExceptAvatar() {
        File rootFile = new File(getUserRoot());
        if (rootFile.exists()) {
            File[] childFiles = rootFile.listFiles();
            String userDir = getDirName(FileParams.DIR_AVATAR);
            String cacheDir = getDirName(FileParams.DIR_CACHE);
            for (File f : childFiles) {
                String name = f.getName();
                if (name.equals(userDir) || name.equals(cacheDir)) {
                    continue;
                }

                deleteDir(f);
            }
        }
    }

    /***
     * Base64 加密数据
     * @param text
     * @return
     */
    public static String encryptPath(String text) {
        return Base64Utils.encrypt(text);
    }

    /***
     * Base64 解密数据
     * @param text
     * @return
     */
    public static String decryptPath(String text) {
        return Base64Utils.decrypt(text);
    }


// 用户头像 - start ################################################################################

    /***
     * 生成用户头像的Key
     * @param destUin
     * @return etc:10001_time_随机串
     */
    public String generateAvatarKey(long destUin) {
        return String.valueOf(destUin) + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString();
    }

    /***
     * 从头像Key获取用户uin
     * @param key etc:10001_time
     * @return 10001
     */
    public long getUinFromAvatarKey(String key) {
        long uin = 0;
        try {
            if (!key.contains("_")) {
                return uin;
            }

            int index = key.indexOf("_");
            if (index != -1) {
                uin = Long.valueOf(key.substring(0, index));
            }
        } catch (NumberFormatException e) {
            //
        }
        return uin;
    }

    public boolean isAvatarExist(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        String path = getUserAvatarDirectoryPath() + encryptPath(key);
        File f = new File(path);
        return f.exists();
    }

    public String getAvatarPath(String key) {
        return getUserAvatarDirectoryPath() + encryptPath(key);
    }

    /***
     * 删除头像文件
     * @param avatars
     */
    public void deleteAvatarFile(String... avatars) {
        if (null == avatars || avatars.length == 0) {
            return;
        }

        int length = avatars.length;
        String path;
        for (int i = 0; i < length; i++) {
            if (!TextUtils.isEmpty(avatars[i])) {
                path = getUserAvatarDirectoryPath() + encryptPath(avatars[i]);
                deleteFile(new File(path));
            }
        }
    }


    // 缩略图 - start ###################################################################################
    private static final int MAX_PHOTO_THUMBNAIL_SIZE = 250 * 1024; // 100KB
    private static final int MAX_PHOTO_ORIGINAL_SIZE = 500 * 1024; // 300KB
    private static final int THUMBNAIL_QUALITY = 40;
    private static final int ORIGINAL_QUALITY = 100;
    private static final int AVATAR_THUMBNAIL_QUALITY = 10;
    private static final int AVATAR_ORIGINAL_QUALITY = 60;

    private static final int AVATAR_THUMBNAIL_WIDTH = 100;
    private static final int AVATAR_THUMBNAIL_HEIGHT = 100;
    private static final int AVATAR_ORIGINAL_WIDTH = 640;
    private static final int AVATAR_ORIGINAL_HEIGHT = 640;

    public boolean writeGroupThumbnail(Bitmap bitmap, long groupId, String imageKey) {
        String destDirPath = getAvatarPath(imageKey);
        File file = new File(destDirPath);
        try {

            File dirFile = file.getParentFile();
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, THUMBNAIL_QUALITY, fos);
            fos.flush();
            fos.close();

            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
            return true;
        } catch (Exception e) {
            if (file.exists()) {
                file.delete();
            }
            return false;
        }
    }

    // 生成头像缩略图 默认缩略图大小为200*200
    public void createAvatarThumbnail(String imageFilePath, String destDirPath, String objectKey) {
        // 生成缩略图片
        Bitmap bmp = BitmapUtil.decodeFromFile(imageFilePath, 200, 200);
        if (bmp != null) {
            File file = new File(destDirPath, encryptPath(objectKey));
            String destPath = file.getAbsolutePath();
            // 生成图片到文件
            BitmapUtil.saveBitmapToFile(bmp, destPath);
            // 回收图片资源
            BitmapUtil.recycler(bmp);
        }
    }

    // 生成头像缩略图
    public void createAvatarOriginal(String imageFilePath, String destDirPath, String objectKey) {
        PictureUtil.getImageFileWithPNG(App.instance, encryptPath(objectKey), imageFilePath, destDirPath, true);
    }


    /**
     * ================================== 本地收藏 start==============================
     */
    //本地收藏的目录名
    private static final String COLLECTION_DIR = "sugram_collection";
    //本地收藏的根路径
    private static final String COLLECTION_PATH = FileUtil.getAppExternalStorageFilePath(App.instance) + SEPARATOR + COLLECTION_DIR + SEPARATOR;

    //获取本地收藏 的文件完整路径

    /**
     * @param dirType   文件类型   语音 视频 gif 图片 文件
     * @param objectkey 文件key 阿里云下载用，也用作本地文件名
     * @return
     */
    public String getCollectionPath(int dirType, String objectkey) {
        String dir = getDirName(dirType);
        if (TextUtils.isEmpty(dir)) {
            dir = "file";
        }
        String path = COLLECTION_PATH + dir + SEPARATOR;
        checkPath(path);
        path += objectkey;
        return path;
    }

    /**
     *  ================================== 本地收藏 end==============================
     */

}
