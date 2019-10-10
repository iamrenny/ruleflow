create table if not exists workflows
(
  id bigserial
     constraint workflows_pk
     primary key,
  name varchar(100) not null,
  version bigint not null,
  workflow text not null
);

create unique index workflows_name_version_uindex
  on workflows (name, version);