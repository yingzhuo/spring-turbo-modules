/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   ___ | '_ | '__| | '_  / _` || || | | | '__| '_  / _
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|__, ||_| __,_|_|  |_.__/ ___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.filter.lib;

/**
 * @author Piotr Piastucki
 * @since 1.0.0
 */
public abstract class AbstractTransformImageOp extends AbstractImageOp {

    private boolean initialized;

    public AbstractTransformImageOp() {
        setEdgeMode(EDGE_CLAMP);
    }

    protected abstract void transform(int x, int y, double[] t);

    protected void init() {
    }

    @Override
    protected void filter(int[] inPixels, int[] outPixels, int width, int height) {
        if (!initialized) {
            init();
            initialized = true;
        }
        double[] t = new double[2];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                transform(x, y, t);
                int pixel = getPixelBilinear(inPixels, t[0], t[1], width, height, getEdgeMode());
                outPixels[x + y * width] = pixel;
            }
        }
    }

}
