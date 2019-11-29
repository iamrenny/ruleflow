alter table workflows alter column country_code type varchar;
alter table workflows alter column created_at set default now();

create table if not exists active_workflows
(
    country_code varchar not null,
    name varchar(256) not null,
    workflow_id bigint not null references workflows(id),
    primary key (country_code, name)
);

create table if not exists active_workflows_history
(
    id bigserial
        constraint workflows_status_history_pk
        primary key,
    workflow_id bigint not null references workflows(id),
    user_id text not null,
    created_at timestamp(6) default now()
);