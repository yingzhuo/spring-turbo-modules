/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.security.hutool.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.lang.NonNull;
import spring.turbo.module.security.jwt.AlgorithmFactory;

/**
 * 国密算法 (SM2)
 *
 * @author 应卓
 * @since 1.0.2
 */
public final class SM2AlgorithmFactory implements AlgorithmFactory {

    private final String publicKey;
    private final String privateKey;

    public SM2AlgorithmFactory(@NonNull String publicKey, @NonNull String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Override
    public Algorithm create() {
        return new SM2Algorithm(publicKey, privateKey);
    }

}
