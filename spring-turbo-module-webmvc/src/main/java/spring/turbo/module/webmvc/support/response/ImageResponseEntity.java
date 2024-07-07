package spring.turbo.module.webmvc.support.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import spring.turbo.util.Asserts;
import spring.turbo.util.io.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * @author 应卓
 * @see #builder()
 * @since 1.0.1
 */
public final class ImageResponseEntity extends ResponseEntity<byte[]> {

    private ImageResponseEntity(byte[] body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 创建器
     */
    public final static class Builder {

        private HttpStatus status = HttpStatus.OK;

        private String format = "png";

        @Nullable
        private BufferedImage image;

        private Builder() {
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder image(BufferedImage image) {
            this.image = image;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public ImageResponseEntity build() {
            Asserts.notNull(status);
            Asserts.notNull(image);
            Asserts.hasText(format);

            final byte[] bytes = ImageUtils.toByteArray(image, format);
            final int size = bytes.length;

            final MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/" + format);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(size));
            return new ImageResponseEntity(bytes, headers, status);
        }
    }

}
