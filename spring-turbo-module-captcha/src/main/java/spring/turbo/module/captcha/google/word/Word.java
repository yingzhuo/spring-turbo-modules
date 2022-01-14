/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.word;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.8
 */
public class Word implements Serializable {

    private String stringForDrawing;
    private String stringForValidation;

    public Word() {
        super();
    }

    public String getStringForDrawing() {
        return stringForDrawing;
    }

    public void setStringForDrawing(String stringForDrawing) {
        this.stringForDrawing = stringForDrawing;
    }

    public String getStringForValidation() {
        return stringForValidation;
    }

    public void setStringForValidation(String stringForValidation) {
        this.stringForValidation = stringForValidation;
    }

}
