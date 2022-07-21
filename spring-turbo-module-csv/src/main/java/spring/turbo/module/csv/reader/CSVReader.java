/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.csv.reader;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import spring.turbo.bean.valueobject.*;
import spring.turbo.io.CloseUtils;
import spring.turbo.io.LineIterator;
import spring.turbo.io.ResourceOption;
import spring.turbo.io.ResourceOptions;
import spring.turbo.module.csv.reader.function.*;
import spring.turbo.module.csv.vistor.BatchVisitor;
import spring.turbo.module.csv.vistor.NullBatchVisitor;
import spring.turbo.module.csv.vistor.ProcessingContext;
import spring.turbo.util.ArrayUtils;
import spring.turbo.util.Asserts;
import spring.turbo.util.CollectionUtils;
import spring.turbo.util.ReflectionObjectSupplier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Supplier;

import static spring.turbo.util.CharsetPool.UTF_8;
import static spring.turbo.util.StringPool.COMMA;

/**
 * @author 应卓
 * @see #builder(Class)
 * @since 1.0.9
 */
public final class CSVReader<T> {

    @Nullable
    private Resource resource;

    @Nullable
    private Charset charset;

    @Nullable
    private HeaderConfig headerConfig;

    @Nullable
    private BatchVisitor<T> visitor;

    @Nullable
    private Class<T> valueObjectType;

    @Nullable
    private Supplier<T> valueObjectSupplier;

    @Nullable
    private Batch<T> batch;

    @Nullable
    private ConversionService conversionService;

    @Nullable
    private List<Validator> validators;

    @Nullable
    private LinePredicate skipLinePredicate;

    @Nullable
    private HeaderNormalizer headerNormalizer;

    @Nullable
    private GlobalValueNormalizer globalValueNormalizer;

    private Map<Integer, ValueNormalizer> normalizerMap = new HashMap<>();

    @Nullable
    private ValueObjectFilter<T> valueObjectFilter;

    @Nullable
    private String[] headerInUse;

    /**
     * 私有构造方法
     */
    private CSVReader() {
        super();
    }

    public static <T> Builder<T> builder(Class<T> valueObject) {
        return new Builder<>(valueObject);
    }

    public void read() {
        read(null);
    }

    public void read(@Nullable ProcessPayload payload) {
        Asserts.notNull(this.resource);
        Asserts.notNull(this.charset);

        final ResourceOption resourceOption = ResourceOptions.of(this.resource);

        if (resourceOption.isAbsent()) {
            throw new UncheckedIOException(new IOException("cannot open resource"));
        }

        final LineIterator lineIterator = resourceOption.getLineIterator(this.charset);

        try {
            doRead(lineIterator, Optional.ofNullable(payload).orElse(ProcessPayload.newInstance()));
        } finally {
            CloseUtils.closeQuietly(lineIterator);
            CloseUtils.closeQuietly(this.resource);
        }
    }

    private void doRead(LineIterator lineIterator, ProcessPayload payload) {
        Asserts.notNull(lineIterator);
        Asserts.notNull(payload);
        Asserts.notNull(this.skipLinePredicate);
        Asserts.notNull(this.valueObjectSupplier);
        Asserts.notNull(this.conversionService);
        Asserts.notNull(this.validators);
        Asserts.notNull(this.batch);
        Asserts.notNull(this.resource);
        Asserts.notNull(this.charset);
        Asserts.notNull(this.visitor);
        Asserts.notNull(this.headerConfig);

        int lineNumber = -1;

        while (lineIterator.hasNext()) {
            lineNumber++;
            String line = lineIterator.next();
            String[] header = getHeader(line, lineNumber);

            if (skipLinePredicate.test(lineNumber, line) || (!headerConfig.isFixed() && headerConfig.getIndex() == lineNumber)) {
                continue;
            }

            if (header == null) {
                continue;
            }

            final String[] dataArray = normalizeValue(line.split(COMMA));

            // 数据实际无意义
            if (ArrayUtils.doseNotContainsAnyElements(dataArray)) {
                continue;
            }

            // 创建一个vo对象
            T vo = valueObjectSupplier.get();

            // 完成数据绑定和验证
            final BindingResult bindingResult = DataBinding.newInstance()
                    .valueObject(vo)
                    .conversionService(conversionService)
                    .validators(validators.toArray(new Validator[0]))
                    .data(NamedArray.builder()
                            .addNames(header)
                            .addObjects(dataArray)
                            .build())
                    .bind();

            // valueObjectFilter 过滤数据
            // 不区分vo对象是不是有绑定错误
            if (valueObjectFilter != null && !valueObjectFilter.test(vo)) {
                continue;
            }

            if (!bindingResult.hasErrors()) {
                payload.incrSuccessCount();
                batch.add(vo);

                if (batch.isFull()) {
                    try {
                        visitor.onValidValueObject(new ProcessingContext(resource), payload, batch);
                    } catch (Throwable e) {
                        payload.incrErrorCount();
                        visitor.onError(new ProcessingContext(resource), payload, e);
                    }
                    batch.clear();
                }

            } else {
                payload.incrInvalidDataCount();
                try {
                    visitor.onInvalidValueObject(new ProcessingContext(resource), payload, vo, bindingResult);
                } catch (Throwable e) {
                    payload.incrErrorCount();
                    visitor.onError(new ProcessingContext(resource), payload, e);
                }
            }
        }

        if (batch.isNotEmpty()) {
            try {
                visitor.onValidValueObject(new ProcessingContext(resource), payload, batch);
            } catch (Throwable e) {
                payload.incrErrorCount();
                visitor.onError(new ProcessingContext(resource), payload, e);
            }
            batch.clear();
        }
    }

