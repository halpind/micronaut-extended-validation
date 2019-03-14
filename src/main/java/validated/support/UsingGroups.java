package validated.support;

import io.micronaut.context.annotation.AliasFor;

import javax.validation.groups.Default;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface UsingGroups {
    Class[] groups() default {Default.class};
}
