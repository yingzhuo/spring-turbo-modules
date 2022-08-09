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
import spring.turbo.bean.BytePair;
import spring.turbo.bean.NumberPair;

/**
 * @author 应卓
 * @see NumberPair
 * @see BytePair
 * @since 1.0.14
 */
@JsonDeserialize(using = BytePairMixin.BytePairJsonDeserializer.class)
public abstract class BytePairMixin {

    public static class BytePairJsonDeserializer extends AbstractNumberPairJsonDeserializer<BytePair> {
        public BytePairJsonDeserializer() {
            super(BytePair.class);
        }
    }

}
