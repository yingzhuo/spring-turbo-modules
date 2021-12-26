/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.webmvc.entity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import spring.turbo.io.ImageUtils;
import spring.turbo.util.Asserts;

import java.awt.image.BufferedImage;

/**
 * @author 应卓
 * @since 1.0.1
 */
public final class ImageResponseEntity extends ResponseEntity<byte[]> {

    private ImageResponseEntity(byte[] body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private HttpStatus status = HttpStatus.OK;
        private BufferedImage image;
        private String format = "png";

        private Builder() {
            super();
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
