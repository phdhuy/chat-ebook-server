-- liquibase formatted sql

-- changeset duchuy:1748404461244-1
CREATE TABLE cited_excerpts
(
    id         UUID             NOT NULL,
    source_id  INTEGER          NOT NULL,
    page       INTEGER          NOT NULL,
    score      DOUBLE PRECISION NOT NULL,
    text       TEXT             NOT NULL,
    message_id BIGINT           NOT NULL,
    CONSTRAINT pk_cited_excerpts PRIMARY KEY (id)
);

-- changeset duchuy:1748404461244-2
ALTER TABLE cited_excerpts
    ADD CONSTRAINT FK_CITED_EXCERPTS_ON_MESSAGE FOREIGN KEY (message_id) REFERENCES messages (id);

