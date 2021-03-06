/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.lang.Nullable;
import spring.turbo.io.CloseUtils;
import spring.turbo.util.Asserts;

import java.io.Closeable;
import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.1
 */
public final class WorkbookAndFileSystem implements Closeable, Serializable {

    private final Workbook workbook;
    private final POIFSFileSystem fileSystem;

    public WorkbookAndFileSystem(Workbook workbook) {
        this(workbook, null);
    }

    public WorkbookAndFileSystem(Workbook workbook, @Nullable POIFSFileSystem fileSystem) {
        Asserts.notNull(workbook);
        this.workbook = workbook;
        this.fileSystem = fileSystem;
    }

    @Override
    public void close() {
        CloseUtils.closeQuietly(workbook);
        CloseUtils.closeQuietly(fileSystem);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public POIFSFileSystem getFileSystem() {
        return fileSystem;
    }
}
