-- liquibase formatted sql

-- changeset duchuy:1745813141954-1
ALTER TABLE messages
    ALTER COLUMN content TYPE TEXT,
    ALTER COLUMN content SET NOT NULL;