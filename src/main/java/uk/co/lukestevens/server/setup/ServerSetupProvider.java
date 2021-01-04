package uk.co.lukestevens.server.setup;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A provider class for parsing command line arguments into a {@link ServerSetup} object
 * 
 * @author Luke Stevens
 */
public class ServerSetupProvider {
	
	static final String PORT_OPT = "p";
	static final String DATABASE_LOGGING_OPT = "d";
	static final String CONFIG_OPT = "c";
	static final String HELP_OPT = "h";
	
	/**
	 * Parses commandline arguments into a setup object.<br/>
	 * Also defines a help command for listing what each option does.
	 * @param args Commandline arguments
	 * @return A setup object, or null if help command specified.
	 * @throws ParseException If there was an error parsing the arguments provided
	 */
	public ServerSetup parseCommandLine(String[] args) throws ParseException {
		
		// Create options object and add all fields to it
		Options options = new Options();
		options.addOption(PORT_OPT, "port", true,  "The port to run this service on. Defaults to 8000");
		options.addOption(DATABASE_LOGGING_OPT, "enable-db-logging", false,  "Enable database logging. Defaults to false");
		options.addOption(CONFIG_OPT, "config", true,  "An optional config file to override environment variables");
		options.addOption(HELP_OPT, "help", false, "Print script usage information");
		
		// Parse commandline args
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		// If usage is specified, and help command provided, then print help and exit
		if(cmd.hasOption(HELP_OPT)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("run", options);
			return null; // Don't continue with application if help specified
		}
		
		ServerSetup setup = new ServerSetup();
		
		// Parse port from arguments
		if(cmd.hasOption(PORT_OPT)) {
			String portString = cmd.getOptionValue(PORT_OPT);
			if(!portString.matches("\\d+")) {
				throw new RuntimeException("Expected port to be a number, was instead " + portString);
			}
			setup.setPort(Integer.parseInt(portString));
		}
		
		// Parse database logging from arguments
		setup.useDatabaseLogging(cmd.hasOption(DATABASE_LOGGING_OPT));
		
		// Parse config file from arguments
		if(cmd.hasOption(CONFIG_OPT)) {
			String configString = cmd.getOptionValue(CONFIG_OPT);
			File config = new File(configString);
			if(!config.exists()) {
				throw new RuntimeException("Config file '" + configString + "' not found");
			}
			setup.setConfigFile(config);
		}
		
		return setup;
	}

}
