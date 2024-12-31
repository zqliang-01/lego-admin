ALTER TABLE crm_contract
	CHANGE COLUMN OWNER_CODE OWNER VARCHAR(255) NOT NULL COMMENT '负责人' AFTER END_TIME,
	ADD COLUMN TYPE VARCHAR(255) NULL DEFAULT NULL COMMENT '类型' AFTER CHECK_STATUS,
	ADD COLUMN PROVINCE VARCHAR(255) NULL DEFAULT NULL COMMENT '省份' AFTER TYPE,
	ADD COLUMN CITY VARCHAR(255) NULL DEFAULT NULL COMMENT '城市' AFTER PROVINCE,
	ADD COLUMN AREA VARCHAR(255) NULL DEFAULT NULL COMMENT '地区' AFTER CITY,
	ADD COLUMN DETAIL VARCHAR(255) NULL DEFAULT NULL COMMENT '详细地址' AFTER AREA;

ALTER TABLE crm_customer
	ADD COLUMN TYPE VARCHAR(255) NULL DEFAULT NULL COMMENT '类型' AFTER CHECK_STATUS;

ALTER TABLE crm_lead
	CHANGE COLUMN BRITHDAY BIRTHDAY DATE NULL DEFAULT NULL COMMENT '生日' AFTER AMOUNT,
	CHANGE COLUMN STATUS STATUS INT(1) NULL DEFAULT NULL COMMENT '状态' AFTER SIZE,
	ADD COLUMN SOURCE VARCHAR(255) NULL DEFAULT NULL COMMENT '来源' AFTER CHECK_STATUS;