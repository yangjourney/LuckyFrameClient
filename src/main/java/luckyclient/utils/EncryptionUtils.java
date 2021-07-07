package luckyclient.utils;

import luckyclient.utils.config.SysConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class EncryptionUtils {

    /**
     * RSA���������Ĵ�С
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA���������Ĵ�С
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * Ĭ�Ϲ�Կ
     */
    private static final String DEFAULT_PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCadmkcep05BmZ3aWH72ab8iw8xD4XYrXmeySwBgKQY4mhHo2MrT8fKiNaG0PC/Jy09inPczBPqf/IPILlE79ujgpc84bHnR27u9IH7kJlyoLiPRGoN+oQbWJakmYTwGkdG4z1Re9xoKi4Ww1WShkvJspMwOWtkwfwub5zkvQtSWQIDAQAB";

    /**
     * Ĭ��˽Կ
     */
    private static final String DEFAULT_PRIVATE_KEY="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJp2aRx6nTkGZndpYfvZpvyLDzEPhditeZ7JLAGApBjiaEejYytPx8qI1obQ8L8nLT2Kc9zME+p/8g8guUTv26OClzzhsedHbu70gfuQmXKguI9Eag36hBtYlqSZhPAaR0bjPVF73GgqLhbDVZKGS8mykzA5a2TB/C5vnOS9C1JZAgMBAAECgYBNTjYNKtDFWY6u9O81PRl2C6LuyvYSG8Bi2AxONDPswGOwdvWLF8LGevXjQ286PEFIK6MRPpI5Kw/awmX3OpSR10nAzLHo7KU03+1+71EpGcGt0OAudDG+Qzzz10rjyoBwV21d8utoJmy4m5MLbp7yxxZ0caGNfkJMj7QJyxsQAQJBAOtTwyqdGbhLle0rD/9WhK5huFBAaXCw21mJK/wkByVFk9ynHN1P0e3fgS4S2KOyWGEwMgfaxRxvn+Tmj8sQLkkCQQCoCBhihIZj0epYvdQdf63sgrzVlUr3d3IIlKio4JLfvo4gFGpQjV/mOlyS7AGNWf5iDFzJvpXoXET5GYkmpEORAkA784LtAEjlIpx3Z1kT+76hjlOeXkp+Yw/+p2uFOMh5PliFBi3cU9FvgFkwm6yFR5IscFLOnXVJ4UYi0nofiWfBAkBMZvnneci9hIog9ZeIHjEP9FY2a16d7RLNsgKKXyqJT9TB42Z/3/h1751+NI90HTJclLBwDxeMgr/d3+2Lw27xAkBdQqmrWTAmHPGS48CZ/VYu9repRhDmV+8nsWtX1fdU410kcfYgib7WX9Y22v4vGQrVt72waBvvEvbjWjXH+Ael";

    /**
     * �û����õ�˽Կ
     */
    private static String USER_PRIVATE_KEY= SysConfig.getConfiguration().getProperty("client.config.privateKey");

    /**
     * �û����õĹ�Կ
     */
    private static String USER_PUBLIC_KEY= SysConfig.getConfiguration().getProperty("client.config.publicKey");

    @Value("${client.config.privateKey}")
    public  void setUserPrivateKey(String userPrivateKey) {
        USER_PRIVATE_KEY = userPrivateKey;
    }

    @Value("${client.config.publicKey}")
    public  void setUserPublicKey(String userPublicKey) {
        USER_PUBLIC_KEY = userPublicKey;
    }

    /**
     * ��ȡ��Կ��
     *
     * @return ��Կ��
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * ��ȡ˽Կ
     *
     * @param privateKey ˽Կ�ַ���
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * ��ȡ��Կ
     *
     * @param publicKey ��Կ�ַ���
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA����
     *
     * @param data ����������
     * @param publicKey ��Կ
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // �����ݷֶμ���
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // ��ȡ��������ʹ��base64���б���,����UTF-8Ϊ��׼ת�����ַ���
        // ���ܺ���ַ���
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA����
     *
     * @param data ����������
     * @param privateKey ˽Կ
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // �����ݷֶν���
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // ���ܺ������
        return new String(decryptedData, "UTF-8");
    }

    /**
     * ǩ��
     *
     * @param data ��ǩ������
     * @param privateKey ˽Կ
     * @return ǩ��
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * ��ǩ
     *
     * @param srcData ԭʼ�ַ���
     * @param publicKey ��Կ
     * @param sign ǩ��
     * @return �Ƿ���ǩͨ��
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * ����
     * @param data
     * @return
     */
    public static String encrypt(String data)
    {
        try
        {
            String publicKey= StringUtils.isNotEmpty(USER_PUBLIC_KEY)?USER_PUBLIC_KEY:DEFAULT_PUBLIC_KEY;
            String encryptData = encrypt(data, getPublicKey(publicKey));
            return encryptData;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * ����
     * @param encryptData
     * @return
     */
    public static String decrypt(String encryptData)
    {
        try
        {
            String privateKey= StringUtils.isNotEmpty(USER_PRIVATE_KEY)?USER_PRIVATE_KEY:DEFAULT_PRIVATE_KEY;
            String data = decrypt(encryptData, getPrivateKey(privateKey));
            return data;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return encryptData;
    }

    public static void main(String[] args) {
        try {
            // ������Կ��
            KeyPair keyPair = getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("˽Կ:"  + privateKey);
            System.out.println("��Կ:" + publicKey);
            // RSA����
            String data = "�����ܵ���������";
            String encryptData = encrypt(data, getPublicKey(publicKey));
            System.out.println("���ܺ�����:" + encryptData);
            // RSA����
            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
            System.out.println("���ܺ�����:" + decryptData);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("�ӽ����쳣");
        }
    }
}