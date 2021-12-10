/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.visitor;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindingResult;
import spring.turbo.module.excel.ProcessPayload;
import spring.turbo.module.excel.reader.ExitPolicy;

/**
 * @author 应卓
 * @since 1.0.0
 */
public interface Visitor {

    public default void beforeProcessing(Resource resource, Workbook workbook, ProcessPayload payload) {
    }

    public default void onValidValueObject(Resource resource, Workbook workbook, Sheet sheet, Row row, ProcessPayload payload, Object valueObject) {
    }

    public default void onInvalidValueObject(Resource resource, Workbook workbook, Sheet sheet, Row row, ProcessPayload payload, Object vo, BindingResult bindingResult) {
    }

    public default ExitPolicy onError(Resource resource, Workbook workbook, Sheet sheet, Row row, ProcessPayload payload, Throwable throwable) {
        return ExitPolicy.CONTINUE;
    }

    public default void afterProcessing(Resource resource, Workbook workbook, ProcessPayload payload) {
    }

}
