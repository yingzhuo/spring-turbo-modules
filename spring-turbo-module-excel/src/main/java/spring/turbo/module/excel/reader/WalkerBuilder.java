/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Validator;
import spring.turbo.bean.Tuple;
import spring.turbo.bean.valueobject.NullValidator;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.cellparser.CellParser;
import spring.turbo.module.excel.cellparser.DefaultCellParser;
import spring.turbo.module.excel.cellparser.GlobalCellParser;
import spring.turbo.module.excel.config.AliasConfig;
import spring.turbo.module.excel.config.HeaderConfig;
import spring.turbo.module.excel.function.RowPredicate;
import spring.turbo.module.excel.function.RowPredicateFactories;
import spring.turbo.module.excel.function.SheetPredicate;
import spring.turbo.module.excel.function.SheetPredicateFactories;
import spring.turbo.module.excel.visitor.NullVisitor;
import spring.turbo.module.excel.visitor.Visitor;
import spring.turbo.util.Asserts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Deprecated
public final class WalkerBuilder {

    private final Class<?> valueObjectType;
    private final List<SheetPredicate> includeSheetPredicates = new LinkedList<>();
    private final List<RowPredicate> excludeRowPredicates = new LinkedList<>();
    private final List<Tuple<Integer, Integer, CellParser>> cellParsers = new LinkedList<>();
    private HeaderConfig headerConfig;
    private AliasConfig aliasConfig;
    private GlobalCellParser globalCellParser;
    private ProcessPayload payload;
    private ConversionService conversionService;
    private List<Validator> validators;
    private Visitor visitor;
    private String password;
    private boolean excludeAllNullRow = true;

    WalkerBuilder(Class<?> valueObjectType) {
        Asserts.notNull(valueObjectType);
        this.valueObjectType = valueObjectType;
    }

    public WalkerBuilder includeSheet(SheetPredicate... px) {
        if (px != null) {
            for (SheetPredicate p : px) {
                if (p != null) {
                    includeSheetPredicates.add(p);
                }
            }
        }
        return this;
    }

    public WalkerBuilder excludeRow(RowPredicate... px) {
        if (px != null) {
            for (RowPredicate p : px) {
                if (p != null) {
                    excludeRowPredicates.add(p);
                }
            }
        }
        return this;
    }

    public WalkerBuilder headerConfig(HeaderConfig config) {
        this.headerConfig = config;
        return this;
    }

    public WalkerBuilder aliasConfig(AliasConfig config) {
        this.aliasConfig = config;
        return this;
    }

    public WalkerBuilder globalCellParser(GlobalCellParser cellParser) {
        this.globalCellParser = cellParser;
        return this;
    }

    public WalkerBuilder addCellParser(int sheetIndex, int columnIndex, CellParser cellParser) {
        this.cellParsers.add(Tuple.of(sheetIndex, columnIndex, cellParser));
        return this;
    }

    public WalkerBuilder conversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
        return this;
    }

    public WalkerBuilder validators(List<Validator> validators) {
        if (!CollectionUtils.isEmpty(validators)) {
            if (this.validators == null) {
                this.validators = validators;
            } else {
                this.validators.addAll(validators);
            }
        }
        return this;
    }

    public WalkerBuilder visitor(Visitor visitor) {
        this.visitor = visitor;
        return this;
    }

    public WalkerBuilder payload(ProcessPayload payload) {
        this.payload = payload;
        return this;
    }

    public WalkerBuilder password(String password) {
        this.password = password;
        return this;
    }

    public WalkerBuilder excludeAllNullRow(boolean exclude) {
        this.excludeAllNullRow = exclude;
        return this;
    }

    public Walker build(Resource resource) {
        return build(ExcelType.XSSF, resource);
    }

    public Walker build(ExcelType excelType, Resource resource) {
        Asserts.notNull(excelType);
        Asserts.notNull(resource);

        return new Walker(
                excelType,
                resource,
                valueObjectType,
                CollectionUtils.isEmpty(this.includeSheetPredicates) ? SheetPredicateFactories.alwaysTrue() : SheetPredicateFactories.any(this.includeSheetPredicates.toArray(new SheetPredicate[0])),
                CollectionUtils.isEmpty(this.excludeRowPredicates) ? RowPredicateFactories.alwaysFalse() : RowPredicateFactories.any(this.excludeRowPredicates.toArray(new RowPredicate[0])),
                Optional.ofNullable(headerConfig).orElseGet(HeaderConfig::newInstance),
                Optional.ofNullable(aliasConfig).orElseGet(AliasConfig::newInstance),
                Optional.ofNullable(globalCellParser).orElseGet(DefaultCellParser::new),
                cellParsers,
                Optional.ofNullable(payload).orElseGet(ProcessPayload::newInstance),
                Optional.ofNullable(conversionService).orElseGet(DefaultFormattingConversionService::new),
                Optional.ofNullable(validators).orElse(Collections.singletonList(NullValidator.getInstance())),
                Optional.ofNullable(visitor).orElseGet(NullVisitor::getInstance),
                password,
                excludeAllNullRow
        );
    }

}
