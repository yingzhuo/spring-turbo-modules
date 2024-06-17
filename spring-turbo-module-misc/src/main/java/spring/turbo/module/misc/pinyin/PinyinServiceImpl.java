/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.pinyin;

import cn.hutool.extra.pinyin.PinyinUtil;
import org.springframework.lang.Nullable;
import spring.turbo.util.Asserts;

import static java.util.Objects.requireNonNullElse;
import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 * @since 3.1.0
 */
public class PinyinServiceImpl implements PinyinService {

    @Override
    public String getPinyin(String text, @Nullable String separator) {
        Asserts.hasText(text, "text is null or blank");
        return PinyinUtil.getPinyin(text, requireNonNullElse(separator, EMPTY));
    }

    @Override
    public String getFirstLetter(String text, @Nullable String separator) {
        Asserts.hasText(text, "text is null or blank");
        return PinyinUtil.getFirstLetter(text, requireNonNullElse(separator, EMPTY));
    }

}
