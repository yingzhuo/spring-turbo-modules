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
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import spring.turbo.io.CloseUtils;
import spring.turbo.util.Asserts;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

import static spring.turbo.util.CharsetPool.ISO_8859_1;
import static spring.turbo.util.CharsetPool.UTF_8;

/**
 * @author 应卓
 * @see #builder()
 * @since 1.0.1
 */
public class AttachmentResponseEntity extends ResponseEntity<byte[]> {

    private AttachmentResponseEntity(byte[] body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 创建器
     */
    public final static class Builder implements spring.turbo.bean.Builder<AttachmentResponseEntity> {

        private HttpStatus status = HttpStatus.OK;

        private MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        @Nullable
        private byte[] content;

        @Nullable
        private String attachmentName;

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

        @Override
        public AttachmentResponseEntity build() {
            Asserts.notNull(content);
            Asserts.notNull(attachmentName);
            Asserts.notNull(status);
            Asserts.notNull(mediaType);

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition
                            .attachment()
                            .filename(new String(attachmentName.getBytes(UTF_8), ISO_8859_1))
                            .build()
            );
            headers.add(HttpHeaders.CONTENT_TYPE, mediaType.toString());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length));
            return new AttachmentResponseEntity(content, headers, status);
        }
    }

}
