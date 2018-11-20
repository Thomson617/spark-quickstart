create table iplocation
(
  id        bigint auto_increment
    primary key,
  longitude varchar(30) not null,
  latitude  varchar(30) not null,
  city      varchar(30) null,
  ips_count int         null
)
  engine = InnoDB;