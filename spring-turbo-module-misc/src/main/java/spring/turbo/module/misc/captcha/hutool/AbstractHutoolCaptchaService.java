/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.hutool;

import cn.hutool.captcha.ICaptcha;
import org.springframework.lang.NonNull;
import spring.turbo.io.CloseUtils;
import spring.turbo.module.misc.captcha.Captcha;
import spring.turbo.module.misc.captcha.CaptchaService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author 应卓
 * @since 1.0.1
 */
public abstract class AbstractHutoolCaptchaService implements CaptchaService {

    @Override
    public final Captcha create() {
        final ICaptcha c = createCaptcha();
        return new Captcha(c.getCode(), toBufferedImage(c));
    }

    private BufferedImage toBufferedImage(ICaptcha c) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;

        try {
            c.write(out);
            in = new ByteArrayInputStream(out.toByteArray());
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            CloseUtils.closeQuietly(in);
            CloseUtils.closeQuietly(out);
        }
    }

    @NonNull
    protected abstract ICaptcha createCaptcha();

}
