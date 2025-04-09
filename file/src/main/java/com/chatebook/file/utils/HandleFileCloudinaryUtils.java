package com.chatebook.file.utils;

import com.chatebook.common.exception.InternalServerException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandleFileCloudinaryUtils {

  private final Cloudinary cloudinary;

  public Map<String, String> uploadFile(MultipartFile file) {
    try {
      Map<String, String> result = new HashMap<>();
      Map<?, ?> uploadResult =
          cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

      result.put("secure_url", uploadResult.get("secure_url").toString());
      result.put("public_id", uploadResult.get("public_id").toString());
      result.put("format", uploadResult.get("format").toString());
      result.put("pages", uploadResult.get("pages").toString());
      result.put("bytes", uploadResult.get("bytes").toString());
      return result;
    } catch (Exception ex) {
      throw new InternalServerException(ex.getMessage());
    }
  }
}
