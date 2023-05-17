package com.sn.gameelectricity.app.util;

import android.text.TextUtils;


import com.sn.gameelectricity.app.util.AESCryptor;
import com.sn.gameelectricity.app.util.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


/**
 * Created by nickyang on 2017/8/1.
 */

public class IsaacCipher {
    private static final String TAG = "IsaacCipher";

    static {
        System.loadLibrary("cipher-lib");
    }

    /**
     * 对字符串加密，加密后转为16进制字符串
     *
     * @param data
     * @param encryptKey
     * @return
     */
    public static String encrypt(String data, String encryptKey) {
        if (TextUtils.isEmpty(encryptKey) || TextUtils.isEmpty(data)) {
            return data;
        }
        try {
            byte[] bytes = data.getBytes("UTF-8");
//            LogUtil.e(" encrypt 前 ----> " + data + "  "+ Arrays.toString(bytes));
            encryptMessage(bytes, encryptKey);
//            LogUtil.e(" encrypt 后 ----> " + data + "  "+ Arrays.toString(bytes));
            return AESCryptor.toHex(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对16进制字符串解密
     *
     * @param data
     * @param encryptKey
     * @return
     */
    public static String decrypt(String data, String encryptKey) {
        if (TextUtils.isEmpty(encryptKey) || TextUtils.isEmpty(data)) {
            return data;
        }
        try {
            byte[] bytes = AESCryptor.toByte(data);
//            LogUtil.e(" decrypt 前 ----> " + data + "  "+ Arrays.toString(bytes));
            decryptMessage(bytes, encryptKey);
            String ret = new String(bytes, "UTF-8");
//            LogUtil.e(" decrypt 后 ----> " + "  "+ Arrays.toString(bytes) + " "+ret);
            return ret;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void encryptData(byte[] data, String encryptKey) {
        if (TextUtils.isEmpty(encryptKey) || data == null || data.length == 0) {
            return;
        }
        encrypt(data, data.length, encryptKey);
    }

    public static void decryptData(byte[] data, String decryptKey) {
        if (TextUtils.isEmpty(decryptKey) || data == null || data.length == 0) {
            return;
        }
        encrypt(data, data.length, decryptKey);
    }

    private static void encryptMessage(byte[] data, String encryptKey) {
        if (TextUtils.isEmpty(encryptKey) || data == null || data.length == 0) {
            return;
        }
        encryptMessage(data, data.length, encryptKey);
    }

    private static void decryptMessage(byte[] data, String decryptKey) {
        if (TextUtils.isEmpty(decryptKey) || data == null || data.length == 0) {
            return;
        }
        encryptMessage(data, data.length, decryptKey);
    }

    public static InputStream decryptToInputStream(String decryptKey, File file) {
        if (TextUtils.isEmpty(decryptKey) || !file.exists()) {
            return null;
        }
        byte[] fByte = FileUtil.readFileToByteArray(file.getAbsolutePath());
        encrypt(fByte, fByte.length, decryptKey);
        InputStream inputStream = new ByteArrayInputStream(fByte);
        return inputStream;
    }

    public static void encrypt(String key, File originFile, File destFile) {
        if (TextUtils.isEmpty(key) || !originFile.exists()) {
            return;
        }
        byte[] fByte = FileUtil.readFileToByteArray(originFile.getAbsolutePath());
        encrypt(fByte, fByte.length, key);
        FileUtil.writeByteArrayToFile(fByte, destFile);
    }

    public static void decrypt(String key, File originFile, File destFile) {
        encrypt(key, originFile, destFile);
    }

    public static class KeyBuilder {
        public static String getKey() {
            return UUID.randomUUID().toString();
//            return "";
        }
    }

    private static native void encrypt(byte[] data, int dataLength, String encryptKey);

    /**
     * utf-8 要加密的文字必须是utf-8编码的, utf-8是变长编码
     * 1字节 0xxxxxxx
     * 2字节 110xxxxx 10xxxxxx
     * 3字节 1110xxxx 10xxxxxx 10xxxxxx
     * 4字节 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
     */
    private static native void encryptMessage(byte[] data, int dataLength, String encryptKey);
}
