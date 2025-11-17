create table sys_app_sort
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   sn                   int(5) not null comment '序号',
   permission_id        bigint not null comment '权限',
   employee_id          bigint not null comment '员工',
   primary key (id)
);

alter table sys_app_sort comment '应用排序';

create unique index unique_code on sys_app_sort
(
   code
);

create table sys_code_generator
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   prefix               varchar(255) comment '前缀',
   serial_length        int(5) not null default 1 comment '序号长度',
   date_pattern         varchar(255) comment '日期格式',
   primary key (id)
);

alter table sys_code_generator comment '编码生成器表';

create unique index unique_code on sys_code_generator
(
   code
);

create table sys_column_sort
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   custom_field_id      bigint not null comment '自定义字段id',
   employee_id          bigint not null comment '员工id',
   width                int(5) comment '宽度',
   visible              int(1) not null default 0 comment '是否显示',
   sn                   int(5) not null default 0 comment '序号',
   primary key (id)
);

alter table sys_column_sort comment '表格列排序表';

create unique index unique_code on sys_column_sort
(
   code
);

create table sys_config
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   value                varchar(1000),
   enable               int not null default 1 comment '是否生效',
   primary key (id)
);

alter table sys_config comment '系统配置表';

create unique index unique_code on sys_config
(
   code
);

create table sys_custom_field
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   field_code           varchar(255) comment '属性值（表字段）',
   form_id              bigint not null comment '表单id',
   relative_form_id     bigint comment '关联表单',
   component_name       varchar(255) comment '组件名称',
   default_value        varchar(1000) comment '默认值',
   form_type            varchar(255) comment '表单类型',
   input_tips           varchar(255) comment '提示语',
   style_percent        varchar(255) comment '长度占比',
   option_dict_type     varchar(255) comment '选项字典类型',
   option_data_type     varchar(255) comment '选项类型（自定义/字典）',
   precisions           int(5) comment '支持小数位',
   setting              text comment '自定义选项内容',
   hidden               int(1) default 0 comment '是否隐藏',
   required             int(1) default 0 comment '是否必填',
   uniqueness           int(1) default 0 comment '是否唯一',
   max_num_restrict     int comment '最大值',
   min_num_restrict     int comment '最小值',
   x_axis               int(5) not null default 0 comment '行号',
   y_axis               int(5) not null default 0 comment '列号',
   generator_id         bigint comment '编码生成器',
   sn                   int(5) not null default 0 comment '序号',
   primary key (id)
);

alter table sys_custom_field comment '自定义字段表';

create unique index unique_code on sys_custom_field
(
   code,
   form_id
);

create table sys_custom_form
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   enable               int not null default 1 comment '是否生效',
   table_id             bigint not null comment '业务表id',
   query_api_url        varchar(255) comment '查询api',
   detail_api_url       varchar(255) comment '详情api',
   add_api_url          varchar(255) comment '新增api',
   update_api_url       varchar(255) comment '修改api',
   delete_api_url       varchar(255) comment '删除api',
   export_api_url       varchar(255) comment '导出api',
   export_all_api_url   varchar(255) comment '导出全部api',
   simple_api_url       varchar(255) comment '简讯api',
   primary key (id)
);

alter table sys_custom_form comment '自定义表单表';

create unique index unique_code on sys_custom_form
(
   code
);

create table sys_dept
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   deleted              int not null default 0 comment '逻辑删除状态：1已删除、0未删除',
   enable               int not null default 1 comment '生效状态：1生效、0失效',
   parent_id            bigint,
   serial_number        int not null default 1 comment '序号',
   leader_id            bigint comment '负责人',
   primary key (id)
);

alter table sys_dept comment '部门表';

create unique index unique_code on sys_dept
(
   code
);

create table sys_dictionaries
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '值',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   type_id              bigint not null comment '字典类型',
   serial_number        int(5) not null default 1 comment '序列',
   enable               int(1) not null default 0 comment '状态',
   primary key (id)
);

alter table sys_dictionaries comment '字典表';

create unique index unique_code on sys_dictionaries
(
   code
);

