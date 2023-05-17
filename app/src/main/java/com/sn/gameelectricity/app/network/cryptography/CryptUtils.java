package com.sn.gameelectricity.app.network.cryptography;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by retio on 2016/8/5.
 */
public class CryptUtils {
    private static final String TAG = "CryptUtils";

    public final static String SHA256 = "SHA-256";
    public final static String MD5 = "MD5";

    public static String EncryptBySHA256(String src) {
        return Encrypt(src, SHA256);
    }

    public static String EncryptByMD5(String src) {
        return Encrypt(src, MD5);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 加密
     *
     * @param src  源串
     * @param type 加密类型
     * @return
     */
    public static String Encrypt(String src, String type) {
        String strDes = null;
        if (src != null) {
            MessageDigest md = null;
            byte[] bt = src.getBytes();
            try {
                md = MessageDigest.getInstance(type);
                md.update(bt);
                strDes = bytesToHexString(md.digest());
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }
        return strDes;
    }


    public static String getFileMD5(String inputFile) throws IOException {
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(inputFile);
            digestInputStream = new DigestInputStream(fileInputStream, messageDigest);

            byte[] buffer = new byte[bufferSize];
            // read的过程中进行MD5处理，直到读完文件
            while (digestInputStream.read(buffer) > 0) ;

            messageDigest = digestInputStream.getMessageDigest();
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
            }
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        //首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        //new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符）
        char[] resultCharArray = new char[byteArray.length * 2];
        //遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        //字符数组组合成字符串返回
        return new String(resultCharArray);
    }

}
