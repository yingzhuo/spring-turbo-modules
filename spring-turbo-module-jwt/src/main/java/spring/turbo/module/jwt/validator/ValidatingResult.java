package spring.turbo.module.jwt.validator;

import java.io.Serializable;

/**
 * 验证结果
 *
 * @author 应卓
 * @since 3.1.1
 */
public enum ValidatingResult implements Serializable {

    /**
     * 没有错误
     */
    NO_PROBLEM,

    /**
     * 令牌格式不合法
     */
    INVALID_JWT_FORMAT,

    /**
     * 令牌签名不合法
     */
    INVALID_SIGNATURE,

    /**
     * 令牌相关事件不合法
     */
    INVALID_TIME

}
