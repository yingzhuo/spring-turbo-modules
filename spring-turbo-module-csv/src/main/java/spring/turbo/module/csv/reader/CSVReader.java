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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import spring.turbo.bean.valueobject.*;
import spring.turbo.io.LineIterator;
import spring.turbo.io.ResourceOption;
import spring.turbo.io.ResourceOptions;
import spring.turbo.module.csv.vistor.BatchVisitor;
import spring.turbo.module.csv.vistor.NullBatchVisitor;
import spring.turbo.module.csv.vistor.ProcessingContext;
import spring.turbo.util.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static spring.turbo.util.StringPool.COMMA;

/**
 * @author 应卓
 * @since 1.0.9
 */
public final class CSVReader<T> {

    private Resource resource;
    private Charset charset;
    private HeaderConfig headerConfig;
    private BatchVisitor<T> visitor;
    private Class<T> valueObjectType;
    private Supplier<T> valueObjectSupplier;
    private String[] headerIsUse;
    private Batch<T> batch;
    private ConversionService conversionService;
    private List<Validator> validators;
    private ValueObjectFilter<T> valueObjectFilter;

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
        final ResourceOption resourceOption = ResourceOptions.of(this.resource);

        if (resourceOption.isAbsent()) {
            throw new UncheckedIOException(new IOException("cannot open resource"));
        }

        final LineIterator lineIterator = resourceOption.getLineIterator(this.charset);

        try {
            doRead(lineIterator, Optional.ofNullable(payload).orElse(ProcessPayload.newInstance()));
        } finally {
            CloseUtils.closeQuietly(lineIterator);
            CloseUtils.closeQuietly(resource);
        }
    }

    private void doRead(@NonNull LineIterator lineIterator, @NonNull ProcessPayload payload) {
        int lineNumber = -1;

        while (lineIterator.hasNext()) {
            lineNumber++;
            String line = lineIterator.next();
            String[] header = getHeader(line, lineNumber);

            if (header == null) {
                continue;
            }

            final String[] dataArray = line.split(COMMA);

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
            if (valueObjectFilter != null && !valueObjectFilter.filter(vo)) {
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

    @Nullable
    private String[] getHeader(String line, int lineNumber) {
        if (headerIsUse != null) {
            return headerIsUse;
        }

        if (headerConfig.isFixed()) {
            this.headerIsUse = mergeWithAlias(headerConfig.getHeader());
            return headerIsUse;
        }

        if (lineNumber == headerConfig.getIndex()) {
            this.headerIsUse = mergeWithAlias(line.split(COMMA));
            return headerIsUse;
        } else {
            return null;
        }
    }

    public String[] mergeWithAlias(String[] array) {
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
        private ConversionService conversionService = new DefaultFormattingConversionService();
        private Resource resource;
        private Charset charset = CharsetPool.UTF_8;
        private BatchVisitor<T> visitor = NullBatchVisitor.getInstance();
        private HeaderConfig headerConfig;
        private int batchSize = 1000;
        private ValueObjectFilter<T> valueObjectFilter;

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
            return reader;
        }
    }

}
