package uk.co.lukestevens.app;

import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import uk.co.lukestevens.injection.ConfigModule;
import uk.co.lukestevens.injection.DatabaseModule;
import uk.co.lukestevens.injection.LoggingModule;
import uk.co.lukestevens.injection.ServerModule;
import uk.co.lukestevens.server.BaseServer;

public class App {
	
	public static void start(Module...modules) {
		Injector injector = createInjector(modules);
		
		BaseServer server = injector.getInstance(BaseServer.class);
		server.init();
	}
	
	public static Injector createInjector(Module...modules) {
		Module finalModule = Modules.override(getModules()).with(modules);
		return Guice.createInjector(finalModule);	
	}
	
	public static List<Module> getModules(){
		return Arrays.asList(
				new ConfigModule(),
				new DatabaseModule(),
				new LoggingModule(),
				new ServerModule()
		);
	}

}
