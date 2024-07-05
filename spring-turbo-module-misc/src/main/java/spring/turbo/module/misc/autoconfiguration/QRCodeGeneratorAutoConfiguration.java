package spring.turbo.module.misc.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import spring.turbo.module.misc.qrcode.QRCodeGenerator;
import spring.turbo.module.misc.qrcode.QRCodeGeneratorImpl;

/**
 * @author 应卓
 * @since 1.3.0
 */
@AutoConfiguration
@ConditionalOnMissingBean(QRCodeGenerator.class)
@ConditionalOnClass(name = "com.google.zxing.BarcodeFormat")
public class QRCodeGeneratorAutoConfiguration {

    @Bean
    public QRCodeGenerator qrCodeGenerator() {
        return new QRCodeGeneratorImpl();
    }

}
