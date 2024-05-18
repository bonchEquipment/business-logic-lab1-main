create table if not exists comment
(
    video_id      uuid,
    subscriber_id uuid,
    text          varchar,
    primary key (video_id, subscriber_id)
);

create table if not exists likee
(
    video_id      uuid,
    subscriber_id uuid,
    primary key (video_id, subscriber_id)
);

create table if not exists userr
(
    id        uuid primary key,
    nick_name varchar,
    role      varchar
);

create table if not exists video_approval
(
    video_id        uuid primary key,
    approval_status varchar,
    comment         varchar
);

create table if not exists video
(
    id             uuid primary key,
    title          varchar,
    description    varchar,
    link           varchar,
    user_id        uuid,
    approved_by    uuid,
    content_mp4    bytea not null
);

