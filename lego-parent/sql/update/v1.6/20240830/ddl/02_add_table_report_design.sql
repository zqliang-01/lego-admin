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

alter table report_design add constraint fk_design_definition foreign key (definition_id)
      references report_definition (id) on delete restrict on update restrict;

create table report_design_category
(
   design_id            bigint not null comment 'id',
   data_category        varchar(50) not null comment '编码',
   primary key (design_id, data_category)
);

alter table report_design_category comment '报表设计分类';

alter table report_design_category add constraint fk_design_category foreign key (design_id)
      references report_design (id) on delete restrict on update restrict;

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
