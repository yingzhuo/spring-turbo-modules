package spring.turbo.module.misc.captcha;

import spring.turbo.io.ImageUtils;
import spring.turbo.util.Asserts;
import spring.turbo.util.ImageFormatPool;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Objects;

/**
 * 经过编码的人机验证码
 *
 * @author 应卓
 * @see Captcha
 * @see #of(Captcha)
 * @since 1.0.0
 */
public final class EncodedCaptcha implements Serializable {

    private final Captcha captcha;
    private final String encodedImage;

    /**
     * 私有构造方法
     *
     * @param captcha 人机验证码
     */
    private EncodedCaptcha(Captcha captcha) {
        this.captcha = captcha;
        this.encodedImage = ImageUtils.encodeToBase64(captcha.getImage(), ImageFormatPool.PNG);
    }

    public static EncodedCaptcha of(Captcha captcha) {
        Asserts.notNull(captcha);
        Asserts.notNull(captcha.getWord());
        Asserts.notNull(captcha.getImage());
        return new EncodedCaptcha(captcha);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EncodedCaptcha that = (EncodedCaptcha) o;
        return captcha.equals(that.captcha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(captcha);
    }

    public String getWord() {
        return captcha.getWord();
    }

    public BufferedImage getImage() {
        return captcha.getImage();
    }

    public String getEncodedImage() {
        return encodedImage;
    }

}
