/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.qrcode;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.lang.Nullable;

import java.awt.image.BufferedImage;

/**
 * QRCode生成器默认实现
 *
 * @author 应卓
 * @since 1.0.0
 */
public interface QRCodeGenerator {

    public BufferedImage generate(String content);

    public BufferedImage generate(String content, @Nullable Logo logo);

    public BufferedImage generate(String content, Logo logo, @Nullable ErrorCorrectionLevel errorCorrectionLevel);

    public BufferedImage generate(String content, Logo logo, @Nullable ErrorCorrectionLevel errorCorrectionLevel, int size);

}
