package spring.turbo.module.misc.tokenizer;

import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 简易分词服务
 *
 * @author 应卓
 * @since 3.1.1
 */
@FunctionalInterface
public interface TokenizerService {

    public List<String> parse(@Nullable String text);

}
