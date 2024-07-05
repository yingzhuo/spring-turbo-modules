package spring.turbo.module.misc.captcha.google.filter.predefined;

import spring.turbo.module.misc.captcha.google.filter.lib.WobbleImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0.0
 */
public class WobbleAbstractRippleFilterFactory extends AbstractRippleFilterFactory {

    protected WobbleImageOp wobble;

    public WobbleAbstractRippleFilterFactory() {
        wobble = new WobbleImageOp();
    }

    @Override
    protected List<BufferedImageOp> getPreRippleFilters() {
        List<BufferedImageOp> list = new ArrayList<>();
        list.add(wobble);
        return list;
    }

}
