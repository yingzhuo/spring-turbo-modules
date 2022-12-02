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
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.SelectorSetImpl;
import spring.turbo.util.Asserts;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author 应卓
 * @since 2.0.1
 */
public class SelectorSetFormatter implements Formatter<SelectorSet>, InitializingBean {

    private String separatorBetweenSelectors = "@@";
    private SelectorFormatter selectorFormatter;
    private boolean ignoreErrorIfUnableToParse = false;
    private boolean ignoreErrorIfUnableToPrint = false;

    /**
     * 构造方法
     */
    public SelectorSetFormatter() {
        super();
    }

    @Override
    public SelectorSet parse(String text, Locale locale) throws ParseException {
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
        // TODO
        return object.toString();
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
