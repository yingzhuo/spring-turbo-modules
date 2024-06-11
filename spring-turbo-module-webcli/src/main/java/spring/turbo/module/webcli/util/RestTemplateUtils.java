package spring.turbo.module.webcli.util;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import spring.turbo.util.Asserts;

/**
 * @author 应卓
 *
 * @since 3.3.1
 */
public final class RestTemplateUtils {

    /**
     * 私有构造方法
     */
    private RestTemplateUtils() {
        super();
    }

    public static RestClient toRestClient(RestTemplate template) {
        Asserts.notNull(template);
        return RestClient.builder(template).build();
    }

}
