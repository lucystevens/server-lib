package uk.co.lukestevens.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.injection.annotations.EnvConfig;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.logging.LoggerLevel;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.provider.ConsoleLoggingProvider;
import uk.co.lukestevens.logging.provider.DatabaseLoggingProvider;

/**
 * Injection module binding dependencies required for managing logging.
 * 
 * Binds a {@link LoggingProvider} for either console or database
 * depending on the `database.loggging.enabled` property
 * 
 * @author Luke Stevens
 */
public class LoggingModule extends AbstractModule {
	
	protected static final String DATABASE_LOGGING_ENABLED_KEY = "database.logging.enabled";
	protected static final boolean DATABASE_LOGGING_ENABLED_DEFAULT = false;
	
	protected static final String LOGGER_LEVEL_KEY = "logging.level";
	protected static final LoggerLevel LOGGER_LEVEL_DEFAULT = LoggerLevel.INFO;

	@Provides
	@Singleton
	protected LoggingProvider providesLogging(@EnvConfig Config config, Database db, ApplicationProperties appProperties) {
		LoggerLevel loggerLevel = config.getParsedValueOrDefault(LOGGER_LEVEL_KEY, LoggerLevel::valueOf, LOGGER_LEVEL_DEFAULT);
		if(config.getAsBooleanOrDefault(DATABASE_LOGGING_ENABLED_KEY, DATABASE_LOGGING_ENABLED_DEFAULT)) {
			return new DatabaseLoggingProvider(db, appProperties, loggerLevel);
		}
		else {
			return new ConsoleLoggingProvider(loggerLevel);
		}
	}
}
