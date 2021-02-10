package uk.co.lukestevens.injection;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.application.ConfiguredApplicationProperties;
import uk.co.lukestevens.config.models.DatabaseConfig;
import uk.co.lukestevens.config.models.EnvironmentConfig;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.injection.annotations.DBConfig;
import uk.co.lukestevens.injection.annotations.EnvConfig;

/**
 * Injection module binding dependencies required for setting up configuration
 * including;
 * <ul>
 * <li>{@link EnvConfig} - Config fetched from environment variables</li>
 * <li>{@link DBConfig} - Config fetched from the database</li>
 * <li>{@link ApplicationProperties} (Maven only)</li>
 * </ul>
 * 
 * @author Luke Stevens
 */
public class ConfigModule extends AbstractModule {
	
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
	protected ApplicationProperties providesApplicationProperties(@EnvConfig Config config) throws IOException {
		return new ConfiguredApplicationProperties(config);
	}

}
