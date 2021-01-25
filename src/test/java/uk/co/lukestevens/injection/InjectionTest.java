package uk.co.lukestevens.injection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.config.models.DatabaseConfig;
import uk.co.lukestevens.config.models.EnvironmentConfig;
import uk.co.lukestevens.config.models.Property;
import uk.co.lukestevens.config.services.DatabasePropertyService;
import uk.co.lukestevens.config.services.PropertyService;
import uk.co.lukestevens.gson.MockSerialisedClass;
import uk.co.lukestevens.hibernate.DaoProvider;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.jdbc.ConfiguredDatabase;
import uk.co.lukestevens.jdbc.Database;
import uk.co.lukestevens.jdbc.result.DatabaseResult;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.logging.LoggingProvider;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;
import uk.co.lukestevens.logging.loggers.DatabaseLogger;
import uk.co.lukestevens.logging.models.Log;
import uk.co.lukestevens.logging.provider.ConsoleLoggingProvider;
import uk.co.lukestevens.logging.provider.DatabaseLoggingProvider;
import uk.co.lukestevens.mocks.MockApiModule;
import uk.co.lukestevens.mocks.MockRouteConfiguration;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.routes.RouteConfiguration;
import uk.co.lukestevens.testing.db.TestDatabase;
import uk.co.lukestevens.testing.mocks.EnvironmentVariableMocker;

public class InjectionTest {
	
	static TestDatabase db;
	
	@BeforeAll
	public static void setup() throws IOException, SQLException {
		db = new TestDatabase();
		db.executeFile("setup");
	}
	
	EnvironmentVariableMocker envVariables;
	
	@BeforeEach
	public void mockEnvironmentVariables() {
		envVariables = EnvironmentVariableMocker.build()
		.with(db.getProperties())
		.with("test.config.source", "environment");
	}
	
	@AfterEach
	public void clearEnvironmentVariables() {
		EnvironmentVariableMocker.clear();
	}
	
	Injector createInjector() {
		return Guice.createInjector(
			new ConfigModule(),
			new DatabaseModule(),
			new LoggingModule(),
			new ServerModule(),
			new MockApiModule()
		);
	}
	
	@Test
	public void testInjection_configWithEnvironmentVariables() throws IOException {
		envVariables.mock();
		Injector injector = createInjector();
		MockIntegrationClass mock = injector.getInstance(MockIntegrationClass.class);
		
		assertNotNull(mock);
		assertNotNull(mock.setupConfig);
		assertTrue(mock.setupConfig instanceof EnvironmentConfig);
		assertEquals("environment", mock.setupConfig.getAsString("test.config.source"));
		
		assertNotNull(mock.applicationConfig);
		assertTrue(mock.applicationConfig instanceof DatabaseConfig);
		assertEquals("database", mock.applicationConfig.getAsString("test.config.source"));
	}
	
	@Test
	public void testInjection_loggingProviderConsole() throws IOException {
		envVariables.mock();
		Injector injector = createInjector();
		LoggingProvider loggingProvider = injector.getInstance(LoggingProvider.class);
		        
		assertNotNull(loggingProvider);
		assertTrue(loggingProvider instanceof ConsoleLoggingProvider);

		Logger logger = loggingProvider.getLogger(InjectionTest.class);
		assertTrue(logger instanceof ConsoleLogger);
		logger.error("Should not throw exception");
	}
	
	@Test
	public void testInjection_loggingProviderDatabase() throws IOException {
		envVariables.with("database.logging.enabled", "true").mock();
		Injector injector = createInjector();
		LoggingProvider loggingProvider = injector.getInstance(LoggingProvider.class);
		        
		assertNotNull(loggingProvider);
		assertTrue(loggingProvider instanceof DatabaseLoggingProvider);
		
		Logger logger = loggingProvider.getLogger(InjectionTest.class);
		assertTrue(logger instanceof DatabaseLogger);
		logger.error("Should not throw exception");
	}
	
	@Test
	public void testInjection_defaultPort() throws IOException {
		envVariables.mock();
		Injector injector = createInjector();
		MockIntegrationClass mock = injector.getInstance(MockIntegrationClass.class);
		assertEquals(8000, mock.port);
	}
	
	@Test
	public void testInjection_definedPort() throws IOException {
		envVariables.with("app.port", "9595").mock();
		Injector injector = createInjector();
		MockIntegrationClass mock = injector.getInstance(MockIntegrationClass.class);
		assertEquals(9595, mock.port);
	}
	
	@Test
	public void testInjection_applicationProperties() throws IOException {
		envVariables.mock();
		Injector injector = createInjector();
		ApplicationProperties appProps = injector.getInstance(ApplicationProperties.class);
		
		assertNotNull(appProps);
		assertEquals("server-lib-test", appProps.getApplicationName());
		assertEquals("uk.co.lukestevens", appProps.getApplicationGroup());
		assertEquals("2.0.0-test", appProps.getApplicationVersion());
	}
	
	@Test
	public void testInjection_database() throws IOException, SQLException {
		envVariables.mock();
		Injector injector = createInjector();
		Database database = injector.getInstance(Database.class);
		
		assertNotNull(database);
		assertTrue(database instanceof ConfiguredDatabase);
		DatabaseResult dbr = database.query("select count(*) from information_schema.tables where table_schema = 'core'");
		assertTrue(dbr.getResultSet().next());
		assertEquals(2, dbr.getResultSet().getInt(1));
	}
	
	@Test
	public void testInjection_propertyService() throws IOException, SQLException {
		envVariables.mock();
		Injector injector = createInjector();
		PropertyService propertyService = injector.getInstance(PropertyService.class);
		
		assertNotNull(propertyService);
		assertTrue(propertyService instanceof DatabasePropertyService);
		
		List<Property> properties = propertyService.load();
		assertEquals(1, properties.size());
		
		assertEquals("test.config.source", properties.get(0).getKey());
		assertEquals("database", properties.get(0).getValue());
	}
	
	@Test
	public void testInjection_daoProvider() throws IOException, SQLException {
		envVariables.mock();
		Injector injector = createInjector();
		DaoProvider daoProvider = injector.getInstance(DaoProvider.class);
		
		assertNotNull(daoProvider);
		assertTrue(daoProvider instanceof HibernateController);
		assertNotNull(daoProvider.getDao(Log.class));
		
	}
	
	@Test
	public void testInjection_baseServerNoPort() throws IOException {
		envVariables.mock();
		Injector injector = createInjector();
		BaseServer server = injector.getInstance(BaseServer.class);
		        
		assertNotNull(server);
	}
	
	@Test
	public void testInjection_baseServerWithPort() throws IOException {
		envVariables.with("app.port", "8001").mock();
		Injector injector = createInjector();
		BaseServer server = injector.getInstance(BaseServer.class);
		        
		assertNotNull(server);
		server.init();
		assertEquals(8001, server.getService().port());
	}
	
	@Test
	public void testInjection_gson() throws IOException {
		envVariables.mock();
		Injector injector = createInjector();
		Gson gson = injector.getInstance(Gson.class);
		
		assertNotNull(gson);
		assertEquals("{\"serialisedValue\":\"some-value\"}", gson.toJson(new MockSerialisedClass()));
	}
	
	@Test
	public void testInjection_routeConfiguration() throws IOException, SQLException {
		envVariables.mock();
		Injector injector = createInjector();
		RouteConfiguration routeConfiguration = injector.getInstance(RouteConfiguration.class);
		
		assertNotNull(routeConfiguration);
		assertTrue(routeConfiguration instanceof MockRouteConfiguration);
	}
	
}
