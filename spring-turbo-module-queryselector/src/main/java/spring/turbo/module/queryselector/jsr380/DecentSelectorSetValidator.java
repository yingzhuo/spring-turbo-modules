/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.jsr380;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.DataType;
import spring.turbo.module.queryselector.LogicType;
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 *
 * @see DecentSelectorSet
 *
 * @since 2.0.1
 */
public class DecentSelectorSetValidator implements ConstraintValidator<DecentSelectorSet, SelectorSet> {

    @Nullable
    private DecentSelectorSet annotation;

    /**
     * 构造方法
     */
    public DecentSelectorSetValidator() {
        super();
    }

    @Override
    public void initialize(DecentSelectorSet constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(@Nullable SelectorSet ss, ConstraintValidatorContext context) {
        Asserts.notNull(annotation);

        if (ss == null) {
            return true;
        }

        final int size = ss.size();
        if (size < annotation.min() || size > annotation.max()) {
            return false;
        }

        for (final Selector s : ss) {
            if (s.getLogicType() == LogicType.IN_RANGE || s.getLogicType() == LogicType.IN_SET) {
                if (s.getDataType() == DataType.STRING) {
                    return false;
                }
            }
        }
        return false;
    }

}
