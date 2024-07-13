package spring.turbo.module.misc.pinyin;

import cn.hutool.extra.pinyin.PinyinUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import static java.util.Objects.requireNonNullElse;
import static spring.turbo.util.StringPool.EMPTY;

/**
 * @author 应卓
 * @since 3.1.0
 */
public class PinyinServiceImpl implements PinyinService {

    @Override
    public String getPinyin(String text, @Nullable String separator) {
        Assert.hasText(text, "text is null or blank");
        return PinyinUtil.getPinyin(text, requireNonNullElse(separator, EMPTY));
    }

    @Override
    public String getFirstLetter(String text, @Nullable String separator) {
        Assert.hasText(text, "text is null or blank");
        return PinyinUtil.getFirstLetter(text, requireNonNullElse(separator, EMPTY));
    }

}
