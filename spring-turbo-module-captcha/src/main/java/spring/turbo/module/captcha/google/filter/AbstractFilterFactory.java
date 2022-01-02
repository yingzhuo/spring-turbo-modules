/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.captcha.google.filter;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.List;

/**
 * @author Piotr Piastucki
 * @since 1.0.0
 */
public abstract class AbstractFilterFactory implements FilterFactory {

    protected abstract List<BufferedImageOp> getFilters();

    public BufferedImage apply(BufferedImage source) {
        BufferedImage dest = source;
        for (BufferedImageOp filter : getFilters()) {
            dest = filter.filter(dest, null);
        }
        int x = (source.getWidth() - dest.getWidth()) / 2;
        int y = (source.getHeight() - dest.getHeight()) / 2;
        source = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        source.getGraphics().drawImage(dest, x, y, null);
        return source;
    }

}
