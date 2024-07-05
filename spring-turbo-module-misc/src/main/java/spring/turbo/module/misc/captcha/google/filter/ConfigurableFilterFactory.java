package spring.turbo.module.misc.captcha.google.filter;

import java.awt.image.BufferedImageOp;
import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
public class ConfigurableFilterFactory extends AbstractFilterFactory {

    private List<BufferedImageOp> filters;

    @Override
    public List<BufferedImageOp> getFilters() {
        return filters;
    }

    public void setFilters(List<BufferedImageOp> filters) {
        this.filters = filters;
    }

}
