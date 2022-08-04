/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.data.page;

import lombok.val;
import org.springframework.data.domain.Page;
import spring.turbo.util.Asserts;
import spring.turbo.webmvc.api.Json;

/**
 * @author 应卓
 * @see org.springframework.data.domain.Page
 * @since 1.1.3
 */
public final class PageUtils {

    /**
     * 私有构造方法
     */
    private PageUtils() {
        super();
    }

    public static <T> Json wrapAsJson(Page<T> page) {
        Asserts.notNull(page);
        val json = Json.newInstance();
        json.payload("content", page.getContent());
        json.payload("pageNumber", page.getNumber());
        json.payload("pageSize", page.getSize());
        json.payload("totalElements", page.getTotalElements());
        json.payload("totalPages", page.getTotalPages());
        json.payload("hasPrev", page.hasPrevious());
        json.payload("hasNext", page.hasNext());
        return json;
    }

}
