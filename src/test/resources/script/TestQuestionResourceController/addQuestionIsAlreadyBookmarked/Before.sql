TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE question CASCADE;
TRUNCATE TABLE bookmarks CASCADE;

INSERT INTO role (id, name)
VALUES (100, 'ROLE_USER');

INSERT INTO user_entity (id, email, is_enabled, last_redaction_date, password, role_id)
VALUES (100, '0@mail.com', true, now(), '$2a$10$lwhIK6IeCSg0NKbEmmKYFOUcxZ8lWvZUwh/3EaxGJloam.IwOwtFi', 100);

INSERT INTO question (id, last_redaction_date, title, description, user_id)
VALUES (100, now(), 'test', 'test', 100);