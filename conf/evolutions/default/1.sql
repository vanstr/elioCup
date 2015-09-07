# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table day_statistic (
  id                        bigserial not null,
  user_id                   bigint,
  online                    boolean,
  date                      timestamp,
  first_time_online         timestamp,
  last_time_online          timestamp,
  availability_in_seconds   bigint,
  constraint pk_day_statistic primary key (id))
;

create table user_table (
  id                        bigserial not null,
  name                      varchar(255),
  active                    boolean,
  constraint pk_user_table primary key (id))
;

alter table day_statistic add constraint fk_day_statistic_user_1 foreign key (user_id) references user_table (id);
create index ix_day_statistic_user_1 on day_statistic (user_id);



# --- !Downs

drop table if exists day_statistic cascade;

drop table if exists user_table cascade;

