package spring.turbo.module.jackson.autoconfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.lang.Nullable;
import spring.turbo.module.jackson.util.JacksonModuleUtils;

/**
 * @author 应卓
 * @since 3.3.1
 */
@AutoConfiguration
public class JacksonModuleAutoConfiguration {

    @Autowired(required = false)
    private void initModules(@Nullable ObjectMapper objectMapper) {
        JacksonModuleUtils.loadAndRegisterModules(objectMapper);
    }

}
