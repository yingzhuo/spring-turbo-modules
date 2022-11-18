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
public abstract class AbstractConvolveImageOp extends AbstractImageOp {

    private final float[][] matrix;

    protected AbstractConvolveImageOp(float[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    protected void filter(int[] inPixels, int[] outPixels, int width, int height) {
        int matrixWidth = matrix[0].length;
        int matrixHeight = matrix.length;
        int mattrixLeft = -matrixWidth / 2;
        int matrixTop = -matrixHeight / 2;
        for (int y = 0; y < height; y++) {
            int ytop = y + matrixTop;
            int ybottom = y + matrixTop + matrixHeight;
            for (int x = 0; x < width; x++) {
                float[] sum = {0.5f, 0.5f, 0.5f, 0.5f};
                int xleft = x + mattrixLeft;
                int xright = x + mattrixLeft + matrixWidth;
                int matrixY = 0;
                for (int my = ytop; my < ybottom; my++, matrixY++) {
                    int matrixX = 0;
                    for (int mx = xleft; mx < xright; mx++, matrixX++) {
                        int pixel = getPixel(inPixels, mx, my, width, height, EDGE_ZERO);
                        float m = matrix[matrixY][matrixX];
                        sum[0] += m * ((pixel >> 24) & 0xff);
                        sum[1] += m * ((pixel >> 16) & 0xff);
                        sum[2] += m * ((pixel >> 8) & 0xff);
                        sum[3] += m * (pixel & 0xff);
                    }
                }
                outPixels[x + y * width] = (limitByte((int) sum[0]) << 24) | (limitByte((int) sum[1]) << 16) | (limitByte((int) sum[2]) << 8) | (limitByte((int) sum[3]));
            }
        }
    }
}
