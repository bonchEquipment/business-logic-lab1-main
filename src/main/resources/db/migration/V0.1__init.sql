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
    nick_name varchar unique,
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

create table if not exists rutube_account
(
    id      uuid primary key,
    user_id uuid,
    value   decimal check (value >= 0)
);

create table if not exists bank_account
(
    id      uuid primary key,
    user_id uuid,
    value   decimal check (value >= 0)
);

create table if not exists subscription_type
(
    name varchar primary key,
    monthly_pay_rub int
);

create table if not exists user_subscription(
    id uuid primary key,
    subscription_name varchar,
    user_id uuid,
    expiration_date timestamp
)
