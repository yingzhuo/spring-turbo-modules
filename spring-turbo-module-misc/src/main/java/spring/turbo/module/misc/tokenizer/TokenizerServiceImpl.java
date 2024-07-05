package spring.turbo.module.misc.tokenizer;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import org.springframework.lang.Nullable;
import spring.turbo.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 简易分词服务实现类
 *
 * @author 应卓
 * @since 3.1.1
 */
public class TokenizerServiceImpl implements TokenizerService {

    private final TokenizerEngine engine = TokenizerUtil.createEngine();

    @Override
    public List<String> parse(@Nullable String text) {
        if (StringUtils.isBlank(text)) {
            return List.of();
        }

        Result result = engine.parse(text);

        List<Word> words = new LinkedList<>();
        for (Word word : result) {
            words.add(word);
        }

        return words.stream().map(Word::getText).toList();
    }

}
