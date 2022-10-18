/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.javassist;

import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import spring.turbo.util.Asserts;

/**
 * 内部工具
 *
 * @author 应卓
 * @since 1.2.2
 */
public final class AnnotationDescriptorHelper {

    private AnnotationDescriptorHelper() {
        super();
    }

    public static MemberValue createMemberValue(AnnotationDescriptor.AnnotationValue annotationValue, ConstPool cp) {
        Asserts.notNull(annotationValue);
        final Object value = annotationValue.getValue();
        final Class<?> valueType = value.getClass();

        if (value instanceof Class) {
            return new ClassMemberValue(((Class<?>) value).getName(), cp);
        }

        if (valueType == String.class) {
            return new StringMemberValue((String) value, cp);
        }

        if (valueType == Integer.class) {
            return new IntegerMemberValue(cp, (Integer) value);
        }

        if (valueType == Long.class) {
            return new LongMemberValue((Long) value, cp);
        }

        if (valueType == Double.class) {
            return new DoubleMemberValue((Double) value, cp);
        }

        if (valueType == Float.class) {
            return new FloatMemberValue((Float) value, cp);
        }

        if (valueType == Short.class) {
            return new ShortMemberValue((Short) value, cp);
        }

        if (valueType == Byte.class) {
            return new ByteMemberValue((Byte) value, cp);
        }

        if (valueType == Character.class) {
            return new CharMemberValue((Character) value, cp);
        }

        if (valueType.isEnum()) {
            EnumMemberValue ret = new EnumMemberValue(cp);
            ret.setType(valueType.getName());
            ret.setValue(value.toString());
            return ret;
        }

        throw new IllegalArgumentException("Not supported yet, sorry!");
    }

}
