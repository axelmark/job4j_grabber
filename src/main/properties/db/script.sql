create table rabbit(
    id serial primary key,
    created_date timestamp
);

create table post(
    id serial primary key,
    title varchar(255),
    link varchar(255) unique,
    description text,
    created timestamp
);