package com.chatebook.file.mapper;

import com.chatebook.common.config.SpringMapStructConfig;
import com.chatebook.file.model.File;
import com.chatebook.file.payload.response.FileInfoResponse;
import org.mapstruct.Mapper;

@Mapper(config = SpringMapStructConfig.class)
public interface FileMapper {

  FileInfoResponse toFileInfoResponse(File file);
}
