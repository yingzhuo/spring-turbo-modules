package spring.turbo.module.misc.captcha.support;

import org.springframework.beans.factory.InitializingBean;
import spring.turbo.bean.injection.ApplicationName;
import spring.turbo.util.StringUtils;
import spring.turbo.util.UUIDUtils;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * {@link AccessKeyGenerator}的默认实现
 *
 * @author 应卓
 * @since 1.0.1
 */
public class SimpleAccessKeyGenerator implements AccessKeyGenerator, InitializingBean {

    @ApplicationName
    private String applicationName;

    @Override
    public String generate() {
        return applicationName + "-captcha-access-key-" + UUIDUtils.uuid32();
    }

    @Override
    public void afterPropertiesSet() {
        this.applicationName = StringUtils.isBlank(this.applicationName) ? EMPTY : applicationName;
    }

}
