CREATE TABLE SYS_APP_SORT
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   SN                   INT(5) NOT NULL COMMENT '序号',
   PERMISSION_ID        BIGINT NOT NULL COMMENT '权限',
   EMPLOYEE_ID          BIGINT NOT NULL COMMENT '员工',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_APP_SORT COMMENT '应用排序';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_APP_SORT
(
   CODE
);

CREATE TABLE SYS_CODE_GENERATOR
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   PREFIX               VARCHAR(255) COMMENT '前缀',
   SERIAL_LENGTH        INT(5) NOT NULL DEFAULT 1 COMMENT '序号长度',
   DATE_PATTERN         VARCHAR(255) COMMENT '日期格式',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_CODE_GENERATOR COMMENT '编码生成器表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_CODE_GENERATOR
(
   CODE
);

CREATE TABLE SYS_COLUMN_SORT
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   CUSTOM_FIELD_ID      BIGINT NOT NULL COMMENT '自定义字段ID',
   EMPLOYEE_ID          BIGINT NOT NULL COMMENT '员工ID',
   WIDTH                INT(5) COMMENT '宽度',
   VISIBLE              INT(1) NOT NULL DEFAULT 0 COMMENT '是否显示',
   SN                   INT(5) NOT NULL DEFAULT 0 COMMENT '序号',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_COLUMN_SORT COMMENT '表格列排序表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_COLUMN_SORT
(
   CODE
);

CREATE TABLE SYS_CONFIG
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   VALUE                VARCHAR(1000),
   ENABLE               INT NOT NULL DEFAULT 1 COMMENT '是否生效',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_CONFIG COMMENT '系统配置表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_CONFIG
(
   CODE
);

CREATE TABLE SYS_CUSTOM_FIELD
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   ENABLE               INT NOT NULL DEFAULT 1 COMMENT '是否生效',
   FIELD_CODE           VARCHAR(255) NOT NULL COMMENT '属性值（表字段）',
   FORM_ID              BIGINT NOT NULL COMMENT '表单ID',
   RELATIVE_FORM_ID     BIGINT COMMENT '关联表单',
   FORM_POSITION        VARCHAR(255) COMMENT '位置坐标',
   COMPONENT_NAME       VARCHAR(255) COMMENT '组件名称',
   DEFAULT_VALUE        VARCHAR(1000) COMMENT '默认值',
   FORM_TYPE            VARCHAR(255) COMMENT '表单类型',
   INPUT_TIPS           VARCHAR(255) COMMENT '提示语',
   STYLE_PERCENT        VARCHAR(255) COMMENT '长度占比',
   OPTION_DICT_TYPE     VARCHAR(255) COMMENT '选项字典类型',
   OPTION_DATA_TYPE     VARCHAR(255) COMMENT '选项类型（自定义/字典）',
   PRECISIONS           INT(5) COMMENT '支持小数位',
   SETTING              TEXT COMMENT '自定义选项内容',
   HIDDEN               INT(1) DEFAULT 0 COMMENT '是否隐藏',
   REQUIRED             INT(1) DEFAULT 0 COMMENT '是否必填',
   UNIQUENESS           INT(1) DEFAULT 0 COMMENT '是否唯一',
   MAX_NUM_RESTRICT     INT COMMENT '最大值',
   MIN_NUM_RESTRICT     INT COMMENT '最小值',
   X_AXIS               INT(5) NOT NULL DEFAULT 0 COMMENT '行号',
   Y_AXIS               INT(5) NOT NULL DEFAULT 0 COMMENT '列号',
   GENERATOR_ID         BIGINT COMMENT '编码生成器',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_CUSTOM_FIELD COMMENT '自定义字段表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_CUSTOM_FIELD
(
   CODE,
   FORM_ID
);

CREATE TABLE SYS_CUSTOM_FORM
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   ENABLE               INT NOT NULL DEFAULT 1 COMMENT '是否生效',
   TABLE_ID             BIGINT NOT NULL COMMENT '业务表ID',
   QUERY_API_URL        VARCHAR(255) COMMENT '查询API',
   DETAIL_API_URL       VARCHAR(255) COMMENT '详情API',
   ADD_API_URL          VARCHAR(255) COMMENT '新增API',
   UPDATE_API_URL       VARCHAR(255) COMMENT '修改API',
   DELETE_API_URL       VARCHAR(255) COMMENT '删除API',
   EXPORT_API_URL       VARCHAR(255) COMMENT '导出API',
   EXPORT_ALL_API_URL   VARCHAR(255) COMMENT '导出全部API',
   DEFINITION_ID        VARCHAR(255) COMMENT '流程定义ID',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_CUSTOM_FORM COMMENT '自定义表单表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_CUSTOM_FORM
