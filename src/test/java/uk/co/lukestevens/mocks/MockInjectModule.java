package uk.co.lukestevens.mocks;

import uk.co.lukestevens.injection.BaseInjectModule;
import uk.co.lukestevens.server.routes.RouteConfiguration;
import uk.co.lukestevens.server.setup.ServerSetup;

public class MockInjectModule extends BaseInjectModule {

	public MockInjectModule(ServerSetup setup) {
		super(setup);
	}

	@Override
	protected void bindRouteConfiguration() {
		bind(RouteConfiguration.class).to(MockRouteConfiguration.class);
	}
	
	

}
