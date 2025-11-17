create table sys_gen_table_column_attr
(
   column_id            bigint not null comment 'id',
   name                 varchar(255) not null comment '属性key',
   value                varchar(255) not null comment '属性值',
   primary key (column_id, name)
);

alter table sys_gen_table_column_attr comment '代码生成业务字段属性表';

create unique index unique_code on sys_gen_table_column_attr
(
   name
);
