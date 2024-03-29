/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.convention;

import org.springframework.core.Ordered;
import spring.turbo.convention.ExtraMessageSourceBasenameConvention;

import java.util.Collection;
import java.util.List;

/**
 * @author 应卓
 *
 * @since 2.0.3
 */
public final class ExtraMessageSourceBasenameConventionImpl implements ExtraMessageSourceBasenameConvention {

    @Override
    public Collection<String> getExtraMessageSourceBasename() {
        return List.of("spring.turbo.module.queryselector.ValidationMessages");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

}
