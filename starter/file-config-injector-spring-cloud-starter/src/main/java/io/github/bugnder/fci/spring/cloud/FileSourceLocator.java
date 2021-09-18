package io.github.bugnder.fci.spring.cloud;

import io.github.bugnder.fci.spring.cloud.config.FileInjectorProperties;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

/**
 * @author bungder
 */
@Slf4j
public class FileSourceLocator implements PropertySourceLocator {

    private FileInjectorProperties fileInjectorProperties;

    public FileSourceLocator(FileInjectorProperties fileInjectorProperties) {
        this.fileInjectorProperties = fileInjectorProperties;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        return null;
    }

    private Map<String, Object> readMapConfigFromFile(String filePath) {

    }
}
