package uk.co.lukestevens.server;

import java.io.File;
import javax.inject.Inject;

import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.ConfigManager;
import uk.co.lukestevens.config.SiteConfigService;
import uk.co.lukestevens.config.annotations.AppVersion;
import uk.co.lukestevens.config.annotations.ConfigFile;
import uk.co.lukestevens.encryption.EncryptionKey;
import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.logging.LoggerFactory;
import uk.co.lukestevens.server.models.APIApplication;

public class MockIntegrationClass {
	
	public EncryptionService encryptionService;
	public ConfigManager configManager;
	public DaoProvider daoProvider;
	public LoggerFactory loggerFactory;
	public BaseServer server;
	public File configFile;
	public String encryptionKey;
	public Config appConfig;
	public SiteConfigService siteConfigService;
	public APIApplication application;
	public String applicationVersion;
	public MockSetup setup;
	
	@Inject
	public MockIntegrationClass(EncryptionService encryptionService, ConfigManager configManager,
			DaoProvider daoProvider, LoggerFactory loggerFactory, BaseServer server, @ConfigFile File configFile,
			@EncryptionKey String encryptionKey, Config appConfig, SiteConfigService siteConfigService,
			APIApplication application, @AppVersion String applicationVersion, MockSetup setup) {
		this.encryptionService = encryptionService;
		this.configManager = configManager;
		this.daoProvider = daoProvider;
		this.loggerFactory = loggerFactory;
		this.server = server;
		this.configFile = configFile;
		this.encryptionKey = encryptionKey;
		this.appConfig = appConfig;
		this.siteConfigService = siteConfigService;
		this.application = application;
		this.applicationVersion = applicationVersion;
		this.setup = setup;
	}

	

}
