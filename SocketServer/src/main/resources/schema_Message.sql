drop table if exists message;

create table message (
       id bigserial primary key,
       user_id bigint references users(id),
       room_id bigint references room(id),
       message_text text not null,
       date_time timestamp

);