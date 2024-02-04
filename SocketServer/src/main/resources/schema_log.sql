drop table if exists log;

create table log (
         id bigserial primary key,
         user_id bigint references users(id),
         room_id bigint references room(id),
         message_index bigint
);