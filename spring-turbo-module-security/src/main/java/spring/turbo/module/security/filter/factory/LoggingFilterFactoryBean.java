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
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import spring.turbo.module.security.DefaultFilterConfiguration;
import spring.turbo.module.security.FilterConfiguration;
import spring.turbo.module.security.filter.HumanReadableRequestLoggingFilter;
import spring.turbo.util.reflection.InstanceUtils;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class LoggingFilterFactoryBean implements FactoryBean<FilterConfiguration<Filter>> {

    private Filter filter;
    private FilterConfiguration.Position position = FilterConfiguration.Position.BEFORE;
    private Class<? extends Filter> positionInChain = DisableEncodeUrlFilter.class;

    /**
     * 默认构造方法
     */
    public LoggingFilterFactoryBean() {
        this(new CommonsRequestLoggingFilter());
    }

    /**
     * 构造方法
     *
     * @param filter 日志过滤器
     */
    public LoggingFilterFactoryBean(Filter filter) {
        this.filter = filter;
    }

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
                positionInChain,
                position
        );
    }

    @Override
    public Class<?> getObjectType() {
        return FilterConfiguration.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void setPosition(FilterConfiguration.Position position) {
        this.position = position;
    }

    public void setPositionInChain(Class<? extends Filter> positionInChain) {
        this.positionInChain = positionInChain;
    }

}
