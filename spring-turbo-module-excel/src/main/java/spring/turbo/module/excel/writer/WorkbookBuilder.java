/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.writer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.NumberUtils;
import spring.turbo.bean.valueobject.ValueObjectGetter;
import spring.turbo.bean.valueobject.ValueObjectUtils;
import spring.turbo.module.excel.style.StyleProvider;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.InstanceCache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

import static spring.turbo.module.excel.writer.PredefinedCellStyleFactories.*;

/**
 * @author 应卓
 * @see SheetMetadata
 * @see WorkbookIO
 * @since 1.0.7
 */
public final class WorkbookBuilder {

    private static final int MAX_CELL_WIDTH = 255 * 256;

    private final List<SheetMetadata<?>> sheetInfo = new ArrayList<>();
    private InstanceCache instanceCache = InstanceCache.newInstance();
    private Supplier<Workbook> workbookSupplier = XSSFWorkbook::new;

    /**
     * 私有构造方法
     */
    private WorkbookBuilder() {
        super();
    }

    public static WorkbookBuilder newInstance() {
        return new WorkbookBuilder();
    }

    public static WorkbookBuilder newInstance(Supplier<Workbook> workbookSupplier) {
        Asserts.notNull(workbookSupplier);

        final WorkbookBuilder builder = new WorkbookBuilder();
        builder.workbookSupplier = workbookSupplier;
        return builder;
    }

    public WorkbookBuilder applicationContext(@Nullable ApplicationContext applicationContext) {
        this.instanceCache = InstanceCache.newInstance(applicationContext);
        return this;
    }

    public <T> WorkbookBuilder sheet(Class<T> valueObjectType,
                                     int sheetIndex,
                                     String sheetName,
                                     @Nullable Collection<T> data) {
        return sheet(valueObjectType, sheetIndex, sheetName, data, Collections.emptySet());
    }

    public <T> WorkbookBuilder sheet(Class<T> valueObjectType,
                                     int sheetIndex,
                                     String sheetName,
                                     @Nullable Collection<T> data,
                                     @Nullable Set<String> includeHeaders) {
        sheetInfo.add(SheetMetadata.newInstance(valueObjectType, sheetIndex, sheetName, data, includeHeaders));
        return this;
    }

    public Workbook build() {

        OrderComparator.sort(sheetInfo);

        final Workbook workbook = Optional.ofNullable(workbookSupplier.get()).orElseGet(XSSFWorkbook::new);

        for (SheetMetadata<?> metadata : sheetInfo) {
            final String sheetName = metadata.getSheetName();
            final Sheet sheet = workbook.createSheet(sheetName);
            writeSheets(workbook, sheet, metadata);
        }

        return workbook;
    }

    private void writeSheets(Workbook workbook, Sheet sheet, SheetMetadata<?> sheetMetadata) {

        final int offset = sheetMetadata.getOffset();
        final List<String> header = sheetMetadata.getHeader();

        // 重新调整header
        final List<String> betterHeader = new ArrayList<>();
        for (String headerName : header) {
            if (!sheetMetadata.shouldSkip(headerName)) {
                betterHeader.add(headerName);
            }
        }

        // 写入头部
        doWriteHeader(workbook,
                sheet,
                betterHeader,
                offset,
                getHeaderStyle(instanceCache, workbook, sheetMetadata)
        );

        // 写入数据部分
        doWriteData(workbook,
                sheet,
                sheetMetadata.getData(),
                betterHeader,
                offset,
                getCommonDataStyle(instanceCache, workbook, sheetMetadata),
                getDateTypeDataStyle(instanceCache, workbook, sheetMetadata),
                sheetMetadata.getValueObjectType()
        );

        this.autoSizeColumns(workbook);
    }

