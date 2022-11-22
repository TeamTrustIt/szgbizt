create table users
(
    id      uuid    not null,
    balance numeric not null,
    primary key (id)
);

create table caff_data
(
    id          uuid        not null,
    file        text        not null,
    name        varchar(30) not null,
    description text,
    price       numeric     not null,
    upload_date date        not null,
    user_id     uuid,
    primary key (id),
    foreign key (user_id) references users (id)
);

create table comment
(
    id           uuid not null,
    message      text,
    upload_date  date not null,
    user_id      uuid,
    caff_data_id uuid,
    primary key (id),
    foreign key (user_id) references users (id),
    foreign key (caff_data_id) references caff_data (id)
)