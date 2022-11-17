/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.csv.vistor;

import org.springframework.validation.BindingResult;
import spring.turbo.bean.valueobject.Batch;
import spring.turbo.bean.valueobject.ProcessPayload;

/**
 * @param <T> ValueObject泛型
 * @author 应卓
 * @since 1.0.9
 */
public interface BatchVisitor<T> {

    public default void onInvalidValueObject(ProcessingContext context, ProcessPayload payload, Object vo, BindingResult bindingResult) {
    }

    public default void onValidValueObject(ProcessingContext context, ProcessPayload payload, Batch<T> batch) {
    }

    public default void onError(ProcessingContext context, ProcessPayload payload, Throwable throwable) {
    }

}
