[![Workflow Status][workflow-badge]][workflow-url]
![Version][version-badge]

# server-lib
server-lib is a Java library for reducing the boilerplate required for API services, allowing them to be built quickly, in a standardised fashion.
The server layer itself is managed by [Spark][spark-repo].

## Installation

Install the latest version of server-lib using Maven:

```	
<dependency>
	<groupId>uk.co.lukestevens</groupId>
	<artifactId>server-lib</artifactId>
	<version>2.0.0-SNAPSHOT</version>
</dependency>
```

### Github Packages Authentication
Currently public packages on Github require authentication to be installed by Maven. Add the following repository to your project's `.m2/settings.xml`

```
<repository>
	<id>github-lukecmstevens</id>
	<name>GitHub lukecmstevens Apache Maven Packages</name>
	<url>https://maven.pkg.github.com/lukecmstevens/packages</url>
	<snapshots><enabled>true</enabled></snapshots>
</repository>
```

For more information see here: [Authenticating with Github packages][gh-package-auth]

## Usage
For example usage, see the [template-api repo][template-api-repo]

When setting up a new API service to use server-lib, you'll need to create a few classes:

### Route Configuration
This can either extend the provided `AbstractRouteConfiguration` class, which contains ease of use methods for standardised response handling,
or it can directly implement the `RouteConfiguration` interface.

This class configures the routes that the http server will set up.

### Dependency Injection
The `BaseInjectModule` manages the provision of various necessary services including config and database classes.
It will need to be extended to provide the required `RouteConfiguration` implementation, and to bind any other services required by
the application (e.g. API services).

To generate the `ServerSetup` object for the `BaseInjectModule` from command line arguments:

```
ServerSetupProvider serverSetupProvider = new ServerSetupProvider();
ServerSetup serverSetup = serverSetupProvider.parseCommandLine(args);
```

The Config classes should be initialised using before using any of the other injected services:

```
ConfigLoader configLoader = injector.getInstance(ConfigLoader.class);
configLoader.initialise();
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

New features, fixes, and bugs should be branched off of develop.

Please make sure to update tests as appropriate.

## License
[MIT][mit-license]

[gh-package-auth]: https://docs.github.com/en/free-pro-team@latest/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages
[workflow-badge]: https://github.com/lukecmstevens/server-lib/workflows/Maven%20Package/badge.svg?branch=develop
[workflow-url]: https://github.com/lukecmstevens/server-lib/actions?query=workflow%3A%22Maven+Package%22
[version-badge]: https://img.shields.io/badge/version-2.0.0--SNAPSHOT-red
[mit-license]: https://choosealicense.com/licenses/mit/
[template-api-repo]: https://github.com/lukecmstevens/template-api
[spark-repo]: https://github.com/perwendel/spark
