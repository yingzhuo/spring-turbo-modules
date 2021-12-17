/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.visitor;

import org.springframework.core.io.Resource;
import org.springframework.validation.BindingResult;
import spring.turbo.module.excel.ExcelType;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.reader.Batch;
import spring.turbo.module.excel.reader.ExitPolicy;

/**
 * @author 应卓
 * @since 1.0.0
 */
public interface BatchVisitor<T> {

    public default void onResourceOpeningError(Resource resource, ExcelType excelType, String password, ProcessPayload payload) {
    }

    public default void beforeProcessing(ProcessingContext context, ProcessPayload payload) {
    }

    public default void afterProcessed(ProcessPayload payload) {
    }

    public default boolean shouldAbort(ProcessPayload payload) {
        return false;
    }

    public default void onInvalidValueObject(ProcessingContext context, ProcessPayload payload, Object vo, BindingResult bindingResult) {
    }

    public default void onValidValueObject(ProcessingContext context, ProcessPayload payload, Batch<T> batch) {
    }

    public default ExitPolicy onError(ProcessingContext context, ProcessPayload payload, Throwable throwable) {
        return ExitPolicy.CONTINUE;
    }

    public default void onAbort(ProcessPayload payload) {
    }

}
