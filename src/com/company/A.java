package com.company;

import sun.rmi.runtime.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import java.security.MessageDigest;
import java.security.Signature;

public class A {
    public static byte[] encryptString(String message, SecretKey secret) throws Exception {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    public static String decryptString(byte[] cipherText, SecretKey secret) throws Exception {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    public static SecretKey generateKey(String password) throws Exception {
        return new SecretKeySpec(password.getBytes(), "AES");
    }
}
