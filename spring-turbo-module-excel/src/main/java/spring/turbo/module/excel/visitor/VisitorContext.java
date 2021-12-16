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
import spring.turbo.lang.Immutable;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Immutable
@Deprecated
public final class VisitorContext implements Serializable {

    private final Resource resource;
    private final Workbook workbook;
    private final Sheet sheet;
    private final Row row;

    public VisitorContext(Resource resource, Workbook workbook) {
        this(resource, workbook, null, null);
    }

    public VisitorContext(Resource resource, Workbook workbook, Sheet sheet, Row row) {
        this.resource = resource;
        this.workbook = workbook;
        this.sheet = sheet;
        this.row = row;
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

}
