/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.turbo.lang.Mutable;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Mutable
@Getter
@Setter
@NoArgsConstructor
public final class HeaderInfo implements Serializable {

    private String sheetName;
    private int sheetIndex;
    private int rowIndex;
    private int firstCellIndex;
    private String[] data;

}
