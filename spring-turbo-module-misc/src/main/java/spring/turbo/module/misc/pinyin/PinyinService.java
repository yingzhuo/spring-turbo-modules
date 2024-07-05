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
