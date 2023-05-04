/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.google;

import spring.turbo.module.misc.captcha.Captcha;
import spring.turbo.module.misc.captcha.CaptchaService;
import spring.turbo.module.misc.captcha.google.background.BackgroundFactory;
import spring.turbo.module.misc.captcha.google.color.ColorFactory;
import spring.turbo.module.misc.captcha.google.filter.FilterFactory;
import spring.turbo.module.misc.captcha.google.font.FontFactory;
import spring.turbo.module.misc.captcha.google.renderer.TextRenderer;
import spring.turbo.module.misc.captcha.google.word.Word;
import spring.turbo.module.misc.captcha.google.word.WordFactory;

import java.awt.image.BufferedImage;

/**
 * @author 应卓
 *
 * @since 1.0.0
 */
public abstract class AbstractGoogleCaptchaService implements CaptchaService {

    protected FontFactory fontFactory;
    protected WordFactory wordFactory;
    protected ColorFactory colorFactory;
    protected BackgroundFactory backgroundFactory;
    protected TextRenderer textRenderer;
    protected FilterFactory filterFactory;
    protected int width;
    protected int height;

    public FontFactory getFontFactory() {
        return fontFactory;
    }

    public void setFontFactory(FontFactory fontFactory) {
        this.fontFactory = fontFactory;
    }

    public WordFactory getWordFactory() {
        return wordFactory;
    }

    public void setWordFactory(WordFactory wordFactory) {
        this.wordFactory = wordFactory;
    }

    public ColorFactory getColorFactory() {
        return colorFactory;
    }

    public void setColorFactory(ColorFactory colorFactory) {
        this.colorFactory = colorFactory;
    }

    public BackgroundFactory getBackgroundFactory() {
        return backgroundFactory;
    }

    public void setBackgroundFactory(BackgroundFactory backgroundFactory) {
        this.backgroundFactory = backgroundFactory;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void setTextRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public Captcha create() {
        BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        backgroundFactory.fillBackground(bufImage);
        Word word = wordFactory.getNextWord();
        textRenderer.draw(word.getStringForDrawing(), bufImage, fontFactory, colorFactory);
        BufferedImage image = filterFactory.apply(bufImage);
        return new Captcha(word.getStringForValidation(), image);
    }

}
