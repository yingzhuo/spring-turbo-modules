package spring.turbo.module.misc.captcha.google.word;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.8
 */
public class Word implements Serializable {

    private String stringForDrawing;
    private String stringForValidation;

    public Word() {
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
