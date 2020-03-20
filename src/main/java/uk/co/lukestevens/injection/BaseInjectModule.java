package uk.co.lukestevens.injection;

import java.io.File;
import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import uk.co.lukestevens.cli.setup.KeyBasedSetup;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.ConfigManager;
import uk.co.lukestevens.config.SiteConfigService;
import uk.co.lukestevens.config.annotations.AppName;
import uk.co.lukestevens.config.annotations.AppVersion;
import uk.co.lukestevens.config.annotations.ConfigFile;
import uk.co.lukestevens.encryption.AESEncryptionService;
import uk.co.lukestevens.encryption.EncryptionKey;
import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.hibernate.Dao;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.jdbc.filter.QueryFilters;
import uk.co.lukestevens.logging.ConfiguredLoggerFactory;
import uk.co.lukestevens.logging.LoggerFactory;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.models.APIApplication;

public class BaseInjectModule<T extends KeyBasedSetup> extends AbstractModule {
	
	final T setup;
	
	public BaseInjectModule(T setup) {
		this.setup = setup;
	}

	@Override
	protected void configure() {
		bind(KeyBasedSetup.class).to(this.setup.getClass());
		bind(EncryptionService.class).to(AESEncryptionService.class);
		bind(ConfigManager.class);
		bind(DaoProvider.class).to(HibernateController.class);
		bind(LoggerFactory.class).to(ConfiguredLoggerFactory.class);
		bind(BaseServer.class);
	}
	
	@Provides @ConfigFile
	File providesKeyBasedSetup() {
		return setup.getConfigFile();
	}
	
	@Provides @EncryptionKey
	String providesEncryptionKey() {
		return setup.getKey();
	}
	
	@Provides
	Config providesConfig(ConfigManager manager) {
		return manager.getAppConfig();
	}
	
	@Provides @AppVersion
	String providesAppVersion(Config config) {
		return config.getApplicationVersion();
	}
	
	@Provides @AppName
	String providesAppName(Config config) {
		return config.getApplicationName();
	}
	
	@Provides
	SiteConfigService providesSiteConfigService(ConfigManager manager) {
		return manager.getSiteConfigService();
	}
	
	@Provides
	APIApplication providesApplication(DaoProvider daoProvider, @AppName String applicationName) throws IOException {
		Dao<APIApplication> dao = daoProvider.getDao(APIApplication.class);
		return dao.get(QueryFilters.column("name").isEqualTo(applicationName));
	}

}
