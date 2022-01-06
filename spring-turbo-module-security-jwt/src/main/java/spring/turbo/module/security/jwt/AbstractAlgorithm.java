/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import spring.turbo.util.CharPool;

/**
 * 抽象JWT签名算法
 *
 * @author 应卓
 * @since 1.0.2
 */
public abstract class AbstractAlgorithm extends Algorithm {

    private static final byte JWT_PART_SEPARATOR = (byte) CharPool.DOT;

    public AbstractAlgorithm(String name, String description) {
        super(name, description);
    }

    protected final byte[] combineHeaderAndPayload(DecodedJWT decodedJWT) {
        return combineSignByte(decodedJWT.getHeader().getBytes(), decodedJWT.getPayload().getBytes());
    }

    private byte[] combineSignByte(byte[] headerBytes, byte[] payloadBytes) {
        // header + payload
        final byte[] hash = new byte[headerBytes.length + payloadBytes.length + 1];
        System.arraycopy(headerBytes, 0, hash, 0, headerBytes.length);
        hash[headerBytes.length] = JWT_PART_SEPARATOR;
        System.arraycopy(payloadBytes, 0, hash, headerBytes.length + 1, payloadBytes.length);
        return hash;
    }

}
