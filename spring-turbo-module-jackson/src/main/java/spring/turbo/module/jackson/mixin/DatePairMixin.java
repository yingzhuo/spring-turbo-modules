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
import spring.turbo.bean.DatePair;
import spring.turbo.format.DatePairParser;
import spring.turbo.format.StringToDatePairConverter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author 应卓
 * @see spring.turbo.bean.DayRange
 * @since 1.0.14
 */
@JsonDeserialize(using = DatePairMixin.DatePairJsonDeserializer.class)
public abstract class DatePairMixin {

    private static final DatePairParser DATE_PAIR_PARSER = new DatePairParser();

    private static final StringToDatePairConverter CONVERTER = new StringToDatePairConverter();

    static class DatePairJsonDeserializer extends JsonDeserializer<DatePair> {
        @Override
        public DatePair deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
            try {
                final String source = p.readValueAs(String.class);
                return DATE_PAIR_PARSER.parse(source, Locale.getDefault());
            } catch (ParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

}
