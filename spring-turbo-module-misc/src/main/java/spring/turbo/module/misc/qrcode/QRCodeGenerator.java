package spring.turbo.module.misc.qrcode;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.lang.Nullable;

import java.awt.image.BufferedImage;

/**
 * QRCode生成器默认实现
 *
 * @author 应卓
 * @see spring.turbo.io.ImageUtils
 * @since 1.0.0
 */
public interface QRCodeGenerator {

    public BufferedImage generate(String content);

    public BufferedImage generate(String content, @Nullable Logo logo);

    public BufferedImage generate(String content, Logo logo, @Nullable ErrorCorrectionLevel errorCorrectionLevel);

    public BufferedImage generate(String content, Logo logo, @Nullable ErrorCorrectionLevel errorCorrectionLevel,
                                  int size);

}
