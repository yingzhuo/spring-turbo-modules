package spring.turbo.module.misc.captcha.google.background;

import spring.turbo.module.misc.captcha.google.color.ColorFactory;
import spring.turbo.module.misc.captcha.google.color.SingleColorFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class SingleColorBackgroundFactory implements BackgroundFactory {

    private ColorFactory colorFactory;

    public SingleColorBackgroundFactory() {
        colorFactory = new SingleColorFactory(Color.WHITE);
    }

    public SingleColorBackgroundFactory(Color color) {
        colorFactory = new SingleColorFactory(color);
    }

    public void setColorFactory(ColorFactory colorFactory) {
        this.colorFactory = colorFactory;
    }

    @Override
    public void fillBackground(BufferedImage dest) {
        Graphics g = dest.getGraphics();
        g.setColor(colorFactory.getColor(0));
        g.fillRect(0, 0, dest.getWidth(), dest.getHeight());
    }

}
