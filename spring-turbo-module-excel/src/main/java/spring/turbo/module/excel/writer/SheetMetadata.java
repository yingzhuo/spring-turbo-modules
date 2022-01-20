/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.writer;

import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.lang.Immutable;
import spring.turbo.module.excel.style.StyleProvider;
import spring.turbo.module.excel.writer.annotation.*;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.InstanceCache;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static spring.turbo.util.CollectionUtils.nullSafeAddAll;
import static spring.turbo.util.StringUtils.blankSafeAddAll;

/**
 * @author 应卓
 * @since 1.0.7
 */
@Immutable
public final class SheetMetadata<T> implements Serializable, Ordered {

    private final Class<T> valueObjectType;
    private final int sheetIndex;
    private final String sheetName;
    private final Collection<T> data;

    private SheetMetadata(
            Class<T> valueObjectType,
            int sheetIndex,
            String sheetName,
            @Nullable Collection<T> data) {

        Asserts.notNull(valueObjectType);
        Asserts.isTrue(sheetIndex >= 0);
        Asserts.hasText(sheetName);

        this.valueObjectType = valueObjectType;
        this.sheetIndex = sheetIndex;
        this.sheetName = sheetName;
        this.data = CollectionUtils.isEmpty(data) ? Collections.emptyList() : data;
    }

    static <T> SheetMetadata<T> newInstance(
            Class<T> valueObjectType,
            int sheetIndex,
            String sheetName) {
        return newInstance(valueObjectType, sheetIndex, sheetName, null);
    }

    static <T> SheetMetadata<T> newInstance(
            Class<T> valueObjectType,
            int sheetIndex,
            String sheetName,
            @Nullable Collection<T> data) {
        return new SheetMetadata<T>(valueObjectType, sheetIndex, sheetName, data);
    }

    public Class<T> getValueObjectType() {
        return valueObjectType;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public Collection<T> getData() {
        return data;
    }

    @Override
    public int getOrder() {
        return getSheetIndex();
    }

    // ---------------------------------------------------------------------------------------------------------------

    @NonNull
    public List<String> getHeader() {
        List<String> list = new ArrayList<>();

        // 处理InlineHeader
        final InlineHeader inlineHeader =
                AnnotationUtils.findAnnotation(valueObjectType, InlineHeader.class);

        if (inlineHeader != null) {
            blankSafeAddAll(list, inlineHeader.value().split("[\\s,]+"));
        }

        // 处理Header元注释
        final Header header = AnnotationUtils.findAnnotation(valueObjectType, Header.class);
        if (header != null) {
            nullSafeAddAll(list, header.value());
            if (header.trim()) {
                list = list.stream()
                        .map(String::trim)
                        .collect(Collectors.toList());
            }
        }

        return Collections.unmodifiableList(list);
    }

    public int getOffset() {
        final Offset annotation = AnnotationUtils.findAnnotation(valueObjectType, Offset.class);
        final int offset = Optional.ofNullable(annotation).map(Offset::value).orElse(0);
        return Math.max(offset, 0);
    }

    @Nullable
    public StyleProvider getHeaderProvider(InstanceCache instanceCache) {
        final HeaderStyle annotation = AnnotationUtils.findAnnotation(valueObjectType, HeaderStyle.class);
        if (annotation == null) {
            return null;
        }
        return instanceCache.findOrCreate(annotation.type());
    }

    @Nullable
    public StyleProvider getDataProvider(InstanceCache instanceCache) {
        final DataStyle annotation = AnnotationUtils.findAnnotation(valueObjectType, DataStyle.class);
        if (annotation == null) {
            return null;
        }
        return instanceCache.findOrCreate(annotation.type());
    }

}
