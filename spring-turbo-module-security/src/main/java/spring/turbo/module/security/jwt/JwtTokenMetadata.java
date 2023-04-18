/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import org.springframework.lang.Nullable;
import spring.turbo.bean.Payload;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class JwtTokenMetadata implements Serializable {

    @Nullable
    private String keyId;

    @Nullable
    private String issuer;

    @Nullable
    private String subject;

    @Nullable
    private List<String> audience = new ArrayList<>();

    @Nullable
    private Date expiresAt;

    @Nullable
    private Date notBefore;

    @Nullable
    private Date issuedAt;

    @Nullable
    private String jwtId;

    @Nullable
    private Payload payloadClaims = Payload.newInstance();

    private JwtTokenMetadata() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @Nullable
    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @Nullable
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Nullable
    public List<String> getAudience() {
        return audience;
    }

    public void setAudience(List<String> audience) {
        this.audience = audience;
    }

    @Nullable
    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Nullable
    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    @Nullable
    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    @Nullable
    public String getJwtId() {
        return jwtId;
    }

    public void setJwtId(String jwtId) {
        this.jwtId = jwtId;
    }

    @Nullable
    public Payload getPayloadClaims() {
        return payloadClaims;
    }

    public void setPayloadClaims(Payload payloadClaims) {
        this.payloadClaims = payloadClaims;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtTokenMetadata that = (JwtTokenMetadata) o;
        return Objects.equals(keyId, that.keyId) &&
                Objects.equals(issuer, that.issuer) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(audience, that.audience) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(notBefore, that.notBefore) &&
                Objects.equals(issuedAt, that.issuedAt) &&
                Objects.equals(jwtId, that.jwtId) &&
                Objects.equals(payloadClaims, that.payloadClaims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, issuer, subject, audience, expiresAt, notBefore, issuedAt, jwtId, payloadClaims);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public final static class Builder {

        private final Payload payloadClaims = Payload.newInstance();
        private String keyId;
        private String issuer;
        private String subject;
        private List<String> audience = new ArrayList<>();
        private Date expiresAt;
        private Date notBefore;
        private Date issuedAt;
        private String jwtId;

        private Builder() {
            super();
        }

        public Builder keyId(String keyId) {
            this.keyId = keyId;
            return this;
        }

        public Builder keyId(Supplier<String> supplier) {
            return keyId(supplier.get());
        }

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder audience(List<String> audience) {
            this.audience = audience;
            return this;
        }

        public Builder audience(String... audience) {
            return audience(Arrays.asList(audience));
        }

        public Builder expiresAt(Date expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder expiresAtFuture(long duration, TimeUnit timeUnit) {
            return expiresAt(afterNow(duration, timeUnit));
        }

        public Builder expiresAtFuture(Duration duration) {
            return expiresAtFuture(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        public Builder notBefore(Date notBefore) {
            this.notBefore = notBefore;
            return this;
        }

        public Builder notBeforeFuture(long duration, TimeUnit timeUnit) {
            return notBefore(this.afterNow(duration, timeUnit));
        }

        public Builder notBeforeFuture(Duration duration) {
            return notBeforeFuture(duration.toMillis(), TimeUnit.MILLISECONDS);
        }

        public Builder issuedAt(Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public Builder issuedAtNow() {
            return issuedAt(new Date());
        }

        public Builder jwtId(String jwtId) {
            this.jwtId = jwtId;
            return this;
        }

        public Builder jwtId(Supplier<String> supplier) {
            return jwtId(supplier.get());
        }

        public Builder putPayloadClaim(String key, Boolean value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, Date value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, Double value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, String value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, String[] value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, Integer value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, Integer[] value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, Long value) {
            return doPutPayloadClaim(key, value);
        }

        public Builder putPayloadClaim(String key, Long[] value) {
            return doPutPayloadClaim(key, value);
        }

        private Builder doPutPayloadClaim(String key, Object value) {
            Asserts.notNull(key);
            Asserts.notNull(value);
            this.payloadClaims.put(key, value);
            return this;
        }

        public JwtTokenMetadata build() {
            var metadata = new JwtTokenMetadata();
            metadata.jwtId = this.jwtId;
            metadata.keyId = this.keyId;
            metadata.issuer = this.issuer;
            metadata.subject = this.subject;
            metadata.audience = this.audience;
            metadata.expiresAt = this.expiresAt;
            metadata.notBefore = this.notBefore;
            metadata.issuedAt = this.issuedAt;
            metadata.payloadClaims = this.payloadClaims;
            return metadata;
        }

        private Date afterNow(long duration, TimeUnit timeUnit) {
            Asserts.notNull(timeUnit);
            return new Date(System.currentTimeMillis() + timeUnit.toMillis(duration));
        }
    }

}
