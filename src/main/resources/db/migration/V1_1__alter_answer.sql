alter table answer
    add column edit_moderator_id int8,
    add constraint answer_editModerator_fk
        foreign key (edit_moderator_id)
            references user_entity;
