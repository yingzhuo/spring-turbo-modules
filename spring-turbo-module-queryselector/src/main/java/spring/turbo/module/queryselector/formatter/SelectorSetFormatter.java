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
import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.SelectorSetImpl;
import spring.turbo.util.Asserts;
import spring.turbo.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author 应卓
 *
 * @see Selector
 * @see SelectorSet
 * @see SelectorFormatter
 *
 * @since 2.0.1
 */
public class SelectorSetFormatter implements Formatter<SelectorSet>, InitializingBean {

    private String separatorBetweenSelectors = "@@";

    private boolean ignoreErrorIfUnableToParse = false;

    private boolean ignoreErrorIfUnableToPrint = false;

    @Nullable
    private SelectorFormatter selectorFormatter;

    /**
     * 构造方法
     */
    public SelectorSetFormatter() {
        super();
    }

    @Override
    public SelectorSet parse(String text, Locale locale) throws ParseException {
        Asserts.notNull(selectorFormatter);
        List<Selector> selectorList = new ArrayList<>();
        for (String selectorString : text.split(this.separatorBetweenSelectors)) {
            try {
                final Selector selector = selectorFormatter.parse(selectorString, locale);
                selectorList.add(selector);
            } catch (ParseException e) {
                if (!ignoreErrorIfUnableToParse) {
                    throw e;
                }
            }
        }
        return new SelectorSetImpl(selectorList);
    }

    @Override
    public String print(SelectorSet object, Locale locale) {
        Asserts.notNull(selectorFormatter);
        final List<String> toStringList = new ArrayList<>();

        for (Selector selector : object) {
            try {
                toStringList.add(selectorFormatter.print(selector, locale));
            } catch (Exception e) {
                if (!ignoreErrorIfUnableToPrint) {
                    throw e;
                }
            }
        }
        return StringUtils.nullSafeJoin(toStringList, this.separatorBetweenSelectors);
    }

    @Override
    public void afterPropertiesSet() {
        Asserts.hasText(this.separatorBetweenSelectors);
        Asserts.notNull(this.selectorFormatter);
    }

    public void setSeparatorBetweenSelectors(String separatorBetweenSelectors) {
        this.separatorBetweenSelectors = separatorBetweenSelectors;
    }

    public void setSelectorFormatter(SelectorFormatter selectorFormatter) {
        this.selectorFormatter = selectorFormatter;
    }

    public void setIgnoreErrorIfUnableToParse(boolean ignoreErrorIfUnableToParse) {
        this.ignoreErrorIfUnableToParse = ignoreErrorIfUnableToParse;
    }

    public void setIgnoreErrorIfUnableToPrint(boolean ignoreErrorIfUnableToPrint) {
        this.ignoreErrorIfUnableToPrint = ignoreErrorIfUnableToPrint;
    }

}
