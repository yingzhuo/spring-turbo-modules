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
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;
import spring.turbo.util.StringUtils;

import java.io.IOException;

/**
 * @author 应卓
 * @see JsonSerialize#nullsUsing()
 * @since 1.0.12
 */
public class DefaultStringValueSerializer extends JsonSerializer<String> {

    private final String valueIfNull;
    private final boolean emptyAsNull;
    private final boolean blankAsNull;

    public DefaultStringValueSerializer() {
        this("<no value>");
    }

    public DefaultStringValueSerializer(String valueIfNull) {
        this(valueIfNull, false, false);
    }

    public DefaultStringValueSerializer(String valueIfNull, boolean emptyAsNull, boolean blankAsNull) {
        Asserts.notNull(valueIfNull);

        this.valueIfNull = valueIfNull;
        this.emptyAsNull = emptyAsNull;
        this.blankAsNull = blankAsNull;
    }

    @Override
    public void serialize(@Nullable String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (emptyAsNull) {
            value = StringUtils.emptyToNull(value);
        }

        if (blankAsNull) {
            value = StringUtils.blankToNull(value);
        }

        gen.writeString(value != null ? value : valueIfNull);
    }

}
