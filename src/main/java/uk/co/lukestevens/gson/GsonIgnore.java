package uk.co.lukestevens.gson;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that defines a field or class that should not be serialised 
 * using Gson. The Gson instance must have been built with {@link GsonIgnoreExclusionStrategy}
 * for this to work.
 * 
 * @author Luke Stevens
 */
@Retention(RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface GsonIgnore {}
