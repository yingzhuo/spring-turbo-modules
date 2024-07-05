package spring.turbo.module.misc.captcha.google.color;

import java.awt.*;
import java.util.Random;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class RandomColorFactory implements ColorFactory {

    private Color min;
    private Color max;

    public RandomColorFactory() {
        min = new Color(0, 0, 0);
        max = new Color(255, 255, 255);
    }

    public void setMin(Color min) {
        this.min = min;
    }

    public void setMax(Color max) {
        this.max = max;
    }

    @Override
    public Color getColor(int index) {
        Random r = new Random();
        return new Color(min.getRed() + r.nextInt((max.getRed() - min.getRed())),
                min.getGreen() + r.nextInt((max.getGreen() - min.getGreen())),
                min.getBlue() + r.nextInt((max.getBlue() - min.getBlue())));
    }

}
