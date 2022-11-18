/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.google.filter.lib;

/**
 * @since 1.0.0
 */
public class SoftenImageOp extends AbstractConvolveImageOp {

    private static final float[][] matrix = {{0 / 16f, 1 / 16f, 0 / 16f},
            {1 / 16f, 12 / 16f, 1 / 16f}, {0 / 16f, 1 / 16f, 0 / 16f}};

    public SoftenImageOp() {
        super(matrix);
    }
}
