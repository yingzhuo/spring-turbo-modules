package spring.turbo.module.misc.captcha.google.filter;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class CompositeFilterFactory implements FilterFactory {

    private final List<FilterFactory> filterFactories;

    public CompositeFilterFactory(FilterFactory... filterFactories) {
        this.filterFactories = Arrays.asList(filterFactories);
    }

    public CompositeFilterFactory(List<FilterFactory> filterFactories) {
        this.filterFactories = filterFactories;
    }

    public static CompositeFilterFactory of(FilterFactory... filterFactories) {
        return new CompositeFilterFactory(filterFactories);
    }

    public static CompositeFilterFactory of(List<FilterFactory> filterFactories) {
        return new CompositeFilterFactory(filterFactories);
    }

    @Override
    public BufferedImage apply(BufferedImage source) {
        BufferedImage image = source;
        for (FilterFactory factory : filterFactories) {
            image = factory.apply(image);
        }
        return image;
    }

}
