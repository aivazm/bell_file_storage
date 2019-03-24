create sequence hibernate_sequence start 2 increment 1;
create extension if not exists pgcrypto;

create table user_role(
  user_id int8 not null,
  roles varchar(255)
);

create table users_file (
  id int8 not null,
  file_name varchar(255),
  user_id int8,
  file_in_bytes bytea,
  download_count int4,
  primary key (id)
);

create table usr (
  id int8 not null,
  activation_code varchar(255),
  active boolean not null,
  email varchar(255) not null,
  password varchar(255) not null,
  user_name varchar(255) not null,
  date_of_registration int8,
  primary key (id)
);

create table request_to_visible_access (
  owner_id int8 not null references usr,
  tenant_id int8 not null references usr,
  primary key (owner_id, tenant_id)
);

create table request_to_download_access (
  owner_id int8 not null references usr,
  tenant_id int8 not null references usr,
  primary key (owner_id, tenant_id)
);

create table visible_access (
  owner_id int8 not null references usr,
  tenant_id int8 not null references usr,
  primary key (owner_id, tenant_id)
);

create table download_access (
  owner_id int8 not null references usr,
  tenant_id int8 not null references usr,
  primary key (owner_id, tenant_id)
);

alter table if exists user_role
  add constraint user_role_user_fk
  foreign key (user_id) references usr;

alter table if exists users_file
  add constraint users_file_user_fk
  foreign key (user_id) references usr;

insert into usr (id, user_name, password, active, email)
values (1, 'admin', '1', true, 'admin@email.com');

insert into user_role (user_id, roles)
values (1, 'USER'), (1, 'ADMIN');

update usr set password = crypt(password, gen_salt('bf', 8));