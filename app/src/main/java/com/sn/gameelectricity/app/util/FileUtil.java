package com.sn.gameelectricity.app.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.sn.gameelectricity.app.util.FileProvider7;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 * 涉及到文件的通用方法请先来本类寻找 若没有及时补充
 * Created by Seaky on 2017/3/4.
 */

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static final String DOCUMENTS_DIR = "documents";

    public static File getAppInternalFilesDir(Context context) {
        for (int a = 0; a < 10; a++) {
            File path = context.getApplicationContext().getFilesDir();
            if (path != null) {
                return path;
            }
        }
        try {
            ApplicationInfo info = context.getApplicationInfo();
            File path = new File(info.dataDir, "files");
            path.mkdirs();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File("/data/data/org.telegram.messenger/files");
    }

    /**
     * 生成一个File, 包含生成相关的目录
     *
     * @param path
     * @return
     */
    public static File createFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            int index = path.lastIndexOf('/');
            String dirPath = path.substring(0, index);
            File dir = new File(dirPath);
            dir.mkdirs();
            file = new File(path);
        }
        return file;
    }

    //创建文件夹
    public static File createDir(String path, boolean delete) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            if (delete) {
                deleteDir(path);
            }
        }
        file.mkdirs();
        return file;
    }

    //删除文件夹
    public static boolean deleteDir(String path) {
        boolean success = deleteChildFiles(path);
        List<File> dirs = getChildDirs(null, path, true);
        for (File dir : dirs) {
            deleteDir(dir.getAbsolutePath());
        }
        deleteFile(path);
        return success;
    }

    //删除文件
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) return false;
        boolean deleteSuccess = false;
        File file = new File(path);
        if (file.exists()) {
            deleteSuccess = file.delete();
        }
        return deleteSuccess;
    }

    /**
     * 删除文件夹下的文件
     */
    public static boolean deleteChildFiles(String path) {
        if (path == null) {
            return false;
        }
        boolean success = true;
        List<File> files = getChildFiles(null, path, true);
        for (File file : files) {
            if (!file.delete()) {
                success = false;
            }
        }
        return success;
    }

    //获取目标路径下的所有文件
    public static List<File> getChildFiles(FileFilter filter, String path, boolean includeHidden) {
        List<File> files = new ArrayList<File>();
        File file = new File(path);
        if ((file != null) && file.isDirectory()) {
            File[] fileArray = file.listFiles(filter);
            if (fileArray != null) {
                for (File childFile : fileArray) {
                    if (!childFile.isDirectory()) {
                        if (includeHidden) {
                            files.add(childFile);
                        } else {
                            if (!childFile.isHidden()) {
                                files.add(childFile);
                            }
                        }

                    }
                }
            }
        }
        return files;
    }

    //获取目标路径下的所有文件夹
    public static List<File> getChildDirs(FileFilter filter, String path, boolean includeHidden) {
        List<File> dirs = new ArrayList<File>();
        File file = new File(path);
        if ((file != null) && file.isDirectory()) {
            File[] fileArray = file.listFiles(filter);
            if (fileArray != null) {
                for (File childFile : fileArray) {
                    if (includeHidden) {
                        if (childFile.isDirectory()) {
                            dirs.add(childFile);
                        }
                    } else {
                        if (childFile.isDirectory() && !childFile.isHidden()) {
                            dirs.add(childFile);
                        }
                    }
                }
            }
        }

        return dirs;
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //判断文件是否存在
    public static boolean isFileExist(File file) {
        return file.exists();
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }


    //拷贝文件
    public static boolean copy(File source, File target) {
        if (!source.exists()) {
            return false;
        }
        boolean isSuccess = false;
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
            isSuccess = true;
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            close(inStream);
            close(in);
            close(outStream);
            close(out);
        }

        return isSuccess;
    }

    /**
     * 将uri中的文件读出来写到targetPath路径里去
     *
     * @param context
     * @param uri
     * @param targetPath
     * @return
     */
    public static boolean createFileByUri(Context context, Uri uri, String targetPath) {
        boolean isSuccess = false;
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            ParcelFileDescriptor descriptor = context.getContentResolver()
                    .openFileDescriptor(uri, "r");
            if (descriptor != null) {
                FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                inStream = new FileInputStream(fileDescriptor);
                outStream = new FileOutputStream(targetPath);
                in = inStream.getChannel();
                out = outStream.getChannel();
                in.transferTo(0, in.size(), out);
                isSuccess = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inStream);
            close(in);
            close(outStream);
            close(out);
        }
        return isSuccess;
    }

    public static long getFileSize(String path) {
        File file = new File(path);
        long size = Long.MAX_VALUE;
        if (file.exists() && file.isFile()) {
            size = file.length();
        }
        return size;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeStringToFile(String filePath, String log, boolean append) {
        writeStringToFile(filePath, log, append, 0);
    }

    public static void writeStringToFile(String filePath, String log, boolean append, long maxFileSize) {
        if (TextUtils.isEmpty(log) || TextUtils.isEmpty(filePath)) {
            return;
        }
        File f = new File(filePath);
        if (!f.exists()) {
            f = createFile(filePath);
        } else {
            if (maxFileSize != 0 && f.length() > maxFileSize) {
                try {
                    f.delete();
                    f = createFile(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(f, append);
            writer.write(log);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }

    public static String readStringFromFile(String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return null;
        }

        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(f));
            String lineTxt = null;
            while ((lineTxt = reader.readLine()) != null) {
                sb.append(lineTxt + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(reader);
        }
        return sb.toString();
    }

    /***
     * 读取文件到字节数组，
     * MappedByteBuffer 可以在处理大文件时，提升性能
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] readFileToByteArray(String filePath) {
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filePath, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fc);
        }

        return null;
    }

    /***
     * 将字节数组写回文件
     * @param data
     * @param filePath
     */
    public static void writeByteArrayToFile(byte[] data, File filePath) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(filePath, "rw");
            raf.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(raf);
        }
    }

    /**
     * 获得文件存储路径
     *
     * @return
     */
    public static String getAppExternalStorageFilePath(Context context) {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {//如果外部储存可用
            file = context.getExternalFilesDir(null); //获得外部存储路径,默认路径为 /storage/emulated/0/Android/data
            if (file == null) {
                try {
                    file = Environment.getExternalStorageDirectory();
                } catch (Exception e) {
//                    MonitorManager.getInstance().addReportMonitorLog("getExternalStorageDirectoryException",
//                            e.getMessage() + "\n" + Log.getStackTraceString(e));
                }
            }
        }
        if (file == null) {
            file = context.getFilesDir(); //直接存在/data/data里，非root手机是看不到的
        }
        return file.getPath();
    }

    /**
     * 检测存储空间够不够用
     *
     * @return
     */
    public static int checkStorageCapacity() {
        long dataSpace = 0, sdcardSpace = 0, dataTotal = 0, sdcardTotal = 0;
        try {
            dataSpace = Environment.getDataDirectory().getFreeSpace();
            dataTotal = Environment.getDataDirectory().getTotalSpace();

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {//如果外部储存可用
                sdcardSpace = Environment.getExternalStorageDirectory().getFreeSpace();
                sdcardTotal = Environment.getExternalStorageDirectory().getTotalSpace();
            }
            if (dataSpace > 0 && dataSpace < 10 * 1024 * 1024) { //可用data空间小于10m
                return 1;
            }
            if (sdcardSpace > 0 && sdcardSpace < 20 * 1024 * 1024) { //可用sdcard空间小于20m
                return 2;
            }
        } catch (Exception e) {
//            LogUtil.e2f(TAG, e.getMessage(), e);
        } finally {
//            LogUtil.i2f(TAG, "-> DataSpace: " + dataSpace + " DataTotal: " + dataTotal
//                    + " SdcardSpace: " + sdcardSpace + " SdcardTotal: " + sdcardTotal);
        }
        return 0;
    }

    public static void serializingOjectToFile(Serializable serializable, String filePath, boolean append) {
        File file = new File(filePath);
        try {
            FileOutputStream outputStream = new FileOutputStream(file, append);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.flush();
            close(objectOutputStream);
            close(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserializingOjectFromFile(String filePath) {
        File file = new File(filePath);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object o = objectInputStream.readObject();
            close(objectInputStream);
            close(inputStream);
            return o;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = FileProvider7.getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }
        return file;
    }


    public static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


}
