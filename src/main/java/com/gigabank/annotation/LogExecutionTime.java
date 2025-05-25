package com.gigabank.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для измерения времени выполнения метода.
 * <p>
 * Применяется к методам, для которых необходимо замерить и выполнить логирование
 * времени их выполнения. Аннотация сохраняется в runtime и может быть обработана
 * с помощью AOP или других инструментов для логирования.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogExecutionTime {
}
