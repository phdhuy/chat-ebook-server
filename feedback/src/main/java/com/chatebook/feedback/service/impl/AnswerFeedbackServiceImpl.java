package com.chatebook.feedback.service.impl;

import com.chatebook.chat.mapper.MessageMapper;
import com.chatebook.chat.model.Message;
import com.chatebook.chat.service.MessageService;
import com.chatebook.common.payload.general.PageInfo;
import com.chatebook.common.payload.general.ResponseDataAPI;
import com.chatebook.feedback.mapper.AnswerFeedbackMapper;
import com.chatebook.feedback.model.AnswerFeedback;
import com.chatebook.feedback.model.enums.ReasonFeedback;
import com.chatebook.feedback.payload.request.AnswerFeedbackRequest;
import com.chatebook.feedback.repository.AnswerFeedbackRepository;
import com.chatebook.feedback.service.AnswerFeedbackService;
import com.chatebook.security.mapper.UserMapper;
import com.chatebook.security.model.User;
import com.chatebook.security.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerFeedbackServiceImpl implements AnswerFeedbackService {

  private final AnswerFeedbackRepository answerFeedbackRepository;

  private final UserRepository userRepository;

  private final MessageService messageService;

  private final AnswerFeedbackMapper answerFeedbackMapper;

  private final UserMapper userMapper;

  private final MessageMapper messageMapper;

  @Override
  public void createAnswerFeedback(
      UUID userId, Long messageId, AnswerFeedbackRequest answerFeedbackRequest) {
    User user = userRepository.getReferenceById(userId);
    Message message = messageService.findById(messageId);
    AnswerFeedback answerFeedback =
        AnswerFeedback.builder()
            .user(user)
            .message(message)
            .reasonFeedback(ReasonFeedback.valueOf(answerFeedbackRequest.getReasonFeedback()))
            .additionalFeedback(answerFeedbackRequest.getAdditionalFeedback())
            .isNegativeFeedback(answerFeedbackRequest.getIsNegativeFeedback())
            .build();

    answerFeedbackRepository.save(answerFeedback);
  }

  @Override
  public ResponseDataAPI getAnswerFeedbackByAdmin(Pageable pageable, String query) {
    Page<AnswerFeedback> answerFeedbacks =
        answerFeedbackRepository.getListAnswerFeedbackByAdmin(pageable, query);

    PageInfo pageInfo =
        new PageInfo(
            pageable.getPageNumber() + 1,
            answerFeedbacks.getTotalPages(),
            answerFeedbacks.getTotalElements());

    return ResponseDataAPI.success(
        answerFeedbacks.stream()
            .map(
                answerFeedback ->
                    answerFeedbackMapper.toAnswerFeedbackResponse(
                        answerFeedback,
                        userMapper.toUserInfoResponse(answerFeedback.getUser()),
                        messageMapper.toMessageInfoResponse(
                            answerFeedback.getMessage(),
                            answerFeedback.getMessage().getConversation().getId()))),
        pageInfo);
  }
}
