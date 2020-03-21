package uk.co.lukestevens.injection;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;

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
import uk.co.lukestevens.gson.GsonIgnoreExclusionStrategy;
import uk.co.lukestevens.hibernate.Dao;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.jdbc.filter.QueryFilters;
import uk.co.lukestevens.logging.ConfiguredLoggerFactory;
import uk.co.lukestevens.logging.LoggerFactory;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.models.APIApplication;

/**
 * A base injection module to make all services available via dependency injection.
 * Binds the following services and their implementations:
 * <ul>
 * <li>KeyBasedSetup</li>
 * <li>EncryptionService</li>
 * <li>ConfigManager</li>
 * <li>DaoProvider</li>
 * <li>LoggerFactory</li>
 * <li>BaseService</li>
 * <li>Config</li>
 * <li>SiteConfigService</li>
 * <li>APIApplication</li>
 * <li>Gson</li>
 * </ul>
 * 
 * Also binds the following values using named annotations:
 * <ul>
 * <li>@ConfigFile</li>
 * <li>@EncryptionKey</li>
 * <li>@AppVersion</li>
 * <li>@AppName</li>
 * </ul>
 * 
 * These values can be overriden using {@link Modules#override(com.google.inject.Module...)}
 * @author Luke Stevens
 *
 * @param <T> The KeyBasedSetup extension this application is using
 */
public class BaseInjectModule<T extends KeyBasedSetup> extends AbstractModule {
	
	final T setup;
	
	/**
	 * Creates a new injection module using the setup class for this application
	 * @param setup The key based setup used by this application
	 */
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
	
	@Provides
	@Singleton
	Gson providesGson() {
		return new GsonBuilder().setExclusionStrategies(new GsonIgnoreExclusionStrategy()).create();
	}

}
