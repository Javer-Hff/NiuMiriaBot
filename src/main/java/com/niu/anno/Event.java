package com.niu.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 事件类标记注解
 * @authoer:hff
 * @Date 2023/9/9 10:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface Event {
}
