package com.sn.gameelectricity.app.util;

import android.text.TextUtils;

import com.sn.gameelectricity.app.network.cryptography.CryptUtils;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by retio on 2016/9/6.
 */
public class AESCryptor {
    private final static String TAG = "AESCryptor";
    private static final String HASH_ALGORITHM = "SHA1PRNG";
    public final static String DEFAULT_KEY = ",.<>{}:*&^%$#@!~";//默认加密key

    // /** 算法/模式/填充 **/
    private static final String CipherMode = "AES/CBC/PKCS5Padding";
    public final static String KEY_ALGORITHM = "AES";

    // private static final String HASH_ALGORITHM = "SHA1PRNG";

    /**
     * 创建密钥
     *
     * @param seed 种子
     * @return
     */
    private static SecretKeySpec createKey(String seed) {
        byte[] data = null;
        if (seed == null) {
            seed = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(seed);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
//            String log = LogUtil.e2f(TAG, "SecretKeySpec err. seed: " + seed, e);
//            MonitorManager.getInstance().addReportMonitorLog(TAG, log);
        }
        return new SecretKeySpec(data, KEY_ALGORITHM);
    }

    /**
     * 创建 IV向量
     *
     * @param ivParameter 向量值
     * @return
     */
    private static IvParameterSpec createIV(String ivParameter) {
        byte[] data = null;
        if (ivParameter == null) {
            ivParameter = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(ivParameter);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
//            String log = LogUtil.e2f(TAG, "createIV err. ivParameter: " + ivParameter, e);
//            MonitorManager.getInstance().addReportMonitorLog(TAG, log);
        }
        return new IvParameterSpec(data);
    }

    /**
     * 加密字节数组
     *
     * @param content 待加密内容
     * @param keySeed 密钥种子
     * @param iv      向量值
     * @return
     */
    public static byte[] encrypt(byte[] content, String keySeed, String iv) {
        try {
            SecretKeySpec key = createKey(keySeed);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
//            String log = LogUtil.e2f(TAG, "encrypt err.  content: " + Arrays.toString(content)
//                    + " keySeed: " + keySeed + " iv: " + iv, e);
//            MonitorManager.getInstance().addReportMonitorLog(TAG, log);
        }
        return null;
    }

    /**
     * 解密字节数组
     *
     * @param content 内容
     * @param keySeed 密钥种子
     * @param iv      向量值
     * @return
     */
    public static byte[] decrypt(byte[] content, String keySeed, String iv) {
        try {
            SecretKeySpec key = createKey(keySeed);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
//            String log = LogUtil.e2f(TAG, "decrypt err. content: " + Arrays.toString(content) + " keySeed: " + keySeed + " iv: " + iv, e);
//            MonitorManager.getInstance().addReportMonitorLog(TAG, log);
        }
        return null;
    }


    /**
     * 十六进制
     */
    private final static String HEX = "0123456789ABCDEF";

    /**
     * 将二进制字节数组转换成十六进制字符串
     *
     * @param buf
     * @return
     */
    public static String toHex(byte[] buf) {
        if (buf == null || buf.length == 0)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            byte b = buf[i];
            result.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
        }
        return result.toString().replaceAll(" ", "");

    }

    /**
     * 将十六进制字符串转换成二进制字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    /***
     * 加密文本
     * @param content 内容
     * @param keySeed 密钥种子
     * @param iv 若iv为空，向量值使用默认值{@link #DEFAULT_KEY}
     * @param cipherMode 算法模式
     * @return
     */
    public static String decrypt(String content, String keySeed, String iv, String cipherMode, boolean createKeyByMD5) throws Exception {
        if (TextUtils.isEmpty(content)) {
            return "";
        }

        String result = content;
        SecretKeySpec key;
        if (createKeyByMD5) {
            key = createKeyByMD5(keySeed);
        } else {
            key = createKey(keySeed);
        }
        Cipher cipher = Cipher.getInstance(cipherMode);
        if (TextUtils.isEmpty(iv)) {
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(DEFAULT_KEY));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
        }
        byte[] decryptedArray = cipher.doFinal(toByte(content));
        result = new String(decryptedArray, "utf-8");
        return result;
    }

    /***
     * 解密文本
     * @param content 内容
     * @param keySeed 密钥种子
     * @param iv 若iv为空，向量值使用默认值{@link #DEFAULT_KEY}
     * @param cipherMode 算法模式
     * @param createKeyByMD5 对Key进行MD5运算
     * @return
     */
    public static String encrypt(String content, String keySeed, String iv, String cipherMode, boolean createKeyByMD5) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }

        String result = content;

        try {
            SecretKeySpec key;
            if (createKeyByMD5) {
                key = createKeyByMD5(keySeed);
            } else {
                key = createKey(keySeed);
            }
            Cipher cipher = Cipher.getInstance(cipherMode);
            if (TextUtils.isEmpty(iv)) {
                cipher.init(Cipher.ENCRYPT_MODE, key, createIV(DEFAULT_KEY));
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
            }
            byte[] encryptedArray = cipher.doFinal(content.getBytes("utf-8"));
            result = toHex(encryptedArray);
        } catch (Exception e) {
//            String log = LogUtil.e2f(TAG, "encrypt err. content: " + content + " keySeed: " + keySeed
//                    + " iv: " + iv + " cipherMode: " + cipherMode + " createKeyByMD5: " + createKeyByMD5, e);
//            MonitorManager.getInstance().addReportMonitorLog(TAG, log);
        }
        return result;
    }

    /**
     * 创建16倍数长度的密钥
     *
     * @param seed 种子
     * @return
     */
    private static SecretKeySpec createKeyByMD5(String seed) {
        byte[] data = null;
        if (seed == null) {
            seed = "";
        }

        String key = CryptUtils.EncryptByMD5(seed);

        try {
            data = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
//            String log = LogUtil.e2f(TAG, "createKeyByMD5 err. seed: " + seed, e);
//            MonitorManager.getInstance().addReportMonitorLog(TAG, log);
        }
        return new SecretKeySpec(data, KEY_ALGORITHM);
    }

    /***
     * 数据库 - 加密文本
     * @param content
     * @param keySeed
     * @return
     */
    public static String encryptDBText(String content, String keySeed) {
        return encrypt(content, keySeed, null, CipherMode, true);
    }

    /***
     * 数据库 - 解密文本
     * @param content
     * @param keySeed
     * @return
     */
    public static String decryptDBText(String content, String keySeed) {
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        String result = content;
        try {
            result = decrypt(content, keySeed, null, CipherMode, true);
        } catch (Exception e) {
//            String log = LogUtil.e2f(TAG, "decryptDBText err. content: " + content + " keySeed: " + keySeed, e);
//            MonitorManager.getInstance().addReportMonitorLog(TAG, log);
        }
        return result;
    }

    /***
     * 业务层 - 加密消息
     * @param content 内容
     * @param keySeed 密钥种子
     * @param iv 向量
     * @return
     */
    public static String encryptMsgText(String content, String keySeed, String iv) {
        return encrypt(content, keySeed, iv, CipherMode, false);
    }

    /***
     *
     * @param content
     * @param keySeed
     * @param iv
     * @return
     */
    public static String decryptMsgText(String content, String keySeed, String iv) throws Exception {
        return decrypt(content, keySeed, iv, CipherMode, false);
    }
}
