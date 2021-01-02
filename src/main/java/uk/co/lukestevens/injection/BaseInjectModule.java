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
import uk.co.lukestevens.server.setup.ServerSetup;

public abstract class BaseInjectModule extends AbstractModule {
	
	protected final ServerSetup setup;
	
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
	
	protected void bindApplicationProperties() {
		try {
			MavenConfig mvnConfig = new MavenConfig();
			mvnConfig.load();
			bind(ApplicationProperties.class).toInstance(mvnConfig);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	protected void bindLogging() {
		if(this.setup.useDatabaseLogging()) {
			bind(LoggingProvider.class).to(DatabaseLoggingProvider.class);
		}
		else {
			bind(LoggingProvider.class).to(ConsoleLoggingProvider.class);
		}
	}
	
	protected abstract void bindRouteConfiguration();
	
	
	@Provides
	@Singleton
	protected Gson providesGson() {
		return new GsonBuilder().setExclusionStrategies(new GsonIgnoreExclusionStrategy()).create();
	}

}
