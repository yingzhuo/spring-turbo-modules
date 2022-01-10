/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.writer;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;
import spring.turbo.util.CloseUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * {@link Workbook} 读写工具
 *
 * @author 应卓
 * @since 1.0.7
 */
public final class WorkbookIO {

    /**
     * 私有构造方法
     */
    private WorkbookIO() {
        super();
    }

    public static void write(@NonNull Workbook workbook, @NonNull OutputStream outputStream) {
        Asserts.notNull(workbook);
        Asserts.notNull(outputStream);

        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            CloseUtils.closeQuietly(outputStream);
            CloseUtils.closeQuietly(workbook);
        }
    }

    public static void write(@NonNull Workbook workbook, @NonNull File file) {
        Asserts.notNull(workbook);
        Asserts.notNull(file);

        try {
            createIfNotExists(file);
            write(workbook, new FileOutputStream(file));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            CloseUtils.closeQuietly(workbook);
        }
    }

    public static void write(@NonNull Workbook workbook, @NonNull Path path) {
        Asserts.notNull(workbook);
        Asserts.notNull(path);
        write(workbook, path.toFile());
    }

    public static Path writeToTempFile(@NonNull Workbook workbook, @Nullable String prefix, @Nullable String suffix) {
        Asserts.notNull(workbook);

        try {
            Path path = Files.createTempFile(prefix, suffix).normalize();
            createIfNotExists(path.toFile());
            write(workbook, path);
            return path;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createIfNotExists(File file) {
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

}
