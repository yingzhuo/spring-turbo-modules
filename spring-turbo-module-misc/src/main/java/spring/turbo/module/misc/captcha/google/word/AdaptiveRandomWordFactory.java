/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   ___ | '_ | '__| | '_  / _` || || | | | '__| '_  / _
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|__, ||_| __,_|_|  |_.__/ ___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.captcha.google.word;

import java.util.Random;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class AdaptiveRandomWordFactory extends RandomWordFactory {

    protected String wideCharacters;

    public AdaptiveRandomWordFactory() {
        this.characters = "absdegkmnopwx23456789";
        this.wideCharacters = "mw";
        this.minLength = 6;
        this.maxLength = 6;
    }

    public void setWideCharacters(String wideCharacters) {
        this.wideCharacters = wideCharacters;
    }

    @Override
    public Word getNextWord() {
        final Random rnd = new Random();
        final StringBuilder sb = new StringBuilder();
        final StringBuilder chars = new StringBuilder(characters);
        int l = minLength + (maxLength > minLength ? rnd.nextInt(maxLength - minLength) : 0);
        for (int i = 0; i < l; i++) {
            int j = rnd.nextInt(chars.length());
            char c = chars.charAt(j);
            if (wideCharacters.indexOf(c) != -1) {
                for (int k = 0; k < wideCharacters.length(); k++) {
                    int idx = chars.indexOf(String.valueOf(wideCharacters.charAt(k)));
                    if (idx != -1) {
                        chars.deleteCharAt(idx);
                    }
                }
            }
            sb.append(c);
        }

        final Word word = new Word();
        word.setStringForDrawing(sb.toString());
        word.setStringForValidation(sb.toString());
        return word;
    }

}
