package com.chatebook.file.service;

import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.file.model.File;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  File saveInfoUploadFile(MultipartFile multipartFile);

  File saveFileByURL(String url);

  ResponseDataAPI getListFileByAdmin(Pageable pageable);
}
