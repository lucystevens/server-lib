package uk.co.lukestevens.mocks;

import java.util.ArrayList;
import java.util.List;

import uk.co.lukestevens.server.routes.DefinedRoute;
import uk.co.lukestevens.server.routes.RouteConfiguration;

public class MockRouteConfiguration implements RouteConfiguration {

	@Override
	public List<DefinedRoute> configureRoutes() {
		return new ArrayList<>();
	}

}
