package com.tuacy.jexl;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @name: JexlTest
 * @author: tuacy.
 * @date: 2019/12/19.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JexlTest {

    @Test
    public void test() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("money", 2100);
            String expression = "money>=2000&&money<=4000";
            Object code = convertToCode(expression, map);
            if ((boolean) code) {
                System.out.println("true");
            } else {
                System.out.println("false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 替换逻辑表达式中的模板
     */
    public static Object convertToCode(String jexlExp, Map<String, Object> map) {
        JexlEngine jexlEngine = new JexlEngine();
        Expression expression = jexlEngine.createExpression(jexlExp);
        JexlContext jexlContext = new MapContext();
        for (String key : map.keySet()) {
            jexlContext.set(key, map.get(key));
        }
        return expression.evaluate(jexlContext);
    }

}
