package spring.turbo.module.misc.captcha;

import spring.turbo.util.Asserts;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Objects;

/**
 * 人机验证码
 *
 * @author 应卓
 * @since 1.0.0
 */
public final class Captcha implements Serializable {

    private final String word;
    private final BufferedImage image;

    public Captcha(String word, BufferedImage image) {
        Asserts.hasLength(word);
        Asserts.notNull(image);
        this.word = word;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Captcha captcha = (Captcha) o;
        return word.equals(captcha.word) && image.equals(captcha.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, image);
    }

    public String getWord() {
        return word;
    }

    public BufferedImage getImage() {
        return image;
    }

}
