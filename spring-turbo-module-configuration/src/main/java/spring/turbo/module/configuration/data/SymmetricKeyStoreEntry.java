package spring.turbo.module.configuration.data;

import java.security.Key;

/**
 * 对称加密算法秘钥条目
 *
 * @author 应卓
 * @since 3.3.1
 */
public interface SymmetricKeyStoreEntry extends KeyStoreEntry {

    /**
     * 获取秘钥
     *
     * @return 秘钥
     */
    public Key key();

    /**
     * {@inheritDoc}
     */
    @Override
    public default String algName() {
        return key().getAlgorithm();
    }

}