create table sys_employee
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   deleted              int not null default 0 comment '逻辑删除状态：1已删除、0未删除',
   password             varchar(255) not null comment '密码',
   dept_id              bigint not null comment '所属部门',
   enable               int not null default 1 comment '是否生效',
   image_id             bigint comment '头像',
   primary key (id)
);

alter table sys_employee comment '员工表';

create unique index unique_code on sys_employee
(
   code
);

create table sys_employee_role
(
   employee_id          bigint not null,
   role_id              bigint not null,
   primary key (employee_id, role_id)
);

alter table sys_employee_role comment '员工角色关联表';

create table sys_file
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   type_id              bigint not null comment '类型',
   size                 bigint comment '大小（字节）',
   creator_id           bigint not null,
   path                 varchar(1000) comment '存储路径',
   location_id          bigint not null comment '存储位置',
   permission_id        bigint not null comment '权限id',
   entity_code          varchar(255) not null comment '领域对象编码',
   primary key (id)
);

alter table sys_file comment '附件表';

create unique index unique_code on sys_file
(
   code
);

create table sys_gen_table
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '表名',
   name                 varchar(255) not null comment '功能名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_id           bigint not null comment '员工id',
   comment              varchar(1000) comment '描述',
   package_name         varchar(255) not null comment '包名（java程序包名）',
   app_code             varchar(255) not null comment '模块编码（子系统编码）',
   class_name           varchar(255) not null comment '类名',
   url_name             varchar(255) not null comment '资源名称',
   field_name           varchar(255) not null comment '属性名称',
   path                 varchar(1000) comment '生成路径',
   permission_code      varchar(255) not null comment '权限编码',
   data_source          varchar(255) comment '数据源',
   primary key (id)
);

alter table sys_gen_table comment '代码生成业务表';

create unique index unique_code on sys_gen_table
(
   code
);

create table sys_gen_table_column
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '列名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   creator_id           bigint not null comment '员工id',
   table_id             bigint not null comment '业务表id',
   relative_table_id    bigint comment '关联表',
   comment              varchar(255) comment '列描述',
   data_type            varchar(255) comment '字段物理类型',
   form_type            varchar(255) comment '自定义表单字段类型',
   java_field           varchar(255) comment 'java字段名',
   java_field_type      varchar(255) comment 'java字段类型',
   required             int(1) default 0 comment '是否必填',
   uniqueness           int(1) default 0 comment '是否唯一',
   sn                   int(5) not null default 1 comment '序号',
   primary key (id)
);

alter table sys_gen_table_column comment '代码生成业务字段表 ';

create unique index unique_code on sys_gen_table_column
(
   code
);

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

create table sys_message
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) comment '标题',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   content              varchar(2000) comment '消息内容',
   readed               int(1) not null default 0 comment '已读',
   type_id              bigint not null comment '消息类型',
   creator_id           bigint not null comment '创建人',
   recipient_id         bigint not null comment '接收人',
   entity_code          varchar(255) comment '实体编码',
   read_time            datetime comment '已读时间',
   form_id              bigint comment '表单',
   primary key (id)
);

alter table sys_message comment '系统消息表';

create unique index unique_code on sys_message
(
   code
);

create table sys_notice
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) comment '标题',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   content              longtext comment '公告内容',
   readed               int(1) not null default 0 comment '已读',
   creator_id           bigint not null comment '创建人',
   recipient_id         bigint not null comment '接收人',
   read_time            datetime comment '已读时间',
   template_id          bigint not null comment '通知模板',
   primary key (id)
);

alter table sys_notice comment '系统公告表';

create unique index unique_code on sys_notice
(
   code
);

create table sys_notice_dept
(
   notice_id            bigint not null comment '公告',
   dept_id              bigint not null comment '部门',
   primary key (notice_id, dept_id)
);

alter table sys_notice_dept comment '系统公告部门表';

create table sys_notice_employee
(
   notice_id            bigint not null comment '公告',
   employee_id          bigint not null comment '员工',
   primary key (notice_id, employee_id)
);

alter table sys_notice_employee comment '系统公告员工表';

