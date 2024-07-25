package spring.turbo.module.jwt;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 验证结果
 *
 * @author 应卓
 * @since 3.3.2
 */
public enum ValidatingResult implements Serializable {

    /**
     * 没有错误
     */
    OK,

    /**
     * 令牌格式不合法
     */
    INVALID_JWT_FORMAT,

    /**
     * 令牌签名不合法
     */
    INVALID_SIGNATURE,

    /**
     * 令牌相关时间不合法
     *
     * @see JwtData#addPayloadExpiresAt(LocalDateTime)
     * @see JwtData#addPayloadExpiresAtFuture(Duration)
     * @see JwtData#addPayloadNotBefore(LocalDateTime)
     * @see JwtData#addPayloadNotBeforeAtFuture(Duration)
     */
    INVALID_TIME,

    /**
     * 被断言的Claim缺失或不正确
     *
     * @see JwtAssertions
     */
    INVALID_CLAIM

}
