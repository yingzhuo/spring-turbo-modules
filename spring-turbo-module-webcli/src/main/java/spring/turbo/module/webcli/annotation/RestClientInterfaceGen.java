package spring.turbo.module.webcli.annotation;

import org.springframework.core.env.Environment;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import spring.turbo.bean.classpath.ClassDefinition;
import spring.turbo.util.function.GenericGenerator;

/**
 * RestClientInterface 生成器 <br>
 * 这是一个内部工具
 *
 * @author 应卓
 * @since 3.3.1
 */
class RestClientInterfaceGen implements GenericGenerator {

    private final ClassDefinition classDef;
    private final Environment environment;
    private final RestClientSupplier restClientSupplier;
    private final ArgumentResolversSupplier globalArgumentResolversSupplier;
    private final ArgumentResolversSupplier argumentResolversSupplier;

    public RestClientInterfaceGen(
            ClassDefinition classDef,
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
