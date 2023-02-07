TRUNCATE TABLE role CASCADE;
TRUNCATE TABLE user_entity CASCADE;
TRUNCATE TABLE group_chat CASCADE;

INSERT INTO role (id, name)
VALUES (100, 'ROLE_USER');

INSERT INTO user_entity (id, email, password, role_id)
VALUES (100, '0@mail.com', '$2a$10$lwhIK6IeCSg0NKbEmmKYFOUcxZ8lWvZUwh/3EaxGJloam.IwOwtFi', 100);

INSERT INTO group_chat (id)
VALUES (100);