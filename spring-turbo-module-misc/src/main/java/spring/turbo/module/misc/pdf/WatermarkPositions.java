/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.pdf;

import spring.turbo.lang.Immutable;
import spring.turbo.util.Asserts;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 水印位置
 *
 * @author 应卓
 * @see Position
 * @see WatermarkPositions#builder()
 * @see WatermarkPositions#DEFAULT
 * @since 1.2.0
 */
public class WatermarkPositions implements Iterable<WatermarkPositions.Position> {

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

    /**
     * 创建器
     */
    static class Builder implements spring.turbo.bean.Builder<WatermarkPositions> {

        private final List<Position> list = new LinkedList<>();

        private Builder() {
        }

        public Builder add(int x, int y, int r) {
            this.list.add(new Position(x, y, r));
            return this;
        }

        public Builder clear() {
            this.list.clear();
            return this;
        }

        @Override
        public WatermarkPositions build() {
            return list.isEmpty() ? DEFAULT : new WatermarkPositions(this.list);
        }
    }    public static final WatermarkPositions DEFAULT = builder()
            .add(300, 250, 30)
            .add(300, 450, 30)
            .add(300, 650, 30)
            .build();

    @Immutable
    public static class Position {

        /**
         * X坐标
         */
        private final int x;

        /**
         * Y坐标
         */
        private final int y;

        /**
         * 旋转角度
         */
        private final int rotation;

        public Position(int x, int y, int rotation) {
            Asserts.isTrue(x >= 0);
            Asserts.isTrue(y >= 0);
            Asserts.isTrue(rotation >= 0);
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getRotation() {
            return rotation;
        }
    }




}
