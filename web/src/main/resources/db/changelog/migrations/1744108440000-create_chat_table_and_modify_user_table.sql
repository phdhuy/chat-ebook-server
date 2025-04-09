-- liquibase formatted sql

-- changeset duchuy:1744108527714-1
CREATE TABLE conversations
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    name       VARCHAR(255),
    file_id    UUID,
    user_id    UUID,
    CONSTRAINT pk_conversations PRIMARY KEY (id)
);

-- changeset duchuy:1744108527714-2
CREATE TABLE files
(
    id         UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    secure_url VARCHAR(255),
    public_id  VARCHAR(255),
    format     VARCHAR(255),
    pages      INTEGER,
    bytes      DECIMAL,
    CONSTRAINT pk_files PRIMARY KEY (id)
);

-- changeset duchuy:1744108527714-3
CREATE TABLE messages
(
    id             UUID NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    content        VARCHAR(255),
    converation_id UUID,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

-- changeset duchuy:1744108527714-4
ALTER TABLE users
    ADD username VARCHAR(255);

ALTER TABLE users
    ADD avatar_url VARCHAR(255);

-- changeset duchuy:1744108527714-5
ALTER TABLE conversations
    ADD CONSTRAINT uc_conversations_file UNIQUE (file_id);

-- changeset duchuy:1744108527714-6
ALTER TABLE conversations
    ADD CONSTRAINT FK_CONVERSATIONS_ON_FILE FOREIGN KEY (file_id) REFERENCES files (id);

-- changeset duchuy:1744108527714-7
ALTER TABLE conversations
    ADD CONSTRAINT FK_CONVERSATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset duchuy:1744108527714-8
ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CONVERATION FOREIGN KEY (converation_id) REFERENCES conversations (id);

-- changeset duchuy:1744108527714-14
ALTER TABLE users
    DROP COLUMN confirmation_token;
ALTER TABLE users
    DROP COLUMN reset_password_sent_at;
ALTER TABLE users
    DROP COLUMN reset_password_token;

