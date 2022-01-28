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
public class NullDefaults {

    /**
     * @see java.lang.String
     */
    public static class String {

        public static class NoValue extends DefaultStringValueSerializer {
            public NoValue() {
                super("<no value>");
            }
        }
    }

    /**
     * @see java.lang.Short
     */
    public static class Short {

        public static class Zero extends DefaultShortValueSerializer {
            public Zero() {
                super((short) 0);
            }
        }

        public static class One extends DefaultShortValueSerializer {
            public One() {
                super((short) 1);
            }
        }

        public static class NegativeOne extends DefaultShortValueSerializer {
            public NegativeOne() {
                super((short) -1);
            }
        }
    }

    /**
     * @see java.lang.Integer
     */
    public static class Integer {

        public static class Zero extends DefaultIntegerValueSerializer {
            public Zero() {
                super(0);
            }
        }

        public static class One extends DefaultIntegerValueSerializer {
            public One() {
                super(1);
            }
        }

        public static class NegativeOne extends DefaultIntegerValueSerializer {
            public NegativeOne() {
                super(-1);
            }
        }
    }

    /**
     * @see java.lang.Long
     */
    public static class Long {

        public static class Zero extends DefaultLongValueSerializer {
            public Zero() {
                super(0L);
            }
        }

        public static class One extends DefaultLongValueSerializer {
            public One() {
                super(1L);
            }
        }

        public static class NegativeOne extends DefaultLongValueSerializer {
            public NegativeOne() {
                super(-1L);
            }
        }
    }

    /**
     * @see java.math.BigInteger
     */
    public static class BigInteger {

        public static class Zero extends DefaultBigIntegerValueSerializer {
            public Zero() {
                super(java.math.BigInteger.ZERO);
            }
        }

        public static class One extends DefaultBigIntegerValueSerializer {
            public One() {
                super(java.math.BigInteger.ONE);
            }
        }

        public static class NegativeOne extends DefaultBigIntegerValueSerializer {
            public NegativeOne() {
                super(java.math.BigInteger.valueOf(-1L));
            }
        }
    }

    /**
     * @see java.lang.Float
     */
    public static class Float {

        public static class Zero extends DefaultFloatValueSerializer {
            public Zero() {
                super((float) 0.0);
            }
        }

        public static class One extends DefaultFloatValueSerializer {
            public One() {
                super((float) 1.0);
            }
        }

        public static class NegativeOne extends DefaultFloatValueSerializer {
            public NegativeOne() {
                super((float) -1);
            }
        }
    }

    /**
     * @see java.lang.Double
     */
    public static class Double {

        public static class Zero extends DefaultDoubleValueSerializer {
            public Zero() {
                super(0.0D);
            }
        }

        public static class One extends DefaultDoubleValueSerializer {
            public One() {
                super(1.0D);
            }
        }

        public static class NegativeOne extends DefaultDoubleValueSerializer {
            public NegativeOne() {
                super(-1.0D);
            }
        }
    }

    /**
     * @see java.math.BigDecimal
     */
    public static class BigDecimal {

        public static class Zero extends DefaultBigDecimalValueSerializer {
            public Zero() {
                super(java.math.BigDecimal.ZERO);
            }
        }

        public static class One extends DefaultBigDecimalValueSerializer {
            public One() {
                super(java.math.BigDecimal.ONE);
            }
        }

        public static class NegativeOne extends DefaultBigDecimalValueSerializer {
            public NegativeOne() {
                super(java.math.BigDecimal.valueOf(-1.0D));
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private NullDefaults() {
    }

}
