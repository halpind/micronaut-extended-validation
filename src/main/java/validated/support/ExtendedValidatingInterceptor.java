package validated.support;

import com.google.common.collect.ImmutableSet;
import io.micronaut.aop.InterceptPhase;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link MethodInterceptor} that validates method invocations.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@Singleton
public class ExtendedValidatingInterceptor implements MethodInterceptor {

    /**
     * The position of the interceptor. See {@link io.micronaut.core.order.Ordered}
     */
    public static final int POSITION = InterceptPhase.VALIDATE.getPosition();

    private static final Logger LOG = LoggerFactory.getLogger(ExtendedValidatingInterceptor.class);

    private final ExecutableValidator executableValidator;

    /**
     * Creates ValidatingInterceptor from the validatorFactory.
     *
     * @param validatorFactory Factory returning initialized {@code Validator} instances
     */
    public ExtendedValidatingInterceptor(Optional<ValidatorFactory> validatorFactory) {

        executableValidator = validatorFactory
                .map(factory -> factory.getValidator().forExecutables())
                .orElse(null);

        if (executableValidator == null && LOG.isWarnEnabled()) {
            LOG.warn("Beans requiring validation present, but no implementation of javax.validation configuration. Add an implementation (such as hibernate-validator) to prevent this error.");
        }
    }

    @Override
    public int getOrder() {
        return POSITION;
    }

    @Override
    public Object intercept(MethodInvocationContext context) {
        if (executableValidator == null) {
            return context.proceed();
        } else {
            Method targetMethod = context.getTargetMethod();
            if (targetMethod.getParameterTypes().length == 0) {
                return context.proceed();
            } else {
                AnnotationValue<UsingGroups> annotation = context.getAnnotation(UsingGroups.class);

                Class[] groups = getGroupsFromAnnotation(annotation);

                Set<ConstraintViolation<Object>> constraintViolations = executableValidator
                        .validateParameters(
                                context.getTarget(),
                                targetMethod,
                                context.getParameterValues(),
                                groups
                        );
                if (constraintViolations.isEmpty()) {
                    return context.proceed();
                } else {
                    throw new ConstraintViolationException(constraintViolations);
                }
            }

        }
    }

    /**
     * Extracts validation groups from annotation. This method will always include the Default group to the group
     * to be validated. If there is no annotation, an array with a single element is returned
     *
     * @param annotation the annotation or {@literal null}
     * @return the validation groups to use. THe outcome will always include an array with a single element
     */
    private Class[] getGroupsFromAnnotation(AnnotationValue<UsingGroups> annotation) {
        Collection<Class<?>> groups = ImmutableSet.<Class<?>>builder().add(Default.class)
                .add(Optional.ofNullable(annotation).map(a -> a.get("groups", Class[].class).orElse(new Class[0])).get()).build();

        return groups.toArray(new Class[0]);

    }
}
