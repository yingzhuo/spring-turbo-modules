package spring.turbo.module.misc.captcha.google.filter.predefined;

import spring.turbo.module.misc.captcha.google.color.ColorFactory;
import spring.turbo.module.misc.captcha.google.filter.lib.CurvesImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0.0
 */
public class CurvesAbstractRippleFilterFactory extends AbstractRippleFilterFactory {

    protected CurvesImageOp curves = new CurvesImageOp();

    public CurvesAbstractRippleFilterFactory() {
    }

    public CurvesAbstractRippleFilterFactory(ColorFactory colorFactory) {
        setColorFactory(colorFactory);
    }

    @Override
    protected List<BufferedImageOp> getPreRippleFilters() {
        List<BufferedImageOp> list = new ArrayList<>();
        list.add(curves);
        return list;
    }

    public void setStrokeMin(float strokeMin) {
        curves.setStrokeMin(strokeMin);
    }

    public void setStrokeMax(float strokeMax) {
        curves.setStrokeMax(strokeMax);
    }

    public void setColorFactory(ColorFactory colorFactory) {
        curves.setColorFactory(colorFactory);
    }
}
