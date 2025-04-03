-- liquibase formatted sql

-- changeset duchuy:1743696108242-2
CREATE TABLE oauth_tokens
(
    id            UUID NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    deleted_at    TIMESTAMP WITHOUT TIME ZONE,
    refresh_token UUID,
    revoked_at    TIMESTAMP WITHOUT TIME ZONE,
    user_id       UUID,
    CONSTRAINT pk_oauth_tokens PRIMARY KEY (id)
);

-- changeset duchuy:1743696108242-3
CREATE TABLE users
(
    id                     UUID    NOT NULL,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    deleted_at             TIMESTAMP WITHOUT TIME ZONE,
    email                  VARCHAR(255),
    password               VARCHAR(255),
    role                   VARCHAR(255),
    confirmation_token     UUID,
    confirmed_at           TIMESTAMP WITHOUT TIME ZONE,
    reset_password_token   UUID,
    reset_password_sent_at TIMESTAMP WITHOUT TIME ZONE,
    is_confirmed           BOOLEAN NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

-- changeset duchuy:1743696108242-4
ALTER TABLE oauth_tokens
    ADD CONSTRAINT FK_OAUTH_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

