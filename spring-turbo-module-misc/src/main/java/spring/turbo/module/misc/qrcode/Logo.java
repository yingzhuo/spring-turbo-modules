package spring.turbo.module.misc.qrcode;

import jakarta.annotation.Nullable;
import org.springframework.core.io.Resource;
import spring.turbo.util.Asserts;
import spring.turbo.util.io.IOExceptionUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Logo
 *
 * @author 应卓
 * @see #builder()
 * @see java.awt.image.BufferedImage
 * @since 1.0.0
 */
public final class Logo implements Serializable {

    @Nullable
    private Image image;

    private boolean compress = true;

    /**
     * 私有构造方法
     */
    private Logo() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Image getImage() {
        Asserts.notNull(image);
        return image;
    }

    public boolean isCompress() {
        return compress;
    }

    /**
     * 创建器
     */
    public static final class Builder {

        @Nullable
        private Image image;

        private boolean compress = true;

        private Builder() {
        }

        public Builder image(Image image) {
            this.image = image;
            return this;
        }

        public Builder image(Resource resource) {
            try {
                return image(resource.getFile());
            } catch (IOException e) {
                throw IOExceptionUtils.toUnchecked(e);
            }
        }

        public Builder image(InputStream inputStream) {
            try {
                this.image = ImageIO.read(inputStream);
                return this;
            } catch (IOException e) {
                throw IOExceptionUtils.toUnchecked(e);
            }
        }

        public Builder image(ImageInputStream inputStream) {
            try {
                this.image = ImageIO.read(inputStream);
                return this;
            } catch (IOException e) {
                throw IOExceptionUtils.toUnchecked(e);
            }
        }

        public Builder image(File file) {
            try {
                this.image = ImageIO.read(file);
                return this;
            } catch (IOException e) {
                throw IOExceptionUtils.toUnchecked(e);
            }
        }

        public Builder compress(boolean compress) {
            this.compress = compress;
            return this;
        }

        public Logo build() {
            Asserts.notNull(this.image);
            Logo logo = new Logo();
            logo.image = Objects.requireNonNull(image);
            logo.compress = this.compress;
            return logo;
        }
    }

}
