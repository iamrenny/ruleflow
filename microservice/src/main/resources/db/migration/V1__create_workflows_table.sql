create table if not exists workflows
(
  id bigserial
     constraint workflows_pk
     primary key,
  country_code char(2) not null,
  name varchar(100) not null,
  version bigint not null,
  workflow text not null,
  user_id text not null,
  created_at timestamp(6)
);

create unique index workflows_country_name_version_uindex
  on workflows (country_code, name, version);