(
   CODE
);

CREATE TABLE SYS_DEPT
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   DELETED              INT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态：1已删除、0未删除',
   ENABLE               INT NOT NULL DEFAULT 1 COMMENT '生效状态：1生效、0失效',
   PARENT_ID            BIGINT,
   SERIAL_NUMBER        INT NOT NULL DEFAULT 1 COMMENT '序号',
   LEADER_ID            BIGINT COMMENT '负责人',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_DEPT COMMENT '部门表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_DEPT
(
   CODE
);

CREATE TABLE SYS_DICTIONARIES
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '值',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   TYPE_ID              BIGINT NOT NULL COMMENT '字典类型',
   SERIAL_NUMBER        INT(5) NOT NULL DEFAULT 1 COMMENT '序列',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_DICTIONARIES COMMENT '字典表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_DICTIONARIES
(
   CODE
);

CREATE TABLE SYS_EMPLOYEE
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   DELETED              INT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态：1已删除、0未删除',
   PASSWORD             VARCHAR(255) NOT NULL COMMENT '密码',
   DEPT_ID              BIGINT NOT NULL COMMENT '所属部门',
   ENABLE               INT NOT NULL DEFAULT 1 COMMENT '是否生效',
   IMAGE_ID             BIGINT COMMENT '头像',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_EMPLOYEE COMMENT '员工表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_EMPLOYEE
(
   CODE
);

CREATE TABLE SYS_EMPLOYEE_ROLE
(
   EMPLOYEE_ID          BIGINT NOT NULL,
   ROLE_ID              BIGINT NOT NULL,
   PRIMARY KEY (EMPLOYEE_ID, ROLE_ID)
);

ALTER TABLE SYS_EMPLOYEE_ROLE COMMENT '员工角色关联表';

CREATE TABLE SYS_FILE
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   TYPE_ID              BIGINT NOT NULL COMMENT '类型',
   SIZE                 BIGINT COMMENT '大小（字节）',
   CREATOR_ID           BIGINT NOT NULL,
   PATH                 VARCHAR(1000) COMMENT '存储路径',
   LOCATION_ID          BIGINT NOT NULL COMMENT '存储位置',
   PERMISSION_ID        BIGINT NOT NULL COMMENT '权限ID',
   ENTITY_CODE          VARCHAR(255) NOT NULL COMMENT '领域对象编码',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_FILE COMMENT '附件表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_FILE
(
   CODE
);

CREATE TABLE SYS_GEN_TABLE
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '表名',
   NAME                 VARCHAR(255) NOT NULL COMMENT '功能名称',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   CREATOR_ID           BIGINT NOT NULL COMMENT '员工ID',
   COMMENT              VARCHAR(1000) COMMENT '描述',
   PACKAGE_NAME         VARCHAR(255) NOT NULL COMMENT '包名（JAVA程序包名）',
   APP_CODE             VARCHAR(255) NOT NULL COMMENT '模块编码（子系统编码）',
   CLASS_NAME           VARCHAR(255) NOT NULL COMMENT '类名',
   URL_NAME             VARCHAR(255) NOT NULL COMMENT '资源名称',
   FIELD_NAME           VARCHAR(255) NOT NULL COMMENT '属性名称',
   PATH                 VARCHAR(1000) COMMENT '生成路径',
   PERMISSION_CODE      VARCHAR(255) NOT NULL COMMENT '权限编码',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_GEN_TABLE COMMENT '代码生成业务表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_GEN_TABLE
(
   CODE
);

