/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader.annotation;

import spring.turbo.module.excel.reader.NullPasswordProvider;
import spring.turbo.module.excel.reader.PasswordProvider;

import java.lang.annotation.*;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * Excel文档密码
 *
 * @author 应卓
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Password {

    /**
     * 指定密码
     *
     * @return 密码
     */
    public String value() default EMPTY;

    /**
     * 指定密码生成器
     *
     * @return 密码生成器类型
     * @since 1.0.4
     */
    public Class<? extends PasswordProvider> provider() default NullPasswordProvider.class;

}
