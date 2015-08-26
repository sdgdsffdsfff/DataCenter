package com.incar.convert;


import com.incar.enums.DataType;

import java.lang.reflect.Method;

public class DataBee {
    private Method setter;
    private Method getter;
    private DataType dataType;
    private Integer order;
    private Object value;
    public DataBee(Method getter,Method setter, DataType dataType) {
        this.getter=getter;
        this.setter = setter;
        this.dataType = dataType;
    }
    public DataBee(Method getter,Method setter, DataType dataType, Integer order) {
        this.getter=getter;
        this.setter = setter;
        this.dataType = dataType;
        this.order = order;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
