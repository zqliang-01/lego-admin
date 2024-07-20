package com.lego.core.data;

import com.lego.core.data.hibernate.IJpaFilterName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    @Getter
    @AllArgsConstructor
    enum Type {
        EMPLOYEE(IJpaFilterName.CREATOR_CODE),
        DEPT(IJpaFilterName.DEPT_CODE);

        private String name;
    }

    Type type() default Type.EMPLOYEE;
}
