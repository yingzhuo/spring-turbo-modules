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
import spring.turbo.module.security.FilterConfiguration;

/**
 * {@link FilterConfiguration} 默认实现
 *
 * @author 应卓
 * @since 3.3.2
 */
public class DefaultFilterConfiguration implements FilterConfiguration<Filter> {

    private final Filter filter;
    private final Class<? extends Filter> positionInChain;
    private final Position position;

    public DefaultFilterConfiguration(Filter filter, Class<? extends Filter> positionInChain, Position position) {
        this.filter = filter;
        this.positionInChain = positionInChain;
        this.position = position;
    }

    @Override
    public Filter create() {
        return filter;
    }

    @Override
    public Class<? extends Filter> positionInChain() {
        return positionInChain;
    }

    @Override
    public Position position() {
        return this.position;
    }

}
