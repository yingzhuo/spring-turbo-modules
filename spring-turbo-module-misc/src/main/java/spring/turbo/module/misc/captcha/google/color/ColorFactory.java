package spring.turbo.module.misc.captcha.google.color;

import java.awt.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
@FunctionalInterface
public interface ColorFactory {

    public Color getColor(int index);

}
