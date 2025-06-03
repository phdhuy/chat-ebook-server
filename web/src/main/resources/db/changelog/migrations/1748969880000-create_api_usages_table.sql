-- liquibase formatted sql

-- changeset duchuy:1748969911207-1
CREATE TABLE api_usages
(
    id             UUID NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    resource_type  VARCHAR(255),
    request_status VARCHAR(255),
    user_id        UUID,
    CONSTRAINT pk_api_usages PRIMARY KEY (id)
);

-- changeset duchuy:1748969911207-2
ALTER TABLE api_usages
    ADD CONSTRAINT FK_API_USAGES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

