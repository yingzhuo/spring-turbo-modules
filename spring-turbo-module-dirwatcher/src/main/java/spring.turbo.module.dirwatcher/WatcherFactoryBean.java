/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.dirwatcher;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 * @since 1.1.1
 */
class WatcherFactoryBean implements
        SmartFactoryBean<DirectoryWatcherWrapper>,
        ApplicationContextAware,
        InitializingBean,
        DisposableBean {

    private final static ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    @Nullable
    private DirectoryListener listener;

    @Nullable
    private String spel;

    @Nullable
    private ApplicationContext applicationContext;

    @Nullable
    private DirectoryWatcherWrapper wrapper;

    @Override
    public DirectoryWatcherWrapper getObject() {

        if (wrapper == null) {
            Asserts.notNull(listener);
            Asserts.notNull(spel);
            Asserts.hasText(spel);

            final Expression expression = EXPRESSION_PARSER.parseExpression(spel);
            final String dirToWatch = expression.getValue(new StandardEvaluationContext(applicationContext), String.class);

            Asserts.notNull(dirToWatch);

            this.wrapper = new DirectoryWatcherWrapper(
                    listener,
                    dirToWatch
            );

            this.wrapper.afterPropertiesSet();
        }

        return this.wrapper;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isEagerInit() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        if (wrapper != null) {
            wrapper.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (wrapper != null) {
            wrapper.afterPropertiesSet();
        }
    }

    @Override
    public Class<?> getObjectType() {
        return DirectoryWatcherWrapper.class;
    }

    public void setSpel(@Nullable String spel) {
        this.spel = spel;
    }

    public void setListener(DirectoryListener listener) {
        this.listener = listener;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
