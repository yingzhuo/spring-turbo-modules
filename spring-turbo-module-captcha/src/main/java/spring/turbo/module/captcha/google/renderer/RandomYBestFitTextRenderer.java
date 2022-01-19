/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.renderer;

import java.util.Random;

/**
 * @since 1.0.0
 */
public class RandomYBestFitTextRenderer extends AbstractTextRenderer {

    @Override
    protected void arrangeCharacters(int width, int height, TextString ts) {
        double widthRemaining = (width - ts.getWidth() - leftMargin - rightMargin) / ts.getCharacters().size();
        double vmiddle = height / 2;
        double x = leftMargin + widthRemaining / 2;
        Random r = new Random();
        height -= topMargin + bottomMargin;
        for (TextCharacter tc : ts.getCharacters()) {
            double heightRemaining = height - tc.getHeight();
            double y = vmiddle + 0.35 * tc.getAscent() + (1 - 2 * r.nextDouble()) * heightRemaining;
            tc.setX(x);
            tc.setY(y);
            x += tc.getWidth() + widthRemaining;
        }
    }

}
