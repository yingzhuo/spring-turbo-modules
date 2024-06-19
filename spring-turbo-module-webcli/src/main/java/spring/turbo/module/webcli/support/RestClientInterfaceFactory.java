/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.webcli.support;

import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import spring.turbo.bean.classpath.ClassDef;

import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 3.3.1
 */
@SuppressWarnings("rawtypes")
class RestClientInterfaceFactory implements Supplier {

    private final ClassDef classDef;
    private final RestClientSupplier restClientSupplier;
    private final GlobalArgumentResolversSupplier globalArgumentResolversSupplier;
    private final ArgumentResolversSupplier argumentResolversSupplier;

    public RestClientInterfaceFactory(
            ClassDef classDef,
            RestClientSupplier restClientSupplier,
            GlobalArgumentResolversSupplier globalArgumentResolversSupplier,
            ArgumentResolversSupplier argumentResolversSupplier) {
        this.classDef = classDef;
        this.restClientSupplier = restClientSupplier;
        this.globalArgumentResolversSupplier = globalArgumentResolversSupplier;
        this.argumentResolversSupplier = argumentResolversSupplier;
    }

    @Override
    public Object get() {
        var restClient = restClientSupplier.get();
        var adapter = RestClientAdapter.create(restClient);
        var factoryBuilder = HttpServiceProxyFactory.builderFor(adapter);

        var argumentResolvers = argumentResolversSupplier.get();
        if (argumentResolvers != null) {
            for (var argumentResolver : argumentResolvers) {
                if (argumentResolver != null) {
                    factoryBuilder.customArgumentResolver(argumentResolver);
                }
            }
        }

        var globalArgumentResolvers = this.globalArgumentResolversSupplier.get();
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
