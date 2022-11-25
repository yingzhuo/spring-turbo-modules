/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.jackson2;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import spring.turbo.core.SpringUtils;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.resolver.SelectorSetResolver;

import java.io.IOException;

/**
 * @author 应卓
 * @since 1.3.1
 */
@JsonSerialize(using = SelectorSetMixin.S.class)
@JsonDeserialize(using = SelectorSetMixin.D.class)
public abstract class SelectorSetMixin {

    public static class S extends JsonSerializer<SelectorSet> {
        @Override
        public void serialize(SelectorSet value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString());
        }
    }

    public static class D extends JsonDeserializer<SelectorSet> {
        @Override
        public SelectorSet deserialize(JsonParser p, DeserializationContext context) throws IOException, JacksonException {
            final String string = p.readValueAs(String.class);
            final SelectorSetResolver resolver = SpringUtils.getBean(SelectorSetResolver.class).orElseGet(SelectorSetResolver::new);
            SelectorSet ret = resolver.convert(string);
            if (ret == null) {
                throw new JsonParseException(p, "Cannot parse SelectorSet instance.");
            }
            return ret;
        }
    }

}
