package com.chatebook.common.utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class EncryptionUtils {

  private EncryptionUtils() {}

  private static String key;

  @Value("${encryption.key}")
  public void setKey(String keyValue) {
    EncryptionUtils.key = keyValue;
  }

  public static String encrypt(String data) {
    log.info("Encrypting key: {}", key);
    if (data == null || data.isEmpty() || key == null) {
      log.warn("Encryption skipped: data or key is null");
      return null;
    }
    try {
      SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      byte[] encrypted = cipher.doFinal(data.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      log.info("Encryption failed: {}", e.getMessage());
      return null;
    }
  }

  public static String decrypt(String encryptedData) {
    if (encryptedData == null || encryptedData.isEmpty() || key == null) {
      log.warn("Decryption skipped: data or key is null");
      return null;
    }
    try {
      encryptedData = encryptedData.trim();
      if (!isValidBase64(encryptedData)) {
        throw new IllegalArgumentException("Invalid Base64 input");
      }
      SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
      return new String(original);
    } catch (IllegalArgumentException e) {
      return encryptedData;
    } catch (Exception e) {
      log.error("Decryption failed: {}", e.getMessage());
      return encryptedData;
    }
  }

  private static boolean isValidBase64(String data) {
    if (data.length() % 4 != 0) {
      return false;
    }
    if (!data.matches("^[A-Za-z0-9+/=]+$")) {
      return false;
    }
    try {
      Base64.getDecoder().decode(data);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}