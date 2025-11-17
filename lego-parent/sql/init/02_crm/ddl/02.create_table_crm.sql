create table crm_contract
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   update_time          datetime not null comment '更新时间',
   creator_code         varchar(50) not null comment '操作员编码',
   lead_id              bigint not null comment '线索',
   customer_id          bigint not null comment '客户',
   start_time           datetime not null comment '开始时间',
   end_time             datetime not null comment '结束时间',
   owner                varchar(255) not null comment '负责人',
   amount               decimal(10,2) default 0 comment '合同金额',
   check_status         varchar(255) comment '审批状态',
   type                 varchar(255) not null comment '类型',
   province             varchar(255) comment '省份',
   city                 varchar(255) comment '城市',
   area                 varchar(255) comment '地区',
   detail               varchar(1000) comment '详细地址',
   primary key (id)
);

alter table crm_contract comment '合同';

create unique index unique_code on crm_contract
(
   code
);

create table crm_customer
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   update_time          datetime not null comment '更新时间',
   creator_code         varchar(50) not null comment '操作员编码',
   mobile               varchar(50) comment '手机号',
   website              varchar(255) comment '网址',
   email                varchar(255) comment '邮箱',
   check_status         varchar(255) comment '审批状态',
   type                 varchar(255) not null comment '类型',
   primary key (id)
);

alter table crm_customer comment '客户';

create unique index unique_code on crm_customer
(
   code
);

create table crm_lead
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   update_time          datetime not null comment '更新时间',
   creator_code         varchar(50) not null comment '操作员编码',
   mobile               varchar(255) comment '手机号码',
   amount               decimal(10,2) comment '金额',
   birthday             date comment '生日',
   address              varchar(255) comment '地址',
   age                  int(3) comment '年龄',
   size                 decimal(10,2) comment '百分数',
   status               int(1) comment '状态',
   email                varchar(255) comment '邮箱',
   employee             varchar(255) comment '员工',
   dept                 varchar(255) comment '部门',
   customer_id          bigint comment '客户',
   check_status         varchar(255) comment '审批状态',
   source               varchar(255) comment '来源',
   primary key (id)
);

alter table crm_lead comment '线索';

create unique index unique_code on crm_lead
(
   code
);

alter table crm_contract add constraint fk_crm_contract_customer foreign key (customer_id)
      references crm_customer (id) on delete restrict on update restrict;

alter table crm_contract add constraint fk_crm_contract_lead foreign key (lead_id)
      references crm_lead (id) on delete restrict on update restrict;

alter table crm_lead add constraint fk_crm_lead_customer foreign key (customer_id)
      references crm_customer (id) on delete restrict on update restrict;

