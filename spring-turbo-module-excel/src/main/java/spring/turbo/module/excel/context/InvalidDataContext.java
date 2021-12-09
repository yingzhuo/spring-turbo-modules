/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.context;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindingResult;
import spring.turbo.lang.Immutable;
import spring.turbo.module.excel.ProcessPayload;

import java.io.Serializable;

/**
 * @param <T> valueObject类型
 * @author 应卓
 * @since 1.0.0
 */
@Immutable
public class InvalidDataContext<T> extends AbstractContext<T> implements Serializable {

    private final BindingResult bindingResult;

    public InvalidDataContext(ProcessPayload payload, Resource resource, Workbook workbook, Sheet sheet, Row row, T valueObject, BindingResult bindingResult) {
        super(payload, resource, workbook, sheet, row, valueObject);
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

}
