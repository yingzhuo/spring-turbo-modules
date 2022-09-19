/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.pdf.watermak;

import spring.turbo.io.LocalFileDescriptor;
import spring.turbo.util.Asserts;

import java.io.File;
import java.nio.file.Path;

/**
 * @author 应卓
 * @since 1.2.0
 */
@FunctionalInterface
public interface WatermarkGenerator {

    /**
     * 为PDF添加水印
     *
     * @param in               输入文件
     * @param out              输出文件
     * @param watermarkContent 水印内容
     */
    public default void addWatermark(Path in, Path out, Object watermarkContent) {
        Asserts.notNull(in);
        Asserts.notNull(out);
        Asserts.notNull(watermarkContent);
        addWatermark(
                LocalFileDescriptor.of(in),
                LocalFileDescriptor.of(out),
                watermarkContent
        );
    }

    /**
     * 为PDF添加水印
     *
     * @param in               输入文件
     * @param out              输出文件
     * @param watermarkContent 水印内容
     */
    public default void addWatermark(File in, File out, Object watermarkContent) {
        Asserts.notNull(in);
        Asserts.notNull(out);
        Asserts.notNull(watermarkContent);
        addWatermark(
                LocalFileDescriptor.of(in),
                LocalFileDescriptor.of(out),
                watermarkContent
        );
    }

    /**
     * 为PDF添加水印
     *
     * @param in               输入文件
     * @param out              输出文件
     * @param watermarkContent 水印内容
     */
    public void addWatermark(LocalFileDescriptor in, LocalFileDescriptor out, Object watermarkContent);

}
