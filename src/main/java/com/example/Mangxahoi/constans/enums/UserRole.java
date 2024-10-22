package com.example.Mangxahoi.constans.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum UserRole implements GrantedAuthority {
    USER("user"),
    ADMIN("admin");


    private String name;
    @JsonValue
    public String getCode() {
        return name;
    }
    @JsonCreator
    public static UserRole parseByCode(String name) {
        if (name == null) {
            return null;
        }
        for (UserRole status : values()) {
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

    @Override
    public String getAuthority() {
        return this.name;
    }
}
