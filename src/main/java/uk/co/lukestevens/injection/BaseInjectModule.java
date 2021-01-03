package uk.co.lukestevens.injection;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import uk.co.lukestevens.annotations.ApplicationConfig;
import uk.co.lukestevens.annotations.SetupConfig;
import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.models.DatabaseConfig;
import uk.co.lukestevens.config.models.EnvironmentConfig;
import uk.co.lukestevens.config.models.FileConfig;
import uk.co.lukestevens.config.models.MavenConfig;
import uk.co.lukestevens.config.services.DatabasePropertyService;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.gson.GsonIgnoreExclusionStrategy;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.provider.ConsoleLoggingProvider;
import uk.co.lukestevens.logging.provider.DatabaseLoggingProvider;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.routes.RouteConfiguration;
import uk.co.lukestevens.server.setup.ServerSetup;

/**
 * The base injection module for Http Server based applications.
 * This binds various services depending on the provided server setup;
 * <ul>
 * <li>{@link Config} (environment, file or database) and {@link ConfigLoader}</li>
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
 * The Config classes should also be initialised using {@link ConfigLoader#initialise()}
 * before using any of the other injected services.
 * 
 * @author Luke Stevens
 */
public abstract class BaseInjectModule extends AbstractModule {
	
	protected final ServerSetup setup;
	
	/**
	 * Create a new injection module based of some setup arguments
	 * @param setup An object containing basic setup arguments for the application.
	 */
	public BaseInjectModule(ServerSetup setup) {
		this.setup = setup;
	}

	@Override
	protected void configure() {
		bindConfig();
		bindLogging();
		bindApplicationProperties();
		
		bind(ConfigLoader.class);
		bind(ServerSetup.class).toInstance(this.setup);
		bind(Integer.class).annotatedWith(AppPort.class).toInstance(this.setup.getPort());
		bind(Database.class).to(ConfiguredDatabase.class);
		bind(PropertyService.class).to(DatabasePropertyService.class);
		bind(DaoProvider.class).to(HibernateController.class);
		bind(BaseServer.class);
		
		bindRouteConfiguration();
	}
	
	// TODO some of these bind methods could be replaced with providers, for better lazy initialisation
	
	/**
	 * Binds the {@link SetupConfig} and {@link ApplicationConfig} injection
	 * annotations based on whether an override config file has been supplied by
	 * the ServerSetup.
	 */
	protected void bindConfig(){
		if(this.setup.hasConfigFile()) {
			Config config = new FileConfig(this.setup.getConfigFile());
			bind(Config.class).annotatedWith(SetupConfig.class).toInstance(config);
			bind(Config.class).annotatedWith(ApplicationConfig.class).toInstance(config);
		}
		else {
			bind(Config.class).annotatedWith(SetupConfig.class).to(EnvironmentConfig.class);
			bind(Config.class).annotatedWith(ApplicationConfig.class).to(DatabaseConfig.class);
		}
	}
	
	/**
	 * Binds the {@link ApplicationProperties} using MavenConfig. Override
	 * if another build tool is used.
	 */
	protected void bindApplicationProperties() {
		try {
			MavenConfig mvnConfig = new MavenConfig();
			mvnConfig.load();
			bind(ApplicationProperties.class).toInstance(mvnConfig);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Binds the {@link LoggingProvider} based on the supplied
	 * use database logging flag.
	 */
	protected void bindLogging() {
		if(this.setup.useDatabaseLogging()) {
			bind(LoggingProvider.class).to(DatabaseLoggingProvider.class);
		}
		else {
			bind(LoggingProvider.class).to(ConsoleLoggingProvider.class);
		}
	}
	
	/**
	 * Binds the specific {@link RouteConfiguration} required for this
	 * application
	 */
	protected abstract void bindRouteConfiguration();
	
	
	/**
	 * @return The Gson instance to bind
	 */
	@Provides
	@Singleton
	protected Gson providesGson() {
		return new GsonBuilder().setExclusionStrategies(new GsonIgnoreExclusionStrategy()).create();
	}

}
