package com.chatebook.feedback.model;

import com.chatebook.chat.model.Message;
import com.chatebook.common.model.AbstractEntity;
import com.chatebook.feedback.model.enums.ReasonFeedback;
import com.chatebook.security.model.User;
import jakarta.persistence.*;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table
@Builder
@Entity(name = "answer_feedbacks")
public class AnswerFeedback extends AbstractEntity {

  @Id @GeneratedValue private UUID id;

  @Enumerated(EnumType.STRING)
  @Column
  private ReasonFeedback reasonFeedback;

  @Column private String additionalFeedback;

  @OneToOne
  @JoinColumn(name = "message_id")
  private Message message;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
