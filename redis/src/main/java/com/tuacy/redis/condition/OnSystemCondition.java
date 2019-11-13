package com.tuacy.redis.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @name: OnSystemCondition
 * @author: tuacy.
 * @date: 2019/11/13.
 * @version: 1.0
 * @Description:
 */
public class OnSystemCondition implements Condition {


    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnSystem.class.getName());
        if (annotationAttributes == null) {
            return false;
        }
        ConditionalOnSystem.SystemType systemType = (ConditionalOnSystem.SystemType) annotationAttributes.get("type");
        switch (systemType) {
            case WINDOWS:
                return context.getEnvironment().getProperty("os.name").contains("Windows");
            case LINUX:
                return context.getEnvironment().getProperty("os.name").contains("Linux ");
            case MAC:
                return context.getEnvironment().getProperty("os.name").contains("Mac ");
        }
        return false;
    }
}
