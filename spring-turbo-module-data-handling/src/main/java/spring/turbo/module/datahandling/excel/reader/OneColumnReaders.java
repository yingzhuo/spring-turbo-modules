/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.excel.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import spring.turbo.io.CloseUtils;
import spring.turbo.module.datahandling.excel.ExcelType;
import spring.turbo.module.datahandling.excel.cellparser.CellParser;
import spring.turbo.module.datahandling.excel.cellparser.DefaultCellParser;
import spring.turbo.module.datahandling.excel.function.RowPredicate;
import spring.turbo.module.datahandling.excel.function.RowPredicateFactories;
import spring.turbo.util.Asserts;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 应卓
 * @since 1.0.1
 */
public class OneColumnReaders {

    private static final int DEFAULT_BATCH_SIZE = 1024;
    private static final CellParser CELL_PARSER = new DefaultCellParser();

    private OneColumnReaders() {
        super();
    }

    public static List<String> read(@NonNull Resource resource, @NonNull ExcelType type, @Nullable String password, int sheetIndex, int columnIndex) {
        return read(resource, type, password, sheetIndex, columnIndex, null);
    }

    public static List<String> read(@NonNull Resource resource, @NonNull ExcelType type, @Nullable String password, int sheetIndex, int columnIndex, @Nullable Set<Integer> excludeSheetIndexes) {
        Asserts.notNull(resource);
        Asserts.notNull(type);

        final List<String> list = new ArrayList<>(DEFAULT_BATCH_SIZE);
        WorkbookAndFileSystem workbookAndFileSystem = null;

        RowPredicate exclude = getRowPredicate(sheetIndex, excludeSheetIndexes);

        try {
            workbookAndFileSystem = WorkbookResourceUtils.createWorkbook(type, resource, password);
            final Workbook workbook = workbookAndFileSystem.getWorkbook();

            Sheet sheet = getSheet(workbook, sheetIndex);
            if (sheet != null) {
                for (Row row : sheet) {
                    if (exclude.test(sheet, row)) {
                        continue;
                    }

                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        list.add(CELL_PARSER.convert(cell));
                    }
                }
            }

            CloseUtils.closeQuietly(workbookAndFileSystem);
            CloseUtils.closeQuietly(resource);
            return list;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        } finally {
            CloseUtils.closeQuietly(workbookAndFileSystem);
        }

    }

    private static RowPredicate getRowPredicate(int sheetIndex, Set<Integer> excludeSheetIndexes) {
        if (CollectionUtils.isEmpty(excludeSheetIndexes)) {
            return RowPredicateFactories.alwaysFalse();
        } else {
            return RowPredicateFactories.indexInSet(sheetIndex, excludeSheetIndexes.toArray(new Integer[0]));
        }
    }

    @Nullable
    private static Sheet getSheet(Workbook workbook, int sheetIndex) {
        try {
            return workbook.getSheetAt(sheetIndex);
        } catch (Exception e) {
            return null;
        }
    }

}
