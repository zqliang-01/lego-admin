create table sharding_algorithms
(
   id                   int not null,
   code                 varchar(50) not null comment '编码',
   name                 varchar(50) not null comment '名称',
   version              int not null default 1,
   enable               int not null default 0 comment '生效',
   create_time          datetime not null,
   description          varchar(255) comment '描述',
   template_id          int not null comment '模板',
   config_id            int not null comment '规则',
   primary key (id)
);

alter table sharding_algorithms comment '算法表';

create unique index code_unique on sharding_algorithms
(
   code
);

create table sharding_config
(
   id                   int not null auto_increment,
   code                 varchar(50) not null comment '编码',
   name                 varchar(50) not null comment '名称',
   version              int not null default 1,
   enable               int not null default 0 comment '生效',
   create_time          datetime not null,
   description          varchar(255) comment '描述',
   primary key (id)
);

alter table sharding_config comment '规则配置表';

create unique index code_unique on sharding_config
(
   code
);

create table sharding_data_source
(
   id                   int not null,
   code                 varchar(50) not null comment '编码',
   name                 varchar(50) not null comment '名称',
   version              int not null default 1,
   enable               int not null default 0 comment '生效',
   create_time          datetime not null,
   description          varchar(255) comment '描述',
   template_id          int not null comment '模板',
   primary key (id)
);

alter table sharding_data_source comment '数据源配置表';

create unique index code_unique on sharding_data_source
(
   code
);

create table sharding_properties
(
   id                   int not null,
   code                 varchar(50) not null comment '编码',
   name                 varchar(1000) not null comment '值',
   version              int not null default 1,
   enable               int not null default 0 comment '生效',
   create_time          datetime not null,
   description          varchar(255) comment '描述',
   config_id            int comment '配置',
   template_id          int not null comment '模板',
   entity_id            int not null comment '归属id',
   primary key (id)
);

alter table sharding_properties comment '配置表';

create unique index code_unique on sharding_properties
(
   code,
   entity_id
);

create table sharding_table
(
   id                   int not null,
   code                 varchar(50) not null comment '编码',
   name                 varchar(50) not null comment '名称',
   version              int not null default 1,
   enable               int not null default 0 comment '生效',
   create_time          datetime not null,
   description          varchar(255) comment '描述',
   logic_table_name     varchar(255) not null comment '逻辑表名称',
   actual_data_nodes    varchar(1000) not null comment '物理表表达式',
   algorithm_id         int comment '算法',
   sharding_column      varchar(255) comment '本片字段',
   template_id          int not null comment '模板',
   config_id            int not null comment '规则',
   data_source_id       int,
   primary key (id)
);

alter table sharding_table comment '表规则配置表';

create unique index code_unique on sharding_table
(
   code
);

create table sharding_template
(
   id                   int not null auto_increment,
   code                 varchar(50) not null comment '编码',
   name                 varchar(50) not null comment '名称',
   version              int not null default 1,
   enable               int not null default 0 comment '生效',
   create_time          datetime not null,
   description          varchar(255) comment '描述',
   json                 text comment '模板内容',
   type_id              int not null comment '模板类型',
   primary key (id)
);

alter table sharding_template comment '模板表，配置各类操作模板数据';

create unique index code_unique on sharding_template
(
   code
);

create table sharding_template_type
(
   id                   int not null auto_increment,
   code                 varchar(50) not null comment '编码',
   name                 varchar(50) not null comment '名称',
   version              int not null default 1,
   enable               int not null default 0 comment '生效',
   create_time          datetime not null,
   description          varchar(255) comment '描述',
   primary key (id)
);

alter table sharding_template_type comment '模板类型表';

create unique index code_unique on sharding_template_type
(
   code
);

alter table sharding_algorithms add constraint fk_algorithms_rule foreign key (config_id)
      references sharding_config (id) on delete restrict on update restrict;

alter table sharding_algorithms add constraint fk_algorithms_template foreign key (template_id)
      references sharding_template (id) on delete restrict on update restrict;

alter table sharding_data_source add constraint fk_data_source_template foreign key (template_id)
      references sharding_template (id) on delete restrict on update restrict;

alter table sharding_properties add constraint fk_properties_rule foreign key (config_id)
      references sharding_config (id) on delete restrict on update restrict;

alter table sharding_properties add constraint fk_properties_template foreign key (template_id)
      references sharding_template (id) on delete restrict on update restrict;

alter table sharding_table add constraint fk_table_algorithms foreign key (algorithm_id)
      references sharding_algorithms (id) on delete restrict on update restrict;

alter table sharding_table add constraint fk_table_datasource foreign key (data_source_id)
      references sharding_data_source (id) on delete restrict on update restrict;

alter table sharding_table add constraint fk_table_rule_config_rule foreign key (config_id)
      references sharding_config (id) on delete restrict on update restrict;

alter table sharding_table add constraint fk_table_rule_config_template foreign key (template_id)
      references sharding_template (id) on delete restrict on update restrict;

alter table sharding_template add constraint fk_template_type foreign key (type_id)
      references sharding_template_type (id) on delete restrict on update restrict;

