package uk.co.lukestevens.injection;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.services.DatabasePropertyService;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.injection.annotations.EnvConfig;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;

/**
 * Injection module binding dependencies required for provisioning database services
 * including;
 * <ul>
 * <li>{@link Database}</li>
 * <li>{@link PropertyService}</li>
 * <li>{@link DaoProvider}</li>
 * </ul>
 * 
 * @author Luke Stevens
 */
public class DatabaseModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(PropertyService.class).to(DatabasePropertyService.class);
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

}
