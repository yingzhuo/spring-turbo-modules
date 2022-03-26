/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import spring.turbo.bean.LongPair;
import spring.turbo.bean.NumberPair;

/**
 * @author 应卓
 * @see NumberPair
 * @see LongPair
 * @since 1.0.14
 */
@JsonDeserialize(using = LongPairMixin.LongPairJsonDeserializer.class)
public abstract class LongPairMixin {

    static class LongPairJsonDeserializer extends AbstractNumberPairJsonDeserializer<LongPair> {
        public LongPairJsonDeserializer() {
            super(LongPair.class);
        }
    }

}
