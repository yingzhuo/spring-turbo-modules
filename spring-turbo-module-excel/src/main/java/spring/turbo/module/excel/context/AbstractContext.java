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
import spring.turbo.lang.Immutable;
import spring.turbo.module.excel.ProcessPayload;

import java.io.Serializable;

/**
 * @param <T> valueObject类型
 * @author 应卓
 * @since 1.0.0
 */
@Immutable
abstract class AbstractContext<T> implements Serializable {

    private final ProcessPayload payload;
    private final Resource resource;
    private final Workbook workbook;
    private final Sheet sheet;
    private final Row row;
    private final T valueObject;

    public AbstractContext(ProcessPayload payload, Resource resource, Workbook workbook, Sheet sheet, Row row, T valueObject) {
        this.payload = payload;
        this.resource = resource;
        this.workbook = workbook;
        this.sheet = sheet;
        this.row = row;
        this.valueObject = valueObject;
    }

    public ProcessPayload getPayload() {
        return payload;
    }

    public Resource getResource() {
        return resource;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public Row getRow() {
        return row;
    }

    public T getValueObject() {
        return valueObject;
    }
}
