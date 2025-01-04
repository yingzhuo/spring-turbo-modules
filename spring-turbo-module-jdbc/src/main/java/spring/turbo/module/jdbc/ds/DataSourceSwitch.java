package spring.turbo.module.jdbc.ds;

import java.lang.annotation.*;

/**
 * 数据源切换
 *
 * @author 应卓
 * @since 3.4.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSourceSwitch {

    /**
     * 数据源名称
     *
     * @return 数据源名称
     */
    public String value();

}
