/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.pdf.watermak;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 水印位置(多个)
 *
 * @author 应卓
 * @see Position
 * @see WatermarkPositions#builder()
 * @see WatermarkPositions#DEFAULT
 * @since 1.2.0
 */
public class WatermarkPositions implements Serializable, Iterable<WatermarkPositions.Position> {

    public static final WatermarkPositions DEFAULT = builder()
            .add(300, 250, 30)
            .add(300, 450, 30)
            .add(300, 650, 30)
            .build();

    private final List<Position> list;

    private WatermarkPositions(List<Position> list) {
        this.list = list;
    }

    public static WatermarkPositions.Builder builder() {
        return new Builder();
    }

    @Override
    public Iterator<Position> iterator() {
        return list.listIterator();
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    static class Builder {

        private final List<Position> list = new LinkedList<>();

        private Builder() {
        }

        public Builder add(int x, int y, int r) {
            this.list.add(new Position(x, y, r));
            return this;
        }

        public WatermarkPositions build() {
            return new WatermarkPositions(this.list);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Position implements Serializable {

        /**
         * X坐标
         */
        private int x;

        /**
         * Y坐标
         */
        private int y;

        /**
         * 旋转角度
         */
        private int rotation;

    }
}