    private void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(sheet.getFirstRowNum());
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    final Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                    int currentColumnWidth = sheet.getColumnWidth(columnIndex);
                    sheet.setColumnWidth(columnIndex, Math.min((currentColumnWidth + 2500), MAX_CELL_WIDTH)); // POI对最大宽度有限制，不得超过256*255
                }
            }
        }
    }

    @NonNull
    private CellStyle getHeaderStyle(InstanceCache cache, Workbook workbook, SheetMetadata<?> sheetMetadata) {
        CellStyle style = null;
        StyleProvider provider = sheetMetadata.getHeaderStyleProvider(cache);
        if (provider != null) {
            style = provider.getStyle(workbook);
        }
        if (style == null) {
            style = createCellStyleForHeader(workbook);
        }
        return style;
    }

    @NonNull
    private CellStyle getCommonDataStyle(InstanceCache cache, Workbook workbook, SheetMetadata<?> sheetMetadata) {
        CellStyle style = null;
        StyleProvider provider = sheetMetadata.getHeaderStyleProvider(cache);
        if (provider != null) {
            style = provider.getStyle(workbook);
        }
        if (style == null) {
            style = createCellStyleForCommonData(workbook);
        }
        return style;
    }

    @NonNull
    private CellStyle getDateTypeDataStyle(InstanceCache cache, Workbook workbook, SheetMetadata<?> sheetMetadata) {
        CellStyle style = null;
        StyleProvider provider = sheetMetadata.getHeaderStyleProvider(cache);
        if (provider != null) {
            style = provider.getStyle(workbook);
        }
        if (style == null) {
            style = createCellStyleForDateTypeData(workbook);
        }
        return style;
    }

    private void doWriteHeader(
            Workbook workbook,
            Sheet sheet, List<String> header, int offset, @Nullable CellStyle headerCellStyle) {

        headerCellStyle = Optional.ofNullable(headerCellStyle).orElse(createCellStyleForHeader(workbook));

        final Row row = sheet.createRow(0);
        for (int i = 0; i < header.size(); i++) {
            final Cell cell = row.createCell(i + offset);
            cell.setCellValue(header.get(i));
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void doWriteData(Workbook workbook,
                             Sheet sheet,
                             Collection<?> data,
                             List<String> header,
                             int offset,
                             @Nullable CellStyle dataCellStyle,
                             @Nullable CellStyle dataCellStyleForDate,
                             Class<?> valueObjectType) {

        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        final Map<String, String> aliasMap = ValueObjectUtils.getAliases(valueObjectType);
        dataCellStyle = Optional.ofNullable(dataCellStyle).orElse(createCellStyleForCommonData(workbook));

        int indexOfData = -1;
        for (Object vo : data) {
            indexOfData++;

            final int rowIndex = indexOfData + 1;
            final ValueObjectGetter getter = new ValueObjectGetter(vo);
            final Row row = sheet.createRow(rowIndex);

            int cellIndex = offset - 1;
            for (String headerName : header) {
                cellIndex++;

                final Cell cell = row.createCell(cellIndex);

                Object property = getPropertyValue(aliasMap, getter, headerName);
                if (property == null) {
                    cell.setBlank();
                    cell.setCellStyle(dataCellStyle);
                    continue;
                }

                if (property instanceof Date) {
                    cell.setCellValue((Date) property);
                    cell.setCellStyle(dataCellStyleForDate);
                } else if (property instanceof Calendar) {
                    cell.setCellValue((Calendar) property);
                    cell.setCellStyle(dataCellStyleForDate);
                } else if (property instanceof LocalDateTime) {
                    cell.setCellValue((LocalDateTime) property);
                    cell.setCellStyle(dataCellStyle);
                } else if (property instanceof LocalDate) {
                    cell.setCellValue((LocalDate) property);
                    cell.setCellStyle(dataCellStyle);
                } else if (property instanceof Number) {
                    cell.setCellValue((Double) NumberUtils.convertNumberToTargetClass((Number) property, Double.class));
                    cell.setCellStyle(dataCellStyle);
                } else {
                    cell.setCellValue(property.toString());
                    cell.setCellStyle(dataCellStyle);
                }
            }
        }
    }

    @Nullable
    private Object getPropertyValue(Map<String, String> aliasMap, ValueObjectGetter getter, String headerName) {
        final String propertyName = aliasMap.get(headerName);
        if (propertyName == null) {
            return null;
        }
        return getter.get(propertyName);
    }

}
