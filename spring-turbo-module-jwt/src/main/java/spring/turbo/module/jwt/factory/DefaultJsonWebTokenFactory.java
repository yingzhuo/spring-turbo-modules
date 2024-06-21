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
import spring.turbo.module.jwt.signer.JsonWebTokenSigner;

import java.util.Objects;
import java.util.SortedMap;

/**
 * JWT令牌生成器的默认实现
 *
 * @author 应卓
 * @since 3.1.1
 */
public class DefaultJsonWebTokenFactory extends AbstractJsonWebTokenFactory implements JsonWebTokenFactory {

    private final ObjectMapper objectMapper;

    /**
     * 构造方法
     *
     * @param signer 签名器
     */
    public DefaultJsonWebTokenFactory(JsonWebTokenSigner signer) {
        this(signer, null);
    }

    /**
     * 构造方法
     *
     * @param signer       签名器
     * @param objectMapper ObjectMapper实例
     */
    public DefaultJsonWebTokenFactory(JsonWebTokenSigner signer, @Nullable ObjectMapper objectMapper) {
        super(signer);
        this.objectMapper = Objects.requireNonNullElseGet(objectMapper, ObjectMapper::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String headerToJson(SortedMap<String, Object> headers) {
        return toJson(headers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String payloadToJson(SortedMap<String, Object> payload) {
        return toJson(payload);
    }

    private String toJson(SortedMap<String, Object> map) {
        try {
            return objectMapper.writer()
                    .withoutFeatures(SerializationFeature.INDENT_OUTPUT)
                    .writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

}
