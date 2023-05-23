package com.gem.loganalysis.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

/**
 * @ClassName: AESUtils
 * @Description: AES加密和解密工具类
 * @Date: 22/05/23 14:35
 */
public class AESUtil {

    /**
     * 密钥
     */
    public static final String KEY = "SKVNMEIHJLOANSKI";

    /**
     * 算法
     */
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static void main(String[] args) {
        String content = "{\"pageNum\":1,\"pageSize\":10}";
        String encrypt = aesEncrypt(content, KEY);
        System.out.println("加密后：" + encrypt);

        String response =
                "K60XBSGOxaQZm/ppjYSJSQWN0OzRIPYCbSgY+i/KJYLquQt3CRyYY+JfkMqRfovztUqaEwMnAw2/MN2rjxn98k8Ge/mbNiFrT0NynS1m9GWDmKx5H/78gjA9Y+KtDT6n85E7BxB2dWfYVyP2uPqpfJoLe8wRdxvx/" +
                        "KyZNVoLH4gl9jUXHLC2VnQs74fTgfH/ZvTZnEAHBpTD2LV9/4s+QwZvk/eKdJZfx3mqn5owi2vi8x+T/" +
                        "0kJpkWfkMFTz+xkoFNHenuQiXhuuxTw+O3d4wTAaBsiHV0E8aBajxTtoc3YIW2C8JXJdaiAEUobpQ8aan3MyMjJDlAq4APQCf44UDhwm8qHeD4w++pi3Npm/O7/FRjN9o1riKjhtn27TV0n1yaX8Eo5O/" +
                        "ouAZbpesmJENjpgEQ+Pj1QLX7yseAfc+LLe1OHv3kY47E+wBnUsvCEFiwhUZ2uCG0XGsGpM/VqTwudjmcmXizFlaCcOh2j3cHraD7f/GhBK3xY7BHKK2zIF0EVFiWSEmvxBl+W6/" +
                        "04LV7xk6u05NhEmVcvv5dNXRnhirCUdnlcWl1SlntHi9JaF+Xrg+ZBNl3jVlyzuiJaZ6OqsfOtNHjAyB7peNuu1y+EmeDHf9urpj2QLb1mUa/u";
        String decrypt = aesDecrypt(response, KEY);
        System.out.println("解密后：" + decrypt);
    }

    /**
     * aes解密
     *
     * @param encrypt 内容
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypt, String time, String key) {
        return aesDecrypt(encrypt, key + time);
    }

    /**
     * aes解密
     *
     * @param encrypt 内容
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypt) {
        return aesDecrypt(encrypt, KEY);
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.encode(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return StrUtil.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }


    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kGen = KeyGenerator.getInstance("AES");
        kGen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }


    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) {
        try {
            return base64Encode(aesEncryptToBytes(content, encryptKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kGen = KeyGenerator.getInstance("AES");
        kGen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }


    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) {
        try {
            return StrUtil.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}


