package spring.turbo.module.jwt;

import java.io.Serializable;

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
     */
    INVALID_TIME,

    /**
     * 重要的Claim缺失或不正确
     */
    INVALID_CLAIM;

}
