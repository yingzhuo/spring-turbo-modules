package spring.turbo.module.jwt;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static spring.turbo.module.jwt.JwtConstants.*;

/**
 * JWT签名所包含的信息
 *
 * @author 应卓
 * @see #newInstance()
 * @see JwtConstants
 * @since 3.1.1
 */
public final class JwtData implements Serializable {

    private final Map<String, Object> headerMap = new HashMap<>();
    private final Map<String, Object> payloadMap = new HashMap<>();

    /**
     * 构造方法
     */
    public JwtData() {
        this.headerMap.put(HEADER_TYPE, "JWT");
    }

    /**
     * 创建新实例
     *
     * @return 新实例
     */
    public static JwtData newInstance() {
        return new JwtData();
    }

    public JwtData addHeaderType(String type) {
        headerMap.put(HEADER_TYPE, type);
        return this;
    }

    public JwtData addHeaderKeyId(String id) {
        headerMap.put(HEADER_KEY_ID, id);
        return this;
    }

    public JwtData addHeaderKeyId(Supplier<String> keyIdSupplier) {
        Assert.notNull(keyIdSupplier, "keyIdSupplier is null");
        return addHeaderKeyId(keyIdSupplier.get());
    }

    public JwtData addHeaderContentType(String contentType) {
        Assert.hasText(contentType, "contentType is null or blank");
        headerMap.put(HEADER_CONTENT_TYPE, contentType);
        return this;
    }

    public JwtData addHeaderAlgorithm(String algorithm) {
        Assert.hasText(algorithm, "algorithm is null or blank");
        headerMap.put(HEADER_ALGORITHM, algorithm);
        return this;
    }

    public JwtData addPayloadIssuer(String issuer) {
        Assert.hasText(issuer, "issuer is null or blank");
        payloadMap.put(PAYLOAD_ISSUER, issuer);
        return this;
    }

    public JwtData addPayloadSubject(String subject) {
        Assert.hasText(subject, "subject is null or blank");
        payloadMap.put(PAYLOAD_SUBJECT, subject);
        return this;
    }

    public JwtData addPayloadAudience(String... audience) {
        Assert.notNull(audience, "audience is null");
        payloadMap.put(PAYLOAD_AUDIENCE, audience);
        return this;
    }

    public JwtData addPayloadExpiresAt(LocalDateTime time) {
        Assert.notNull(time, "time is null");
        payloadMap.put(PAYLOAD_EXPIRES, toDate(time));
        return this;
    }

    public JwtData addPayloadExpiresAtFuture(Duration duration) {
        Assert.notNull(duration, "duration is null");
        payloadMap.put(PAYLOAD_EXPIRES, toDate(LocalDateTime.now().plus(duration)));
        return this;
    }

    public JwtData addPayloadNotBefore(LocalDateTime time) {
        Assert.notNull(time, "time is null");
        payloadMap.put(PAYLOAD_NOT_BEFORE, toDate(time));
        return this;
    }

    public JwtData addPayloadNotBeforeAtFuture(Duration duration) {
        Assert.notNull(duration, "duration is null");
        payloadMap.put(PAYLOAD_NOT_BEFORE, toDate(LocalDateTime.now().plus(duration)));
        return this;
    }

    public JwtData addPayloadIssuedAt(LocalDateTime time) {
        Assert.notNull(time, "time is null");
        payloadMap.put(PAYLOAD_ISSUED_AT, toDate(time));
        return this;
    }

    public JwtData addPayloadIssuedAtNow() {
        return addPayloadIssuedAt(LocalDateTime.now());
    }

    public JwtData addPayloadJwtId(Object jwtId) {
        Assert.notNull(jwtId, "jwtId is null");
        payloadMap.put(PAYLOAD_JWT_ID, jwtId);
        return this;
    }

    public JwtData addPayloadJwtId(Supplier<Object> jwtIdSupplier) {
        Assert.notNull(jwtIdSupplier, "jwtIdSupplier is null");
        return addPayloadJwtId(jwtIdSupplier.get());
    }

    public JwtData addHeader(String name, Object value) {
        Assert.hasText(name, "name is null or blank");
        Assert.notNull(value, "value is null");
        headerMap.put(name, value);
        return this;
    }

    public JwtData addPayload(String name, Object value) {
        Assert.hasText(name, "name is null or blank");
        Assert.notNull(value, "value is null");
        payloadMap.put(name, value);
        return this;
    }

    public Map<String, Object> getHeaderMap() {
        return Collections.unmodifiableMap(this.headerMap);
    }

    public Map<String, Object> getPayloadMap() {
        return Collections.unmodifiableMap(this.payloadMap);
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
