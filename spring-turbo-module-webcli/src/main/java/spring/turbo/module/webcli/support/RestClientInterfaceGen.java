/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.support;

import org.springframework.core.env.Environment;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import spring.turbo.bean.classpath.ClassDef;
import spring.turbo.util.function.GenericGenerator;

/**
 * RestClientInterface 生成器
 *
 * @author 应卓
 * @since 3.3.1
 */
class RestClientInterfaceGen implements GenericGenerator {

    private final ClassDef classDef;
    private final Environment environment;
    private final RestClientSupplier restClientSupplier;
    private final ArgumentResolversSupplier globalArgumentResolversSupplier;
    private final ArgumentResolversSupplier argumentResolversSupplier;

    public RestClientInterfaceGen(
            ClassDef classDef,
            Environment environment,
            RestClientSupplier restClientSupplier,
            ArgumentResolversSupplier globalArgumentResolversSupplier,
            ArgumentResolversSupplier argumentResolversSupplier) {
        this.classDef = classDef;
        this.environment = environment;
        this.restClientSupplier = restClientSupplier;
        this.globalArgumentResolversSupplier = globalArgumentResolversSupplier;
        this.argumentResolversSupplier = argumentResolversSupplier;
    }

    @Override
    public Object generate() {
        var restClient = restClientSupplier.get();
        var adapter = RestClientAdapter.create(restClient);

        var factoryBuilder = HttpServiceProxyFactory.builderFor(adapter)
                .embeddedValueResolver(environment::resolvePlaceholders);

        var argumentResolvers = argumentResolversSupplier.get();
        if (argumentResolvers != null) {
            for (var argumentResolver : argumentResolvers) {
                if (argumentResolver != null) {
                    factoryBuilder.customArgumentResolver(argumentResolver);
                }
            }
        }

        var globalArgumentResolvers = globalArgumentResolversSupplier.get();
        if (globalArgumentResolvers != null) {
            for (var globalArgumentResolver : globalArgumentResolvers) {
                if (globalArgumentResolver != null) {
                    factoryBuilder.customArgumentResolver(globalArgumentResolver);
                }
            }
        }

        return factoryBuilder.build()
                .createClient(classDef.getBeanClass());
    }

}
