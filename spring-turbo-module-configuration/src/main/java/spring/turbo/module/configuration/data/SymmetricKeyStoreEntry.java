/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.configuration.data;

import java.security.Key;

/**
 * 对称加密算法秘钥条目
 *
 * @author 应卓
 * @see SymmetricKeyStoreEntryEditor
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
