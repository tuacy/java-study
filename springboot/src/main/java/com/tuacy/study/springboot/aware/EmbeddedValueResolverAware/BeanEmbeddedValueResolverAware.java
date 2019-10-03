package com.tuacy.study.springboot.aware.EmbeddedValueResolverAware;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class BeanEmbeddedValueResolverAware implements EmbeddedValueResolverAware {

    /**
     * 实际开发中，一般会使用@Value注解来提点这一部分的用法。
     */
    private StringValueResolver resolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;

        // 实际开发中，一般会使用@Value注解来提点这一部分的用法。
        // 读取配置文件里面ppid对应的值
        StringBuilder name = new StringBuilder("${").append("ppid").append("}");
        String value = resolver.resolveStringValue(name.toString());
    }
}
