create table report_condition
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   sn                   int(5) not null default 1 comment '序列',
   enable               int(1) not null default 0 comment '是否生效',
   definition_id        bigint not null comment '报表定义',
   dependent_code       varchar(50) comment '依赖条件',
   data_definition_id   bigint comment '数据集定义',
   required             int(1) not null default 0 comment '是否必须',
   default_value        varchar(255) comment '默认值',
   type                 varchar(50) not null comment '类型',
   sql_key              varchar(50) not null comment '字段',
   primary key (id)
);

alter table report_condition comment '报表条件表';

create unique index unique_code on report_condition
(
   code
);

create table report_definition
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   data_source          varchar(255) comment '数据源',
   sn                   int(5) not null default 1 comment '序列',
   enable               int(1) not null default 0 comment '是否生效',
   sql_text             longtext not null comment 'sql脚本',
   max_export_size      int(8) not null default 1000 comment '最大导出数量',
   creator_code         varchar(50) not null comment '创建人',
   type                 varchar(50) not null comment '类型',
   primary key (id)
);

alter table report_definition comment '报表定义表';

create unique index unique_code on report_definition
(
   code
);

create table report_design
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   sn                   int(5) not null default 1 comment '序列',
   enable               int(1) not null default 0 comment '是否生效',
   creator_code         varchar(50) not null comment '创建人',
   definition_id        bigint not null comment '报表定义',
   position             varchar(50) not null comment '位置',
   chart_type           varchar(50) comment '图表类型',
   data_dimension       varchar(50) comment '维度列编码',
   primary key (id)
);

alter table report_design comment '报表设计';

create unique index unique_code on report_design
(
   code
);

create table report_design_category
(
   design_id            bigint not null comment 'id',
   data_category        varchar(50) not null comment '编码',
   primary key (design_id, data_category)
);

alter table report_design_category comment '报表设计分类';

create table report_design_sort
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   sn                   int(5) not null default 1 comment '序列',
   enable               int(1) not null default 0 comment '是否生效',
   creator_code         varchar(50) not null comment '创建人',
   design_code          bigint not null comment '报表设计编码',
   primary key (id)
);

alter table report_design_sort comment '首页排序';

create unique index unique_code on report_design_sort
(
   code
);

create table report_title
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   sn                   int(5) not null default 1 comment '序列',
   align                varchar(50) not null comment '对齐方式',
   definition_id        bigint not null comment '定义',
   sql_key              varchar(50) not null comment '字段',
   primary key (id)
);

alter table report_title comment '报表表头表';

create unique index unique_code on report_title
(
   code
);

alter table report_condition add constraint fk_condition_definition foreign key (definition_id)
      references report_definition (id) on delete restrict on update restrict;

alter table report_condition add constraint fk_data_definition foreign key (data_definition_id)
      references report_definition (id) on delete restrict on update restrict;

alter table report_design add constraint fk_design_definition foreign key (definition_id)
      references report_definition (id) on delete restrict on update restrict;

alter table report_design_category add constraint fk_design_category foreign key (design_id)
      references report_design (id) on delete restrict on update restrict;

alter table report_title add constraint fk_title_definition foreign key (definition_id)
      references report_definition (id) on delete restrict on update restrict;

