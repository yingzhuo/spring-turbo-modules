/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.csv.vistor;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import spring.turbo.lang.Mutable;
import spring.turbo.util.Asserts;

import java.io.Serializable;

/**
 * @author 应卓
 * @since 1.0.9
 */
@Mutable
public final class ProcessingContext implements Serializable {

    private Resource resource;

    public ProcessingContext() {
        super();
    }

    public ProcessingContext(@NonNull Resource resource) {
        Asserts.notNull(resource);
        this.resource = resource;
    }

    @NonNull
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
