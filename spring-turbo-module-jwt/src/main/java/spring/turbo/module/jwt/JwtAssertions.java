package spring.turbo.module.jwt;

import java.util.HashMap;
import java.util.Map;

import static spring.turbo.module.jwt.JwtConstants.*;

/**
 * 验证JWT时，对Claim断言
 *
 * @author 应卓
 * @see #newInstance()
 * @see JwtConstants
 * @see ValidatingResult#INVALID_CLAIM
 * @since 3.3.2
 */
public final class JwtAssertions extends HashMap<String, Object> implements Map<String, Object> {

    /**
     * 私有构造方法
     */
    private JwtAssertions() {
    }

    /**
     * 创建对象
     *
     * @return JwtAssertions实例
     */
    public static JwtAssertions newInstance() {
        return new JwtAssertions();
    }

    /**
     * 添加断言 jti为期望值
     *
     * @param expectedId 期望值
     * @return this
     */
    public JwtAssertions requireId(String expectedId) {
        return addAssertion(PAYLOAD_JWT_ID, expectedId);
    }

    /**
     * 添加断言 sub为期望值
     *
     * @param expectedSubject 期望值
     * @return this
     */
    public JwtAssertions requireSubject(Object expectedSubject) {
        return addAssertion(PAYLOAD_SUBJECT, expectedSubject);
    }

    /**
     * 添加断言 iss为期望值
     *
     * @param expectedIssuer 期望值
     * @return this
     */
    public JwtAssertions requireIssuer(Object expectedIssuer) {
        return addAssertion(PAYLOAD_ISSUER, expectedIssuer);
    }

    /**
     * 添加断言
     *
     * @param claimName  字段名
     * @param claimValue 字段值
     * @return this
     */
    public JwtAssertions addAssertion(String claimName, Object claimValue) {
        this.put(claimName, claimValue);
        return this;
    }

}
