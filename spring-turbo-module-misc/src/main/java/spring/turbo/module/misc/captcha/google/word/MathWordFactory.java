/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.google.word;

import spring.turbo.util.RandomUtils;
import spring.turbo.util.StringFormatter;

/**
 * @author 应卓
 * @since 1.0.9
 */
public class MathWordFactory implements WordFactory {

    public MathWordFactory() {
        super();
    }

    @Override
    public Word getNextWord() {
        final int left = RandomUtils.nextInt(1, 100);
        final int right = RandomUtils.nextInt(1, 100);
        final boolean op = RandomUtils.nextBoolean();

        final Word word = new Word();
        word.setStringForDrawing(getStringForDrawing(left, op, right));
        word.setStringForValidation(getStringForValidation(left, op, right));
        return word;
    }

    // true -> +
    // false -> -
    private String getStringForDrawing(int left, boolean op, int right) {
        return StringFormatter.format("{} {} {}", left, op ? "+" : "-", right);
    }

    // true -> +
    // false -> -
    private String getStringForValidation(int left, boolean op, int right) {
        int value = op ? left + right : left - right;
        return String.valueOf(value);
    }

}
