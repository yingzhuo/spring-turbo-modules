package spring.turbo.module.jwt;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * JWT签名所包含的信息
 *
 * @author 应卓
 * @see #newInstance()
 * @since 3.1.1
 */
public final class JwtData implements Serializable {

    // headers
    // -----------------------------------------------------------------------------------------------------------------
    public static final String HEADER_TYPE = "typ";
    public static final String HEADER_KEY_ID = "kid";
    public static final String HEADER_ALGORITHM = "alg";
    public static final String HEADER_CONTENT_TYPE = "cty";

    // payload
    // -----------------------------------------------------------------------------------------------------------------
    public static final String PAYLOAD_ISSUER = "iss";
    public static final String PAYLOAD_SUBJECT = "sub";
    public static final String PAYLOAD_AUDIENCE = "aud";
    public static final String PAYLOAD_EXPIRES = "exp";
    public static final String PAYLOAD_NOT_BEFORE = "nbf";
    public static final String PAYLOAD_ISSUED_AT = "iat";
    public static final String PAYLOAD_JWT_ID = "jti";

    // -----------------------------------------------------------------------------------------------------------------

    private final Map<String, Object> headerMap = new HashMap<>();
    private final Map<String, Object> payloadMap = new HashMap<>();

    /**
     * 构造方法
     */
    public JwtData() {
        this.headerMap.put(HEADER_TYPE, "JWT");
    }

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

    public JwtData addHeaderKeyId(Supplier<String> idSupplier) {
        return addHeaderKeyId(idSupplier.get());
    }

    public JwtData addHeaderContentType(String contentType) {
        headerMap.put(HEADER_CONTENT_TYPE, contentType);
        return this;
    }

    public JwtData addHeaderAlgorithm(String algorithm) {
        headerMap.put(HEADER_ALGORITHM, algorithm);
        return this;
    }

    public JwtData addPayloadIssuer(String issuer) {
        payloadMap.put(PAYLOAD_ISSUER, issuer);
        return this;
    }

    public JwtData addPayloadSubject(String subject) {
        payloadMap.put(PAYLOAD_SUBJECT, subject);
        return this;
    }

    public JwtData addPayloadAudience(String... audience) {
        payloadMap.put(PAYLOAD_AUDIENCE, audience);
        return this;
    }

    public JwtData addPayloadExpiresAt(LocalDateTime time) {
        payloadMap.put(PAYLOAD_EXPIRES, toDate(time));
        return this;
    }

    public JwtData addPayloadExpiresAtFuture(Duration duration) {
        payloadMap.put(PAYLOAD_EXPIRES, toDate(LocalDateTime.now().plus(duration)));
        return this;
    }

    public JwtData addPayloadNotBefore(LocalDateTime time) {
        payloadMap.put(PAYLOAD_NOT_BEFORE, toDate(time));
        return this;
    }

    public JwtData addPayloadNotBeforeAtFuture(Duration duration) {
        payloadMap.put(PAYLOAD_NOT_BEFORE, toDate(LocalDateTime.now().plus(duration)));
        return this;
    }

    public JwtData addPayloadIssuedAt(LocalDateTime time) {
        payloadMap.put(PAYLOAD_ISSUED_AT, toDate(time));
        return this;
    }

    public JwtData addPayloadIssuedAtNow() {
        return addPayloadIssuedAt(LocalDateTime.now());
    }

    public JwtData addPayloadJwtId(Object jwtId) {
        payloadMap.put(PAYLOAD_JWT_ID, jwtId);
        return this;
    }

    public JwtData addPayloadJwtId(Supplier<Object> jwtIdSupplier) {
        return addPayloadJwtId(jwtIdSupplier.get());
    }

    public JwtData addHeader(String name, Object value) {
        headerMap.put(name, value);
        return this;
    }

    public JwtData addPayload(String name, Object value) {
        payloadMap.put(name, value);
        return this;
    }

    public Map<String, Object> getHeaderMap() {
        return this.headerMap;
    }

    public Map<String, Object> getPayloadMap() {
        return this.payloadMap;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
