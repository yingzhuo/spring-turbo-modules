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

    public RestClientInterfaceFactory(ClassDef classDef, RestClientSupplier restClientSupplier) {
        this.classDef = classDef;
        this.restClientSupplier = restClientSupplier;
    }

    @Override
    public Object get() {
        var restClient = restClientSupplier.get();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(classDef.getBeanClass());
    }

}
