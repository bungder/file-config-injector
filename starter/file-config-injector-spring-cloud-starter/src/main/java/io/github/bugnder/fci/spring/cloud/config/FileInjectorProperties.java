package io.github.bugnder.fci.spring.cloud.config;

import com.google.common.base.Joiner;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * config for file injector
 *
 * @author bungder
 */
@Slf4j
@RefreshScope
@ConfigurationProperties(prefix = "io.github.bungder.file-config")
@Data
public class FileInjectorProperties {
    /**
     * whether to enable file config inject
     */
    private boolean enabled;
    /**
     * file path list
     */
    private List<String> files;

    @PostConstruct
    private void init() {
        log.info("{} is initialized: {}", FileInjectorProperties.class.getSimpleName(), toString());
    }

    @Override
    public String toString() {
        return "FileInjectorProperties{" +
            "enabled=" + enabled +
            ", files=" + "[" + Joiner.on(", ").join(ListUtils.emptyIfNull(files)) + "]" +
            '}';
    }
}
