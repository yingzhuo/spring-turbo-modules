/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   ___ | '_ | '__| | '_  / _` || || | | | '__| '_  / _
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|__, ||_| __,_|_|  |_.__/ ___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.word;

import java.util.Random;

/**
 * @author Piotr Piastucki
 * @since 1.0.0
 */
public class RandomWordFactory implements WordFactory {

    protected String characters;
    protected int minLength;
    protected int maxLength;

    public RandomWordFactory() {
        characters = "absdegkmnopwx23456789";
        minLength = 6;
        maxLength = 6;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getNextWord() {
        Random rnd = new Random();
        final StringBuffer sb = new StringBuffer();
        int l = minLength + (maxLength > minLength ? rnd.nextInt(maxLength - minLength) : 0);
        for (int i = 0; i < l; i++) {
            int j = rnd.nextInt(characters.length());
            sb.append(characters.charAt(j));
        }
        return sb.toString();
    }

}
