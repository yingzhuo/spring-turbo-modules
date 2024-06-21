/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.lang.Nullable;
import spring.turbo.io.IOExceptionUtils;
import spring.turbo.module.jwt.JsonWebTokenData;
import spring.turbo.module.jwt.signer.JsonWebTokenSigner;
import spring.turbo.util.Asserts;
import spring.turbo.util.StringFormatter;

import java.util.Base64;
import java.util.Objects;

import static spring.turbo.util.CharsetPool.UTF_8;

/**
 * @author 应卓
 * @since 3.1.1
 */
public class DefaultJsonWebTokenFactory implements JsonWebTokenFactory {

    private final JsonWebTokenSigner signer;
    private final ObjectMapper objectMapper;

    public DefaultJsonWebTokenFactory(JsonWebTokenSigner signer) {
        this(signer, null);
    }

    public DefaultJsonWebTokenFactory(JsonWebTokenSigner signer, @Nullable ObjectMapper objectMapper) {
        Asserts.notNull(signer, "signer is required");

        this.signer = signer;
        this.objectMapper = Objects.requireNonNullElseGet(
                objectMapper,
                () -> {
                    var om = new ObjectMapper();
                    om.configure(SerializationFeature.INDENT_OUTPUT, false);
                    return om;
                }
        );
    }

    @Override
    public String apply(JsonWebTokenData jsonWebTokenData) {

        // override header alg
        jsonWebTokenData.headerAlgorithm(signer.getAlgorithm());

        var encoder = Base64.getUrlEncoder().withoutPadding();

        try {
            var header = objectMapper.writeValueAsString(jsonWebTokenData.getHeaderMap());
            header = encoder.encodeToString(header.getBytes(UTF_8));

            var payload = objectMapper.writeValueAsString(jsonWebTokenData.getPayloadMap());
            payload = encoder.encodeToString(payload.getBytes(UTF_8));

            var sign = signer.sign(header, payload);
            return StringFormatter.format("{}.{}.{}", header, payload, sign);
        } catch (JsonProcessingException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

}
