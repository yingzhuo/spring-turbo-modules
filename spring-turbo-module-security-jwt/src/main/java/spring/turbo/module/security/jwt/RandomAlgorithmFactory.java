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
import spring.turbo.util.RandomStringUtils;
import spring.turbo.util.RandomUtils;

/**
 * @author 应卓
 * @since 1.0.1
 */
final class RandomAlgorithmFactory implements AlgorithmFactory {

    RandomAlgorithmFactory() {
        super();
    }

    @Override
    public Algorithm create() {
        final String uuid = RandomStringUtils.randomUUID(RandomUtils.nextBoolean());
        final int n = RandomUtils.nextInt(0, 3);
        switch (n) {
            case 0:
                return Algorithm.HMAC256(uuid);
            case 1:
                return Algorithm.HMAC384(uuid);
            default:
                return Algorithm.HMAC512(uuid);
        }
    }

}
