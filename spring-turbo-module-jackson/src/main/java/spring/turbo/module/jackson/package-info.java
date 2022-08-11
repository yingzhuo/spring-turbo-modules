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
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.NonNullFields;
import org.springframework.lang.Nullable;
import spring.turbo.bean.*;
import spring.turbo.bean.condition.ConditionalOnModule;
import spring.turbo.integration.Modules;
import spring.turbo.module.jackson.mixin.*;
import spring.turbo.module.queryselector.SelectorSet;

/**
 * 自动注册
 *
 * @author 应卓
 * @since 1.0.12
 */
@AutoConfiguration
class SpringBootAutoConfiguration {

    @Autowired(required = false)
    void configObjectMapper(@Nullable ObjectMapper om) {
        if (om != null) {
            om.addMixIn(DateRange.class, DateRangeMixin.class);
            om.addMixIn(NumberPair.class, NumberPairMixin.class);
            om.addMixIn(IntegerPair.class, IntegerPairMixin.class);
            om.addMixIn(LongPair.class, LongPairMixin.class);
            om.addMixIn(BytePair.class, BytePairMixin.class);
            om.addMixIn(ShortPair.class, ShortPairMixin.class);
            om.addMixIn(FloatPair.class, FloatPairMixin.class);
            om.addMixIn(DoublePair.class, DoublePairMixin.class);
            om.addMixIn(BigIntegerPair.class, BigIntegerPairMixin.class);
            om.addMixIn(BigDecimalPair.class, BigDecimalPairMixin.class);
            om.addMixIn(NumberZones.class, NumberZonesMixin.class);
        }
    }

}

/**
 * 自动注册
 *
 * @author 应卓
 * @since 1.1.0
 */
@AutoConfiguration
@ConditionalOnModule(Modules.SPRING_TURBO_QUERYSELECTOR)
class SpringBootAutoConfigurationQueryselectorModule {

    @Autowired(required = false)
    void configObjectMapper(@Nullable ObjectMapper om) {
        if (om != null) {
            om.addMixIn(SelectorSet.class, SelectorSetMixin.class);
        }
    }

}
