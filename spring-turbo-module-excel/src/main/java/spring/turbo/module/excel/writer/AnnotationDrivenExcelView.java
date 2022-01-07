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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;
import spring.turbo.bean.valueobject.ValueObjectGetter;
import spring.turbo.bean.valueobject.ValueObjectUtils;
import spring.turbo.core.AnnotationUtils;
import spring.turbo.lang.Beta;
import spring.turbo.module.excel.filter.ValueObjectFilter;
import spring.turbo.module.excel.style.StyleProvider;
import spring.turbo.module.excel.writer.annotation.Header;
import spring.turbo.module.excel.writer.annotation.*;
import spring.turbo.util.Asserts;
import spring.turbo.util.InstanceCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static spring.turbo.util.CollectionUtils.nullSafeAddAll;

/**
 * 元注释驱动的ExcelView
 *
 * @author 应卓
 * @see org.springframework.web.servlet.View
 * @see org.springframework.web.servlet.ViewResolver
 * @see org.springframework.web.servlet.view.BeanNameViewResolver
 * @see Filename
 * @see SheetName
 * @see Header
 * @see spring.turbo.bean.valueobject.Alias
 * @see Filter
 * @see GlobalDatePattern
 * @see HeaderStyle
 * @see DataStyle
 * @since 1.0.6
 */
@Beta
public class AnnotationDrivenExcelView<T> extends AbstractXlsxStreamingView {

    private static final String DEFAULT_SHEET_NAME = "0";
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final int DEFAULT_OFFSET = 0;

    private final Class<T> valueObjectType;
    private final ValueObjectCollectionProvider<T> valueObjectCollectionProvider;
    private final DateFormat dateFormat;
    private InstanceCache instanceCache = InstanceCache.newInstance();

    public AnnotationDrivenExcelView(@NonNull ApplicationContext applicationContext, @NonNull Class<T> valueObjectType, @NonNull String modelsKey) {
        this(applicationContext, valueObjectType, ValueObjectCollectionProvider.getByModelsKey(modelsKey));
    }

    public AnnotationDrivenExcelView(@NonNull ApplicationContext applicationContext, @NonNull Class<T> valueObjectType, @NonNull ValueObjectCollectionProvider<T> valueObjectCollectionProvider) {
        Asserts.notNull(applicationContext);
        Asserts.notNull(valueObjectType);
        Asserts.notNull(valueObjectCollectionProvider);
        this.configApplicationContext(applicationContext);
        this.valueObjectType = valueObjectType;
        this.valueObjectCollectionProvider = valueObjectCollectionProvider;
        this.dateFormat = getDateFormat();
    }

