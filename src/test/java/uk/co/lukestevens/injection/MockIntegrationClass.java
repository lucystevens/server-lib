package uk.co.lukestevens.injection;

import javax.inject.Inject;

import com.google.gson.Gson;

import uk.co.lukestevens.annotations.ApplicationConfig;
import uk.co.lukestevens.annotations.SetupConfig;
import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.injection.AppPort;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.setup.ServerSetup;


public class MockIntegrationClass {
	
	public Config setupConfig;
	public Config applicationConfig;
	public LoggingProvider loggingProvider;
	public ServerSetup serverSetup;
	public int port;
	public ApplicationProperties applicationProperties;
	public Database database;
	public PropertyService propertyService;
	public DaoProvider daoProvider;
	public BaseServer baseServer;
	public Gson gson;
	
	@Inject
	public MockIntegrationClass(@SetupConfig Config setupConfig, @ApplicationConfig Config applicationConfig, LoggingProvider loggingProvider,
			ServerSetup serverSetup, @AppPort int port, ApplicationProperties applicationProperties, Database database,
			PropertyService propertyService, DaoProvider daoProvider, BaseServer baseServer, Gson gson) {
		this.setupConfig = setupConfig;
		this.applicationConfig = applicationConfig;
		this.loggingProvider = loggingProvider;
		this.serverSetup = serverSetup;
		this.port = port;
		this.applicationProperties = applicationProperties;
		this.database = database;
		this.propertyService = propertyService;
		this.daoProvider = daoProvider;
		this.baseServer = baseServer;
		this.gson = gson;
	}
	
	
	

}
