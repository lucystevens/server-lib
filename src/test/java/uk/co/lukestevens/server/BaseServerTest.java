package uk.co.lukestevens.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import spark.route.HttpMethod;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import uk.co.lukestevens.config.ApplicationProperties;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.server.routes.DefinedRoute;
import uk.co.lukestevens.server.routes.RouteConfiguration;

public class BaseServerTest {

	ApplicationProperties appProps;
	RouteConfiguration routeConfig;
	BaseServer server;
	OkHttpClient client = new OkHttpClient();
	
	@BeforeEach
	public void setup() {
		this.appProps = mock(ApplicationProperties.class);
		this.routeConfig = mock(RouteConfiguration.class);
		this.server = new BaseServer(8080, appProps, routeConfig);
	}
	
	@AfterEach
	public void tearDown() {
		this.server.getService().awaitStop();
	}
	
	@Test
	public void testBaseServerIntegration() throws IOException {
		DefinedRoute testRoute = new DefinedRoute("/api/test", (req, res) -> "tested", HttpMethod.get);
		when(routeConfig.configureRoutes()).thenReturn(Arrays.asList(testRoute));
		
		when(appProps.getApplicationName()).thenReturn("server-lib-test");
		when(appProps.getApplicationVersion()).thenReturn("2.0.0-test");
		
		this.server.init();
		assertEquals(8080, server.getService().port());

		verify(routeConfig, times(1)).configureRoutes();
		
		assertEquals("tested", get("/api/test"));
		assertEquals("{\"name\":\"server-lib-test\",\"version\":\"2.0.0-test\"}", get("/api/status"));
	}
	
	public String get(String url) throws IOException {
		Request request = new Request.Builder()
				.url("http://localhost:8080" + url)
				.build();
		
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

}
