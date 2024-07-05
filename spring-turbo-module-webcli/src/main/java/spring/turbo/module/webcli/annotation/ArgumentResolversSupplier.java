package spring.turbo.module.webcli.annotation;

import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 3.3.1
 */
public interface ArgumentResolversSupplier extends Supplier<Collection<HttpServiceArgumentResolver>> {

    @Nullable
    @Override
    public default Collection<HttpServiceArgumentResolver> get() {
        return Set.of();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static class Default implements ArgumentResolversSupplier {
    }

}
