/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.dirwatcher;

import io.methvin.watcher.DirectoryWatcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import spring.turbo.io.IOExceptionUtils;
import spring.turbo.util.Asserts;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 应卓
 * @since 1.1.1
 */
class DirectoryWatcherWrapper implements InitializingBean, DisposableBean {

    private final DirectoryWatcher watcher;

    public DirectoryWatcherWrapper(DirectoryListener listener, List<String> dirsToWatch) {
        Asserts.notNull(listener);
        Asserts.notNull(dirsToWatch);

        try {
            this.watcher = DirectoryWatcher.builder()
                    .listener(event -> {
                        switch (event.eventType()) {
                            case CREATE:
                                listener.onCreated(event.path());
                                break;
                            case MODIFY:
                                listener.onModified(event.path());
                                break;
                            case DELETE:
                                listener.onDeleted(event.path());
                                break;
                            case OVERFLOW:
                                listener.onOverflowed(event.path());
                                break;
                        }
                    })
                    .fileHashing(true)
                    .paths(dirsToWatch.stream().map(Paths::get).collect(Collectors.toList()))
                    .build();
        } catch (IOException e) {
            throw IOExceptionUtils.toUnchecked(e);
        }
    }

    public DirectoryWatcherWrapper(DirectoryListener listener, String dirToWatch) {
        this(listener, Collections.singletonList(dirToWatch));
    }

    @Override
    public void afterPropertiesSet() {
        watcher.watchAsync();
    }

    @Override
    public void destroy() throws Exception {
        watcher.close();
    }

}
