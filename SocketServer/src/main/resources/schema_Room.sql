drop table if exists room;

create table room (
             id bigserial primary key,
             name text not null,
             user_id bigint references users(id)

);