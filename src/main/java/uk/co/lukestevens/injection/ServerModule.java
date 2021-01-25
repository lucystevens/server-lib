package uk.co.lukestevens.injection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.gson.GsonIgnoreExclusionStrategy;
import uk.co.lukestevens.injection.annotations.AppPort;
import uk.co.lukestevens.injection.annotations.EnvConfig;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.routes.RouteConfiguration;

/**
 * Injection module for Http Server based applications.
 * This binds dependencies required for provisioning a server
 * <ul>
 * <li>Application port (via {@link AppPort} annotation)</li>
 * <li>{@link Gson} (with {@link GsonIgnoreExclusionStrategy}</li>
 * <li>{@link BaseServer}</li>
 * </ul>
 * 
 * The {@link RouteConfiguration} binding must be provided by an additional inject 
 * module, with that class defining the routes to be served by the BaseServer
 * once initialised.<br/>
 * 
 * @author Luke Stevens
 */
public class ServerModule extends AbstractModule {
	
	protected static final String APP_PORT_KEY = "app.port";
	protected static final Integer APP_PORT_DEFAULT = 8000;

	@Override
	protected void configure() {
		bind(BaseServer.class);
	}
	
	@Provides @AppPort
	@Singleton
	protected Integer providesAppPort(@EnvConfig Config config) {
		return config.getAsIntOrDefault(APP_PORT_KEY, APP_PORT_DEFAULT);
	}	

	@Provides
	@Singleton
	protected Gson providesGson() {
		return new GsonBuilder().setExclusionStrategies(new GsonIgnoreExclusionStrategy()).create();
	}

}
