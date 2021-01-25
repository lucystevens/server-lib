package uk.co.lukestevens.injection;

import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.models.DatabaseConfig;
import uk.co.lukestevens.config.models.EnvironmentConfig;
import uk.co.lukestevens.config.models.MavenConfig;
import uk.co.lukestevens.config.services.DatabasePropertyService;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.gson.GsonIgnoreExclusionStrategy;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.injection.annotations.AppPort;
import uk.co.lukestevens.injection.annotations.DBConfig;
import uk.co.lukestevens.injection.annotations.EnvConfig;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.provider.ConsoleLoggingProvider;
import uk.co.lukestevens.logging.provider.DatabaseLoggingProvider;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.routes.RouteConfiguration;

/**
 * The base injection module for Http Server based applications.
 * This binds various services depending on environment variables;
 * <ul>
 * <li>{@link Config} (environment and database)</li>
 * <li>{@link ApplicationProperties} for Maven builds</li>
 * <li>{@link LoggingProvider} (console or database)</li>
 * <li>Application port (via {@link AppPort} annotation)</li>
 * <li>Database Services ({@link Database}, {@link DaoProvider})</li>
 * <li>{@link Gson} (with {@link GsonIgnoreExclusionStrategy}</li>
 * <li>{@link BaseServer}</li>
 * </ul>
 * 
 * The {@link RouteConfiguration} binding must be provided by an overriding 
 * superclass, with that class defining the routes to be served by the BaseServer
 * once initialised.<br/>
 * 
 * @author Luke Stevens
 */
public abstract class BaseInjectModule extends AbstractModule {
	
	protected static final String APP_PORT_KEY = "app.port";
	protected static final Integer APP_PORT_DEFAULT = 8000;
	protected static final String DATABASE_LOGGING_ENABLED_KEY = "database.logging.enabled";
	protected static final boolean DATABASE_LOGGING_ENABLED_DEFAULT = false;

	@Override
	protected void configure() {
		bind(PropertyService.class).to(DatabasePropertyService.class);
		bind(BaseServer.class);
		
		bindRouteConfiguration();
	}
	
	@Provides @AppPort
	@Singleton
	protected Integer providesAppPort(@EnvConfig Config config) {
		return config.getAsIntOrDefault(APP_PORT_KEY, APP_PORT_DEFAULT);
	}
	
	@Provides
	@Singleton
	protected DaoProvider providesDaoProvider(@EnvConfig Config config, ApplicationProperties appProperties){
		return new HibernateController(config, appProperties);
	}
	
	@Provides
	@Singleton
	protected Database providesDatabase(@EnvConfig Config config) throws IOException{
		return new ConfiguredDatabase(config);
	}
	
	@Provides @EnvConfig
	@Singleton
	protected Config providesEnvConfig() throws IOException{
		Config config = new EnvironmentConfig();
		config.load();
		return config;
	}
	
	@Provides @DBConfig
	@Singleton
	protected Config providesDBConfig(PropertyService propertyService) throws IOException{
		Config config = new DatabaseConfig(propertyService);
		config.load();
		return config;
	}
	
	@Provides
	@Singleton
	protected ApplicationProperties providesApplicationProperties() throws IOException {
		MavenConfig mvnConfig = new MavenConfig();
		mvnConfig.load();
		return mvnConfig;
	}
	
	@Provides
	@Singleton
	protected LoggingProvider providesLogging(@EnvConfig Config config, Database db, ApplicationProperties appProperties) {
		if(config.getAsBooleanOrDefault(DATABASE_LOGGING_ENABLED_KEY, DATABASE_LOGGING_ENABLED_DEFAULT)) {
			return new DatabaseLoggingProvider(db, appProperties);
		}
		else {
			return new ConsoleLoggingProvider();
		}
	}
	
	protected abstract void bindRouteConfiguration();
	

	@Provides
	@Singleton
	protected Gson providesGson() {
		return new GsonBuilder().setExclusionStrategies(new GsonIgnoreExclusionStrategy()).create();
	}

}
