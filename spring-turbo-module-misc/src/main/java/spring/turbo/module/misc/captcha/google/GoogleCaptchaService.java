/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.google;

import spring.turbo.module.misc.captcha.CaptchaService;
import spring.turbo.module.misc.captcha.google.background.SingleColorBackgroundFactory;
import spring.turbo.module.misc.captcha.google.color.RandomColorFactory;
import spring.turbo.module.misc.captcha.google.filter.predefined.CurvesAbstractRippleFilterFactory;
import spring.turbo.module.misc.captcha.google.font.RandomFontFactory;
import spring.turbo.module.misc.captcha.google.renderer.BestFitTextRenderer;
import spring.turbo.module.misc.captcha.google.word.AdaptiveRandomWordFactory;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class GoogleCaptchaService extends AbstractGoogleCaptchaService implements CaptchaService {

    public GoogleCaptchaService() {
        backgroundFactory = new SingleColorBackgroundFactory();
        wordFactory = new AdaptiveRandomWordFactory();
        fontFactory = new RandomFontFactory();
        textRenderer = new BestFitTextRenderer();
        colorFactory = new RandomColorFactory();
        filterFactory = new CurvesAbstractRippleFilterFactory(colorFactory);
        textRenderer.setLeftMargin(10);
        textRenderer.setRightMargin(10);
        width = 160;
        height = 70;
    }

}