create table sys_notice_template
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) comment '标题',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   content              longtext comment '公告内容',
   creator_id           bigint not null comment '创建人',
   published            int(1) not null default 0 comment '已发布',
   published_time       datetime comment '发布时间',
   primary key (id)
);

alter table sys_notice_template comment '系统公告模板表';

create unique index unique_code on sys_notice_template
(
   code
);

create table sys_operation_log
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(50) not null comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   operator_id          bigint not null comment '操作员id',
   description          text comment '操作记录',
   action_type          varchar(50) not null comment '操作类型',
   permission_id        bigint comment '功能id',
   entity_code          varchar(50) comment '实体编码',
   entity_name          varchar(255) comment '实体名称',
   primary key (id)
);

alter table sys_operation_log comment '操作日志表';

create unique index unique_code on sys_operation_log
(
   code
);

create index generic_entity on sys_operation_log
(
   operator_id,
   permission_id,
   entity_code
);

create table sys_permission
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   enable               int(1) not null default 1 comment '是否生效',
   parent_id            bigint comment '父权限id',
   sn                   int comment '序号',
   type_id              bigint not null comment '类型',
   icon                 varchar(255) comment '图标',
   form_id              bigint comment '表单',
   route_type_id        bigint comment '路由类型',
   relate_code          varchar(255) comment '报表编码',
   primary key (id)
);

alter table sys_permission comment '权限表';

create unique index unique_code on sys_permission
(
   code
);

create table sys_print_log
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   content              longtext comment '模板内容',
   creator_id           bigint not null default 1 comment '创建人',
   template_id          bigint not null comment '打印模板',
   entity_code          varchar(255) not null comment '实体对象编码',
   permission_id        bigint not null comment '操作功能',
   primary key (id)
);

alter table sys_print_log comment '打印日志';

create unique index unique_code on sys_print_log
(
   code
);

create table sys_print_template
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) comment '名称',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   content              longtext comment '模板内容',
   creator_id           bigint not null default 1 comment '创建人',
   form_id              bigint not null comment '关联表单',
   primary key (id)
);

alter table sys_print_template comment '打印模板';

create unique index unique_code on sys_print_template
(
   code
);

create table sys_role
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '编码',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   deleted              int not null default 0 comment '逻辑删除状态：1已删除、0未删除',
   data_scope           int not null default 0 comment '数据权限：0、本人，1、本部门，2、本部门及下级部门，3、全部 ',
   primary key (id)
);

alter table sys_role comment '角色表';

create unique index unique_code on sys_role
(
   code
);

create table sys_role_permission
(
   role_id              bigint not null,
   permission_id        bigint not null,
   primary key (role_id, permission_id)
);

alter table sys_role_permission comment '角色权限关联表';

create table sys_scene
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   visible              int not null default 1 comment '是否可见',
   employee_id          bigint not null comment '员工id',
   data                 text comment '自定义数据',
   current              int not null default 0 comment '是否默认',
   form_id              bigint not null comment '表单id',
   primary key (id)
);

alter table sys_scene comment '场景表';

create unique index unique_code on sys_scene
(
   code
);

create table sys_sequence
(
   id                   bigint,
   name                 varchar(50)
);

alter table sys_sequence comment '序列表';

create table sys_simple_type
(
   id                   bigint not null comment 'id',
   code                 varchar(50) not null comment '工号',
   name                 varchar(255) not null comment '姓名',
   version              varchar(50) not null comment '版本号',
   create_time          datetime not null comment '创建时间',
   class_type           varchar(255) not null comment '枚举实体类名',
   serial_number        int(5) not null default 1 comment '序列',
   primary key (id)
);

alter table sys_simple_type comment '枚举表';

create unique index unique_code on sys_simple_type
(
   code,
   class_type
);

alter table sys_app_sort add constraint fk_permission_sort foreign key (permission_id)
      references sys_permission (id) on delete restrict on update restrict;

alter table sys_column_sort add constraint fk_custom_field_sort foreign key (custom_field_id)
      references sys_custom_field (id) on delete restrict on update restrict;

alter table sys_custom_field add constraint fk_custom_field_form foreign key (form_id)
      references sys_custom_form (id) on delete restrict on update restrict;

