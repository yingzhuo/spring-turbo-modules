package spring.turbo.module.misc.captcha.google.size;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class SingleSizeFactory implements SizeFactory {

    private int width = 160;
    private int height = 70;

    public SingleSizeFactory() {
    }

    public SingleSizeFactory(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
