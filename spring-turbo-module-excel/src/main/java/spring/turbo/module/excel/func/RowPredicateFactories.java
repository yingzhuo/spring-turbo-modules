/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.func;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import spring.turbo.util.Asserts;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    public static RowPredicate ofIndex(final Integer... indexes) {
        Asserts.notEmpty(indexes);
        Asserts.noNullElements(indexes);
        return (sheet, row) -> Arrays.asList(indexes).contains(row.getRowNum());
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
