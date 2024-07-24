package spring.turbo.module.jwt;

import java.util.HashMap;

/**
 * @author 应卓
 * @see #newInstance()
 * @since 3.3.1
 */
public class JwtAssertions extends HashMap<String, Object> {

    /**
     * 私有构造方法
     */
    private JwtAssertions() {
    }

    public static JwtAssertions newInstance() {
        return new JwtAssertions();
    }

    public JwtAssertions addAssertion(String claimName, Object claimValue) {
        this.put(claimName, claimValue);
        return this;
    }

}
