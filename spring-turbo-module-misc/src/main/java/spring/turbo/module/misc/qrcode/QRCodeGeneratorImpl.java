/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.lang.Nullable;
import spring.turbo.util.CharsetPool;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * QRCode生成器默认实现
 *
 * @author 应卓
 *
 * @see QRCodeGenerator
 *
 * @since 1.0.0
 */
public class QRCodeGeneratorImpl implements QRCodeGenerator {

    private static final String CHARSET = CharsetPool.UTF_8_VALUE;

    private ErrorCorrectionLevel defaultErrorCorrectionLevel = ErrorCorrectionLevel.H;
    private int defaultSize = 200;
    private int defaultMargin = 1;

    @Override
    public BufferedImage generate(String content) {
        return generate(content, null);
    }

    @Override
    public BufferedImage generate(String content, @Nullable Logo logo) {
        return generate(content, logo, defaultErrorCorrectionLevel);
    }

    @Override
    public BufferedImage generate(String content, @Nullable Logo logo,
            @Nullable ErrorCorrectionLevel errorCorrectionLevel) {
        return generate(content, logo, errorCorrectionLevel, defaultSize);
    }

    @Override
    public BufferedImage generate(String content, @Nullable Logo logo,
            @Nullable ErrorCorrectionLevel errorCorrectionLevel, int size) {
        try {
            final Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            hints.put(EncodeHintType.ERROR_CORRECTION,
                    errorCorrectionLevel != null ? errorCorrectionLevel : defaultErrorCorrectionLevel);
            hints.put(EncodeHintType.MARGIN, defaultMargin);

            int qrCodeSize = size >= 0 ? size : defaultSize; // 最终的size

            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize,
                    hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            if (logo != null) {
                insertLogo(image, logo, qrCodeSize);
            }

            return image;
        } catch (WriterException e) {
            throw new UncheckedIOException(e.getMessage(), new IOException(e));
        }
    }

    // ----------------------------------------------------------------------------------------------------------------

    private void insertLogo(BufferedImage source, Logo logo, int qrCodeSize) {
        Image src = logo.getImage();
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (logo.isCompress()) { // 压缩LOGO
            if (width > 60) {
                width = 60;
            }
            if (height > 60) {
                height = 60;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }

        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (qrCodeSize - width) / 2;
        int y = (qrCodeSize - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    // ----------------------------------------------------------------------------------------------------------------

    public void setDefaultErrorCorrectionLevel(ErrorCorrectionLevel defaultErrorCorrectionLevel) {
        this.defaultErrorCorrectionLevel = defaultErrorCorrectionLevel;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public void setDefaultMargin(int defaultMargin) {
        this.defaultMargin = defaultMargin;
    }

}
