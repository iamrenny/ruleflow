create table if not exists lists
(
    id serial constraint list_pk primary key,
    list_name VARCHAR(100) unique not null,
    description VARCHAR(200),
    status VARCHAR(20) not null,
    created_at timestamp(6) default now(),
    updated_at timestamp(6) default now(),
    created_by VARCHAR(50) not null,
    last_updated_by VARCHAR(50)
);

create table if not exists list_items
(
    list_id bigint not null references lists(id),
    value text not null,
    constraint list_items_pk primary key (list_id,  value)
);

create table if not exists list_history
(
    id bigserial constraint list_history_pk primary key,
    list_id bigint not null references lists(id),
    modification_type varchar(30) not null,
    created_at timestamp(6) default now(),
    responsible varchar(50) not null,
    change_log JSONB not null
);

create table if not exists lists_workflows
(
    list_id bigint not null references lists(id),
    workflow_id bigint not null references workflows(id),
    constraint lists_workflows_pk primary key (list_id,  workflow_id)
);

create index if not exists list_name_idx ON lists (list_name);