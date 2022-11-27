create table if not exists users
(
    id    bigint generated by default as identity
        primary key,
    email varchar(255) not null
        constraint uk_email_user
            unique,
    name  varchar(255)
);

create table if not exists categories
(
    id   bigint generated by default as identity
        primary key,
    name varchar(255) not null
        constraint uk_name_categories
            unique
);

create table if not exists event
(
    id                 bigint generated by default as identity
        primary key,
    annotation         varchar(2000),
    confirmed_requests bigint,
    created_on         timestamp,
    description        varchar(7000),
    event_date         timestamp,
    desc_location      varchar(255),
    lat                real,
    lon                real,
    paid               boolean,
    participant_limit  integer,
    published_on       timestamp,
    request_moderation boolean,
    state              integer,
    title              varchar(255),
    views              bigint,
    categories_id      bigint
        constraint fk_event_categories_id
            references categories,
    user_id            bigint
        constraint fk_event_user_id
            references users
);

create table if not exists request
(
    id           bigint generated by default as identity
        primary key,
    created      timestamp,
    status       integer,
    event_id     bigint
        constraint fk_request_event_id
            references event,
    requester_id bigint
        constraint fk_requester_id
            references users
);

create table if not exists compilations
(
    id     bigint generated by default as identity
        primary key,
    pinned boolean,
    title  varchar(255)
);

create table if not exists compilations_events
(
    compilations_id bigint not null
        constraint fk_compilations_id
            references compilations,
    events_id       bigint not null
        constraint uk_events_id
            unique
        constraint fk_events_id
            references event,
    primary key (compilations_id, events_id)
);

create table if not exists comment
(
    id          bigint generated by default as identity
        primary key,
    date_create timestamp,
    date_edit   timestamp,
    edited      boolean,
    state       integer,
    text        varchar(2000),
    author_id   bigint
        constraint fk_comment_user
            references users,
    event_id    bigint
        constraint fk_comment_event
            references event
);

alter table users
    owner to root;

alter table categories
    owner to root;

alter table event
    owner to root;

alter table request
    owner to root;

alter table compilations
    owner to root;

alter table compilations_events
    owner to root;

alter table comment
    owner to root;