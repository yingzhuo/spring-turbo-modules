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
import org.springframework.lang.Nullable;
import spring.turbo.module.queryselector.SelectorSet;
import spring.turbo.module.queryselector.sql.exception.SQLBuildingException;
import spring.turbo.util.ClassPathDirUtils;
import spring.turbo.util.StringPool;
import spring.turbo.util.collection.StringObjectMap;

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
    private static final String TEMPLATE_CLASS_PATH = ClassPathDirUtils.getClassPathDir(THIS_TYPE);

    private final Map<String, String> itemNameToTableColumnMap;
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
     * @param itemNameToTableColumnMap item名称于数据库字段映射关系
     */
    public WhereClauseBuilderImpl(@Nullable Map<String, String> itemNameToTableColumnMap) {
        itemNameToTableColumnMap = itemNameToTableColumnMap != null ? itemNameToTableColumnMap : Collections.emptyMap();
        this.itemNameToTableColumnMap = itemNameToTableColumnMap;

        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setTemplateLoader(new ClassTemplateLoader(THIS_TYPE, TEMPLATE_CLASS_PATH));
        cfg.setAPIBuiltinEnabled(true);
        cfg.setCacheStorage(NullCacheStorage.INSTANCE);
        this.freemarkerConfiguration = cfg;
    }

    @Override
    public String apply(@Nullable SelectorSet selectors, Map<String, String> itemNameToTableColumnMap) {
        if (selectors == null || selectors.isEmpty()) {
            return " (1 = 1) ";
        }

        try {
            final Map<String, String> map = new HashMap<>();
            map.putAll(this.itemNameToTableColumnMap);
            map.putAll(itemNameToTableColumnMap);

            final StringObjectMap data = StringObjectMap.newInstance()
                    .add("selectorList", selectors.toList())
                    .add("itemNameToTableColumnMap", Collections.unmodifiableMap(map));

            final StringWriter writer = new StringWriter();
            final Template template = this.freemarkerConfiguration.getTemplate(TEMPLATE_NAME);
            template.process(data, writer);
            return formatSql(writer.toString());
        } catch (Exception e) {
            throw new SQLBuildingException(e.getMessage(), e);
        }
    }

    private String formatSql(String sql) {
        return sql.replaceAll("\n", StringPool.EMPTY)     // 消除换行
                .replaceAll("[ ]+", StringPool.SPACE);  // 连续多个空格替换成一个空格
    }

}
