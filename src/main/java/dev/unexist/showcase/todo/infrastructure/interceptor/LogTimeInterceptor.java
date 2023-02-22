/**
 * @package Showcase-AOP-Micronaut
 *
 * @file AOP execution time interceptor
 * @copyright 2021-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.interceptor;

import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@InterceptorBean(LogTime.class)
public class LogTimeInterceptor implements MethodInterceptor<Object, Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogTimeInterceptor.class);

    /**
     * Intercept method calls and print duration of call
     *
     * @param  context  {@link MethodInvocationContext} of this call
     *
     * @return Result of the intercepted method
     **/

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        long start = System.nanoTime();

        Object result = context.proceed();

        long end = System.nanoTime() - start;

        LOGGER.info("Execution of {}.{} took {}ms", context.getDeclaringType().getSimpleName(),
                context.getName(), (end/1000));

        return result;
    }
}
