TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE chat CASCADE;

INSERT INTO role (id, name)
VALUES (100, 'ROLE_USER');

INSERT INTO user_entity (id, email, is_enabled, last_redaction_date, password, role_id)
VALUES (100, '0@mail.com', true, now(), '$2a$10$sas6S77dkMjtev/s.77JousVSc7hbodeq/bl7TB1tQ2ENd5eFFWQK', 100);

INSERT INTO chat (id)
VALUES (100);

INSERT INTO group_chat (chat_id, is_global)
VALUES (100, true);

INSERT INTO message (id, last_redaction_date, message, chat_id, user_sender_id)
VALUES (generate_series(100, 121), now(), CONCAT('message ', generate_series(100, 121)), 100, 100);