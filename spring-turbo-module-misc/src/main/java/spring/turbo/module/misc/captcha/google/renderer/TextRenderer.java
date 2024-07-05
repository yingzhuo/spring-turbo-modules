package spring.turbo.module.misc.captcha.google.renderer;

import spring.turbo.module.misc.captcha.google.color.ColorFactory;
import spring.turbo.module.misc.captcha.google.font.FontFactory;

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
