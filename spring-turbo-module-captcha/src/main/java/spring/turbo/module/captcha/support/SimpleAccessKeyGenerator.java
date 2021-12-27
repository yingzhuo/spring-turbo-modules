/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.support;

import org.springframework.beans.factory.InitializingBean;
import spring.turbo.bean.SpringApplicationName;
import spring.turbo.util.RandomStringUtils;
import spring.turbo.util.StringUtils;

import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class SimpleAccessKeyGenerator implements AccessKeyGenerator, InitializingBean {

    @SpringApplicationName
    private String applicationName;

    @Override
    public String generate() {
        return applicationName + "-captcha-access-key-" + RandomStringUtils.randomUUID(true);
    }

    @Override
    public void afterPropertiesSet() {
        this.applicationName = StringUtils.isBlank(this.applicationName) ? EMPTY : applicationName;
    }

}
