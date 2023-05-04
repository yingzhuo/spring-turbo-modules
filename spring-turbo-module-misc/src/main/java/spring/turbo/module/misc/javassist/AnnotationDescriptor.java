/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.javassist;

import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 应卓
 *
 * @see PojoDescriptor
 * @see #builder(String)
 * @see #builder(Class)
 *
 * @since 1.2.2
 */
public final class AnnotationDescriptor implements Serializable {

    private final String annotationFqn;
    private final List<AnnotationValue> values;

    /**
     * 私有构造方法
     *
     * @param annotationFqn
     *            元注释名
     * @param values
     *            元注释包含的值
     */
    private AnnotationDescriptor(String annotationFqn, @Nullable List<AnnotationValue> values) {
        Asserts.notNull(annotationFqn);
        this.annotationFqn = annotationFqn;
        this.values = values != null ? values : Collections.emptyList();
    }

    public static Builder builder(Class<? extends Annotation> annotationType) {
        Asserts.notNull(annotationType);
        return new Builder(annotationType.getName());
    }

    public static Builder builder(String annotationFqn) {
        Asserts.notNull(annotationFqn);
        return new Builder(annotationFqn);
    }

    public String getAnnotationFqn() {
        return annotationFqn;
    }

    public List<AnnotationValue> getValues() {
        return values;
    }

    public static final class AnnotationValue implements Serializable {
        private final String name;
        private final Object value;

        public AnnotationValue(String name, Object value) {
            Asserts.notNull(name);
            Asserts.notNull(value);
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    /**
     * 创建器
     */
    public static final class Builder {
        private final List<AnnotationValue> annotationValues = new ArrayList<>(0);
        private final String annotationFqn;

        private Builder(String annotationFqn) {
            this.annotationFqn = annotationFqn;
        }

        public Builder addValue(String valueName, Object valueObject) {
            this.annotationValues.add(new AnnotationValue(valueName, valueObject));
            return this;
        }

        public AnnotationDescriptor build() {
            Asserts.notNull(this.annotationFqn);
            return new AnnotationDescriptor(this.annotationFqn, this.annotationValues);
        }
    }

}
