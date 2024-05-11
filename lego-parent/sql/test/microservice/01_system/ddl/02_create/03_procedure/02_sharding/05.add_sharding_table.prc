CREATE PROCEDURE add_sharding_table (
    IN vCode VARCHAR(255),
    IN vName VARCHAR(255),
    IN vLogicTable VARCHAR(255),
    IN vActualTable VARCHAR(255),
    IN vShardingColumn VARCHAR(255),
    IN vAlgorithmCode VARCHAR(255),
    IN vConfigCode VARCHAR(255),
    IN vTemplateCode VARCHAR(255),
    IN vDataSourceCode VARCHAR(255)
)
BEGIN
    DECLARE vId BIGINT(15);
    DECLARE vAlgorithmId BIGINT(15);
    DECLARE vConfigId BIGINT(15);
    DECLARE vTemplateId BIGINT(15);
    DECLARE vDataSourceId BIGINT(15);

    SET vId = next_id('general');
    SELECT id INTO vAlgorithmId FROM sharding_algorithms t WHERE t.code = vAlgorithmCode;
    SELECT id INTO vConfigId FROM sharding_config t WHERE t.code = vConfigCode;
    SELECT id INTO vTemplateId FROM sharding_template t WHERE t.code = vTemplateCode;
    SELECT id INTO vDataSourceId FROM sharding_data_source t WHERE t.code = vDataSourceCode;

	INSERT INTO sharding_table
	    (ID, CODE, NAME, DESCRIPTION, LOGIC_TABLE_NAME, ACTUAL_DATA_NODES, ALGORITHM_ID, SHARDING_COLUMN, TEMPLATE_ID, CONFIG_ID, VERSION, ENABLE, CREATE_TIME, DATA_SOURCE_ID)
	VALUES
	    (vId, vCode, vName, NULL, vLogicTable, vActualTable, vAlgorithmId, vShardingColumn, vTemplateId, vConfigId, 1, 1, sysdate(), vDataSourceId);
END;