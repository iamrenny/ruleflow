create table if not exists active_workflows
(
	id bigserial not null
		constraint active_workflows_pk
			primary key,
	name varchar(100) not null,
	version bigint not null,
	created_at timestamp(6) not null,
	updated_at timestamp(6) not null default now()
);

create unique index active_workflows_name_uindex
  on active_workflows (name);

