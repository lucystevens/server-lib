package uk.co.lukestevens.server;

import com.google.inject.AbstractModule;

import uk.co.lukestevens.encryption.EncryptionService;
import uk.co.lukestevens.encryption.IgnoredEncryptionService;

public class NoEncryptionModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(EncryptionService.class).to(IgnoredEncryptionService.class);
	}
	

}
