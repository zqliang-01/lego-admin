
CALL add_system_permission('doc', '知识库', 'app', NULL, 'icon-administration', 5, 'local');

CALL add_system_permission('doc_book', '我的知识库', 'menu', 'doc', 'book', 52, 'local');
CALL add_system_permission('doc_book_read', '查看列表', 'auth', 'doc_book', NULL, 521, NULL);
CALL add_system_permission('doc_book_add', '新增', 'auth', 'doc_book', NULL, 522, NULL);
CALL add_system_permission('doc_book_delete', '删除', 'auth', 'doc_book', NULL, 523, NULL);
CALL add_system_permission('doc_book_update', '更新', 'auth', 'doc_book', NULL, 524, NULL);

CALL add_system_permission('doc_recycle', '回收站', 'menu', 'doc', 'recycle-bin', 54, 'local');
CALL add_system_permission('doc_recycle_read', '查看列表', 'auth', 'doc_recycle', NULL, 541, NULL);
CALL add_system_permission('doc_recycle_enable', '恢复', 'auth', 'doc_recycle', NULL, 542, NULL);
CALL add_system_permission('doc_recycle_delete', '彻底删除', 'auth', 'doc_recycle', NULL, 543, NULL);