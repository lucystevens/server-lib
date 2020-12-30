package uk.co.lukestevens.injection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import spark.route.HttpMethod;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.encryption.IgnoredEncryptionService;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.injection.BaseInjectModule;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;
import uk.co.lukestevens.server.BaseServer;
import uk.co.lukestevens.server.setup.ServerSetup;
import uk.co.lukestevens.testing.db.TestDatabase;

public class BaseInjectModuleTest {
	
	@BeforeAll
	public static void setup() throws IOException, SQLException {
		TestDatabase db = new TestDatabase();
		db.executeFile("setup");
	}
	
	@Test
	public void testInjection_baseServerNoPort() throws IOException {
		ServerSetup setup = new ServerSetup();
		BaseInjectModule module = new BaseInjectModule(setup);
		Injector injector = Guice.createInjector(module);
		BaseServer server = injector.getInstance(BaseServer.class);
		        
		assertNotNull(server);
	}
	
	@Test
	public void testInjection_baseServerWithPort() throws IOException {
		ServerSetup setup = new ServerSetup();
		setup.setPort(8001);
		BaseInjectModule module = new BaseInjectModule(setup);
		Injector injector = Guice.createInjector(module);
		BaseServer server = injector.getInstance(BaseServer.class);
		        
		assertNotNull(server);
	}
	
	@Test
	public void testInjection_gson() throws IOException {
		ServerSetup setup = new ServerSetup();
		BaseInjectModule module = new BaseInjectModule(setup);
		Injector injector = Guice.createInjector(module);
		Gson gson = injector.getInstance(Gson.class);
		
		assertNotNull(gson);
		assertEquals("{\"serialisedValue\":\"some-value\"}", gson.toJson(new MockSerialisedClass()));
	}

}
