create table mobile_app_config
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   appid                varchar(50) not null comment '外系统应用id',
   secret               varchar(50) not null comment '外系统应用密钥',
   type_id              bigint not null comment '应用类型',
   primary key (id)
);

alter table mobile_app_config comment '移动应用配置信息';

create unique index unique_appid on mobile_app_config
(
   appid,
   type_id
);

create table mobile_simple_type
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   class_type           varchar(255) not null comment '类类型',
   serial_number        int(5) not null default 0 comment '序号',
   primary key (id)
);

alter table mobile_simple_type comment '移动端简单类型';

create unique index unique_code on mobile_simple_type
(
   code,
   class_type
);

create table mobile_user_bind
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   openid               varchar(50) not null comment '外系统应用id',
   update_time          datetime not null comment '更新时间',
   expired_time         datetime not null comment '到期时间',
   type_id              bigint not null comment '应用类型',
   employee_code        varchar(50) not null comment '登陆工号',
   token                varchar(225) comment '登陆token',
   primary key (id)
);

alter table mobile_user_bind comment '移动用户绑定信息';

create unique index unique_openid on mobile_user_bind
(
   openid,
   type_id
);

alter table mobile_app_config add constraint fk_mobile_app_type foreign key (type_id)
      references mobile_simple_type (id) on delete restrict on update restrict;

alter table mobile_user_bind add constraint fk_user_bind_type foreign key (type_id)
      references mobile_simple_type (id) on delete restrict on update restrict;

