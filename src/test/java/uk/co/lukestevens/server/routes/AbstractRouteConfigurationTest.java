package uk.co.lukestevens.server.routes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.co.lukestevens.logging.Logger;
import uk.co.lukestevens.server.exceptions.ServerException;

public class AbstractRouteConfigurationTest {
	
	Gson gson;
	Logger logger;
	AbstractRouteConfiguration routeConfig;
	
	
	@BeforeEach
	public void setup() {
		gson = new Gson();
		logger = mock(Logger.class);
		routeConfig = new StubbedRouteConfiguration(s -> logger, gson);
	}
	
	Request mockRequest() {
		Request req = mock(Request.class);
		when(req.ip()).thenReturn("127.0.0.1");
		when(req.requestMethod()).thenReturn("GET");
		when(req.url()).thenReturn("/api/test");
		return req;
	}
	
	@Test
	public void testHandleRoute_success() throws Exception {
		Route route = (req, res) -> "OK";
		Route sparkRoute = routeConfig.handleRoute(route);
		
		Request req = mockRequest();
		Response res = mock(Response.class);
		
		Object actualJson = sparkRoute.handle(req, res);
		String expectedJson = "{\"success\":true,\"data\":\"OK\"}";
		assertEquals(expectedJson, actualJson);
		
		verify(logger).info("Request received from 127.0.0.1: GET /api/test");
	}
	
	@Test
	public void testHandleRoute_serverExceptionWithLogging() throws Exception {
		ServerException exception = ServerException.badRequest().logMessage("Error!").build();
		Route route = (req, res) -> { throw exception; };
		
		Route sparkRoute = routeConfig.handleRoute(route);
		
		Request req = mockRequest();
		Response res = mock(Response.class);
		
		Object actualJson = sparkRoute.handle(req, res);
		String expectedJson = "{\"success\":false,\"errors\":{}}";
		assertEquals(expectedJson, actualJson);
		
		verify(res).status(400);
		verify(logger).info("Request received from 127.0.0.1: GET /api/test");
		verify(logger).error(exception);
	}
	
	@Test
	public void testHandleRoute_serverExceptionWithErrors() throws Exception {
		Route route = (req, res) -> {
			throw ServerException.invalidRequest()
				.withError("input", "too long")
				.withError("image", "not a png")
				.build();
		};
		
		Route sparkRoute = routeConfig.handleRoute(route);
		
		Request req = mockRequest();
		Response res = mock(Response.class);
		
		Object actualJson = sparkRoute.handle(req, res);
		String expectedJson = "{\"success\":false,\"errors\":{\"input\":\"too long\",\"image\":\"not a png\"}}";
		assertEquals(expectedJson, actualJson);
		
		verify(res).status(200);
		verify(logger).info("Request received from 127.0.0.1: GET /api/test");
		verify(logger, never()).error(anyString());
	}
	
	@Test
	public void testHandleRoute_generalException() throws Exception {
		IndexOutOfBoundsException e = new IndexOutOfBoundsException("Index: 0, Size: 0");
		Route route = (req, res) -> { throw e; };
		
		Route sparkRoute = routeConfig.handleRoute(route);
		
		Request req = mockRequest();
		Response res = mock(Response.class);
		
		Object actualJson = sparkRoute.handle(req, res);
		String expectedJson = "{\"success\":false,\"errors\":{\"error\":\"Something went wrong, try again later\"}}";
		assertEquals(expectedJson, actualJson);
		
		verify(res).status(400);
		verify(logger).info("Request received from 127.0.0.1: GET /api/test");
		verify(logger).error(e);
	}

}
