/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.factory;

import spring.turbo.module.jwt.JsonWebTokenData;
import spring.turbo.module.jwt.signer.JsonWebTokenSigner;
import spring.turbo.util.Base64;
import spring.turbo.util.StringFormatter;

import java.util.SortedMap;

import static spring.turbo.util.CharsetPool.UTF_8;

/**
 * @author 应卓
 * @since 3.1.1
 */
public abstract class AbstractJsonWebTokenFactory implements JsonWebTokenFactory {

    private final JsonWebTokenSigner signer;

    public AbstractJsonWebTokenFactory(JsonWebTokenSigner signer) {
        this.signer = signer;
    }

    @Override
    public String apply(JsonWebTokenData data) {
        // override header alg
        data.headerAlgorithm(signer.getAlgorithm());

        // add header typ
        if (!data.getHeaderMap().containsKey(JsonWebTokenData.HEADER_TYPE)) {
            data.headerType("JWT");
        }

        var header = headerToJson(data.getHeaderMap());
        header = Base64.encodeWithoutPadding(header.getBytes(UTF_8));

        var payload = payloadToJson(data.getPayloadMap());
        payload = Base64.encodeWithoutPadding(payload.getBytes(UTF_8));

        var sign = signer.sign(header, payload);

        return StringFormatter.format("{}.{}.{}", header, payload, sign);
    }

    protected abstract String headerToJson(SortedMap<String, Object> headers);

    protected abstract String payloadToJson(SortedMap<String, Object> payload);

}
