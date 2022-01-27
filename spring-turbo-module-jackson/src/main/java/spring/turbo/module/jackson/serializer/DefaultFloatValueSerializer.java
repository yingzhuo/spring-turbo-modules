/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * @author 应卓
 * @see JsonSerialize#nullsUsing()
 * @since 1.0.12
 */
public class DefaultFloatValueSerializer extends AbstractNumberValueSerializer<Float> {

    public DefaultFloatValueSerializer() {
        this((float) 0);
    }

    public DefaultFloatValueSerializer(Float valueIfNull) {
        super(valueIfNull);
    }

    @Override
    public void serialize(@Nullable Float value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value != null ? value : super.valueIfNull);
    }

}
