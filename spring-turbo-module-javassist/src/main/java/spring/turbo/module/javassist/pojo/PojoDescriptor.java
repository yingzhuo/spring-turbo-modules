/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.javassist.pojo;

import org.springframework.lang.Nullable;
import org.springframework.util.comparator.ComparableComparator;
import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.lang.Mutable;
import spring.turbo.module.javassist.AnnotationDescriptor;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;

import java.util.*;

/**
 * @author 应卓
 * @see #newInstance(String)
 * @since 1.2.2
 */
@Mutable
@SuppressWarnings("rawtypes")
public class PojoDescriptor extends TreeMap<String, Class<?>> implements Map<String, Class<?>> {

    private final static Comparator<String> DEFAULT_PROPERTY_COMPARATOR = new ComparableComparator<>();

    /**
     * 类型级别元注释
     */
    private final String pojoFqn;

    private final List<AnnotationDescriptor> classLevelAnnotations = new ArrayList<>();
    private final List<Pair<AnnotationPos, List<AnnotationDescriptor>>> allPropertyAnnotationConfig = new ArrayList<>();
    private final List<Tuple<String, AnnotationPos, List<AnnotationDescriptor>>> propertyAnnotationConfig = new ArrayList<>();
    private final Comparator<String> propertyComparator;

    /**
     * 私有构造方法
     */
    private PojoDescriptor(String pojoFqn, @Nullable Comparator<String> propertyComparator) {
        super(Optional.ofNullable(propertyComparator).orElse(DEFAULT_PROPERTY_COMPARATOR));
        Asserts.notNull(pojoFqn);
        this.pojoFqn = pojoFqn;
        this.propertyComparator = Optional.ofNullable(propertyComparator).orElse(DEFAULT_PROPERTY_COMPARATOR);
    }

    public static PojoDescriptor newInstance(String pojoFqn) {
        return newInstance(pojoFqn, null);
    }

    public static PojoDescriptor newInstance(String pojoFqn, @Nullable Comparator<String> propertyComparator) {
        return new PojoDescriptor(pojoFqn, propertyComparator);
    }

    public static PojoDescriptor fromMap(String pojoFqn, Map map) {
        return fromMap(pojoFqn, map, null);
    }

    public static PojoDescriptor fromMap(String pojoFqn, Map map, @Nullable Comparator<String> propertyComparator) {
        Asserts.notNull(map);
        PojoDescriptor ret = PojoDescriptor.newInstance(pojoFqn, propertyComparator);
        for (Object key : map.keySet()) {
            if (!(key instanceof CharSequence)) {
                throw new IllegalArgumentException("Unsupported key type.");
            } else {
                String keyString = key.toString();
                Object value = map.get(key);
                if (value != null) {
                    ret = ret.add(keyString, value.getClass());
                }
            }
        }
        return ret;
    }

    public PojoDescriptor add(String fieldName, Class fieldType) {
        this.put(fieldName, fieldType);
        return this;
    }

    public PojoDescriptor addClassLevelAnnotations(AnnotationDescriptor... descriptors) {
        CollectionUtils.nullSafeAddAll(this.classLevelAnnotations, descriptors);
        return this;
    }

    public PojoDescriptor addPropertyLevelAnnotationsForAllProperties(AnnotationPos annotationPos, AnnotationDescriptor... descriptors) {
        Asserts.notNull(annotationPos);
        Asserts.notEmpty(descriptors);
        this.allPropertyAnnotationConfig.add(Pair.ofNonNull(annotationPos, Arrays.asList(descriptors)));
        return this;
    }

    public PojoDescriptor addPropertyLevelAnnotations(String propertyName, AnnotationPos annotationPos, AnnotationDescriptor... descriptors) {
        Asserts.notNull(propertyName);
        Asserts.notNull(annotationPos);
        Asserts.notEmpty(descriptors);
        this.propertyAnnotationConfig.add(Tuple.ofNonNull(propertyName, annotationPos, Arrays.asList(descriptors)));
        return this;
    }

    @Override
    public String toString() {
        return this.pojoFqn;
    }

    public String getPojoFqn() {
        return pojoFqn;
    }

    public List<AnnotationDescriptor> getClassLevelAnnotations() {
        return classLevelAnnotations;
    }

    public List<Pair<AnnotationPos, List<AnnotationDescriptor>>> getAllPropertyAnnotationConfig() {
        return allPropertyAnnotationConfig;
    }

    public List<Tuple<String, AnnotationPos, List<AnnotationDescriptor>>> getPropertyAnnotationConfig() {
        return propertyAnnotationConfig;
    }

    public Comparator<String> getPropertyComparator() {
        return propertyComparator;
    }

    /**
     * 元注释位置
     */
    public static enum AnnotationPos {
        GETTER, SETTER, FIELD
    }

}
