CREATE SCHEMA IF NOT EXISTS core;

DROP TABLE IF EXISTS core.config;

CREATE TABLE core.config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	application_name VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);

INSERT INTO core.config VALUES (default, 'test.config.source', 'database',  'server-lib-test', 10);

DROP TABLE IF EXISTS core.logs;
	
CREATE TABLE core.logs (
	id SERIAL PRIMARY KEY,  
	application_name VARCHAR NOT NULL, 
	application_version VARCHAR(32), 
	logger_name VARCHAR NOT NULL, 
	message VARCHAR NOT NULL, 
	severity VARCHAR(32) NOT NULL, 
	timestamp TIMESTAMP NOT NULL
);