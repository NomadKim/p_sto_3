alter table group_chat
    add column title varchar(255);

alter table chat
    drop column title;