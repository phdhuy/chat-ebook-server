package com.chatebook.file.service.impl;

import com.chatebook.common.constant.CommonConstant;
import com.chatebook.common.payload.general.PageInfo;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.file.mapper.FileMapper;
import com.chatebook.file.model.File;
import com.chatebook.file.repository.FileRepository;
import com.chatebook.file.service.FileService;
import com.chatebook.file.utils.FileBaseUtils;
import com.chatebook.file.utils.FileUtils;
import com.chatebook.file.utils.HandleFileCloudinaryUtils;
import java.math.BigInteger;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;

  private final HandleFileCloudinaryUtils handleFileCloudinaryUtils;

  private final FileUtils fileUtils;

  private final FileBaseUtils fileBaseUtils;

  private final FileMapper fileMapper;

  @Override
  @Transactional
  public File saveInfoUploadFile(MultipartFile multipartFile) {
    Map<String, String> result;
    if (multipartFile.getSize() > CommonConstant.FILE_SIZE_10MB) {
      result = fileBaseUtils.uploadFile(multipartFile);
    } else {
      result = handleFileCloudinaryUtils.uploadFile(multipartFile);
    }

    return this.save(result);
  }

  public File saveFileByURL(String url) {
    if (url == null || url.isEmpty()) {
      return null;
    }
    try {
      java.io.File tempFile = fileUtils.downloadFileToTemp(url);
      if (tempFile == null) {
        return null;
      }
      String contentType = fileUtils.getContentTypeFromUrl(url);
      long size = tempFile.length();
      int pages = fileUtils.extractPdfPageCount(tempFile, contentType);

      File file = new File();
      file.setSecureUrl(url);
      file.setBytes(BigInteger.valueOf(size));
      file.setPages(pages);
      file.setFormat(contentType);
      file.setFileName(fileUtils.getFileNameFromUrl(url));

      return fileRepository.save(file);
    } catch (Exception e) {
      log.error("Error processing file from URL {}: {}", url, e.getMessage());
      return null;
    }
  }

  @Override
  public ResponseDataAPI getListFileByAdmin(Pageable pageable, String query) {
    Page<File> files = fileRepository.getListFileByAdmin(pageable, query);

    PageInfo pageInfo =
        new PageInfo(pageable.getPageNumber() + 1, files.getTotalPages(), files.getTotalElements());

    return ResponseDataAPI.success(files.stream().map(fileMapper::toFileInfoResponse), pageInfo);
  }

  private File save(Map<String, String> result) {
    File file = new File();
    file.setSecureUrl(result.get("secure_url"));
    file.setPublicId(result.get("public_id"));
    file.setFormat(result.get("format"));
    file.setPages(Integer.valueOf(result.get("pages")));
    file.setBytes(BigInteger.valueOf(Long.parseLong(result.get("bytes"))));

    return fileRepository.save(file);
  }
}
