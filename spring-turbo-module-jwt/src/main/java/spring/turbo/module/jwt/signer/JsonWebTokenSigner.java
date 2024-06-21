/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.jwt.signer;

/**
 * JWT签名器
 *
 * @author 应卓
 * @since 3.1.1
 */
public interface JsonWebTokenSigner {

    /**
     * 签名
     *
     * @param headerBase64  JWT头的JSON字符串的Base64表示
     * @param payloadBase64 JWT载荷的JSON字符串Base64表示
     * @return 签名结果Base64，即JWT的第三部分
     */
    public String sign(String headerBase64, String payloadBase64);

    /**
     * 验签
     *
     * @param headerBase64  JWT头的JSON字符串Base64表示
     * @param payloadBase64 JWT载荷的JSON字符串Base64表示
     * @param signBase64    被验证的签名Base64表示
     * @return 签名是否一致
     */
    public boolean verify(String headerBase64, String payloadBase64, String signBase64);

    /**
     * 获取算法名称
     *
     * @return 算法名称
     */
    public String getAlgorithm();

}
