create table doc_book
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_code         varchar(50) not null comment '创建人',
   enable               int(1) not null default 0 comment '是否生效',
   description          varchar(1000) comment '描述',
   open                 int(1) not null default 0 comment '是否公开',
   cover_id             bigint comment '封面',
   update_time          datetime comment '更新时间',
   sn                   int(5) not null default 0 comment '序号',
   primary key (id)
);

alter table doc_book comment '知识库图书';

create unique index unique_code on doc_book
(
   code
);

create table doc_collect
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_code         varchar(50) not null comment '创建人',
   enable               int(1) not null default 0 comment '是否生效',
   node_id              bigint not null comment '节点',
   primary key (id)
);

alter table doc_collect comment '知识库收藏表';

create unique index unique_code on doc_collect
(
   code
);

create unique index unique_node on doc_collect
(
   creator_code,
   node_id
);

create table doc_file
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_code         varchar(50) not null comment '创建人',
   type_id              bigint not null comment '类型',
   size                 bigint not null comment '大小（字节）',
   path                 varchar(1000) comment '存储路径',
   location_id          bigint not null comment '存储位置',
   enable               int(1) not null default 0 comment '生效',
   primary key (id)
);

alter table doc_file comment '知识库文件表';

create unique index unique_code on doc_file
(
   code
);

create table doc_node
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   class_type           varchar(255) not null comment '类类型',
   creator_code         varchar(50) not null comment '创建人',
   enable               int(1) not null default 0 comment '是否生效',
   description          varchar(1000) comment '描述',
   page_id              bigint comment '内容',
   file_id              bigint comment '文件',
   book_id              bigint not null comment '归属图书',
   type_id              bigint not null comment '节点类型',
   parent_id            bigint comment '父节点',
   update_time          datetime comment '更新时间',
   sn                   int(5) not null default 0 comment '序号',
   primary key (id)
);

alter table doc_node comment '知识库图书节点';

create unique index unique_code on doc_node
(
   code
);

create table doc_page
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_code         varchar(50) not null comment '创建人',
   enable               int(1) not null default 0 comment '是否生效',
   content              longtext comment '内容',
   book_id              bigint not null comment '图书',
   primary key (id)
);

alter table doc_page comment '知识库文章表';

create unique index unique_code on doc_page
(
   code
);

create table doc_page_dept
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_code         varchar(50) not null comment '创建人',
   dept_code            varchar(50) not null comment '部门编码',
   page_id              bigint not null comment '文章',
   primary key (id)
);

alter table doc_page_dept comment '知识库文章协作部门';

create unique index unique_code on doc_page_dept
(
   code
);

create table doc_page_user
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_code         varchar(50) not null comment '创建人',
   user_code            varchar(50) not null comment '用户编码',
   page_id              bigint not null comment '文章',
   primary key (id)
);

alter table doc_page_user comment '知识库文章协作用户';

create unique index unique_code on doc_page_user
(
   code
);

create table doc_simple_type
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

alter table doc_simple_type comment '知识库简单类型';

create unique index unique_code on doc_simple_type
(
   code,
   class_type
);

alter table doc_book add constraint fk_book_cover foreign key (cover_id)
      references doc_file (id) on delete restrict on update restrict;

alter table doc_collect add constraint fk_collect_node foreign key (node_id)
      references doc_node (id) on delete restrict on update restrict;

alter table doc_file add constraint fk_file_location foreign key (location_id)
      references doc_simple_type (id) on delete restrict on update restrict;

alter table doc_file add constraint fk_file_type foreign key (type_id)
      references doc_simple_type (id) on delete restrict on update restrict;

alter table doc_node add constraint fk_node_book foreign key (book_id)
      references doc_book (id) on delete restrict on update restrict;

alter table doc_node add constraint fk_node_file foreign key (file_id)
      references doc_file (id) on delete restrict on update restrict;

alter table doc_node add constraint fk_node_page foreign key (page_id)
      references doc_page (id) on delete restrict on update restrict;

alter table doc_node add constraint fk_node_type foreign key (type_id)
      references doc_simple_type (id) on delete restrict on update restrict;

alter table doc_page add constraint fk_page_book foreign key (book_id)
      references doc_book (id) on delete restrict on update restrict;

alter table doc_page_dept add constraint fk_dept_page foreign key (page_id)
      references doc_page (id) on delete restrict on update restrict;

alter table doc_page_user add constraint fk_user_page foreign key (page_id)
      references doc_page (id) on delete restrict on update restrict;

