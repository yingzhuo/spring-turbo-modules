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
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.NumberUtils;
import org.springframework.web.context.request.ServletWebRequest;
import spring.turbo.bean.valueobject.ValueObjectGetter;
import spring.turbo.bean.valueobject.ValueObjectUtils;
import spring.turbo.module.excel.style.StyleProvider;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.InstanceCache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author 应卓
 * @since 1.0.7
 */
public class AnnotationDrivenExcelView extends AbstractAnnotationDrivenExcelView {

    private final InstanceCache instanceCache;

    public AnnotationDrivenExcelView(@NonNull ApplicationContext applicationContext, WorkbookMetadata workbookMetadata) {
        super(workbookMetadata);
        Asserts.notNull(applicationContext);
        this.instanceCache = InstanceCache.newInstance(applicationContext);
    }

    @Override
    protected void buildExcelSheet(Workbook workbook, Sheet sheet, SheetMetadata<?> sheetMetadata, Collection<?> data, ServletWebRequest servletWebRequest) {

        final int offset = sheetMetadata.getOffset();
        final List<String> header = sheetMetadata.getHeader();
        final StyleProvider headerStyleProvider = sheetMetadata.getHeaderProvider(instanceCache);
        final StyleProvider dataStyleProvider = sheetMetadata.getDataProvider(instanceCache);

        // 设置自适应列宽
        for (int i = offset; i < header.size() + offset; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入头部
        doWriteHeader(workbook, sheet,
                header, offset,
                Optional.ofNullable(headerStyleProvider).map(p -> p.getStyle(workbook)).orElse(null)
        );

        // 写入数据部分
        doWriteData(workbook, sheet,
                data, header, offset,
                Optional.ofNullable(dataStyleProvider).map(p -> p.getStyle(workbook)).orElse(null),
                sheetMetadata.getValueObjectType()
        );
    }

    private void doWriteHeader(
            @NonNull Workbook workbook,
            @NonNull Sheet sheet, @NonNull List<String> header, int offset, @Nullable CellStyle headerCellStyle) {

        headerCellStyle = Optional.ofNullable(headerCellStyle).orElse(createDefaultCellStyleForHeader(workbook));

        final Row row = sheet.createRow(0);
        for (int i = 0; i < header.size(); i++) {
            final Cell cell = row.createCell(i + offset);
            cell.setCellValue(header.get(i));
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void doWriteData(@NonNull Workbook workbook,
                             @NonNull Sheet sheet,
                             @NonNull Collection<?> data,
                             @NonNull List<String> header,
                             @NonNull int offset,
                             @Nullable CellStyle dataCellStyle,
                             @NonNull Class<?> valueObjectType) {

        if (CollectionUtils.isEmpty(data)) {
            return;
        }

        final Map<String, String> aliasMap = ValueObjectUtils.getAliases(valueObjectType);
        dataCellStyle = Optional.ofNullable(dataCellStyle).orElse(createDefaultCellStyleForData(workbook));

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
                cell.setCellStyle(dataCellStyle);

                Object property = getPropertyValue(aliasMap, getter, headerName);
                if (property == null) {
                    cell.setBlank();
                    continue;
                }

                if (property instanceof Date) {
                    cell.setCellValue((Date) property);
                } else if (property instanceof Calendar) {
                    cell.setCellValue((Calendar) property);
                } else if (property instanceof LocalDateTime) {
                    cell.setCellValue((LocalDateTime) property);
                } else if (property instanceof LocalDate) {
                    cell.setCellValue((LocalDate) property);
                } else if (property instanceof Number) {
                    cell.setCellValue((Double) NumberUtils.convertNumberToTargetClass((Number) property, Double.class));
                } else {
                    cell.setCellValue(property.toString());
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
