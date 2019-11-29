package com.tuacy.redis.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @name: OnPropertyExistCondition
 * @author: tuacy.
 * @date: 2019/11/12.
 * @version: 1.0
 * @Description: 判断配置文件里面指定的属性是否存在
 */
public class OnPropertyExistCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(ConditionalOnPropertyExist.class.getName());
        if (annotationAttributes == null) {
            return false;
        }
        String propertyName = (String) annotationAttributes.get("name");
        boolean values = (boolean) annotationAttributes.get("exist");
        String propertyValue = conditionContext.getEnvironment().getProperty(propertyName);
        if(values) {
            return !StringUtils.isEmpty(propertyValue);
        } else {
            return StringUtils.isEmpty(propertyValue);
        }
    }
}
