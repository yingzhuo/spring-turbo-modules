/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.writer;

import org.springframework.core.OrderComparator;
import spring.turbo.lang.Immutable;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static spring.turbo.util.CollectionUtils.nullSafeAddAll;

/**
 * @author 应卓
 * @see SheetMetadata
 * @since 1.0.7
 */
@Immutable
public final class WorkbookMetadata implements Serializable {

    private final List<SheetMetadata> sheetMetadata = new ArrayList<>();
    private String filename;
    private WorkbookMetadata() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getFilename() {
        return filename;
    }

    public List<SheetMetadata> getSheetMetadata() {
        return Collections.unmodifiableList(sheetMetadata);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public static final class Builder {

        private final List<SheetMetadata> sheetMetadataList = new ArrayList<>();
        private String filename;

        private Builder() {
            super();
        }

        public Builder workbook(String filename) {
            if (filename != null) {
                this.filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            return this;
        }

        public <T> Builder sheet(Class<T> valueObjectType, int sheetIndex, DataCollectionSupplier<T> dataCollectionSupplier) {
            sheetMetadataList.add(SheetMetadata.newInstance(valueObjectType, sheetIndex, String.valueOf(sheetIndex), dataCollectionSupplier));
            return this;
        }

        public <T> Builder sheet(Class<T> valueObjectType, int sheetIndex, String sheetName, DataCollectionSupplier<T> dataCollectionSupplier) {
            sheetMetadataList.add(SheetMetadata.newInstance(valueObjectType, sheetIndex, sheetName, dataCollectionSupplier));
            return this;
        }

        public <T> Builder sheet(Class<T> valueObjectType, int sheetIndex, String modelMapKey) {
            sheetMetadataList.add(SheetMetadata.newInstance(valueObjectType, sheetIndex, String.valueOf(sheetIndex), modelMapKey));
            return this;
        }

        public <T> Builder sheet(Class<T> valueObjectType, int sheetIndex, String sheetName, String modelMapKey) {
            sheetMetadataList.add(SheetMetadata.newInstance(valueObjectType, sheetIndex, sheetName, modelMapKey));
            return this;
        }

        public WorkbookMetadata build() {
            final WorkbookMetadata metadata = new WorkbookMetadata();
            metadata.filename = this.filename;
            nullSafeAddAll(metadata.sheetMetadata, this.sheetMetadataList);
            OrderComparator.sort(metadata.sheetMetadata);
            return metadata;
        }
    }

}
