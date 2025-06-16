-- liquibase formatted sql

-- changeset duchuy:1750077747339-1
CREATE TABLE answer_feedbacks
(
    id                  UUID NOT NULL,
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    updated_at          TIMESTAMP WITHOUT TIME ZONE,
    deleted_at          TIMESTAMP WITHOUT TIME ZONE,
    reason_feedback     VARCHAR(255),
    additional_feedback VARCHAR(255),
    message_id          BIGINT,
    user_id             UUID,
    CONSTRAINT pk_answer_feedbacks PRIMARY KEY (id)
);

-- changeset duchuy:1750077747339-2
ALTER TABLE answer_feedbacks
    ADD CONSTRAINT uc_answer_feedbacks_message UNIQUE (message_id);

-- changeset duchuy:1750077747339-3
ALTER TABLE answer_feedbacks
    ADD CONSTRAINT FK_ANSWER_FEEDBACKS_ON_MESSAGE FOREIGN KEY (message_id) REFERENCES messages (id);

-- changeset duchuy:1750077747339-4
ALTER TABLE answer_feedbacks
    ADD CONSTRAINT FK_ANSWER_FEEDBACKS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
