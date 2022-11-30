create table users
(
    id       uuid         not null,
    username varchar(30)  not null unique,
    password varchar(255) not null,
    email    varchar(30)  not null unique,
    roles    varchar(10)  not null,
    primary key (id)
);

create table black_listed_access_tokens
(
    id               uuid      not null,
    token            text      not null,
    date_of_inserted timestamp not null default current_timestamp,
    primary key (id)
);