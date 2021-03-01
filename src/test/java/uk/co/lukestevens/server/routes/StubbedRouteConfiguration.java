package uk.co.lukestevens.server.routes;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import uk.co.lukestevens.logging.LoggingProvider;

public class StubbedRouteConfiguration extends AbstractRouteConfiguration {

	public StubbedRouteConfiguration(LoggingProvider loggingProvider, Gson gson) {
		super(loggingProvider, gson);
	}

	@Override
	public List<DefinedRoute> configureRoutes() {
		return new ArrayList<>();
	}
	
}
