/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.misc.autoconfiguration;

import com.github.yingzhuo.avatargenerator.Avatar;
import com.github.yingzhuo.avatargenerator.IdenticonAvatar;
import com.github.yingzhuo.avatargenerator.layers.masks.RoundRectMaskLayer;
import com.github.yingzhuo.avatargenerator.layers.shadows.ScoreShadowLayer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author 应卓
 * @since 2.2.0
 */
@AutoConfiguration
@ConditionalOnClass(name = "com.github.yingzhuo.avatargenerator.Avatar")
public class AvatarGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Avatar avatar() {
        return IdenticonAvatar.newAvatarBuilder()
                .layers(new ScoreShadowLayer(), new RoundRectMaskLayer())
                .build();
    }

}
