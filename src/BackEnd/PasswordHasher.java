package BackEnd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class PasswordHasher {
    public static String hashPassword(String password) {
        try{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found");
        }}
        public static boolean verifyPassword(String enteredPassword, String storedHash) {
            String hashOfInput = hashPassword(enteredPassword);
            return hashOfInput.equals(storedHash);
        }


    }


