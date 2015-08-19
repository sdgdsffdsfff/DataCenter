package com.incar.convert;

import com.incar.DataType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RUNTIME)
public @interface DataMel{
    DataType type() default DataType.AUTO;
    int order() default -1;
}
