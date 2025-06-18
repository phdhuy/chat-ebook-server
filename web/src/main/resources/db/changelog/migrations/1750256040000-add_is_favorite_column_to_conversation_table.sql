-- liquibase formatted sql

-- changeset duchuy:1750256058252-1
ALTER TABLE conversations
    ADD is_favorite BOOLEAN;
