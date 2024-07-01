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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import spring.turbo.module.security.FilterConfiguration;
import spring.turbo.module.security.filter.CleanupFilter;

import java.util.function.BiConsumer;

/**
 * @author 应卓
 * @since 3.3.2
 */
public class CleanupFilterFactoryBean implements FactoryBean<FilterConfiguration<Filter>> {

    private final BiConsumer<HttpServletRequest, HttpServletResponse> logic;
    private final boolean ignoreExceptions;

    public CleanupFilterFactoryBean(BiConsumer<HttpServletRequest, HttpServletResponse> logic) {
        this(logic, true);
    }

    public CleanupFilterFactoryBean(BiConsumer<HttpServletRequest, HttpServletResponse> logic, boolean ignoreExceptions) {
        this.logic = logic;
        this.ignoreExceptions = ignoreExceptions;
    }

    @Override
    public FilterConfiguration<Filter> getObject() {
        return new FilterConfiguration.Default(
                new CleanupFilter(logic, ignoreExceptions),
                AuthorizationFilter.class,
                FilterConfiguration.Position.AFTER
        );
    }

    @Override
    public Class<?> getObjectType() {
        return FilterConfiguration.class;
    }

}
