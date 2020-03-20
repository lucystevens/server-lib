package uk.co.lukestevens.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import spark.route.HttpMethod;
import uk.co.lukestevens.config.Config;
import uk.co.lukestevens.config.GenericSiteConfigService;
import uk.co.lukestevens.encryption.IgnoredEncryptionService;
import uk.co.lukestevens.hibernate.HibernateController;
import uk.co.lukestevens.injection.BaseInjectModule;
import uk.co.lukestevens.logging.ConfiguredLoggerFactory;
import uk.co.lukestevens.logging.loggers.ConsoleLogger;
import uk.co.lukestevens.test.db.TestDatabase;

public class IntegrationTest {
	
	@BeforeAll
	public static void setup() throws IOException, SQLException {
		TestDatabase db = new TestDatabase();
		db.executeFile("setup");
	}
	
	@Test
	public void testSetup() throws IOException {
		MockSetup setup = new MockSetup();
		BaseInjectModule<MockSetup> module = new BaseInjectModule<>(setup);
		Injector injector = Guice.createInjector(Modules.override(module).with(new NoEncryptionModule()));
		MockIntegrationClass mock = injector.getInstance(MockIntegrationClass.class);
		
		assertNotNull(mock.encryptionService);
		assertEquals(IgnoredEncryptionService.class, mock.encryptionService.getClass());
		assertEquals("samevalue", mock.encryptionService.encrypt("samevalue"));
		
		assertNotNull(mock.setup);
		assertNotNull(mock.configManager);
		
		assertNotNull(mock.daoProvider);
		assertEquals(HibernateController.class, mock.daoProvider.getClass());
		
		assertNotNull(mock.loggerFactory);
		assertEquals(ConfiguredLoggerFactory.class, mock.loggerFactory.getClass());
		assertEquals(ConsoleLogger.class, mock.loggerFactory.getLogger(IntegrationTest.class).getClass());
		
		assertNotNull(mock.configFile);
		assertEquals(new File("src/test/resources/conf/test.conf"), mock.configFile);
		
		assertNotNull(mock.encryptionKey);
		assertEquals("secretkey", mock.encryptionKey);
		
		assertNotNull(mock.appConfig);
		assertEquals("sa", mock.appConfig.getAsString("test.db.username"));
		assertEquals("test", mock.appConfig.getEncrypted("hibernate.db.alias"));
		
		assertNotNull(mock.siteConfigService);
		assertEquals(GenericSiteConfigService.class, mock.siteConfigService.getClass());
		Config site1 = mock.siteConfigService.get("site1");
		assertEquals("googlekey1", site1.getEncrypted("google.api.key"));
		assertEquals("all-sites", site1.getAsString("global.key"));
		
		assertNotNull(mock.application);
		assertEquals("server-lib-test", mock.application.getName());
		assertEquals("test.lukestevens.co.uk", mock.application.getDomain());
		assertEquals("Integration server for server library", mock.application.getDescription());
		assertEquals("N/A", mock.application.getGitRepo());
		assertEquals(8501, mock.application.getRunningPort());
		assertEquals(8502, mock.application.getUpgradePort());
		assertEquals(8503, mock.application.getInternalPort());
		
		assertNotNull(mock.applicationVersion);
		assertEquals("0.0.1-TEST", mock.applicationVersion);
		        
		assertNotNull(mock.server);
		mock.server.addRoute(HttpMethod.get, "/test", (req, res) -> "this is a test");
		mock.server.init();
		
		{
			OkHttpClient http = new OkHttpClient();
			Request request = new Request.Builder()
			  .url("http://localhost:8501/test")
			  .get()
			  .build();
	
			Response response = http.newCall(request).execute();
			assertEquals("{\"success\":true,\"data\":\"this is a test\"}", response.body().string());
		}
		
		{
			OkHttpClient http = new OkHttpClient();
			Request request = new Request.Builder()
			  .url("http://localhost:8501/status")
			  .get()
			  .build();
	
			Response response = http.newCall(request).execute();
			assertEquals("{\"name\":\"server-lib-test\",\"version\":\"0.0.1-TEST\"}", response.body().string());
		}
	}

}
