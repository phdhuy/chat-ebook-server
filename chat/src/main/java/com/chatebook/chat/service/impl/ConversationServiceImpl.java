package com.chatebook.chat.service.impl;

import com.chatebook.ai.payload.response.UploadFileAIResponse;
import com.chatebook.ai.service.AIService;
import com.chatebook.chat.event.ConversationCreatedEvent;
import com.chatebook.chat.mapper.ConversationMapper;
import com.chatebook.chat.model.Conversation;
import com.chatebook.chat.payload.request.UpdateConversationRequest;
import com.chatebook.chat.payload.response.ConversationInfoResponse;
import com.chatebook.chat.repository.ConversationRepository;
import com.chatebook.chat.service.ConversationService;
import com.chatebook.common.common.CommonFunction;
import com.chatebook.common.constant.MessageConstant;
import com.chatebook.common.exception.ForbiddenException;
import com.chatebook.common.exception.NotFoundException;
import com.chatebook.common.payload.general.PageInfo;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.file.mapper.FileMapper;
import com.chatebook.file.service.FileService;
import com.chatebook.security.repository.UserRepository;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

  private final ConversationRepository conversationRepository;

  private final UserRepository userRepository;

  private final FileService fileService;

  private final AIService aiService;

  private final FileMapper fileMapper;

  private final ConversationMapper conversationMapper;

  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public ConversationInfoResponse createConversation(UUID userId, MultipartFile file)
      throws IOException {
    Conversation conversation = new Conversation();

    conversation.setFile(fileService.saveInfoUploadFile(file));
    conversation.setName(file.getOriginalFilename());
    conversation.setUser(userRepository.getReferenceById(userId));

    conversationRepository.save(conversation);

    UploadFileAIResponse uploadFileAIResponse =
        aiService.uploadFile(file, conversation.getId(), userId);

    eventPublisher.publishEvent(
        new ConversationCreatedEvent(this, conversation, uploadFileAIResponse, userId));

    return conversationMapper.toConversationInfoResponse(
        conversation, fileMapper.toFileInfoResponse(conversation.getFile()));
  }

  @Override
  public ResponseDataAPI getMyConversations(Pageable pageable, UUID userId) {
    Page<Conversation> conversations = conversationRepository.getMyConversations(pageable, userId);

    PageInfo pageInfo =
        new PageInfo(
            pageable.getPageNumber() + 1,
            conversations.getTotalPages(),
            conversations.getTotalElements());

    return ResponseDataAPI.success(
        conversations.stream()
            .map(
                conversation ->
                    conversationMapper.toConversationInfoResponse(
                        conversation, fileMapper.toFileInfoResponse(conversation.getFile()))),
        pageInfo);
  }

  @Override
  public ConversationInfoResponse getDetailConversation(UUID userId, UUID conversationId) {
    Conversation conversation = this.findById(conversationId);

    if (!conversation.getUser().getId().equals(userId)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }

    return conversationMapper.toConversationInfoResponse(
        conversation, fileMapper.toFileInfoResponse(conversation.getFile()));
  }

  @Override
  public Conversation findById(UUID conversationId) {
    return conversationRepository
        .findById(conversationId)
        .orElseThrow(() -> new NotFoundException(MessageConstant.CONVERSATION_NOT_FOUND));
  }

  @Override
  public Conversation checkConversationOwnership(UUID conversationId, UUID userId) {
    Conversation conversation = this.findById(conversationId);
    if (!conversation.getUser().getId().equals(userId)) {
      throw new ForbiddenException(MessageConstant.FORBIDDEN_ERROR);
    }
    return conversation;
  }

  @Override
  public void deleteConversation(UUID conversationId, UUID userId) {
    Conversation conversation = this.checkConversationOwnership(conversationId, userId);

    conversation.setDeletedAt(CommonFunction.getCurrentDateTime());
    conversationRepository.save(conversation);
  }

  @Override
  public ConversationInfoResponse updateInfoConversation(
      UUID userId, UUID conversationId, UpdateConversationRequest updateConversationRequest) {
    Conversation conversation = this.checkConversationOwnership(conversationId, userId);

    conversation.setName(updateConversationRequest.getName());

    conversationRepository.save(conversation);

    return conversationMapper.toConversationInfoResponse(
        conversation, fileMapper.toFileInfoResponse(conversation.getFile()));
  }

  @Override
  public ResponseDataAPI getListConversationByAdmin(Pageable pageable) {
    Page<Conversation> conversations = conversationRepository.getListConversationByAdmin(pageable);

    PageInfo pageInfo =
        new PageInfo(
            pageable.getPageNumber() + 1,
            conversations.getTotalPages(),
            conversations.getTotalElements());

    return ResponseDataAPI.success(
        conversations.stream()
            .map(
                conversation ->
                    conversationMapper.toConversationInfoResponse(
                        conversation, fileMapper.toFileInfoResponse(conversation.getFile()))),
        pageInfo);
  }
}
