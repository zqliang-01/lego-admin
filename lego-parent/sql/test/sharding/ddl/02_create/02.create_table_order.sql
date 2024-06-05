
CREATE TABLE test_order_0 (
	id INT(11) NOT NULL,
	name VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	create_time DATETIME NOT NULL,
	PRIMARY KEY (id) USING BTREE
);

CREATE TABLE test_order_1 (
	id INT(11) NOT NULL,
	name VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	create_time DATETIME NOT NULL,
	PRIMARY KEY (id) USING BTREE
);
