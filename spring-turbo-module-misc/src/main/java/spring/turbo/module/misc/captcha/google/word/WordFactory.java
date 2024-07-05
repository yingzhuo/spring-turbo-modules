package spring.turbo.module.misc.captcha.google.word;

/**
 * @author 应卓
 * @since 1.0.0
 */
@FunctionalInterface
public interface WordFactory {

    public Word getNextWord();

}
