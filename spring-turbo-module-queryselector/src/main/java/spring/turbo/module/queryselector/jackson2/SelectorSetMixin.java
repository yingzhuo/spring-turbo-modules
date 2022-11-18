/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.resolver.SelectorSetResolver;
import spring.turbo.module.queryselector.resolver.SelectorSetResolverImpl;

import java.io.IOException;

/**
 * @author 应卓
 * @since 1.3.0
 */
@JsonDeserialize(using = SelectorSetMixin.SelectorSetJsonSerializer.class)
public abstract class SelectorSetMixin {

    public static final SelectorSetResolver RESOLVER = new SelectorSetResolverImpl();

    public static class SelectorSetJsonSerializer extends JsonDeserializer<SelectorSet> {

        @Override
        public SelectorSet deserialize(JsonParser p, DeserializationContext context) throws IOException {
            final String string = p.readValueAs(String.class);
            return RESOLVER.resolve(string).orElse(SelectorSet.empty());
        }
    }

}
