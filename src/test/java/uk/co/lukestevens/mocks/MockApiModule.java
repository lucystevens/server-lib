package uk.co.lukestevens.mocks;

import com.google.inject.AbstractModule;

import uk.co.lukestevens.server.routes.RouteConfiguration;

public class MockApiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RouteConfiguration.class).to(MockRouteConfiguration.class);
	}
	

}