    private String[] normalizeHeader(String[] header) {
        for (int i = 0; i < header.length; i++) {
            String c = header[i];
            if (this.headerNormalizer != null) {
                c = this.headerNormalizer.normalize(c);
            }
            header[i] = c;
        }
        return header;
    }

    private String[] normalizeValue(String[] dataArray) {
        for (int i = 0; i < dataArray.length; i++) {
            String c = dataArray[i];
            if (this.globalValueNormalizer != null) {
                c = this.globalValueNormalizer.normalize(c);
            }

            ValueNormalizer normalizer = this.normalizerMap.get(i);
            if (normalizer != null) {
                c = normalizer.normalize(c);
            }
            dataArray[i] = c;
        }

        return dataArray;
    }

    @Nullable
    private String[] getHeader(String line, int lineNumber) {
        Asserts.notNull(headerConfig);

        if (headerInUse != null) {
            return headerInUse;
        }

        if (headerConfig.isFixed()) {
            String[] configHeader = headerConfig.getHeader();
            Asserts.notNull(configHeader);
            this.headerInUse = mergeWithAlias(normalizeHeader(configHeader));
            return headerInUse;
        }

        if (lineNumber == headerConfig.getIndex()) {
            this.headerInUse = mergeWithAlias(normalizeHeader(line.split(COMMA)));
            return headerInUse;
        } else {
            return null;
        }
    }

    public String[] mergeWithAlias(String[] array) {
        Asserts.notNull(valueObjectType);
        final Map<String, String> aliasMap = ValueObjectUtils.getAliases(valueObjectType);
        for (int i = 0; i < array.length; i++) {
            String to = aliasMap.get(array[i]);
            if (to != null) {
                array[i] = to;
            }
        }
        return array;
    }

    // ----------------------------------------------------------------------------------------------------------------
    public static class Builder<T> {

        private final Class<T> valueObjectType;
        private final List<Validator> validators = new ArrayList<>();
        private final Map<Integer, ValueNormalizer> normalizerMap = new HashMap<>();
        private ConversionService conversionService = new DefaultFormattingConversionService();
        private Resource resource;
        private Charset charset = UTF_8;
        private BatchVisitor<T> visitor = NullBatchVisitor.getInstance();
        private HeaderConfig headerConfig;
        private int batchSize = 1024;
        private ValueObjectFilter<T> valueObjectFilter;
        private LinePredicate skipLinePredicate = LinePredicateFactories.alwaysFalse();
        private HeaderNormalizer headerNormalizer = NullHeaderNormalizer.getInstance();
        private GlobalValueNormalizer globalValueNormalizer = NullValueNormalizer.getInstance();

        /**
         * 私有构造方法
         */
        private Builder(Class<T> valueObjectType) {
            this.valueObjectType = valueObjectType;
        }

        public Builder<T> resource(Resource resource) {
            this.resource = resource;
            return this;
        }

        public Builder<T> charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder<T> charset(String charset) {
            return charset(Charset.forName(charset));
        }

        public Builder<T> visitor(BatchVisitor<T> visitor) {
            this.visitor = visitor;
            return this;
        }

        public Builder<T> header(int index) {
            this.headerConfig = new HeaderConfig(index);
            return this;
        }

        public Builder<T> header(String[] header) {
            this.headerConfig = new HeaderConfig(header);
            return this;
        }

        public Builder<T> conversionService(ConversionService conversionService) {
            this.conversionService = conversionService;
            return this;
        }

        public Builder<T> validator(Validator... validators) {
            CollectionUtils.nullSafeAddAll(this.validators, validators);
            return this;
        }

        public Builder<T> batchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public Builder<T> valueObjectFilter(ValueObjectFilter<T> filter) {
            this.valueObjectFilter = filter;
            return this;
        }

        public Builder<T> skipLinePredicate(LinePredicate predicate) {
            this.skipLinePredicate = predicate;
            return this;
        }

        public Builder<T> headerNormalizer(HeaderNormalizer headerNormalizer) {
            this.headerNormalizer = headerNormalizer;
            return this;
        }

        public Builder<T> globalValueNormalizer(GlobalValueNormalizer normalizer) {
            this.globalValueNormalizer = normalizer;
            return this;
        }

        public Builder<T> addNormalizer(Integer columnIndex, ValueNormalizer normalizer) {
            this.normalizerMap.put(columnIndex, normalizer);
            return this;
        }

        public CSVReader<T> build() {
            Asserts.notNull(valueObjectType);
            Asserts.notNull(resource);
            Asserts.notNull(charset);
            Asserts.notNull(visitor);
            Asserts.notNull(headerConfig);

            if (CollectionUtils.isEmpty(validators)) {
                validators.add(NullValidator.getInstance());
            }

            final CSVReader<T> reader = new CSVReader<>();
            reader.resource = this.resource;
            reader.charset = this.charset;
            reader.valueObjectType = this.valueObjectType;
            reader.valueObjectSupplier = new ReflectionObjectSupplier<>(this.valueObjectType);
            reader.headerConfig = this.headerConfig;
            reader.visitor = this.visitor;
            reader.conversionService = this.conversionService;
            reader.validators = this.validators;
            reader.batch = new Batch<>(this.batchSize);
            reader.valueObjectFilter = this.valueObjectFilter;
            reader.skipLinePredicate = this.skipLinePredicate;
            reader.globalValueNormalizer = this.globalValueNormalizer;
            reader.normalizerMap = this.normalizerMap;
            reader.headerNormalizer = this.headerNormalizer;
            return reader;
        }
    }

}
