/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.pinyin;

import org.springframework.lang.Nullable;

/**
 * @author 应卓
 * @since 3.1.0
 */
public interface PinyinService {

    public default String getPinyin(String text) {
        return getPinyin(text, null);
    }

    public String getPinyin(String text, @Nullable String separator);

    public default String getFirstLetter(String text) {
        return getFirstLetter(text, null);
    }

    public String getFirstLetter(String text, @Nullable String separator);

}
