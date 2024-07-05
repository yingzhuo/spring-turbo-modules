package spring.turbo.module.misc.captcha.google.filter;

import java.awt.image.BufferedImage;

/**
 * @author 应卓
 * @since 1.0.0
 */
@FunctionalInterface
public interface FilterFactory {

    public BufferedImage apply(BufferedImage source);

}
