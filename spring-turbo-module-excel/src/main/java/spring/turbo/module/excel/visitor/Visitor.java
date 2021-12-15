/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.visitor;

import org.springframework.validation.BindingResult;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.reader.ExitPolicy;

/**
 * @author 应卓
 * @since 1.0.0
 */
public interface Visitor {

    public default void beforeProcessing(VisitorContext context, ProcessPayload payload) {
    }

    public default boolean shouldAbort(ProcessPayload payload) {
        return false;
    }

    public default void onValidValueObject(VisitorContext context, ProcessPayload payload, Object valueObject) {
    }

    public default void onInvalidValueObject(VisitorContext context, ProcessPayload payload, Object vo, BindingResult bindingResult) {
    }

    public default ExitPolicy onError(VisitorContext context, ProcessPayload payload, Throwable throwable) {
        return ExitPolicy.CONTINUE;
    }

    public default void afterProcessing(VisitorContext context, ProcessPayload payload) {
    }

    public default void onAborted(ProcessPayload payload) {
    }

}
