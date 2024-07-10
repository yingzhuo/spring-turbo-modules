package spring.turbo.module.webmvc.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 2024-07-10
 */
@Data
@ConfigurationProperties(prefix = "springboot.webmvc")
public class SpringBootWebMvcProperties implements Serializable {

    private boolean dataBinderInitializingAdvice = true;

}
