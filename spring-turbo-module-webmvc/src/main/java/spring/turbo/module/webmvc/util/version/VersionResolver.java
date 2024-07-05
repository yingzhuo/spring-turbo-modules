package spring.turbo.module.webmvc.util.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;

/**
 * Rest API 版本解析器
 *
 * @author 应卓
 * @since 2.0.9
 */
@FunctionalInterface
public interface VersionResolver extends Ordered {

    @Nullable
    public String resolve(HttpServletRequest request);

    @Override
    public default int getOrder() {
        return 0;
    }

}
