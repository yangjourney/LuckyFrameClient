package luckyclient.execution.dispose.actionkeyword;

import java.lang.annotation.*;

/**
 * �����ؼ���ע�ⶨ��
 * @author: sunshaoyan
 * @date: Created on 2019/4/13
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Action {
    String name() default "";
}