alter table sys_custom_field add constraint fk_relative_form foreign key (relative_form_id)
      references sys_custom_form (id) on delete restrict on update restrict;

alter table sys_custom_field add constraint fk_sys_generator_field foreign key (generator_id)
      references sys_code_generator (id) on delete restrict on update restrict;

alter table sys_custom_form add constraint fk_custom_form_table foreign key (table_id)
      references sys_gen_table (id) on delete restrict on update restrict;

alter table sys_dept add constraint fk_dept_leader foreign key (leader_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_dictionaries add constraint fk_sys_dictionaries_type foreign key (type_id)
      references sys_simple_type (id) on delete restrict on update restrict;

alter table sys_employee_role add constraint fk_employee_role foreign key (role_id)
      references sys_role (id) on delete restrict on update restrict;

alter table sys_employee_role add constraint fk_role_employee foreign key (employee_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_file add constraint fk_sys_file_employee foreign key (creator_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_file add constraint fk_sys_file_permission foreign key (permission_id)
      references sys_permission (id) on delete restrict on update restrict;

alter table sys_gen_table add constraint fk_gen_table_creator foreign key (creator_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_gen_table_column add constraint fk_gen_table_column_creator foreign key (creator_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_gen_table_column add constraint fk_sys_relative_table foreign key (relative_table_id)
      references sys_gen_table (id) on delete restrict on update restrict;

alter table sys_gen_table_column_attr add constraint fk_sys_column_attribute foreign key (column_id)
      references sys_gen_table_column (id) on delete restrict on update restrict;

alter table sys_message add constraint fk_message_creator foreign key (creator_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_message add constraint fk_message_form foreign key (form_id)
      references sys_custom_form (id) on delete restrict on update restrict;

alter table sys_message add constraint fk_message_recipient foreign key (recipient_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_notice add constraint fk_notice_creator foreign key (creator_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_notice add constraint fk_notice_recipient foreign key (recipient_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_notice add constraint fk_notice_template foreign key (template_id)
      references sys_notice_template (id) on delete restrict on update restrict;

alter table sys_notice_dept add constraint fk_dept_notice foreign key (notice_id)
      references sys_notice_template (id) on delete restrict on update restrict;

alter table sys_notice_dept add constraint fk_notice_dept foreign key (dept_id)
      references sys_dept (id) on delete restrict on update restrict;

alter table sys_notice_employee add constraint fk_employee_notice foreign key (notice_id)
      references sys_notice_template (id) on delete restrict on update restrict;

alter table sys_notice_employee add constraint fk_notice_employee foreign key (employee_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_operation_log add constraint fk_log_employee foreign key (operator_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_operation_log add constraint fk_log_permission foreign key (permission_id)
      references sys_permission (id) on delete restrict on update restrict;

alter table sys_permission add constraint fk_permission_form foreign key (form_id)
      references sys_custom_form (id) on delete restrict on update restrict;

alter table sys_permission add constraint fk_permission_route_type foreign key (route_type_id)
      references sys_simple_type (id) on delete restrict on update restrict;

alter table sys_permission add constraint fk_permission_type foreign key (type_id)
      references sys_simple_type (id) on delete restrict on update restrict;

alter table sys_print_log add constraint fk_print_log_permission foreign key (permission_id)
      references sys_permission (id) on delete restrict on update restrict;

alter table sys_print_log add constraint fk_print_log_template foreign key (template_id)
      references sys_print_template (id) on delete restrict on update restrict;

alter table sys_print_template add constraint fk_print_template_form foreign key (form_id)
      references sys_custom_form (id) on delete restrict on update restrict;

alter table sys_role_permission add constraint fk_permission_role foreign key (role_id)
      references sys_role (id) on delete restrict on update restrict;

alter table sys_role_permission add constraint fk_role_permission foreign key (permission_id)
      references sys_permission (id) on delete restrict on update restrict;

alter table sys_scene add constraint fk_scene_employee foreign key (employee_id)
      references sys_employee (id) on delete restrict on update restrict;

alter table sys_scene add constraint fk_scene_form foreign key (form_id)
      references sys_custom_form (id) on delete restrict on update restrict;

