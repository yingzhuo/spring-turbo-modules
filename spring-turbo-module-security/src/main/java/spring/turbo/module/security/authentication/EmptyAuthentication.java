/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.authentication;

import spring.turbo.lang.Mutable;

/**
 * @author 应卓
 * @since 1.0.4
 */
@Mutable
public final class EmptyAuthentication extends Authentication {

    public static EmptyAuthentication newInstance() {
        return new EmptyAuthentication();
    }

    private EmptyAuthentication() {
        super(null);
        super.setAuthenticated(false);
    }

    @Override
    public String getName() {
        return "null";
    }

    @Override
    public Object getDetails() {
        return null;
    }

}
