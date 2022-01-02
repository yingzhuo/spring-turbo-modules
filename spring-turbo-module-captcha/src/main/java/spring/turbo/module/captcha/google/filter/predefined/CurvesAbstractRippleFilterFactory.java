/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.filter.predefined;

import spring.turbo.module.captcha.google.color.ColorFactory;
import spring.turbo.module.captcha.google.filter.lib.CurvesImageOp;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Piastucki
 * @since 1.0.0
 */
public class CurvesAbstractRippleFilterFactory extends AbstractRippleFilterFactory {

    protected CurvesImageOp curves = new CurvesImageOp();

    public CurvesAbstractRippleFilterFactory() {
        super();
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
