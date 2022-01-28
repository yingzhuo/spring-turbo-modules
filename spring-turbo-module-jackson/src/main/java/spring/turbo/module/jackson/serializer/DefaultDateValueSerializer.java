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
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * @author 应卓
 * @see JsonSerialize#nullsUsing()
 * @since 1.0.12
 */
public class DefaultDateValueSerializer extends DateSerializer {

    @Nullable
    private final Date valueIfNull;

    public DefaultDateValueSerializer() {
        this(null);
    }

    public DefaultDateValueSerializer(@Nullable Date valueIfNull) {
        this.valueIfNull = valueIfNull;
    }

    @Override
    public void serialize(@Nullable Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        Date dateInUse = Optional.ofNullable(value).orElse(valueIfNull);
        if (dateInUse == null) {
            dateInUse = new Date();
        }

        super.serialize(dateInUse, gen, serializers);
    }

}
