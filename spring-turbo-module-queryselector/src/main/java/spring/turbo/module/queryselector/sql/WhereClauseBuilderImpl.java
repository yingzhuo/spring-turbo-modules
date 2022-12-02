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
import freemarker.core.HTMLOutputFormat;
import freemarker.core.PlainTextOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.Selector;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.sql.exception.SQLBuildingException;
import spring.turbo.util.ClassPathDirUtils;
import spring.turbo.util.StringPool;
import spring.turbo.util.collection.StringObjectMap;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 应卓
 * @since 2.0.1
 */
public class WhereClauseBuilderImpl implements WhereClauseBuilder {

    private static final Class<WhereClauseBuilderImpl> THIS_TYPE = WhereClauseBuilderImpl.class;
    private static final String TEMPLATE_NAME = THIS_TYPE.getSimpleName() + ".ftl";
    private static final String TEMPLATE_CLASS_PATH = ClassPathDirUtils.getClassPathDir(THIS_TYPE);

    private final Map<String, String> itemNameToTableColumnMappings;
    private final Configuration freemarkerConfiguration;

    /**
     * 构造方法
     */
    public WhereClauseBuilderImpl() {
        this(null);
    }

    /**
     * 构造方法
     *
     * @param mappings item名称于数据库字段映射关系
     */
    public WhereClauseBuilderImpl(@Nullable Map<String, String> mappings) {
        this.freemarkerConfiguration = createFreemarkerConfiguration();
        this.itemNameToTableColumnMappings = Objects.requireNonNullElse(mappings, Collections.emptyMap());
    }

    private Configuration createFreemarkerConfiguration() {
        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setTemplateLoader(new ClassTemplateLoader(THIS_TYPE, TEMPLATE_CLASS_PATH));
        cfg.setAPIBuiltinEnabled(true);
        cfg.setCacheStorage(NullCacheStorage.INSTANCE);
        return cfg;
    }

    @Override
    public String apply(@Nullable SelectorSet selectors, Map<String, String> itemNameToTableColumnMap) {
        if (selectors == null || selectors.isEmpty()) {
            return " (1 = 1) ";
        }

        try {
            final Map<String, String> map = new HashMap<>();
            map.putAll(createItemNameMap(selectors));
            map.putAll(this.itemNameToTableColumnMappings);
            map.putAll(itemNameToTableColumnMap);

            final StringObjectMap data = StringObjectMap.newInstance()
                    .add("itemNameToTableColumnMap", Collections.unmodifiableMap(map))
                    .add("selectorList", selectors.toList());

            final StringWriter writer = new StringWriter();
            final Template template = this.freemarkerConfiguration.getTemplate(TEMPLATE_NAME);
            template.process(data, writer);
            return formatSql(writer.toString());
        } catch (Exception e) {
            throw new SQLBuildingException(e.getMessage(), e);
        }
    }

    private String formatSql(String sql) {
        return sql.replaceAll("\n", StringPool.EMPTY)   // 消除换行
                .replaceAll("[ ]+", StringPool.SPACE);  // 连续多个空格替换成一个空格
    }

    private Map<String, String> createItemNameMap(SelectorSet selectors) {
        Map<String, String> ret = new HashMap<>();
        for (Selector selector : selectors) {
            ret.put(selector.getItem().getName(), selector.getItem().getName());
        }
        return ret;
    }
}
