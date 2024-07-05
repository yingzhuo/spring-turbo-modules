package spring.turbo.module.misc.captcha.google.filter.lib;

/**
 * @since 1.0.0
 */
public class BlurImageOp extends AbstractConvolveImageOp {

    private static final float[][] matrix = {{1 / 16f, 2 / 16f, 1 / 16f}, {2 / 16f, 4 / 16f, 2 / 16f},
            {1 / 16f, 2 / 16f, 1 / 16f}};

    public BlurImageOp() {
        super(matrix);
    }

}
