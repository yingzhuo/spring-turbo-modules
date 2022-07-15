/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.resolver;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.*;
import spring.turbo.module.queryselector.exception.SelectorResolvingException;
import spring.turbo.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code SelectorSet}解析器默认实现
 *
 * @author 应卓
 * @since 1.1.0
 */
public class SelectorSetResolverImpl implements SelectorSetResolver, InitializingBean {

    private String separatorBetweenSelectors = "@@";
    private String separatorInSelector = "#";
    private String separatorInRange = "<==>";
    private String separatorInSet = "'";
    private String datePattern = "yyyy-MM-dd";
    private String datetimePattern = "yyyy-MM-dd HH:mm:ss";
    private boolean skipErrorIfUnableToResolve = false;

    /**
     * 构造方法
     */
    public SelectorSetResolverImpl() {
        super();
    }

    @Override
    public Optional<SelectorSet> resolve(@Nullable String string) {
        if (StringUtils.isBlank(string)) {
            return Optional.empty();
        }

        final List<Selector> selectors = new ArrayList<>();

        for (String selectorString : resolveSelectorStrings(string)) {
            if (StringUtils.isBlank(selectorString)) {
                continue;
            }

            final Optional<Selector> selectorOption = this.resolveSelector(selectorString);
            selectorOption.ifPresent(selectors::add);
        }

        return Optional.of(new SelectorSetImpl(selectors));
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.hasText(this.separatorBetweenSelectors);
        Asserts.hasText(this.separatorInSelector);
        Asserts.hasText(this.separatorInRange);
        Asserts.hasText(this.separatorInSet);
    }

    private List<String> resolveSelectorStrings(String string) {
        final String[] array = string.split(this.separatorBetweenSelectors);
        return StreamFactories.newStream(array)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private Optional<Selector> resolveSelector(String string) {
        try {
            return doResolveSelector(string);
        } catch (Exception e) {
            if (this.skipErrorIfUnableToResolve) {
                return Optional.empty();
            } else {
                throw new SelectorResolvingException(e.getMessage());
            }
        }
    }

    private Optional<Selector> doResolveSelector(String string) {
        final String[] array = string.split(this.separatorInSelector);

        // index 0: name
        final String name = array[0];
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        // index 1: logic type
        final LogicType logicType = EnumUtils.getEnumIgnoreCase(LogicType.class, array[1]);
        if (logicType == null) {
            return Optional.empty();
        }

        // index 2: data type
        final DataType dataType = EnumUtils.getEnumIgnoreCase(DataType.class, array[2]);
        if (dataType == null) {
            return Optional.empty();
        }

        // index 3: other
        final String other = array[3];
        if (StringUtils.isBlank(other)) {
            return Optional.empty();
        }

        Selector selector = null;
        switch (logicType) {
            case IS:
            case NOT:
                switch (dataType) {
                    case STRING:
                        return Optional.of(new SelectorImpl(name, logicType, dataType, other));
                    case NUMBER:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, NumberParseUtils.parse(other, BigDecimal.class))
                        );
                    case DATE:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, DateParseUtils.parse(other, this.datePattern))
                        );
                    case DATETIME:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, DateParseUtils.parse(other, this.datetimePattern))
                        );
                }
            case IN_RANGE:
            case NOT_IN_RANGE:
                final String[] leftAndRightString = other.split(this.separatorInRange);

                // 不能区分左右时无意义
                if (leftAndRightString.length != 2) {
                    break;
                }

                final String leftString = leftAndRightString[0];
                final String rightString = leftAndRightString[1];

                switch (dataType) {
                    case STRING:
                        // 无意义
                        break;
                    case NUMBER:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, null,
                                        NumberParseUtils.parse(leftString, BigDecimal.class),
                                        NumberParseUtils.parse(rightString, BigDecimal.class),
                                        null)
                        );
                    case DATE:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, null,
                                        DateParseUtils.parse(leftString, this.datePattern),
                                        DateParseUtils.parse(rightString, this.datePattern),
                                        null)
                        );
                    case DATETIME:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, null,
                                        DateParseUtils.parse(leftString, this.datetimePattern),
                                        DateParseUtils.parse(rightString, this.datetimePattern),
                                        null)
                        );
                }
            case IN_SET:
            case NOT_IN_SET:

                final String[] elements = other.split(this.separatorInSet);

                switch (dataType) {
                    case STRING:
                        // 无意义是
                        return Optional.empty();
                    case NUMBER:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, null, null, null,
                                        Stream.of(elements)
                                                .map(it -> NumberParseUtils.parse(it, BigDecimal.class))
                                                .collect(Collectors.toSet())
                                )
                        );
                    case DATE:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, null, null, null,
                                        Stream.of(elements)
                                                .map(it -> DateParseUtils.parse(it, this.datePattern))
                                                .collect(Collectors.toSet())
                                )
                        );
                    case DATETIME:
                        return Optional.of(
                                new SelectorImpl(name, logicType, dataType, null, null, null,
                                        Stream.of(elements)
                                                .map(it -> DateParseUtils.parse(it, this.datetimePattern))
                                                .collect(Collectors.toSet())
                                )
                        );
                }
        }

        return Optional.ofNullable(selector);
    }

    public void setSeparatorBetweenSelectors(String separatorBetweenSelectors) {
        this.separatorBetweenSelectors = separatorBetweenSelectors;
    }

    public void setSeparatorInSelector(String separatorInSelector) {
        this.separatorInSelector = separatorInSelector;
    }

    public void setSeparatorInRange(String separatorInRange) {
        this.separatorInRange = separatorInRange;
    }

    public void setSeparatorInSet(String separatorInSet) {
        this.separatorInSet = separatorInSet;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public void setDatetimePattern(String datetimePattern) {
        this.datetimePattern = datetimePattern;
    }

    public void setSkipErrorIfUnableToResolve(boolean skipErrorIfUnableToResolve) {
        this.skipErrorIfUnableToResolve = skipErrorIfUnableToResolve;
    }

}
