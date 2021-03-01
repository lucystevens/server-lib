package uk.co.lukestevens.injection;

import javax.inject.Inject;

import com.google.gson.Gson;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.db.Database;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.injection.annotations.AppPort;
import uk.co.lukestevens.injection.annotations.DBConfig;
import uk.co.lukestevens.injection.annotations.EnvConfig;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.routes.RouteConfiguration;


public class MockIntegrationClass {
	
	public Config setupConfig;
	public Config applicationConfig;
	public LoggingProvider loggingProvider;
	public int port;
	public ApplicationProperties applicationProperties;
	public Database database;
	public PropertyService propertyService;
	public DaoProvider daoProvider;
	public BaseServer baseServer;
	public Gson gson;
	public RouteConfiguration routeConfiguration;
	
	@Inject
	public MockIntegrationClass(@EnvConfig Config setupConfig, @DBConfig Config applicationConfig, LoggingProvider loggingProvider,
			@AppPort int port, ApplicationProperties applicationProperties, Database database, PropertyService propertyService,
			DaoProvider daoProvider, BaseServer baseServer, Gson gson, RouteConfiguration routeConfiguration) {
		this.setupConfig = setupConfig;
		this.applicationConfig = applicationConfig;
		this.loggingProvider = loggingProvider;
		this.port = port;
		this.applicationProperties = applicationProperties;
		this.database = database;
		this.propertyService = propertyService;
		this.daoProvider = daoProvider;
		this.baseServer = baseServer;
		this.gson = gson;
		this.routeConfiguration = routeConfiguration;
	}
	
	
	

}
