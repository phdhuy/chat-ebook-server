package com.chatebook.file.service;

import com.chatebook.file.model.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  File saveInfoUploadFile(MultipartFile multipartFile);
}
