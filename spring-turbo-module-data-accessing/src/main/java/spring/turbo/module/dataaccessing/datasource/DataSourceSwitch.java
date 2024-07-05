package spring.turbo.module.dataaccessing.datasource;

import java.lang.annotation.*;

/**
 * 切换用元注释
 *
 * @author 应卓
 * @see DataSourceSwitchingAdvice
 * @since 1.1.0
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceSwitch {

    /**
     * 要切换的数据源的名称
     *
     * @return 数据源的名称
     */
    public String value();

}
