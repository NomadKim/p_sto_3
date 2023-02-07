TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE comment CASCADE;

INSERT INTO role (id, name)
VALUES (100, 'ROLE_USER');

INSERT INTO user_entity (id, email, is_enabled, last_redaction_date, password, role_id)
VALUES (100, '0@mail.com', true, now(), '$2a$10$sas6S77dkMjtev/s.77JousVSc7hbodeq/bl7TB1tQ2ENd5eFFWQK', 100);

INSERT INTO question (id, description, last_redaction_date, title, user_id)
VALUES (100, 'Question Description 1', now(), 'Question Title 1', 100);

INSERT INTO comment (id, comment_type, text, user_id)
VALUES (generate_series(100, 121), 1, CONCAT('comment ', generate_series(100, 121)), 100);

INSERT INTO comment_question (comment_id, question_id)
VALUES (generate_series(100, 121), 100);
