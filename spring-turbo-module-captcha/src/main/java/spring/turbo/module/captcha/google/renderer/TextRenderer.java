/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.renderer;

import spring.turbo.module.captcha.google.color.ColorFactory;
import spring.turbo.module.captcha.google.font.FontFactory;

import java.awt.image.BufferedImage;

/**
 * @since 1.0.0
 */
public interface TextRenderer {

    void setLeftMargin(int leftMargin);

    void setRightMargin(int rightMargin);

    void setTopMargin(int topMargin);

    void setBottomMargin(int bottomMargin);

    void draw(String text, BufferedImage canvas, FontFactory fontFactory, ColorFactory colorFactory);

}