    private void configApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.instanceCache = InstanceCache.newInstance(applicationContext);
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> models, Workbook workbook, HttpServletRequest request, HttpServletResponse response) {
        // TODO: 春节做完
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        final String sheetName = getSheetName();
        final int offset = getOffset();
        final List<String> header = getHeader();
        final Sheet sheet = workbook.createSheet(sheetName);
        final String filename = getFilename();
        final StyleProvider headerStyleProvider = getHeaderProvider();
        final StyleProvider dataStyleProvider = getDataProvider();

        // 尝试设置文件名
        if (filename != null) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename);
        }

        doWriteHeader(workbook, sheet,
                header, offset,
                Optional.ofNullable(headerStyleProvider).map(p -> p.getStyle(workbook)).orElse(null)
        );

        // 获取原始数据
        Collection<T> collection = getDataCollection(models);
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }

        // 过滤数据
        final ValueObjectFilter<T> filter = getValueObjectFilter();
        if (filter != null) {
            collection = collection.stream().filter(filter::filter).collect(Collectors.toList());
        }

        doWriteData(workbook, sheet,
                collection,
                header, offset,
                Optional.ofNullable(dataStyleProvider).map(p -> p.getStyle(workbook)).orElse(null)
        );
    }

    @Nullable
    private Object getPropertyValue(Map<String, String> aliasMap, ValueObjectGetter getter, String headerName) {
        final String propertyName = aliasMap.get(headerName);
        if (propertyName == null) {
            return null;
        }
        return getter.get(propertyName);
    }

    @Nullable
    private Collection<T> getDataCollection(Map<String, Object> models) {
        return valueObjectCollectionProvider.apply(models, valueObjectType);
    }

    @Nullable
    private ValueObjectFilter<T> getValueObjectFilter() {
        final Filter annotation = AnnotationUtils.findAnnotation(valueObjectType, Filter.class);
        return annotation == null ? null : instanceCache.findOrCreate(annotation.type());
    }

    @NonNull
    private List<String> getHeader() {
        final List<String> list = new ArrayList<>();
        final Header headerAnnotation = AnnotationUtils.findAnnotation(valueObjectType, Header.class);
        if (headerAnnotation != null) {
            nullSafeAddAll(list, headerAnnotation.value());
        }
        return Collections.unmodifiableList(list);
    }

    @NonNull
    private String getSheetName() {
        final SheetName annotation = AnnotationUtils.findAnnotation(valueObjectType, SheetName.class);
        return Optional.ofNullable(annotation).map(SheetName::value).orElse(DEFAULT_SHEET_NAME);
    }

    @NonNull
    private CellStyle createDefaultCellStyleForHeader(Workbook workbook) {
        final Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);

        final CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

    @NonNull
    private CellStyle createDefaultCellStyleForData(Workbook workbook) {
        final Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(false);

        final CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private int getOffset() {
        final Header annotation = AnnotationUtils.findAnnotation(valueObjectType, Header.class);
        return Optional.ofNullable(annotation).map(Header::offset).orElse(DEFAULT_OFFSET);
    }

    @NonNull
    private DateFormat getDateFormat() {
        final GlobalDatePattern annotation = AnnotationUtils.findAnnotation(valueObjectType, GlobalDatePattern.class);
        return Optional.ofNullable(annotation)
                .map(GlobalDatePattern::value)
                .map(SimpleDateFormat::new)
                .orElse(new SimpleDateFormat(DEFAULT_DATE_PATTERN));
    }

    @Nullable
    private String getFilename() {
        final Filename annotation = AnnotationUtils.findAnnotation(valueObjectType, Filename.class);
        return Optional.ofNullable(annotation)
                .map(Filename::value)
                .map(filename -> new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
                .orElse(null);
    }

    @Nullable
    private StyleProvider getHeaderProvider() {
        final HeaderStyle annotation = AnnotationUtils.findAnnotation(valueObjectType, HeaderStyle.class);
        if (annotation == null) {
            return null;
        }
        return instanceCache.findOrCreate(annotation.type());
    }

    @Nullable
    private StyleProvider getDataProvider() {
        final DataStyle annotation = AnnotationUtils.findAnnotation(valueObjectType, DataStyle.class);
        if (annotation == null) {
            return null;
        }
        return instanceCache.findOrCreate(annotation.type());
    }

    // ----------------------------------------------------------------------------------------------------------------

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
                             @NonNull Collection<T> collection,
                             @NonNull List<String> header,
                             int offset,
                             @Nullable CellStyle dataCellStyle) {

        final Map<String, String> aliasMap = ValueObjectUtils.getAliases(valueObjectType);
        dataCellStyle = Optional.ofNullable(dataCellStyle).orElse(createDefaultCellStyleForData(workbook));

        int indexOfData = -1;
        for (Object vo : collection) {
            indexOfData++;

            final int rowIndex = indexOfData + 1;
            final ValueObjectGetter getter = new ValueObjectGetter(vo);
            final Row row = sheet.createRow(rowIndex);

            int cellIndex = offset - 1;
            for (String headerName : header) {
                cellIndex++;
                Object property = getPropertyValue(aliasMap, getter, headerName);
                if (property == null) {
                    continue;
                }
                Cell cell = row.createCell(cellIndex);
                cell.setCellStyle(dataCellStyle);
                if (property instanceof Date) {
                    final String dateString = formatDate((Date) property);
                    if (dateString != null) {
                        cell.setCellValue(dateFormat.format((Date) property));
                    }
                } else {
                    cell.setCellValue(property.toString());
                }
            }
        }
    }

    @Nullable
    private String formatDate(@Nullable Date property) {
        try {
            return dateFormat.format(property);
        } catch (Exception e) {
            return null;
        }
    }

}
