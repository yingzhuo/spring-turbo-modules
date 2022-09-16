/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.pdf.watermak;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 应卓
 * @since 1.2.0
 */
public class WatermarkPositions implements Serializable, Iterable<WatermarkPosition> {

    private final List<WatermarkPosition> list;

    public static WatermarkPositions.Builder builder() {
        return new Builder();
    }

    private WatermarkPositions(List<WatermarkPosition> list) {
        this.list = list;
    }

    @Override
    public Iterator<WatermarkPosition> iterator() {
        return list.listIterator();
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    static class Builder {

        private final List<WatermarkPosition> list = new LinkedList<>();

        private Builder() {
        }

        public Builder add(int x, int y, int r) {
            this.list.add(new WatermarkPosition(x, y, r));
            return this;
        }

        public WatermarkPositions build() {
            return new WatermarkPositions(this.list);
        }
    }

}
