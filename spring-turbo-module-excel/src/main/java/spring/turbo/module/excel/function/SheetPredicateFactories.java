/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.function;

import org.apache.poi.ss.usermodel.Sheet;
import spring.turbo.util.Asserts;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class SheetPredicateFactories {

    private SheetPredicateFactories() {
        super();
    }

    public static SheetPredicate alwaysTrue() {
        return sheet -> true;
    }

    public static SheetPredicate alwaysFalse() {
        return sheet -> false;
    }

    public static SheetPredicate not(final SheetPredicate p) {
        Asserts.notNull(p);
        return sheet -> !p.test(sheet);
    }

    public static SheetPredicate or(final SheetPredicate p1, final SheetPredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return any(p1, p2);
    }

    public static SheetPredicate and(final SheetPredicate p1, final SheetPredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return all(p1, p2);
    }

    public static SheetPredicate xor(final SheetPredicate p1, final SheetPredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return sheet -> p1.test(sheet) ^ p2.test(sheet);
    }

    public static SheetPredicate any(final SheetPredicate... predicates) {
        Asserts.notEmpty(predicates);
        Asserts.noNullElements(predicates);
        return new Any(predicates);
    }

    public static SheetPredicate all(final SheetPredicate... predicates) {
        Asserts.notEmpty(predicates);
        Asserts.noNullElements(predicates);
        return new All(predicates);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static SheetPredicate ofName(final String... sheetNames) {
        Asserts.notEmpty(sheetNames);
        Asserts.noNullElements(sheetNames);
        return sheet -> Stream.of(sheetNames).anyMatch(name -> sheet.getSheetName().equals(name));
    }

    public static SheetPredicate ofNamePattern(final String regex) {
        Asserts.hasText(regex);
        return sheet -> sheet.getSheetName().matches(regex);
    }

    public static SheetPredicate ofIndex(final Integer... indexes) {
        Asserts.notEmpty(indexes);
        Asserts.noNullElements(indexes);
        return sheet -> {
            int i = sheet.getWorkbook().getSheetIndex(sheet);
            return Arrays.asList(indexes).contains(i);
        };
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class Any implements SheetPredicate {

        private final List<SheetPredicate> predicates = new LinkedList<>();

        private Any(SheetPredicate... predicates) {
            Collections.addAll(this.predicates, predicates);
        }

        @Override
        public boolean test(Sheet sheet) {
            return predicates.stream().anyMatch(predicate -> predicate.test(sheet));
        }
    }

    private static class All implements SheetPredicate {

        private final List<SheetPredicate> predicates = new LinkedList<>();

        private All(SheetPredicate... predicates) {
            Collections.addAll(this.predicates, predicates);
        }

        @Override
        public boolean test(Sheet sheet) {
            return predicates.stream().allMatch(predicate -> predicate.test(sheet));
        }
    }

}
