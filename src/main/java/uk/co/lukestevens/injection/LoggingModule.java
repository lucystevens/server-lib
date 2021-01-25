package uk.co.lukestevens.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.injection.annotations.EnvConfig;
import uk.co.lukestevens.jdbc.Database;
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
}
