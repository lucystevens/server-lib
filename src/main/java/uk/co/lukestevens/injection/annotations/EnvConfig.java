package uk.co.lukestevens.injection.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Injection annotation to denote config fetched from
 * the System environment variables, used for setting up the 
 * application and database connections.
 * 
 * @author Luke Stevens
 */
@Qualifier @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
public @interface EnvConfig {

}
