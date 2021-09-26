package com.example.demo;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class TestEnabledCondition implements ExecutionCondition {

    static class AnnotationDescription {
        String name;
        Boolean annotationEnabled;
        AnnotationDescription(String prefix, String property) {
            this.name = prefix + property;
        }
        String getName() {
            return name;
        }
        AnnotationDescription setAnnotationEnabled(Boolean value) {
            this.annotationEnabled = value;
            return this;
        }
        Boolean isAnnotationEnabled() {
            return annotationEnabled;
        }
    }

    private AnnotationDescription makeDescription(ExtensionContext context, String property) {
        String prefix = context.getTestClass()
                .map(cl -> cl.getAnnotation(TestEnabledPrefix.class))
                .map(TestEnabledPrefix::prefix)
                .map(pref -> !pref.isEmpty() && !pref.endsWith(".") ? pref + "." : "")
                .orElse("");
        return new AnnotationDescription(prefix, property);
    }

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Environment environment = SpringExtension.getApplicationContext(context).getEnvironment();

        return context.getElement()
                .map(e -> e.getAnnotation(TestEnabled.class))
                .map(TestEnabled::property)
                .map(property -> makeDescription(context, property))
                .map(description -> description.setAnnotationEnabled(environment.getProperty(description.getName(), Boolean.class)))
                .map(description -> {
                    if (description.isAnnotationEnabled()) {
                        return ConditionEvaluationResult.enabled("Enabled by property: "+description.getName());
                    } else {
                        return ConditionEvaluationResult.disabled("Disabled by property: "+description.getName());
                    }
                }).orElse(
                        ConditionEvaluationResult.enabled("Enabled by default")
                );

    }
}