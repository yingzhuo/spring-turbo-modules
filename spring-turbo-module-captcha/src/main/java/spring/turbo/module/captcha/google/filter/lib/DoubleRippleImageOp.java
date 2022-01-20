/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.filter.lib;

/**
 * @since 1.0.0
 */
public class DoubleRippleImageOp extends RippleImageOp {

    @Override
    protected void transform(int x, int y, double[] t) {
        double tx = Math.sin((double) y / yWavelength + yRandom) + 1.3 * Math.sin(0.6 * y / yWavelength + yRandom);
        double ty = Math.cos((double) x / xWavelength + xRandom) + 1.3 * Math.cos(0.6 * x / xWavelength + xRandom);
        t[0] = x + xAmplitude * tx;
        t[1] = y + yAmplitude * ty;
    }

}
