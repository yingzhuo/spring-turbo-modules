package spring.turbo.module.misc.captcha.google.color;

import java.awt.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class SingleColorFactory implements ColorFactory {

    private Color color;

    public SingleColorFactory() {
        color = Color.BLACK;
    }

    public SingleColorFactory(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor(int index) {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
