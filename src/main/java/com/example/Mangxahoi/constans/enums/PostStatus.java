package com.example.Mangxahoi.constans.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
@AllArgsConstructor
@Getter
public enum PostStatus {
    ENABLE("enable"),
    DISABLE("disable");


    private String name;

    @JsonCreator
    public static PostStatus parseByCode(@JsonProperty("name") String name) {
        if (name == null) {
            return null;
        }
        for (PostStatus status : values()) {
            if (Objects.equals(status.name, name)) {
                return status;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
