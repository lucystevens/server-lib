package uk.co.lukestevens.injection.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import uk.co.lukestevens.config.Config;

/**
 * Injection annotation to denote {@link Config} fetched from
 * the database.
 * 
 * @author Luke Stevens
 */
@Qualifier @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
public @interface DBConfig {

}
