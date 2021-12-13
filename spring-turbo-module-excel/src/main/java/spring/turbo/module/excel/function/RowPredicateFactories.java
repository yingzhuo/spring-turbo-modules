/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.function;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import spring.turbo.module.excel.util.RowUtils;
import spring.turbo.module.excel.util.SheetUtils;
import spring.turbo.util.Asserts;

import java.util.*;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class RowPredicateFactories {

    private RowPredicateFactories() {
        super();
    }

    public static RowPredicate alwaysTrue() {
        return (sheet, cells) -> true;
    }

    public static RowPredicate alwaysFalse() {
        return (sheet, cells) -> false;
    }

    public static RowPredicate not(final RowPredicate p) {
        Asserts.notNull(p);
        return (sheet, row) -> !p.test(sheet, row);
    }

    public static RowPredicate or(final RowPredicate p1, final RowPredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return any(p1, p2);
    }

    public static RowPredicate and(final RowPredicate p1, final RowPredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return all(p1, p2);
    }

    public static RowPredicate xor(final RowPredicate p1, final RowPredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return (sheet, row) -> p1.test(sheet, row) ^ p2.test(sheet, row);
    }

    public static RowPredicate any(final RowPredicate... predicates) {
        Asserts.notEmpty(predicates);
        Asserts.noNullElements(predicates);
        return new Any(predicates);
    }

    public static RowPredicate all(final RowPredicate... predicates) {
        Asserts.notEmpty(predicates);
        Asserts.noNullElements(predicates);
        return new All(predicates);
    }

    public static RowPredicate indexInSet(final String sheetName, Integer... indexes) {
        Asserts.hasText(sheetName);
        Asserts.notNull(indexes);
        Asserts.noNullElements(indexes);

        final Set<Integer> set = new HashSet<>(Arrays.asList(indexes));
        return (sheet, row) -> sheetName.equals(SheetUtils.getName(sheet)) && set.contains(row.getRowNum());
    }

    public static RowPredicate indexInSet(final int sheetIndex, Integer... indexes) {
        Asserts.isTrue(sheetIndex >= 0);
        Asserts.notNull(indexes);
        Asserts.noNullElements(indexes);

        final Set<Integer> set = new HashSet<>(Arrays.asList(indexes));
        return (sheet, row) -> sheetIndex == SheetUtils.getIndex(sheet) && set.contains(row.getRowNum());
    }

    public static RowPredicate indexInRange(final String sheetName, final int minInclude, final int maxExclude) {
        Asserts.hasText(sheetName);
        Asserts.isTrue(minInclude <= maxExclude);
        return (sheet, row) -> {
            int rowIndex = row.getRowNum();
            return sheetName.equals(SheetUtils.getName(sheet)) && rowIndex >= minInclude && rowIndex < maxExclude;
        };
    }

    public static RowPredicate indexInRange(final int sheetIndex, final int minInclude, final int maxExclude) {
        Asserts.isTrue(sheetIndex >= 0);
        Asserts.isTrue(minInclude <= maxExclude);
        return (sheet, row) -> {
            int rowIndex = row.getRowNum();
            return sheetIndex == SheetUtils.getIndex(sheet) && rowIndex >= minInclude && rowIndex < maxExclude;
        };
    }

    public static RowPredicate isZeroHeight() {
        return (sheet, row) -> RowUtils.isZeroHeight(row);
    }

    public static RowPredicate isNotZeroHeight() {
        return (sheet, row) -> RowUtils.isNotZeroHeight(row);
    }

    public static RowPredicate isFormatted() {
        return (sheet, row) -> RowUtils.isFormatted(row);
    }

    public static RowPredicate isNotFormatted() {
        return (sheet, row) -> RowUtils.isNotFormatted(row);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class Any implements RowPredicate {

        private final List<RowPredicate> predicates = new LinkedList<>();

        private Any(RowPredicate... predicates) {
            Collections.addAll(this.predicates, predicates);
        }

        @Override
        public boolean test(Sheet sheet, Row row) {
            return predicates.stream().anyMatch(predicate -> predicate.test(sheet, row));
        }
    }

    private static class All implements RowPredicate {

        private final List<RowPredicate> predicates = new LinkedList<>();

        private All(RowPredicate... predicates) {
            Collections.addAll(this.predicates, predicates);
        }

        @Override
        public boolean test(Sheet sheet, Row row) {
            return predicates.stream().allMatch(predicate -> predicate.test(sheet, row));
        }
    }

}
