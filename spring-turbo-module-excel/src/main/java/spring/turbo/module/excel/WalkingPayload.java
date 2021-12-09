/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel;

import org.springframework.util.LinkedMultiValueMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 应卓
 * @since 1.0.0
 */
public final class WalkingPayload
        extends LinkedMultiValueMap<String, Object>
        implements Map<String, List<Object>>, Serializable {

    public static WalkingPayload newInstance() {
        return new WalkingPayload();
    }

    private WalkingPayload() {
        super();
    }

    private long handledRow = 0L;
    private long handledRowSuccess = 0L;
    private long handledRowError = 0L;

    public long getHandledRow() {
        return handledRow;
    }

    public long getHandledRowSuccess() {
        return handledRowSuccess;
    }

    public long getHandledRowError() {
        return handledRowError;
    }

    public void incrHandledRow() {
        incrHandledRow(1L);
    }

    public void incrHandledRow(long n) {
        handledRow += n;
    }

    public void incrHandledRowSuccess() {
        incrHandledRowSuccess(1L);
    }

    public void incrHandledRowSuccess(long n) {
        handledRowSuccess += n;
    }

    public void incrHandleRowError() {
        incrHandleRowError(1L);
    }

    public void incrHandleRowError(long n) {
        handledRowError += n;
    }

}
