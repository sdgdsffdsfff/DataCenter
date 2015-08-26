package com.incar.convert;

import com.incar.enums.DataType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RUNTIME)
public @interface DataMel{
    DataType value() default DataType.AUTO;
    int order() default -1;
}
