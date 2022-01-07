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
import spring.turbo.module.excel.writer.annotation.DataStyle;
import spring.turbo.module.excel.writer.annotation.Header;
import spring.turbo.module.excel.writer.annotation.HeaderStyle;
import spring.turbo.util.Asserts;
import spring.turbo.util.InstanceCache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static spring.turbo.util.CollectionUtils.nullSafeAddAll;

/**
 * @author 应卓
 * @since 1.0.7
 */
@Immutable
public final class SheetMetadata<T> implements Serializable, Ordered {

    private final Class<T> valueObjectType;
    private final int sheetIndex;
    private final String sheetName;
    private final DataCollectionSupplier<T> dataCollectionSupplier;

    private SheetMetadata(
            @NonNull Class<T> valueObjectType,
            @NonNull int sheetIndex,
            @NonNull String sheetName,
            @NonNull DataCollectionSupplier<T> dataCollectionSupplier) {

        Asserts.notNull(valueObjectType);
        Asserts.isTrue(sheetIndex >= 0);
        Asserts.hasText(sheetName);
        Asserts.notNull(dataCollectionSupplier);

        this.valueObjectType = valueObjectType;
        this.sheetIndex = sheetIndex;
        this.sheetName = sheetName;
        this.dataCollectionSupplier = dataCollectionSupplier;
    }

    public static <T> SheetMetadata<T> newInstance(
            @NonNull Class<T> valueObjectType,
            @NonNull int sheetIndex,
            @NonNull String sheetName,
            @NonNull String modelMapKey
    ) {
        return newInstance(valueObjectType, sheetIndex, sheetName, DataCollectionSupplier.getDefault(valueObjectType, modelMapKey));
    }

    public static <T> SheetMetadata<T> newInstance(
            @NonNull Class<T> valueObjectType,
            @NonNull int sheetIndex,
            @NonNull String sheetName,
            @NonNull DataCollectionSupplier<T> dataCollectionSupplier) {
        return new SheetMetadata<T>(valueObjectType, sheetIndex, sheetName, dataCollectionSupplier);
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

    public DataCollectionSupplier getDataCollectionSupplier() {
        return dataCollectionSupplier;
    }

    @Override
    public int getOrder() {
        return getSheetIndex();
    }

    // ---------------------------------------------------------------------------------------------------------------

    @NonNull
    List<String> getHeader() {
        final List<String> list = new ArrayList<>();
        final Header headerAnnotation = AnnotationUtils.findAnnotation(valueObjectType, Header.class);
        if (headerAnnotation != null) {
            nullSafeAddAll(list, headerAnnotation.value());
        }
        return Collections.unmodifiableList(list);
    }

    @NonNull
    int getOffset() {
        final Header annotation = AnnotationUtils.findAnnotation(valueObjectType, Header.class);
        return Optional.ofNullable(annotation).map(Header::offset).orElse(0);
    }

    @Nullable
    StyleProvider getHeaderProvider(InstanceCache instanceCache) {
        final HeaderStyle annotation = AnnotationUtils.findAnnotation(valueObjectType, HeaderStyle.class);
        if (annotation == null) {
            return null;
        }
        return instanceCache.findOrCreate(annotation.type());
    }

    @Nullable
    StyleProvider getDataProvider(InstanceCache instanceCache) {
        final DataStyle annotation = AnnotationUtils.findAnnotation(valueObjectType, DataStyle.class);
        if (annotation == null) {
            return null;
        }
        return instanceCache.findOrCreate(annotation.type());
    }

}
