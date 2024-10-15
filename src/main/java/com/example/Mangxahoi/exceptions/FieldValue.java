package com.example.Mangxahoi.exceptions;

import java.io.Serializable;

public class FieldValue implements Serializable {

    private final String field;
    private final transient Object value;

    public FieldValue(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String toString() {
        return "field = '" + this.field + "' - value = '" + this.value + "'";
    }

    public String getField() {
        return this.field;
    }

    public Object getValue() {
        return this.value;
    }
}
