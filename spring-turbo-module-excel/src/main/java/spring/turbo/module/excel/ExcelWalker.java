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
import spring.turbo.module.excel.function.RowPredicate;
import spring.turbo.module.excel.function.RowPredicateFactories;
import spring.turbo.module.excel.function.SheetPredicate;
import spring.turbo.module.excel.function.SheetPredicateFactories;
import spring.turbo.util.Asserts;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class ExcelWalker {

    private Resource resource;
    private ExcelType type;
    private SheetPredicate sheetPredicate;
    private RowPredicate rowPredicate;
    private ExcelWalkerInterceptor interceptor;
    private Supplier<WalkingPayload> payloadSupplier;

    private ExcelWalker() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void walk() {
        try {
            doWalk();
        } catch (AbortError e) {
            // nop
        }
    }

    private void doWalk() {
        final WalkingPayload WalkingPayload = Optional.ofNullable(payloadSupplier.get()).orElse(null);

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

            interceptor.onWorkbook(wb, WalkingPayload);

            for (Sheet sheet : wb) {
                if (sheetPredicate.test(sheet)) {
                    interceptor.onSheet(wb, sheet, WalkingPayload);
                    for (Row row : sheet) {
                        if (rowPredicate.test(sheet, row)) {
                            interceptor.onRow(wb, sheet, row, WalkingPayload);
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
        private ExcelWalkerInterceptor interceptor = ExcelWalkerInterceptor.getDefault();
        private Supplier<WalkingPayload> payloadSupplier = WalkingPayload::newInstance;

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

        public Builder payloadSupplier(Supplier<WalkingPayload> payloadSupplier) {
            Asserts.notNull(payloadSupplier);
            this.payloadSupplier = payloadSupplier;
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
            w.payloadSupplier = payloadSupplier;
            return w;
        }
    }

}
