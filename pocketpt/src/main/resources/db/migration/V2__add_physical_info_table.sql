create table physical_info (
    physical_info_id bigint not null auto_increment,
    created_at datetime(6),
    is_deleted boolean default false not null,
    updated_at datetime(6),
    bmi float(23),
    body_fat_percentage float(23),
    date date,
    inbody_score integer,
    skeletal_muscle_mass float(23),
    waist_hip_ratio float(23),
    weight float(23),
    account_id bigint,
    primary key (physical_info_id)
) engine=InnoDB;

alter table physical_info
    add constraint FK5j0s164qqdkhu58ye4mxjox5e
    foreign key (account_id)
    references account (account_id);

alter table physical_info
    add constraint physical_info_uk unique (account_id, date);