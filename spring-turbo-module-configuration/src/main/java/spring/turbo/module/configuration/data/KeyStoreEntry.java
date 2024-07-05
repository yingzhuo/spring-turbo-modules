package spring.turbo.module.configuration.data;

import java.io.Serializable;

/**
 * 秘钥库条目
 *
 * @author 应卓
 * @since 3.3.1
 */
public interface KeyStoreEntry extends Serializable {

    /**
     * 获取别名
     *
     * @return 别名
     */
    public String alias();

    /**
     * 获取算法名称
     *
     * @return 算法名称
     */
    public String algName();

}
