-- liquibase formatted sql

-- changeset duchuy:1748669667468-1
CREATE TABLE mind_maps
(
    id             UUID NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    deleted_at     TIMESTAMP WITHOUT TIME ZONE,
    payload        JSONB,
    conversation_id UUID,
    CONSTRAINT pk_mind_maps PRIMARY KEY (id)
);

-- changeset duchuy:1748669667468-2
ALTER TABLE mind_maps
    ADD CONSTRAINT FK_MIND_MAPS_ON_CONVERATION FOREIGN KEY (conversation_id) REFERENCES conversations (id);

-- changeset duchuy:1748669826699-rename-column typo
ALTER TABLE messages
    RENAME COLUMN converation_id TO conversation_id;

-- changeset duchuy:1748669826699-drop-old-fk
ALTER TABLE messages
    DROP CONSTRAINT FK_MESSAGES_ON_CONVERATION;

-- changeset duchuy:1748669826699-3
ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversations (id);

