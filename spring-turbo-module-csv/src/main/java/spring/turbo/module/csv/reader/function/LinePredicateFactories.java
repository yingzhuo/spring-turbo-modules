/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.csv.reader.function;

import spring.turbo.util.Asserts;
import spring.turbo.util.SetFactories;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author 应卓
 * @see LinePredicate
 * @since 1.0.13
 */
public final class LinePredicateFactories {

    /**
     * 私有构造方法
     */
    private LinePredicateFactories() {
        super();
    }

    public static LinePredicate alwaysTrue() {
        return (lineNumber, line) -> true;
    }

    public static LinePredicate alwaysFalse() {
        return (lineNumber, line) -> false;
    }

    public static LinePredicate any(LinePredicate... predicates) {
        return new Any(predicates);
    }

    public static LinePredicate all(LinePredicate... predicates) {
        return new All(predicates);
    }

    public static LinePredicate or(LinePredicate p1, LinePredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return any(p1, p2);
    }

    public static LinePredicate and(LinePredicate p1, LinePredicate p2) {
        Asserts.notNull(p1);
        Asserts.notNull(p2);
        return all(p1, p2);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public static LinePredicate lineNumbers(Integer... lineNumbers) {
        final Set<Integer> lineNumberSet = SetFactories.newUnmodifiableSet(lineNumbers);
        return (lineNumber, line) -> lineNumberSet.contains(lineNumber);
    }

    // -----------------------------------------------------------------------------------------------------------------

    private static class All implements LinePredicate {
        private final List<LinePredicate> predicates;

        public All(LinePredicate... predicates) {
            Asserts.notNull(predicates);
            Asserts.noNullElements(predicates);
            this.predicates = Arrays.asList(predicates);
        }

        @Override
        public boolean test(int lineNumber, String line) {
            for (final LinePredicate predicate : this.predicates) {
                if (!predicate.test(lineNumber, line)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class Any implements LinePredicate {
        private final List<LinePredicate> predicates;

        public Any(LinePredicate... predicates) {
            Asserts.notNull(predicates);
            Asserts.noNullElements(predicates);
            this.predicates = Arrays.asList(predicates);
        }

        @Override
        public boolean test(int lineNumber, String line) {
            for (final LinePredicate predicate : this.predicates) {
                if (predicate.test(lineNumber, line)) {
                    return true;
                }
            }
            return false;
        }
    }

}
