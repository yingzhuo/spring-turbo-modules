/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.mixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.core.convert.TypeDescriptor;
import spring.turbo.bean.LongPair;
import spring.turbo.bean.NumberPair;
import spring.turbo.format.StringToNumberPairConverter;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author 应卓
 * @see NumberPair
 * @see LongPair
 * @since 1.0.14
 */
@JsonDeserialize(using = LongPairMixin.LongPairJsonDeserializer.class)
public abstract class LongPairMixin {

    private static final StringToNumberPairConverter CONVERTER = new StringToNumberPairConverter();

    static class LongPairJsonDeserializer extends JsonDeserializer<LongPair> {

        @Override
        @Nullable
        public LongPair deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            final String source = p.readValueAs(String.class);
            return (LongPair) CONVERTER.convert
                    (
                            source,
                            TypeDescriptor.valueOf(String.class),
                            TypeDescriptor.valueOf(LongPair.class)
                    );
        }
    }

}
