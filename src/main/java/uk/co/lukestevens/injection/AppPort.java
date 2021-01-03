package uk.co.lukestevens.injection;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;

/**
 * Injection annotation for the port an application should run on (int).
 * 
 * @author Luke Stevens
 *
 */
@Qualifier @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
public @interface AppPort {}

