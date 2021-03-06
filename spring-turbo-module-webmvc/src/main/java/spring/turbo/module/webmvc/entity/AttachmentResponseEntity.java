/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webmvc.entity;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import spring.turbo.io.CloseUtils;
import spring.turbo.util.Asserts;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class AttachmentResponseEntity extends ResponseEntity<byte[]> {

    private AttachmentResponseEntity(byte[] body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final static class Builder {

        private HttpStatus status = HttpStatus.OK;
        private byte[] content;
        private String attachmentName;
        private MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        private Builder() {
            super();
        }

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder content(byte[] bytes) {
            this.content = bytes;
            return this;
        }

        public Builder content(Resource resource) {
            try {
                this.content = StreamUtils.copyToByteArray(resource.getInputStream());
                return this;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } finally {
                CloseUtils.closeQuietly(resource);
            }
        }

        public Builder content(File file) {
            return content(new FileSystemResource(file));
        }

        public Builder content(Path path) {
            return content(new FileSystemResource(path));
        }

        public Builder attachmentName(String name) {
            attachmentName = name;
            return this;
        }

        public AttachmentResponseEntity build() {
            Asserts.notNull(content);
            Asserts.hasText(attachmentName);
            Asserts.notNull(status);
            Asserts.notNull(mediaType);

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition
                            .attachment()
                            .filename(new String(attachmentName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                            .build()
            );
            headers.add(HttpHeaders.CONTENT_TYPE, mediaType.toString());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length));
            return new AttachmentResponseEntity(content, headers, status);
        }
    }

}
