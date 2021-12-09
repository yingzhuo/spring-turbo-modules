/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader.annotation;

import spring.turbo.module.excel.ExitPolicy;
import spring.turbo.module.excel.context.ErrorContext;
import spring.turbo.module.excel.context.SuccessContext;
import spring.turbo.module.excel.context.ThrowableContext;

/**
 * @author 应卓
 * @since 1.0.0
 */
public interface ValueObjectListener<T> {

    public default void onSuccess(SuccessContext<T> context) {
    }

    public default void onError(ErrorContext<T> context) {
    }

    public default ExitPolicy onThrowable(ThrowableContext context) {
        return ExitPolicy.CONTINUE;
    }

}
