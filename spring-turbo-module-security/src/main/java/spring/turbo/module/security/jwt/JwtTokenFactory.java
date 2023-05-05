/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static spring.turbo.module.security.jwt.JwtConstants.*;

/**
 * JWT令牌工厂
 *
 * @author 应卓
 *
 * @since 1.0.0
 */
public sealed interface JwtTokenFactory permits JwtTokenFactoryImpl {

    public String create(Data data);

    public static final class Data implements Serializable {

        private final Map<String, Object> headerMap = new HashMap<>();
        private final Map<String, Object> payloadMap = new HashMap<>();

        /**
         * 私有构造方法
         */
        private Data() {
            super();
        }

        public static Data newInstance() {
            return new Data();
        }

        // Registered Header
        public Data type(String type) {
            headerMap.put(HEADER_TYPE, type);
            return this;
        }

        // Registered Header
        public Data keyId(Object id) {
            headerMap.put(HEADER_KEY_ID, id);
            return this;
        }

        // Registered Header
        public Data contentType(Object contentType) {
            headerMap.put(HEADER_CONTENT_TYPE, contentType);
            return this;
        }

        // Registered Header
        public Data algorithm(String algorithm) {
            headerMap.put(HEADER_ALGORITHM, algorithm);
            return this;
        }

        // Registered Claim
        public Data issuer(String issuer) {
            payloadMap.put(PAYLOAD_ISSUER, issuer);
            return this;
        }

        // Registered Claim
        public Data subject(String subject) {
            payloadMap.put(PAYLOAD_SUBJECT, subject);
            return this;
        }

        // Registered Claim
        public Data audience(String... audience) {
            payloadMap.put(PAYLOAD_AUDIENCE, audience);
            return this;
        }

        // Registered Claim
        public Data expiresAt(Date time) {
            payloadMap.put(PAYLOAD_EXPIRES_AT, time);
            return this;
        }

        public Data expiresAtFuture(Duration duration) {
            payloadMap.put(PAYLOAD_EXPIRES_AT, new Date(System.currentTimeMillis() + duration.toMillis()));
            return this;
        }

        // Registered Claim
        public Data notBefore(Date time) {
            payloadMap.put(PAYLOAD_NOT_BEFORE, time);
            return this;
        }

        public Data notBeforeAtFuture(Duration duration) {
            payloadMap.put(PAYLOAD_NOT_BEFORE, new Date(System.currentTimeMillis() + duration.toMillis()));
            return this;
        }

        // Registered Claim
        public Data issuedAt(Date time) {
            payloadMap.put(PAYLOAD_ISSUED_AT, time);
            return this;
        }

        public Data issuedAtNow() {
            return issuedAt(new Date());
        }

        // Registered Claim
        public Data jwtId(Object jwtId) {
            payloadMap.put(PAYLOAD_JWT_ID, jwtId);
            return this;
        }

        public Data addPayload(String name, Object value) {
            payloadMap.put(name, value);
            return this;
        }

        public Map<String, Object> getHeaderMap() {
            return Collections.unmodifiableMap(headerMap);
        }

        public Map<String, Object> getPayloadMap() {
            return Collections.unmodifiableMap(payloadMap);
        }
    }

}
