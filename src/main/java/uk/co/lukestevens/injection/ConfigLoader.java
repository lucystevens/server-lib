package uk.co.lukestevens.injection;

import java.io.IOException;

import javax.inject.Inject;

import uk.co.lukestevens.annotations.ApplicationConfig;
import uk.co.lukestevens.annotations.SetupConfig;
import uk.co.lukestevens.config.Config;

public class ConfigLoader {
	
	final Config setupConfig;
	final Config appConfig;
	
	@Inject
	public ConfigLoader(@SetupConfig Config setupConfig, @ApplicationConfig Config appConfig) {
		this.setupConfig = setupConfig;
		this.appConfig = appConfig;
	}
	
	public void initialise() throws IOException {
		this.setupConfig.load();
		if(this.setupConfig != this.appConfig) {
			this.appConfig.load();
		}
	}

}
