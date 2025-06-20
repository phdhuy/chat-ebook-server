package com.chatebook.file.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.chatebook.common.exception.InternalServerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileBaseUtils {

  private final AmazonS3 s3;

  private static final String BUCKET = "chat-ebook";

  @Value("${filebase.gateway}")
  private String gateway;

  public Map<String, String> uploadFile(MultipartFile file) {
    Map<String, String> result = new HashMap<>();
    try {
      String original = file.getOriginalFilename();
      String format =
          original != null && original.contains(".")
              ? original.substring(original.lastIndexOf('.') + 1).toLowerCase()
              : "";

      try (PDDocument pdf = PDDocument.load(file.getInputStream())) {
        int pages = pdf.getNumberOfPages();
        result.put("pages", String.valueOf(pages));
      }

      String key = System.currentTimeMillis() + "_" + original;
      ObjectMetadata meta = new ObjectMetadata();
      meta.setContentLength(file.getSize());
      meta.setContentType(file.getContentType());

      PutObjectRequest req =
          new PutObjectRequest(BUCKET, key, file.getInputStream(), meta)
              .withCannedAcl(CannedAccessControlList.PublicRead);
      s3.putObject(req);
      S3Object s3Object = s3.getObject(new GetObjectRequest(BUCKET, key));

      log.info(gateway.concat(s3Object.getObjectMetadata().getUserMetadata().get("cid")));
      result.put(
          "secure_url", gateway.concat(s3Object.getObjectMetadata().getUserMetadata().get("cid")));
      result.put("public_id", key);
      result.put("format", format);
      result.put("bytes", String.valueOf(file.getSize()));
      return result;
    } catch (IOException e) {
      throw new InternalServerException("Filebase upload failed");
    }
  }

  public S3Object download(String key) {
    return s3.getObject(new GetObjectRequest(BUCKET, key));
  }

  public void delete(String key) {
    s3.deleteObject(BUCKET, key);
  }
}
