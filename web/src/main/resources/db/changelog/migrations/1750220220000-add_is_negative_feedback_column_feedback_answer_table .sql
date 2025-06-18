-- liquibase formatted sql

-- changeset duchuy:1750220213144-1
ALTER TABLE answer_feedbacks
    ADD is_negative_feedback BOOLEAN;