CREATE TABLE SYS_GEN_TABLE_COLUMN
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '列名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   CREATOR_ID           BIGINT NOT NULL COMMENT '员工ID',
   TABLE_ID             BIGINT NOT NULL COMMENT '业务表ID',
   RELATIVE_TABLE_ID    BIGINT COMMENT '关联表',
   COMMENT              VARCHAR(255) COMMENT '列描述',
   DATA_TYPE            VARCHAR(255) COMMENT '字段物理类型',
   FORM_TYPE            VARCHAR(255) COMMENT '自定义表单字段类型',
   JAVA_FIELD           VARCHAR(255) COMMENT 'JAVA字段名',
   JAVA_FIELD_TYPE      VARCHAR(255) COMMENT 'JAVA字段类型',
   REQUIRED             INT(1) DEFAULT 0 COMMENT '是否必填',
   UNIQUENESS           INT(1) DEFAULT 0 COMMENT '是否唯一',
   SN                   INT(5) NOT NULL DEFAULT 1 COMMENT '序号',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_GEN_TABLE_COLUMN COMMENT '代码生成业务字段表 ';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_GEN_TABLE_COLUMN
(
   CODE
);

CREATE TABLE SYS_OPERATION_LOG
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(50) NOT NULL COMMENT '名称',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   OPERATOR_ID          BIGINT NOT NULL COMMENT '操作员ID',
   DESCRIPTION          TEXT COMMENT '操作记录',
   ACTION_TYPE          VARCHAR(50) NOT NULL COMMENT '操作类型',
   PERMISSION_ID        BIGINT COMMENT '功能ID',
   ENTITY_CODE          VARCHAR(50) COMMENT '实体编码',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_OPERATION_LOG COMMENT '操作日志表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_OPERATION_LOG
(
   CODE
);

CREATE INDEX GENERIC_ENTITY ON SYS_OPERATION_LOG
(
   OPERATOR_ID,
   PERMISSION_ID,
   ENTITY_CODE
);

CREATE TABLE SYS_PERMISSION
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   ENABLE               INT(1) NOT NULL DEFAULT 1 COMMENT '是否生效',
   PARENT_ID            BIGINT COMMENT '父权限ID',
   SN                   INT COMMENT '序号',
   TYPE_ID              BIGINT NOT NULL COMMENT '类型',
   ICON                 VARCHAR(255) COMMENT '图标',
   FORM_ID              BIGINT COMMENT '表单',
   ROUTE_TYPE_ID        BIGINT COMMENT '路由类型',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_PERMISSION COMMENT '权限表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_PERMISSION
(
   CODE
);

CREATE TABLE SYS_ROLE
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   DELETED              INT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态：1已删除、0未删除',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_ROLE COMMENT '角色表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_ROLE
(
   CODE
);

CREATE TABLE SYS_ROLE_PERMISSION
(
   ROLE_ID              BIGINT NOT NULL,
   PERMISSION_ID        BIGINT NOT NULL,
   PRIMARY KEY (ROLE_ID, PERMISSION_ID)
);

ALTER TABLE SYS_ROLE_PERMISSION COMMENT '角色权限关联表';

CREATE TABLE SYS_SCENE
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   VISIBLE              INT NOT NULL DEFAULT 1 COMMENT '是否可见',
   EMPLOYEE_ID          BIGINT NOT NULL COMMENT '员工ID',
   DATA                 TEXT COMMENT '自定义数据',
   CURRENT              INT NOT NULL DEFAULT 0 COMMENT '是否默认',
   FORM_ID              BIGINT NOT NULL COMMENT '表单ID',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_SCENE COMMENT '场景表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_SCENE
(
   CODE
);

CREATE TABLE SYS_SEQUENCE
(
   ID                   BIGINT,
   NAME                 VARCHAR(50)
);

ALTER TABLE SYS_SEQUENCE COMMENT '序列表';

CREATE TABLE SYS_SIMPLE_TYPE
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '工号',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   CLASS_TYPE           VARCHAR(255) NOT NULL COMMENT '枚举实体类名',
   SERIAL_NUMBER        INT(5) NOT NULL DEFAULT 1 COMMENT '序列',
   PRIMARY KEY (ID)
);

ALTER TABLE SYS_SIMPLE_TYPE COMMENT '枚举表';

CREATE UNIQUE INDEX UNIQUE_CODE ON SYS_SIMPLE_TYPE
(
   CODE,
   CLASS_TYPE
);

