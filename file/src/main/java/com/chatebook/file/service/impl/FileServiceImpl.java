package com.chatebook.file.service.impl;

import com.chatebook.file.model.File;
import com.chatebook.file.repository.FileRepository;
import com.chatebook.file.service.FileService;
import com.chatebook.file.utils.HandleFileCloudinaryUtils;
import java.math.BigInteger;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;

  private final HandleFileCloudinaryUtils handleFileCloudinaryUtils;

  @Override
  @Transactional
  public File saveInfoUploadFile(MultipartFile multipartFile) {
    Map<String, String> result = handleFileCloudinaryUtils.uploadFile(multipartFile);

    return this.save(result);
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
