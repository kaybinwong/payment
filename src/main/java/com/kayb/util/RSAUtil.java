package com.kayb.util;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA Util
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class RSAUtil {

    /**
     * encrypt algorithm
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * signature algorithm
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    /**
     * public key map key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * private key map key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * public/private key size
     */
    private static final Integer KEY_SIZE = 1024;

    /**
     * init a public/private key pair
     */
    public static Map<String, Object> initKey(){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // get public key
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // get private key
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        }
    }
    /**
     * generate signed data with private key
     * @param signing original data
     * @param privateKey selves' private key
     * @param charset 字符编码
     * @return signed data
     */
    public static String sign(String signing, String privateKey, String charset){
        try {
            return sign(signing.getBytes(charset), privateKey);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    /**
     * generate signed data with private key
     * @param data original data
     * @param privateKey selves' private key
     * @return signed data
     */
    public static String sign(byte[] data, String privateKey){
        try {
            PrivateKey priKey = string2PrivateKey(privateKey);
            // sign data with private key and SIGNATURE_ALGORITHM algorithm
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(data);

            // encode signed data with Base64
            return base64Encode(signature.sign());

        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    public static PrivateKey string2PrivateKey(String privateKey) {

        // generate PrivateKey object
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(privateKey);

            // construct PKCS8EncodedKeySpec obejct
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

            // get key factory of KEY_ALGORITHM
            KeyFactory keyFactory = KeyFactory. getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    /**
     * verify signed data with public key
     * @param signing original data
     * @param signed signed data
     * @param publicKey public key
     * @param charset charset
     * @return true if verify successfully, or false
     */
    public static boolean verify(String signing, String signed, String publicKey, String charset) {
        try {
            return verify(signing.getBytes(charset), signed, publicKey);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    /**
     * verify signed data with public key
     * @param data original data
     * @param signed signed data
     * @param publicKey others' public key
     * @return true if verify successfully, or false
     */
    public static boolean verify(byte[] data, String signed, String publicKey) {
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(publicKey);

            // construct X509EncodedKeySpec object
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            // KEY_ALGORITHM
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            // get key factory of KEY_ALGORITHM
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            // sign data with public key and SIGNATURE_ALGORITHM algorithm
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data);

            // 验证签名是否正常
            return signature.verify(base64Decode(signed));
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * decrypt data with private key
     * @param data encrypted data
     * @param key private key
     * @return decrypted data
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) {
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(key);

            // get PKCS8EncodedKeySpec object
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // decrypt data with private key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * decrypt data with public key
     * @param data encrypted data
     * @param key public key
     * @return decrypted data
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) {
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct X509EncodedKeySpec object
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // decrypt data with public key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * encrypt data with public key
     * @param data original data
     * @param key public key
     * @return encrypted data
     */
    public static byte[] encryptByPublicKey(byte[] data, String key){
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct X509EncodedKeySpec data
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // encrypt data with public key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * encrypt data with private key
     * @param data original data
     * @param key private key
     * @return encrypted data
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key){
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct PKCS8EncodedKeySpec data
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // encrypt data with private key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    /**
     * get encoded private key
     * @param keyMap key pair
     * @return encoded private key
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return base64Encode(key.getEncoded());
    }

    /**
     * get encoded public key
     * @param keyMap key pair
     * @return encoded public key
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return base64Encode(key.getEncoded());
    }

    /**
     * encode with Base64
     * @param key key
     * @return encoded key
     */
    public static String base64Encode(byte[] key){
        return Base64.encode(key);
    }

    /**
     * decode with Base64
     * @param key key
     * @return decoded key
     */
    public static byte[] base64Decode(String key) {
        return Base64.decode(key);
    }

}
