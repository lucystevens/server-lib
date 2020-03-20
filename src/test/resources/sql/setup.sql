CREATE SCHEMA IF NOT EXISTS core;

DROP TABLE IF EXISTS core.api_applications;
DROP TABLE IF EXISTS core.site_config;

CREATE TABLE core.api_applications (
	id SERIAL PRIMARY KEY, 
	name VARCHAR NOT NULL UNIQUE, 
	domain VARCHAR, 
	description VARCHAR,
    git_repo VARCHAR NOT NULL,
	running_port INT, 
	upgrade_port INT, 
	internal_port INT
);

INSERT INTO core.api_applications (
	name, 
	domain, 
	description,
    git_repo,
	running_port, 
	upgrade_port, 
	internal_port
) VALUES (
	'server-lib-test',
	'test.lukestevens.co.uk',
	'Integration server for server library',
	'N/A',
	8501,
	8502,
	8503
);

CREATE TABLE core.site_config(
	id SERIAL PRIMARY KEY,
	key VARCHAR NOT NULL,
	value VARCHAR NOT NULL,
	site VARCHAR NOT NULL,
	refresh_rate BIGINT NOT NULL
);

INSERT INTO core.site_config
	(key, value, site, refresh_rate) VALUES
	('google.api.key', 'googlekey1', 'site1', 600),
	('google.api.key', 'googlekey2', 'site2', 600),
	('global.key', 'all-sites', '*', 600);
	