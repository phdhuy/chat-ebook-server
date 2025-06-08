package com.chatebook.common.exception;

public class FileUploadException extends BadRequestException {
  public FileUploadException(String message) {
    super(message);
  }
}
