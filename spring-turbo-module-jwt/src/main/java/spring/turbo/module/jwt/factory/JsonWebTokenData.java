package spring.turbo.module.jwt.factory;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * JWT签名所包含的信息
 *
 * @author 应卓
 * @see #newInstance()
 * @since 3.1.1
 */
public final class JsonWebTokenData implements Serializable {

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

    private final SortedMap<String, Object> headerMap = new TreeMap<>();
    private final SortedMap<String, Object> payloadMap = new TreeMap<>();

    /**
     * 构造方法
     */
    public JsonWebTokenData() {
        this.headerMap.put(HEADER_TYPE, "JWT");
    }

    public static JsonWebTokenData newInstance() {
        return new JsonWebTokenData();
    }

    public JsonWebTokenData headerType(String type) {
        headerMap.put(HEADER_TYPE, type);
        return this;
    }

    public JsonWebTokenData headerKeyId(String id) {
        headerMap.put(HEADER_KEY_ID, id);
        return this;
    }

    public JsonWebTokenData headerKeyId(Supplier<String> idSupplier) {
        return headerKeyId(idSupplier.get());
    }

    public JsonWebTokenData headerContentType(String contentType) {
        headerMap.put(HEADER_CONTENT_TYPE, contentType);
        return this;
    }

    public JsonWebTokenData headerAlgorithm(String algorithm) {
        headerMap.put(HEADER_ALGORITHM, algorithm);
        return this;
    }

    public JsonWebTokenData payloadIssuer(String issuer) {
        payloadMap.put(PAYLOAD_ISSUER, issuer);
        return this;
    }

    public JsonWebTokenData payloadSubject(String subject) {
        payloadMap.put(PAYLOAD_SUBJECT, subject);
        return this;
    }

    public JsonWebTokenData payloadAudience(String... audience) {
        payloadMap.put(PAYLOAD_AUDIENCE, audience);
        return this;
    }

    public JsonWebTokenData payloadExpiresAt(LocalDateTime time) {
        payloadMap.put(PAYLOAD_EXPIRES, toDate(time));
        return this;
    }

    public JsonWebTokenData payloadExpiresAtFuture(Duration duration) {
        payloadMap.put(PAYLOAD_EXPIRES, toDate(LocalDateTime.now().plus(duration)));
        return this;
    }

    public JsonWebTokenData payloadNotBefore(LocalDateTime time) {
        payloadMap.put(PAYLOAD_NOT_BEFORE, toDate(time));
        return this;
    }

    public JsonWebTokenData payloadNotBeforeAtFuture(Duration duration) {
        payloadMap.put(PAYLOAD_NOT_BEFORE, toDate(LocalDateTime.now().plus(duration)));
        return this;
    }

    public JsonWebTokenData payloadIssuedAt(LocalDateTime time) {
        payloadMap.put(PAYLOAD_ISSUED_AT, toDate(time));
        return this;
    }

    public JsonWebTokenData payloadIssuedAtNow() {
        return payloadIssuedAt(LocalDateTime.now());
    }

    public JsonWebTokenData payloadJwtId(Object jwtId) {
        payloadMap.put(PAYLOAD_JWT_ID, jwtId);
        return this;
    }

    public JsonWebTokenData payloadJwtId(Supplier<Object> jwtIdSupplier) {
        return payloadJwtId(jwtIdSupplier.get());
    }

    public JsonWebTokenData addHeader(String name, Object value) {
        headerMap.put(name, value);
        return this;
    }

    public JsonWebTokenData addPayload(String name, Object value) {
        payloadMap.put(name, value);
        return this;
    }

    public boolean containsHeader(String headerName) {
        return headerMap.containsKey(headerName);
    }

    public boolean containsPayload(String payloadAttribute) {
        return payloadMap.containsKey(payloadAttribute);
    }

    public SortedMap<String, Object> getHeaderMap() {
        return this.headerMap;
    }

    public SortedMap<String, Object> getPayloadMap() {
        return this.payloadMap;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
