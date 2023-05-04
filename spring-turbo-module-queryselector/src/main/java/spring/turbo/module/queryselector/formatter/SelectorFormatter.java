/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.formatter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.format.Formatter;
import spring.turbo.module.queryselector.DataType;
import spring.turbo.module.queryselector.LogicType;
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.SelectorImpl;
import spring.turbo.util.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link Selector} 专属 {@link Formatter}
 *
 * @author 应卓
 *
 * @see SelectorSetFormatter
 *
 * @since 2.0.1
 */
public class SelectorFormatter implements Formatter<Selector>, InitializingBean {

    private String separatorInSelector = "#";
    private String separatorInRange = "<==>";
    private String separatorInSet = "'";
    private String datePattern = "yyyy-MM-dd";
    private String datetimePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 构造方法
     */
    public SelectorFormatter() {
        super();
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.hasText(this.separatorInSelector);
        Asserts.hasText(this.separatorInRange);
        Asserts.hasText(this.separatorInSet);
        Asserts.hasText(this.datePattern);
        Asserts.hasText(this.datetimePattern);
    }

    @Override
    public Selector parse(String text, Locale locale) throws ParseException {
        var selectorOption = doResolveSelector(text);
        if (selectorOption.isPresent()) {
            return selectorOption.get();
        } else {
            final String msg = StringFormatter.format("\"{}\" can not parsed as selector", text);
            throw new ParseException(msg, 0);
        }
    }

    @Override
    public String print(Selector object, Locale locale) {
        final StringBuilder builder = new StringBuilder();
        builder.append(object.getItem());
        builder.append(this.separatorInSelector);
        builder.append(object.getLogicType());
        builder.append(this.separatorInSelector);
        builder.append(object.getDataType());

        switch (object.getLogicType()) {
        case IS:
        case NOT:
            switch (object.getDataType()) {
            case STRING:
                builder.append(object.getSimpleValue().toString());
                break;
            case NUMBER:
                builder.append(object.getSimpleValue().toString());
                break;
            case DATE:
                builder.append(DateUtils.format((Date) object.getSimpleValue(), this.datePattern));
                break;
            case DATETIME:
                builder.append(DateUtils.format((Date) object.getSimpleValue(), this.datetimePattern));
                break;
            }
            break;
        case IN_RANGE:
        case NOT_IN_RANGE:
            switch (object.getDataType()) {
            case STRING:
                break;
            case NUMBER:
                builder.append(object.getRangeValue().getRequiredA());
                builder.append(this.separatorInRange);
                builder.append(object.getRangeValue().getRequiredB());
                break;
            case DATE:
                builder.append(DateUtils.format((Date) object.getRangeValue().getRequiredA(), this.datePattern));
                builder.append(this.separatorInRange);
                builder.append(DateUtils.format((Date) object.getRangeValue().getRequiredB(), this.datePattern));
                break;
            case DATETIME:
                builder.append(DateUtils.format((Date) object.getRangeValue().getRequiredA(), this.datetimePattern));
                builder.append(this.separatorInRange);
                builder.append(DateUtils.format((Date) object.getRangeValue().getRequiredB(), this.datetimePattern));
                break;
            }
            break;
        case IN_SET:
        case NOT_IN_SET:
            switch (object.getDataType()) {
            case STRING:
                builder.append(StringUtils.nullSafeJoin(object.getSetValue(), this.separatorInSet));
                break;
            case NUMBER:
                builder.append(StringUtils.nullSafeJoin(object.getSetValue(), this.separatorInSet));
                break;
            case DATE:
                Set<String> dateStringSet = object.getSetValue().stream()
                        .map(d -> DateUtils.format((Date) d, datePattern)).collect(Collectors.toSet());
                builder.append(StringUtils.nullSafeJoin(dateStringSet, separatorInSet));
                break;
            case DATETIME:
                Set<String> datetimeStringSet = object.getSetValue().stream()
                        .map(d -> DateUtils.format((Date) d, datetimePattern)).collect(Collectors.toSet());
                builder.append(StringUtils.nullSafeJoin(datetimeStringSet, separatorInSet));
                break;
            }
        }

        return builder.toString();
    }

    private Optional<Selector> doResolveSelector(String text) {
        final String[] array = text.split(this.separatorInSelector);

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

        switch (logicType) {
        case IS:
        case NOT:
            switch (dataType) {
            case STRING:
                return Optional.of(new SelectorImpl(name, logicType, dataType, other));
            case NUMBER:
                return Optional.of(
                        new SelectorImpl(name, logicType, dataType, NumberParseUtils.parse(other, BigDecimal.class)));
            case DATE:
                return Optional
                        .of(new SelectorImpl(name, logicType, dataType, DateParseUtils.parse(other, this.datePattern)));
            case DATETIME:
                return Optional.of(
                        new SelectorImpl(name, logicType, dataType, DateParseUtils.parse(other, this.datetimePattern)));
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
                return Optional.of(new SelectorImpl(name, logicType, dataType, null,
                        NumberParseUtils.parse(leftString, BigDecimal.class),
                        NumberParseUtils.parse(rightString, BigDecimal.class), null));
            case DATE:
                return Optional.of(new SelectorImpl(name, logicType, dataType, null,
                        DateParseUtils.parse(leftString, this.datePattern),
                        DateParseUtils.parse(rightString, this.datePattern), null));
            case DATETIME:
                return Optional.of(new SelectorImpl(name, logicType, dataType, null,
                        DateParseUtils.parse(leftString, this.datetimePattern),
                        DateParseUtils.parse(rightString, this.datetimePattern), null));
            }
        case IN_SET:
        case NOT_IN_SET:

            final String[] elements = other.split(this.separatorInSet);

            switch (dataType) {
            case STRING:
                return Optional.of(new SelectorImpl(name, logicType, dataType, null, null, null,
                        Stream.of(elements).collect(Collectors.toSet())));
            case NUMBER:
                return Optional.of(new SelectorImpl(name, logicType, dataType, null, null, null, Stream.of(elements)
                        .map(it -> NumberParseUtils.parse(it, BigDecimal.class)).collect(Collectors.toSet())));
            case DATE:
                return Optional.of(new SelectorImpl(name, logicType, dataType, null, null, null, Stream.of(elements)
                        .map(it -> DateParseUtils.parse(it, this.datePattern)).collect(Collectors.toSet())));
            case DATETIME:
                return Optional.of(new SelectorImpl(name, logicType, dataType, null, null, null, Stream.of(elements)
                        .map(it -> DateParseUtils.parse(it, this.datetimePattern)).collect(Collectors.toSet())));
            }
        }

        return Optional.empty();
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

}
