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

    /**
     * 创建令牌
     *
     * @param data
     *            令牌数据
     *
     * @return 令牌
     */
    public String create(Data data);

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * 令牌携带信息
     *
     * @author 应卓
     */
    public static final class Data implements Serializable {

        /**
         * 头部信息
         */
        private final Map<String, Object> headerMap = new HashMap<>();

        /**
         * 载荷信息
         */
        private final Map<String, Object> payloadMap = new HashMap<>();

        /**
         * 私有构造方法
         */
        private Data() {
            super();
        }

        /**
         * 创建 {@link Data} 实例
         *
         * @return 实例
         */
        public static Data newInstance() {
            return new Data();
        }

        /**
         * 设置头信息 typ
         *
         * @param type
         *            值
         *
         * @return this
         *
         * @see JwtConstants#HEADER_TYPE
         */
        public Data type(String type) {
            headerMap.put(HEADER_TYPE, type);
            return this;
        }

        /**
         * 设置头信息 kid
         *
         * @param id
         *            值
         *
         * @return this
         *
         * @see JwtConstants#HEADER_KEY_ID
         */
        public Data keyId(String id) {
            headerMap.put(HEADER_KEY_ID, id);
            return this;
        }

        /**
         * 设置头信息 cty
         *
         * @param contentType
         *            值
         *
         * @return this
         *
         * @see JwtConstants#HEADER_CONTENT_TYPE
         */
        public Data contentType(String contentType) {
            headerMap.put(HEADER_CONTENT_TYPE, contentType);
            return this;
        }

        /**
         * 设置头信息 alg
         *
         * @param algorithm
         *            值
         *
         * @return this
         *
         * @see JwtConstants#HEADER_ALGORITHM
         */
        public Data algorithm(String algorithm) {
            headerMap.put(HEADER_ALGORITHM, algorithm);
            return this;
        }

        /**
         * 设置载荷信息 iss
         *
         * @param issuer
         *            值
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_ISSUER
         */
        public Data issuer(String issuer) {
            payloadMap.put(PAYLOAD_ISSUER, issuer);
            return this;
        }

        /**
         * 设置载荷信息 sub
         *
         * @param subject
         *            值
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_SUBJECT
         */
        public Data subject(String subject) {
            payloadMap.put(PAYLOAD_SUBJECT, subject);
            return this;
        }

        /**
         * 设置载荷信息 aud
         *
         * @param audience
         *            值
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_AUDIENCE
         */
        public Data audience(String... audience) {
            payloadMap.put(PAYLOAD_AUDIENCE, audience);
            return this;
        }

        /**
         * 设置载荷信息 exp
         *
         * @param time
         *            过期的时间点 值
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_EXPIRES
         */
        public Data expiresAt(Date time) {
            payloadMap.put(PAYLOAD_EXPIRES, time);
            return this;
        }

        /**
         * 设置载荷信息 exp
         *
         * @param duration
         *            从当前时刻开始到过期时间的跨度 值
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_EXPIRES
         */
        public Data expiresAtFuture(Duration duration) {
            payloadMap.put(PAYLOAD_EXPIRES, new Date(System.currentTimeMillis() + duration.toMillis()));
            return this;
        }

        /**
         * 设置载荷信息 nbf
         *
         * @param time
         *            最初生效的时刻
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_NOT_BEFORE
         */
        public Data notBefore(Date time) {
            payloadMap.put(PAYLOAD_NOT_BEFORE, time);
            return this;
        }

        /**
         * 设置载荷信息 nbf
         *
         * @param duration
         *            当前时刻到最初生效的时刻的跨度
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_NOT_BEFORE
         */
        public Data notBeforeAtFuture(Duration duration) {
            payloadMap.put(PAYLOAD_NOT_BEFORE, new Date(System.currentTimeMillis() + duration.toMillis()));
            return this;
        }

        /**
         * 设置载荷信息 nbf
         *
         * @param time
         *            令牌签发时刻
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_ISSUED_AT
         */
        public Data issuedAt(Date time) {
            payloadMap.put(PAYLOAD_ISSUED_AT, time);
            return this;
        }

        /**
         * 设置载荷信息 nbf, 以当前时刻作为令牌签发时刻。
         *
         * @return this
         *
         * @see JwtConstants#PAYLOAD_ISSUED_AT
         */
        public Data issuedAtNow() {
            return issuedAt(new Date());
        }

        /**
         * 设置载荷信息 jti
         *
         * @param jwtId
         *            jwt id
         *
         * @return this
         */
        public Data jwtId(Object jwtId) {
            payloadMap.put(PAYLOAD_JWT_ID, jwtId);
            return this;
        }

        /**
         * 设置其他载荷信息
         *
         * @param name
         *            名
         * @param value
         *            值
         *
         * @return this
         */
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
