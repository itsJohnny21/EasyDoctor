package edu.asu.easydoctor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public  class Encrypter {
    private final static String key;
    private final static String algorithm;

    static {
        Properties props = new Properties();

        try {
            FileInputStream in = new FileInputStream(".env");
            props.load(in);
            in.close();

        } catch (IOException e) {
            System.out.println("Error reading .env file");
        }

        key = props.getProperty("db_key");
        algorithm = props.getProperty("db_algorithm");
    }

    private Encrypter() {
        System.out.println("Encrypter created! This should not happen! Use static methods instead!");
    }

    public static String SHA256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return String.format("%064x", new BigInteger(1, digest));
    }

    public static String encrypt(String input) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(input.getBytes());
        String ecnryptedString = Base64.getEncoder().encodeToString(encrypted);
        return ecnryptedString;
    }
}