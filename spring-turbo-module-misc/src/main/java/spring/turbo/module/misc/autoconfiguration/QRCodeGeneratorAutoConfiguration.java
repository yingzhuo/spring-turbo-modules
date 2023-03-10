/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
@ConditionalOnClass(name = "com.google.zxing.qrcode.decoder.ErrorCorrectionLevel")
public class QRCodeGeneratorAutoConfiguration {

    @Bean
    public QRCodeGenerator qrCodeGenerator() {
        return new QRCodeGeneratorImpl();
    }

}
