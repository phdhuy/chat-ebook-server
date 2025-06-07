package com.chatebook.file.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileUtils {

  private final OkHttpClient okHttpClient;

  public java.io.File downloadFileToTemp(String url) {
    Request request = new Request.Builder().url(url).build();

    try (Response response = okHttpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        log.info("Failed to download file from {}: {}", url, response.message());
        return null;
      }

      java.io.File tempFile = java.io.File.createTempFile("downloaded_", ".tmp");

      try (InputStream is = response.body().byteStream();
          FileOutputStream fos = new FileOutputStream(tempFile)) {

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
          fos.write(buffer, 0, bytesRead);
        }
      }

      return tempFile;
    } catch (IOException e) {
      log.error("Error downloading file from {}: {}", url, e.getMessage());
      return null;
    }
  }

  public String getContentTypeFromUrl(String url) {
    Request request = new Request.Builder().url(url).head().build();

    try (Response response = okHttpClient.newCall(request).execute()) {
      if (response.isSuccessful()) {
        return response.header("Content-Type");
      }
    } catch (IOException e) {
      log.debug("Failed to get content type from {}: {}", url, e.getMessage());
    }
    return null;
  }

  public int extractPdfPageCount(java.io.File file, String contentType) {
    if (!"application/pdf".equals(contentType)) {
      return 0;
    }

    try (PDDocument document = PDDocument.load(file)) {
      return document.getNumberOfPages();
    } catch (IOException e) {
      log.info("Failed to read PDF page count: {}", e.getMessage());
      return 0;
    }
  }

  public String getFileNameFromUrl(String url) {
    try {
      String cleanUrl = url.split("\\?")[0].split("#")[0];

      String fileName = cleanUrl.substring(cleanUrl.lastIndexOf('/') + 1);

      if (fileName.isEmpty()) {
        return "downloaded_file.pdf";
      }

      fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
      fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

      if (!fileName.toLowerCase().contains(".pdf")) {
        fileName += ".pdf";
      }

      return fileName;
    } catch (Exception e) {
      log.debug("Failed to extract filename from URL {}: {}", url, e.getMessage());
      return "downloaded_file.pdf";
    }
  }
}
