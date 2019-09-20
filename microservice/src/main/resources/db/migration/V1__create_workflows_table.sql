create table public.workflows
(
  id serial
     constraint workflows_pk
     primary key,
  name varchar(100) not null,
  version int not null,
  workflow text not null
);

create unique index workflows_name_version_uindex
  on public.workflows (name, version);