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

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @since 1.0.0
 */
public abstract class AbstractTextRenderer implements TextRenderer {

    protected int leftMargin;
    protected int rightMargin;
    protected int topMargin;
    protected int bottomMargin;

    public AbstractTextRenderer() {
        leftMargin = rightMargin = 5;
        topMargin = bottomMargin = 5;
    }

    protected abstract void arrangeCharacters(int width, int height, TextString ts);

    @Override
    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    @Override
    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    @Override
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }

    @Override
    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    @Override
    public void draw(String text, BufferedImage canvas, FontFactory fontFactory, ColorFactory colorFactory) {
        Graphics2D g = (Graphics2D) canvas.getGraphics();
        TextString ts = convertToCharacters(text, g, fontFactory, colorFactory);
        arrangeCharacters(canvas.getWidth(), canvas.getHeight(), ts);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        for (TextCharacter tc : ts.getCharacters()) {
            g.setColor(tc.getColor());
            g.drawString(tc.iterator(), (float) tc.getX(), (float) tc.getY());
        }
    }

    protected TextString convertToCharacters(String text, Graphics2D g, FontFactory fontFactory, ColorFactory colorFactory) {
        TextString characters = new TextString();
        FontRenderContext frc = g.getFontRenderContext();
        double lastx = 0;
        for (int i = 0; i < text.length(); i++) {
            Font font = fontFactory.getFont(i);
            char c = text.charAt(i);
            FontMetrics fm = g.getFontMetrics(font);
            Rectangle2D bounds = font.getStringBounds(String.valueOf(c), frc);
            TextCharacter tc = new TextCharacter();
            tc.setCharacter(c);
            tc.setFont(font);
            tc.setWidth(fm.charWidth(c));
            tc.setHeight(fm.getAscent() + fm.getDescent());
            tc.setAscent(fm.getAscent());
            tc.setDescent(fm.getDescent());
            tc.setX(lastx);
            tc.setY(0);
            tc.setFont(font);
            tc.setColor(colorFactory.getColor(i));
            lastx += bounds.getWidth();
            characters.addCharacter(tc);
        }
        return characters;
    }
}
