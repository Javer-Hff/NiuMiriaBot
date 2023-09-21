package com.niu.core.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 指令类标记注解
 *
 * @authoer:hff
 * @Date 2023/8/2 10:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface Command {
    String[] name();
}
