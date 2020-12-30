CREATE SCHEMA IF NOT EXISTS core;

DROP TABLE IF EXISTS core.site_config;

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
	