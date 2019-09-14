package com.tuacy.study.springboot.aware.EmbeddedValueResolverAware;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class BeanEmbeddedValueResolverAware implements EmbeddedValueResolverAware {

    private StringValueResolver resolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;

        // 读取配置文件里面ppid对应的值
        StringBuilder name = new StringBuilder("${").append("ppid").append("}");
        String value = resolver.resolveStringValue(name.toString());
    }
}
