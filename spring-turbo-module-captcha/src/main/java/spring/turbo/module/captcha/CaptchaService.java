/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   ___ | '_ | '__| | '_  / _` || || | | | '__| '_  / _
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|__, ||_| __,_|_|  |_.__/ ___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha;

import java.util.function.Supplier;

/**
 * @author Piotr Piastucki
 * @author 应卓
 * @since 1.0.0
 */
@FunctionalInterface
public interface CaptchaService extends Supplier<Captcha> {

    public Captcha create();

    @Override
    public default Captcha get() {
        return create();
    }

}
