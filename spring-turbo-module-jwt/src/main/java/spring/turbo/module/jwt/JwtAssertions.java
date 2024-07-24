package spring.turbo.module.jwt;

import java.util.HashMap;

/**
 * 验证JWT时，对Claim断言
 *
 * @author 应卓
 * @see #newInstance()
 * @see JwtConstants
 * @see ValidatingResult#INVALID_CLAIM
 * @since 3.3.2
 */
public final class JwtAssertions extends HashMap<String, Object> {

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

    public JwtAssertions addAssertion(String claimName, Object claimValue) {
        this.put(claimName, claimValue);
        return this;
    }

    public JwtAssertions requireId(String expectedId) {
        return addAssertion(JwtConstants.PAYLOAD_JWT_ID, expectedId);
    }

    public JwtAssertions requireSubject(Object expectedSubject) {
        return addAssertion(JwtConstants.PAYLOAD_SUBJECT, expectedSubject);
    }

    public JwtAssertions requireIssuer(Object expectedIssuer) {
        return addAssertion(JwtConstants.PAYLOAD_ISSUER, expectedIssuer);
    }

}
