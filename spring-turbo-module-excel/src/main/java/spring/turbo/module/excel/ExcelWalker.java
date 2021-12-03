/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import spring.turbo.lang.Immutable;
import spring.turbo.module.excel.func.RowPredicate;
import spring.turbo.module.excel.func.RowPredicateFactories;
import spring.turbo.module.excel.func.SheetPredicate;
import spring.turbo.module.excel.func.SheetPredicateFactories;
import spring.turbo.util.Asserts;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Immutable
public final class ExcelWalker {

    private Resource resource;
    private ExcelType type;
    private SheetPredicate sheetPredicate;
    private RowPredicate rowPredicate;
    private ExcelWalkerInterceptor interceptor;

    private ExcelWalker() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void walk() {
        InputStream inputStream = null;
        Workbook wb = null;
        try {
            inputStream = resource.getInputStream();

            switch (type) {
                case HSSF:
                    wb = new HSSFWorkbook(inputStream);
                    break;
                case XSSF:
                    wb = new XSSFWorkbook(inputStream);
                    break;
                default:
                    throw new AssertionError();
            }

            for (Sheet sheet : wb) {
                if (sheetPredicate.test(sheet)) {
                    for (Row row : sheet) {
                        if (rowPredicate.test(sheet, row)) {
                            interceptor.onRow(wb, sheet, row);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            close(wb);
            close(inputStream);
        }
    }

    private void close(Closeable workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException ignored) {
                // nop
            }
        }
    }

    public static final class Builder {

        private ExcelType type = ExcelType.XSSF;
        private SheetPredicate sheetPredicate = SheetPredicateFactories.alwaysTrue();
        private RowPredicate rowPredicate = RowPredicateFactories.alwaysTrue();
        private ExcelWalkerInterceptor interceptor = NullExcelWalkerInterceptor.getInstance();

        private Builder() {
            super();
        }

        public Builder type(ExcelType type) {
            Asserts.notNull(type);
            this.type = type;
            return this;
        }

        public Builder sheetPredicates(SheetPredicate... predicates) {
            Asserts.notEmpty(predicates);
            Asserts.noNullElements(predicates);
            this.sheetPredicate = SheetPredicateFactories.any(predicates);
            return this;
        }

        public Builder rowPredicate(RowPredicate... predicates) {
            Asserts.notEmpty(predicates);
            Asserts.noNullElements(predicates);
            this.rowPredicate = RowPredicateFactories.any(predicates);
            return this;
        }

        public Builder interceptor(ExcelWalkerInterceptor interceptor) {
            Asserts.notNull(interceptor);
            this.interceptor = interceptor;
            return this;
        }

        public ExcelWalker build(Resource resource) {
            Asserts.notNull(resource);

            final ExcelWalker w = new ExcelWalker();
            w.resource = resource;
            w.type = this.type;
            w.sheetPredicate = this.sheetPredicate;
            w.rowPredicate = this.rowPredicate;
            w.interceptor = this.interceptor;
            return w;
        }
    }

}
