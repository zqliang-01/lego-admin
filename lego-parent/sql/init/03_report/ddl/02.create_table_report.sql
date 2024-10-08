CREATE TABLE REPORT_CONDITION
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   SN                   INT(5) NOT NULL DEFAULT 1 COMMENT '序列',
   ENABLE               INT(1) NOT NULL DEFAULT 0 COMMENT '是否生效',
   DEFINITION_ID        BIGINT NOT NULL COMMENT '报表定义',
   DEPENDENT_CODE       VARCHAR(50) COMMENT '依赖条件',
   DATA_DEFINITION_ID   BIGINT COMMENT '数据集定义',
   REQUIRED             INT(1) NOT NULL DEFAULT 0 COMMENT '是否必须',
   DEFAULT_VALUE        VARCHAR(255) COMMENT '默认值',
   TYPE                 VARCHAR(50) NOT NULL COMMENT '类型',
   SQL_KEY              VARCHAR(50) NOT NULL COMMENT '字段',
   PRIMARY KEY (ID)
);

ALTER TABLE REPORT_CONDITION COMMENT '报表条件表';

CREATE UNIQUE INDEX UNIQUE_CODE ON REPORT_CONDITION
(
   CODE
);

CREATE TABLE REPORT_DEFINITION
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   DATA_SOURCE          VARCHAR(255) COMMENT '数据源',
   SN                   INT(5) NOT NULL DEFAULT 1 COMMENT '序列',
   ENABLE               INT(1) NOT NULL DEFAULT 0 COMMENT '是否生效',
   SQL_TEXT             LONGTEXT NOT NULL COMMENT 'SQL脚本',
   MAX_EXPORT_SIZE      INT(8) NOT NULL DEFAULT 1000 COMMENT '最大导出数量',
   CREATOR_CODE         VARCHAR(50) NOT NULL COMMENT '创建人',
   TYPE                 VARCHAR(50) NOT NULL COMMENT '类型',
   PRIMARY KEY (ID)
);

ALTER TABLE REPORT_DEFINITION COMMENT '报表定义表';

CREATE UNIQUE INDEX UNIQUE_CODE ON REPORT_DEFINITION
(
   CODE
);

CREATE TABLE REPORT_DESIGN
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   SN                   INT(5) NOT NULL DEFAULT 1 COMMENT '序列',
   ENABLE               INT(1) NOT NULL DEFAULT 0 COMMENT '是否生效',
   CREATOR_CODE         VARCHAR(50) NOT NULL COMMENT '创建人',
   DEFINITION_ID        BIGINT NOT NULL COMMENT '报表定义',
   POSITION             VARCHAR(50) NOT NULL COMMENT '位置',
   CHART_TYPE           VARCHAR(50) COMMENT '图表类型',
   DATA_DIMENSION       VARCHAR(50) COMMENT '维度列编码',
   PRIMARY KEY (ID)
);

ALTER TABLE REPORT_DESIGN COMMENT '报表设计';

CREATE UNIQUE INDEX UNIQUE_CODE ON REPORT_DESIGN
(
   CODE
);

CREATE TABLE REPORT_DESIGN_CATEGORY
(
   DESIGN_ID            BIGINT NOT NULL COMMENT 'ID',
   DATA_CATEGORY        VARCHAR(50) NOT NULL COMMENT '编码',
   PRIMARY KEY (DESIGN_ID, DATA_CATEGORY)
);

ALTER TABLE REPORT_DESIGN_CATEGORY COMMENT '报表设计分类';

CREATE TABLE REPORT_DESIGN_SORT
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   SN                   INT(5) NOT NULL DEFAULT 1 COMMENT '序列',
   ENABLE               INT(1) NOT NULL DEFAULT 0 COMMENT '是否生效',
   CREATOR_CODE         VARCHAR(50) NOT NULL COMMENT '创建人',
   DESIGN_CODE          BIGINT NOT NULL COMMENT '报表设计编码',
   PRIMARY KEY (ID)
);

ALTER TABLE REPORT_DESIGN_SORT COMMENT '首页排序';

CREATE UNIQUE INDEX UNIQUE_CODE ON REPORT_DESIGN_SORT
(
   CODE
);

CREATE TABLE REPORT_TITLE
(
   ID                   BIGINT NOT NULL COMMENT 'ID',
   CODE                 VARCHAR(50) NOT NULL COMMENT '编码',
   NAME                 VARCHAR(255) NOT NULL COMMENT '姓名',
   VERSION              VARCHAR(50) NOT NULL COMMENT '版本号',
   CREATE_TIME          DATETIME NOT NULL COMMENT '创建时间',
   SN                   INT(5) NOT NULL DEFAULT 1 COMMENT '序列',
   ALIGN                VARCHAR(50) NOT NULL COMMENT '对齐方式',
   DEFINITION_ID        BIGINT NOT NULL COMMENT '定义',
   SQL_KEY              VARCHAR(50) NOT NULL COMMENT '字段',
   PRIMARY KEY (ID)
);

ALTER TABLE REPORT_TITLE COMMENT '报表表头表';

CREATE UNIQUE INDEX UNIQUE_CODE ON REPORT_TITLE
(
   CODE
);

ALTER TABLE REPORT_CONDITION ADD CONSTRAINT FK_CONDITION_DEFINITION FOREIGN KEY (DEFINITION_ID)
      REFERENCES REPORT_DEFINITION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE REPORT_CONDITION ADD CONSTRAINT FK_DATA_DEFINITION FOREIGN KEY (DATA_DEFINITION_ID)
      REFERENCES REPORT_DEFINITION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE REPORT_DESIGN ADD CONSTRAINT FK_DESIGN_DEFINITION FOREIGN KEY (DEFINITION_ID)
      REFERENCES REPORT_DEFINITION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE REPORT_DESIGN_CATEGORY ADD CONSTRAINT FK_DESIGN_CATEGORY FOREIGN KEY (DESIGN_ID)
      REFERENCES REPORT_DESIGN (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE REPORT_TITLE ADD CONSTRAINT FK_TITLE_DEFINITION FOREIGN KEY (DEFINITION_ID)
      REFERENCES REPORT_DEFINITION (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

