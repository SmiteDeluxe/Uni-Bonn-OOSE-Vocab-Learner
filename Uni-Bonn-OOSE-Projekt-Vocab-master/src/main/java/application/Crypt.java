package application;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Class to encrypt Strings using AES (primarily the PW)
 */
public class Crypt {
    private SecretKeySpec secretKeySpec = null;

    /**
     * Constructor to create encryption Key from String
     * @param s which String to use for key
     */
    public Crypt(String s) {
        try {
            //generate key based on String input
            byte[] key = (s).getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key  = sha.digest(key);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt using the secretKey
     * @param text What to encrypt
     * @return decrypted text
     */
    public String encrypt(String text) {
        String decrypted = null;
        //encrypt
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(text.getBytes());

            //Return encrypted message (prettier in Base64
            decrypted = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;

    }

    /**
     * Descrypt using secretKey
     * @param text what to decrypt
     * @return decrypted String
     */
    public String decrypt(String text) {
        String encrypted = null;
        try {
            //Decoder and what key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            
            //Decode and to String
            encrypted = new String(cipher.doFinal(Base64.getDecoder().decode(text)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }
}
