/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.resolver;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.util.Asserts;
import spring.turbo.util.SetFactories;

import java.util.Set;

/**
 * @author 应卓
 * @since 1.1.0
 */
public class StringToSelectorSetConverter implements GenericConverter {

    private static final Set<ConvertiblePair> CONVERTIBLE_TYPES = SetFactories.newUnmodifiableSet(
            new ConvertiblePair(CharSequence.class, SelectorSet.class)
    );

    private final SelectorSetResolver selectorSetResolver;

    public StringToSelectorSetConverter(SelectorSetResolver selectorSetResolver) {
        Asserts.notNull(selectorSetResolver);
        this.selectorSetResolver = selectorSetResolver;
    }

    @Nullable
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return CONVERTIBLE_TYPES;
    }

    @Nullable
    @Override
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        return selectorSetResolver.resolve(source.toString());
    }

}
