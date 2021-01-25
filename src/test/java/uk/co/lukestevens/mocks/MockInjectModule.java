package uk.co.lukestevens.mocks;

import uk.co.lukestevens.injection.BaseInjectModule;
import uk.co.lukestevens.server.routes.RouteConfiguration;

public class MockInjectModule extends BaseInjectModule {

	@Override
	protected void bindRouteConfiguration() {
		bind(RouteConfiguration.class).to(MockRouteConfiguration.class);
	}
	

}
