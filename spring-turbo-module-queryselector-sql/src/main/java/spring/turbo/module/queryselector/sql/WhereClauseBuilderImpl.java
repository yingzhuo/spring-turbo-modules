/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.queryselector.sql;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.sql.exception.SQLBuildingException;
import spring.turbo.util.*;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.1.2
 */
public class WhereClauseBuilderImpl implements WhereClauseBuilder {

    private static final Class<WhereClauseBuilderImpl> THIS_TYPE = WhereClauseBuilderImpl.class;
    private static final String TEMPLATE_NAME = THIS_TYPE.getSimpleName() + ".ftl";
    private static final String TEMPLATE_CLASS_PATH;

    static {
        String path = ClassUtils.getPackageName(WhereClauseBuilderImpl.class)
                .replaceAll("\\.", StringPool.SLASH);
        if (!StringUtils.startsWith(path, StringPool.SLASH)) {
            path = StringPool.SLASH + path;
        }
        if (!StringUtils.endsWith(path, StringPool.SLASH)) {
            path = path + StringPool.SLASH;
        }
        TEMPLATE_CLASS_PATH = path;
    }

    private final Map<String, String> itemNameTableColumnMap;
    private final Configuration freemarkerConfiguration;

    /**
     * 构造方法
     */
    public WhereClauseBuilderImpl() {
        this(Collections.emptyMap());
    }

    /**
     * 构造方法
     *
     * @param itemNameTableColumnMap item名称于数据库字段映射关系
     */
    public WhereClauseBuilderImpl(Map<String, String> itemNameTableColumnMap) {
        Asserts.notNull(itemNameTableColumnMap);
        this.itemNameTableColumnMap = itemNameTableColumnMap;

        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), TEMPLATE_CLASS_PATH));
        cfg.setAPIBuiltinEnabled(true);
        cfg.setCacheStorage(NullCacheStorage.INSTANCE);
        this.freemarkerConfiguration = cfg;
    }

    @Override
    public String apply(SelectorSet selectors, Map<String, String> itemNameTableColumnMap) {
        try {
            final Map<String, String> map = new HashMap<>();
            map.putAll(this.itemNameTableColumnMap);
            map.putAll(itemNameTableColumnMap);

            final StringObjectMap data = StringObjectMap.newInstance()
                    .add("selectorList", selectors.toList())
                    .add("itemNameTableColumnMap", Collections.unmodifiableMap(map));

            final StringWriter writer = new StringWriter();
            final Template template = this.freemarkerConfiguration.getTemplate(TEMPLATE_NAME);
            template.process(data, writer);
            return writer.toString()
                    .replaceAll("\n", StringPool.EMPTY)     // 消除换行
                    .replaceAll("[ ]+", StringPool.SPACE);  // 连续多个空格替换成一个空格
        } catch (Exception e) {
            throw new SQLBuildingException(e.getMessage(), e);
        }
    }

}