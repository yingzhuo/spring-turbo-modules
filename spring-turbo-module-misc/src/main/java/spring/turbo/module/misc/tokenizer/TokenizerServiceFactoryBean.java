package spring.turbo.module.misc.tokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.lang.Nullable;

/**
 * @author 应卓
 * @since 3.3.1
 */
public class TokenizerServiceFactoryBean implements SmartFactoryBean<TokenizerService> {

    // 为何需要FactoryBean再过度一次。
    // 实际上cn.hutool.extra.tokenizer.TokenizerEngine也是一个门面的经典案列
    // 如果缺失依赖则会造成启动失败

    private static final Logger log = LoggerFactory.getLogger(TokenizerServiceFactoryBean.class);

    @Nullable
    @Override
    public TokenizerService getObject() {
        try {
            return new TokenizerServiceImpl();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Class<?> getObjectType() {
        return TokenizerService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }
}
