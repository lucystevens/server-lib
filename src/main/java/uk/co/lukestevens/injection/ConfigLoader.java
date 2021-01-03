package uk.co.lukestevens.injection;

import java.io.IOException;

import javax.inject.Inject;

import uk.co.lukestevens.annotations.ApplicationConfig;
import uk.co.lukestevens.annotations.SetupConfig;
import uk.co.lukestevens.config.Config;

/**
 * An ease of use class for initialising injected configuration classes
 * 
 * @author Luke Stevens
 */
public class ConfigLoader {
	
	final Config setupConfig;
	final Config appConfig;
	
	/**
	 * @param setupConfig The config for initial application setup.
	 * This should generally contain database access properties.
	 * @param appConfig The config for running the application.
	 */
	@Inject
	public ConfigLoader(@SetupConfig Config setupConfig, @ApplicationConfig Config appConfig) {
		this.setupConfig = setupConfig;
		this.appConfig = appConfig;
	}
	
	/**
	 * Initialises the injected setup and application configs by loading
	 * their properties from source.
	 * @throws IOException If there is an error loading configs
	 */
	public void initialise() throws IOException {
		this.setupConfig.load();
		if(this.setupConfig != this.appConfig) {
			this.appConfig.load();
		}
	}

}
