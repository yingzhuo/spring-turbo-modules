/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.color;

import java.awt.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class GradientColorFactory implements ColorFactory {

    private Color start;
    private Color step;

    public GradientColorFactory() {
        start = new Color(192, 192, 0);
        step = new Color(192, 128, 128);
    }

    @Override
    public Color getColor(int index) {
        return new Color((start.getRed() + step.getRed() * index) % 256,
                (start.getGreen() + step.getGreen() * index) % 256,
                (start.getBlue() + step.getBlue() * index) % 256);
    }

    public void setStart(Color start) {
        this.start = start;
    }

    public void setStep(Color step) {
        this.step = step;
    }

}
