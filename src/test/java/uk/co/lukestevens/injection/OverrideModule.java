package uk.co.lukestevens.injection;

import com.google.inject.AbstractModule;

import uk.co.lukestevens.injection.annotations.AppPort;

public class OverrideModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(Integer.class).annotatedWith(AppPort.class).toInstance(10);
	}
	
}
