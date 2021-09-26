package com.example.demo;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(TestEnabledCondition.class)
public @interface TestEnabled {
    String property();
}
