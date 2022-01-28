/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
@NonNullApi
@NonNullFields
package spring.turbo.module.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import org.springframework.lang.Nullable;
import spring.turbo.module.jackson.mixin.MixInFactory;
import spring.turbo.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.12
 */
class SpringBootAutoConfiguration {

    @Autowired(required = false)
    private List<MixInFactory> mixInFactories;

    @Autowired(required = false)
    void configObjectMapper(@Nullable ObjectMapper om) {
        if (om != null) {
            om.setAnnotationIntrospector(new CustomJacksonAnnotationIntrospector());

            handleMixIns(om);
        }
    }

    private void handleMixIns(ObjectMapper om) {
        if (CollectionUtils.isNotEmpty(mixInFactories)) {
            for (MixInFactory mixInFactory : mixInFactories) {
                if (mixInFactory != null) {
                    Optional.ofNullable(mixInFactory.create()).ifPresent(p -> om.addMixIn(p.getA(), p.getB()));
                }
            }
        }
    }
}
