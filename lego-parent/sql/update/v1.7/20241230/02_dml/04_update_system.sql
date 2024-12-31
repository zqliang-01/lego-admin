UPDATE sys_custom_field t SET t.FORM_TYPE = 'boolean' WHERE t.FORM_TYPE = 'boolean_value';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'cronInput' WHERE t.FORM_TYPE = 'cron_input';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'pictureEditor' WHERE t.FORM_TYPE = 'doc_cover';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'multipleUser' WHERE t.FORM_TYPE = 'multiple_user';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'multipleStructure' WHERE t.FORM_TYPE = 'multiple_structure';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'richTextEditor' WHERE t.FORM_TYPE = 'rich_text_editor';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'float' WHERE t.FORM_TYPE = 'floatnumber';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'descText' WHERE t.FORM_TYPE = 'desc_text';
UPDATE sys_custom_field t SET t.FORM_TYPE = 'handwritingSign' WHERE t.FORM_TYPE = 'handwriting_sign';

UPDATE sys_custom_field t SET t.FIELD_CODE = 'owner' WHERE t.FIELD_CODE = 'ownerCode';
UPDATE sys_custom_field t SET t.FIELD_CODE = 'birthday' WHERE t.FIELD_CODE = 'brithday';

UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'boolean' WHERE t.FORM_TYPE = 'boolean_value';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'cronInput' WHERE t.FORM_TYPE = 'cron_input';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'pictureEditor' WHERE t.FORM_TYPE = 'doc_cover';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'multipleUser' WHERE t.FORM_TYPE = 'multiple_user';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'multipleStructure' WHERE t.FORM_TYPE = 'multiple_structure';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'richTextEditor' WHERE t.FORM_TYPE = 'rich_text_editor';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'float' WHERE t.FORM_TYPE = 'floatnumber';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'descText' WHERE t.FORM_TYPE = 'desc_text';
UPDATE sys_gen_table_column t SET t.FORM_TYPE = 'handwritingSign' WHERE t.FORM_TYPE = 'handwriting_sign';