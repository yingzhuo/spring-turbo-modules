/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.filter.factory;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.util.ClassUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import spring.turbo.module.security.DefaultFilterConfiguration;
import spring.turbo.module.security.FilterConfiguration;
import spring.turbo.module.security.filter.HumanReadableRequestLoggingFilter;
import spring.turbo.util.reflection.InstanceUtils;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class LoggingFilterFactoryBean implements FactoryBean<FilterConfiguration<Filter>> {

    private final Filter filter;

    /**
     * 构造方法
     *
     * @param filterClass 日志过滤器的类型
     */
    public LoggingFilterFactoryBean(Class<? extends Filter> filterClass) {
        if (filterClass == HumanReadableRequestLoggingFilter.class ||
                ClassUtils.isAssignable(AbstractRequestLoggingFilter.class, filterClass)) {
            this.filter = InstanceUtils.newInstanceElseThrow(filterClass);
        } else {
            throw new IllegalArgumentException("not supported filter type: " + filterClass.getName());
        }
    }

    @Override
    public FilterConfiguration<Filter> getObject() {
        return new DefaultFilterConfiguration(
                filter,
                DisableEncodeUrlFilter.class,
                FilterConfiguration.Position.BEFORE
        );
    }

    @Override
    public Class<?> getObjectType() {
        return FilterConfiguration.class;
    }

}
