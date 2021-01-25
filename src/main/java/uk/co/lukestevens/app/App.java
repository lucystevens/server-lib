package uk.co.lukestevens.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import uk.co.lukestevens.injection.ConfigModule;
import uk.co.lukestevens.injection.DatabaseModule;
import uk.co.lukestevens.injection.LoggingModule;
import uk.co.lukestevens.injection.ServerModule;
import uk.co.lukestevens.server.BaseServer;

public class App {
	
	public static void start(Module apiModule) {
		Injector injector = Guice.createInjector(
					new ConfigModule(),
					new DatabaseModule(),
					new LoggingModule(),
					new ServerModule(),
					apiModule
				);
		
		BaseServer server = injector.getInstance(BaseServer.class);
		server.init();
	}

}