ALTER TABLE SYS_APP_SORT ADD CONSTRAINT FK_PERMISSION_SORT FOREIGN KEY (PERMISSION_ID)
      REFERENCES SYS_PERMISSION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_COLUMN_SORT ADD CONSTRAINT FK_CUSTOM_FIELD_SORT FOREIGN KEY (CUSTOM_FIELD_ID)
      REFERENCES SYS_CUSTOM_FIELD (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_CUSTOM_FIELD ADD CONSTRAINT FK_CUSTOM_FIELD_FORM FOREIGN KEY (FORM_ID)
      REFERENCES SYS_CUSTOM_FORM (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_CUSTOM_FIELD ADD CONSTRAINT FK_RELATIVE_FORM FOREIGN KEY (RELATIVE_FORM_ID)
      REFERENCES SYS_CUSTOM_FORM (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_CUSTOM_FIELD ADD CONSTRAINT FK_SYS_GENERATOR_FIELD FOREIGN KEY (GENERATOR_ID)
      REFERENCES SYS_CODE_GENERATOR (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_CUSTOM_FORM ADD CONSTRAINT FK_CUSTOM_FORM_TABLE FOREIGN KEY (TABLE_ID)
      REFERENCES SYS_GEN_TABLE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_DEPT ADD CONSTRAINT FK_DEPT_LEADER FOREIGN KEY (LEADER_ID)
      REFERENCES SYS_EMPLOYEE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_DICTIONARIES ADD CONSTRAINT FK_SYS_DICTIONARIES_TYPE FOREIGN KEY (TYPE_ID)
      REFERENCES SYS_SIMPLE_TYPE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_EMPLOYEE_ROLE ADD CONSTRAINT FK_EMPLOYEE_ROLE FOREIGN KEY (ROLE_ID)
      REFERENCES SYS_ROLE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_EMPLOYEE_ROLE ADD CONSTRAINT FK_ROLE_EMPLOYEE FOREIGN KEY (EMPLOYEE_ID)
      REFERENCES SYS_EMPLOYEE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_FILE ADD CONSTRAINT FK_SYS_FILE_EMPLOYEE FOREIGN KEY (CREATOR_ID)
      REFERENCES SYS_EMPLOYEE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_FILE ADD CONSTRAINT FK_SYS_FILE_PERMISSION FOREIGN KEY (PERMISSION_ID)
      REFERENCES SYS_PERMISSION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_GEN_TABLE ADD CONSTRAINT FK_GEN_TABLE_CREATOR FOREIGN KEY (CREATOR_ID)
      REFERENCES SYS_EMPLOYEE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_GEN_TABLE_COLUMN ADD CONSTRAINT FK_GEN_TABLE_COLUMN_CREATOR FOREIGN KEY (CREATOR_ID)
      REFERENCES SYS_EMPLOYEE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_GEN_TABLE_COLUMN ADD CONSTRAINT FK_SYS_RELATIVE_TABLE FOREIGN KEY (RELATIVE_TABLE_ID)
      REFERENCES SYS_GEN_TABLE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_OPERATION_LOG ADD CONSTRAINT FK_LOG_EMPLOYEE FOREIGN KEY (OPERATOR_ID)
      REFERENCES SYS_EMPLOYEE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_OPERATION_LOG ADD CONSTRAINT FK_LOG_PERMISSION FOREIGN KEY (PERMISSION_ID)
      REFERENCES SYS_PERMISSION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_PERMISSION ADD CONSTRAINT FK_PERMISSION_FORM FOREIGN KEY (FORM_ID)
      REFERENCES SYS_CUSTOM_FORM (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_PERMISSION ADD CONSTRAINT FK_PERMISSION_ROUTE_TYPE FOREIGN KEY (ROUTE_TYPE_ID)
      REFERENCES SYS_SIMPLE_TYPE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_PERMISSION ADD CONSTRAINT FK_PERMISSION_TYPE FOREIGN KEY (TYPE_ID)
      REFERENCES SYS_SIMPLE_TYPE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_ROLE_PERMISSION ADD CONSTRAINT FK_PERMISSION_ROLE FOREIGN KEY (ROLE_ID)
      REFERENCES SYS_ROLE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_ROLE_PERMISSION ADD CONSTRAINT FK_ROLE_PERMISSION FOREIGN KEY (PERMISSION_ID)
      REFERENCES SYS_PERMISSION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_SCENE ADD CONSTRAINT FK_SCENE_EMPLOYEE FOREIGN KEY (EMPLOYEE_ID)
      REFERENCES SYS_EMPLOYEE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SYS_SCENE ADD CONSTRAINT FK_SCENE_FORM FOREIGN KEY (FORM_ID)
      REFERENCES SYS_CUSTOM_FORM (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

