
create table post (
    id bigint not null,
    anons varchar(255) not null,
    full_text varchar(2048) not null,
    title varchar(255) not null,
    views integer not null,
    viewsPerMonth integer not null,
    viewsPerWeek integer not null,
    user_id bigint,
    primary key (id)
) engine=MyISAM;

create table user (
    id bigint not null,
    activation_code varchar(255),
    active bit not null,
    email varchar(255),
    password varchar(255) not null,
    newPassword varchar(255),
    newEmail varchar(255),
    username varchar(255) not null,
    primary key (id)
) engine=MyISAM;

create table user_role (
    user_id bigint not null,
    roles varchar(255)
) engine=MyISAM;

create table user_subscriptions (
    channel_id bigint not null references user,
    subscriber_id bigint not null references user,
    primary key (channel_id, subscriber_id)
) engine=MyISAM;

create table post_likes (
    user_id bigint not null references user,
    post_id bigint not null references post,
    primary key (user_id, post_id)
) engine=MyISAM;

create table post_dislikes (
    user_id bigint not null references user,
    post_id bigint not null references post,
    primary key (user_id, post_id)
) engine=MyISAM;

create table post_oks (
    user_id bigint not null references user,
    post_id bigint not null references post,
    primary key (user_id, post_id)
) engine=MyISAM;

alter table post
    add constraint post_user_fk
    foreign key (user_id) references user (id);

alter table user_role
    add constraint user_role_user_fk
    foreign key (user_id) references user (id);
