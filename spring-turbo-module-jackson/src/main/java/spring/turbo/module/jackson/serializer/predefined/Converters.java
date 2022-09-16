/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jackson.serializer.predefined;

import spring.turbo.module.jackson.serializer.*;

/**
 * @author 应卓
 * @since 1.0.12
 */
public class Converters {

    // -----------------------------------------------------------------------------------------------------------------
    private Converters() {
        super();
    }

    public static class String {
        // @formatter:off
        public static class ToShort extends StringToShortSerializer {
        }

        public static class ToInteger extends StringToIntegerSerializer {
        }

        public static class ToLong extends StringToLongSerializer {
        }

        public static class ToBigInteger extends StringToBigIntegerSerializer {
        }

        public static class ToFloat extends StringToFloatSerializer {
        }

        public static class ToDouble extends StringToDoubleSerializer {
        }

        public static class ToBigDecimal extends StringToBigDecimalSerializer {
        }
        // @formatter:on
    }

}
