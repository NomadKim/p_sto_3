create sequence message_star_seq start 1 increment 1;
create table message_star (
                              id int8 not null,
                              persist_date timestamp not null,
                              message_id int8 not null,
                              user_id int8 not null,
                              primary key (id)
);

alter table message_star
    add constraint message_star_message_fk
        foreign key (message_id)
            references message;

alter table message_star
    add constraint message_star_user_fk
        foreign key (user_id)
            references user_entity;