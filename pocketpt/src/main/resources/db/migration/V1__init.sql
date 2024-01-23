create table account (
    discount_rate float(23),
    height float(23),
    is_deleted boolean default false not null,
    service_fee_rate float(23),
    total_sales integer default 0,
    weight float(23),
    account_id bigint not null auto_increment,
    birthdate datetime(6),
    created_at datetime(6),
    oauth2_id bigint,
    updated_at datetime(6),
    account_role varchar(255),
    career_certificate_url varchar(255),
    email varchar(255),
    expertise varchar(255),
    gender varchar(255),
    identification_code varchar(255),
    introduce varchar(255),
    name varchar(255),
    nickname varchar(255),
    oauth_access_token varchar(255),
    password varchar(255),
    phone_number varchar(255),
    profile_picture_url varchar(255),
    provider varchar(255),
    recommender_code varchar(255),
    primary key (account_id)
) engine=InnoDB;

create table career (
    is_deleted boolean default false not null,
    account_id bigint,
    career_id bigint not null auto_increment,
    created_at datetime(6),
    updated_at datetime(6),
    date varchar(255),
    title varchar(255),
    type varchar(255),
    primary key (career_id)
) engine=InnoDB;

create table chatting_message (
    is_deleted boolean default false not null,
    is_edited bit not null,
    not_view_count integer not null,
    account bigint,
    chatting_message_id bigint not null auto_increment,
    chatting_room bigint,
    created_at datetime(6),
    updated_at datetime(6),
    content varchar(255),
    file_url TEXT,
    primary key (chatting_message_id)
) engine=InnoDB;

create table chatting_message_bookmark (
    is_deleted boolean default false not null,
    account_id bigint not null,
    chatting_message_id bigint not null,
    chatting_room_id bigint not null,
    created_at datetime(6),
    updated_at datetime(6),
    primary key (account_id, chatting_message_id, chatting_room_id)
) engine=InnoDB;

create table chatting_participant (
    is_deleted boolean default false not null,
    is_host bit not null,
    not_view_count integer not null,
    account_id bigint not null,
    chatting_room_entry_time datetime(6),
    chatting_room_exit_time datetime(6),
    chatting_room_id bigint not null,
    created_at datetime(6),
    updated_at datetime(6),
    simp_session_id varchar(255),
    primary key (account_id, chatting_room_id)
) engine=InnoDB;

create table chatting_room (
    is_deleted boolean default false not null,
    number_of_participant integer not null,
    status tinyint not null,
    chatting_room_id bigint not null auto_increment,
    created_at datetime(6),
    host_id bigint not null,
    updated_at datetime(6),
    room_name varchar(255),
    primary key (chatting_room_id)
) engine=InnoDB;

create table historical_data (
    is_deleted boolean default false not null,
    account_id bigint not null,
    created_at datetime(6),
    date datetime(6),
    historical_data_id bigint not null auto_increment,
    updated_at datetime(6),
    description varchar(255),
    scope varchar(255),
    title varchar(255),
    primary key (historical_data_id)
) engine=InnoDB;

create table historical_data_file (
    is_deleted boolean default false not null,
    created_at datetime(6),
    historical_data_file_id bigint not null auto_increment,
    historical_data_id bigint not null,
    updated_at datetime(6),
    file_url varchar(255),
    scope varchar(255),
    primary key (historical_data_file_id)
) engine=InnoDB;

create table monthly_pt_price (
    is_deleted boolean default false not null,
    period integer,
    price integer,
    account_id bigint,
    created_at datetime(6),
    monthly_pt_price_id bigint not null auto_increment,
    updated_at datetime(6),
    primary key (monthly_pt_price_id)
) engine=InnoDB;

create table pt_matching (
    is_deleted boolean default false not null,
    is_new_subscription bit,
    payment_amount integer,
    subscription_period integer,
    created_at datetime(6),
    expired_date datetime(6),
    pt_matching_id bigint not null auto_increment,
    start_date datetime(6),
    trainee_account_id bigint,
    trainer_account_id bigint,
    updated_at datetime(6),
    contact_type varchar(255),
    precaution varchar(255),
    reject_reason varchar(255),
    status varchar(255),
    primary key (pt_matching_id)
) engine=InnoDB;

create table purpose (
    is_deleted boolean default false not null,
    target_date date,
    account_id bigint,
    created_at datetime(6),
    purpose_id bigint not null auto_increment,
    updated_at datetime(6),
    content varchar(255),
    title varchar(255),
    primary key (purpose_id)
) engine=InnoDB;

create table top_chatting_room (
    is_deleted boolean default false not null,
    account_id bigint not null,
    chatting_room_id bigint not null,
    created_at datetime(6),
    updated_at datetime(6),
    primary key (account_id, chatting_room_id)
) engine=InnoDB;

alter table account
   add constraint UK_83mc3wdw5cs7otc48exl2g9oa unique (identification_code);

alter table career
   add constraint FKjsolsxeh4qap0ga7sidb5ol70
   foreign key (account_id)
   references account (account_id);

alter table chatting_message_bookmark
   add constraint FK401m1640clwl9coknhu8utwms
   foreign key (account_id)
   references account (account_id);

alter table chatting_message_bookmark
   add constraint FK2xr546hkhv3nt4tvm8qx9ais9
   foreign key (chatting_message_id)
   references chatting_message (chatting_message_id);

alter table chatting_message_bookmark
   add constraint FKf2pp5gljs27gb65ry9g5cn8as
   foreign key (chatting_room_id)
   references chatting_room (chatting_room_id);

alter table chatting_participant
   add constraint FKo72wxi8iuobqia3ib16l7wngw
   foreign key (account_id)
   references account (account_id);

alter table chatting_participant
   add constraint FK7qrx8qm8j9udnlvryi0obyy02
   foreign key (chatting_room_id)
   references chatting_room (chatting_room_id);

alter table historical_data
   add constraint FKa9y3h19bvcclpixx3ogr3c7nb
   foreign key (account_id)
   references account (account_id);

alter table historical_data_file
   add constraint FKeara218058068op1wpf8err2t
   foreign key (historical_data_id)
   references historical_data (historical_data_id);

alter table monthly_pt_price
   add constraint FKlgvwurtxja3r9bsxf5b9bflwd
   foreign key (account_id)
   references account (account_id);

alter table pt_matching
   add constraint FK2uqsyc1k5sfhxs2wekbqppmgg
   foreign key (trainee_account_id)
   references account (account_id);

alter table pt_matching
   add constraint FKeohpalxemt7bjhd7jurrxprxi
   foreign key (trainer_account_id)
   references account (account_id);

alter table purpose
   add constraint FKj3p0cuhk14r0u44okaiyqdw7g
   foreign key (account_id)
   references account (account_id);

alter table top_chatting_room
   add constraint FKnvm80wb2hv5eed2f5soafe73q
   foreign key (account_id)
   references account (account_id);

alter table top_chatting_room
   add constraint FK50uww0n32n3cce3myx4g27lib
   foreign key (chatting_room_id)
   references chatting_room (chatting_room_id);
