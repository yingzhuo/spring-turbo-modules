package spring.turbo.module.misc.captcha.google.filter.predefined;

import spring.turbo.module.misc.captcha.google.filter.FilterFactory;

import java.awt.image.BufferedImage;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class NoopFilterFactory implements FilterFactory {

    @Override
    public BufferedImage apply(BufferedImage source) {
        return source;
    }

}
