create table movies
(
    id  varchar(50) not null constraint movies_pk primary key,
    title varchar(500) not null,
    release_year integer not null,
    imdb varchar(20) not null,
    likes integer not null default 0,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);
