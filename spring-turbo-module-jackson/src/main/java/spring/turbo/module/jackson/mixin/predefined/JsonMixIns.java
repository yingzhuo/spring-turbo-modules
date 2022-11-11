/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.mixin.predefined;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import spring.turbo.module.jackson.serializer.predefined.Converters;

/**
 * @author 应卓
 * @see spring.turbo.webmvc.api.Json
 * @since 1.0.12
 */
public final class JsonMixIns {

    /**
     * 私有构造方法
     */
    private JsonMixIns() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @JsonIgnoreProperties({"deprecated"})
    public static abstract class Style1 {
        @JsonSerialize(using = Converters.String.ToLong.class)
        public abstract String getCode();

        @JsonProperty("error")
        public abstract String getErrorMessage();

        @JsonProperty("data")
        @JsonPropertyOrder(alphabetic = true)
        public abstract Object getPayload();
    }

    @JsonIgnoreProperties({"deprecated"})
    public static abstract class Style2 {

        @JsonProperty("error")
        public abstract String getErrorMessage();

        @JsonProperty("data")
        @JsonPropertyOrder(alphabetic = true)
        public abstract Object getPayload();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @JsonIgnoreProperties({
            "password",
            "passwd",
            "pwd",
            "secret",
            "hidden"
    })
    public static abstract class SensitiveIgnoring {
    }

}
