/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.excel.reader;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import spring.turbo.bean.valueobject.ProcessPayload;

/**
 * @author 应卓
 * @see spring.turbo.module.datahandling.excel.reader.annotation.Password
 * @since 1.0.4
 */
@FunctionalInterface
public interface PasswordProvider {

    @Nullable
    public String getPassword(ExcelDiscriminator discriminator, Resource resource, ProcessPayload payload);

}
