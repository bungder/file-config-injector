package io.github.bugnder.fci.spring.cloud;

import io.github.bugnder.fci.spring.cloud.config.FileInjectorProperties;
import io.github.bungder.fci.common.FileWatcher;
import io.github.bungder.fci.common.parse.MapConfigFileReader;
import io.github.bungder.fci.common.parse.MapConfigFileReaderRegistry;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

/**
 * @author bungder
 */
@Slf4j
public class FileSourceLocator implements PropertySourceLocator {

    private FileInjectorProperties fileInjectorProperties;

    private MapConfigFileReaderRegistry mapConfigFileReaderRegistry;

    private ApplicationContext applicationContext;

    public FileSourceLocator(FileInjectorProperties fileInjectorProperties,
                             MapConfigFileReaderRegistry mapConfigFileReaderRegistry,
                             ApplicationContext applicationContext) {
        this.fileInjectorProperties = fileInjectorProperties;
        this.mapConfigFileReaderRegistry = mapConfigFileReaderRegistry;
        this.applicationContext = applicationContext;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        Map<String, Object> finalMap = new HashMap<>();
        for (String file : ListUtils.emptyIfNull(fileInjectorProperties.getFiles())) {
            try {
                Map<String, Object> map = this.readMapConfigFromFile(file);
                FileWatcher.watch(file, (p) -> {
                    applicationContext.publishEvent(
                        new RefreshEvent(this, null,
                            MessageFormat.format("refresh file config due to {0} changed: {1} {2}",
                                p.getPath(), String.valueOf(p.getSource().context()), p.getSource().kind().name())));
                });
                finalMap.putAll(map);
            } catch (Throwable e) {
                throw new IllegalArgumentException(
                    MessageFormat.format("Could not read config from file {0}", file),
                    e
                );
            }
        }
        return new MapPropertySource("file-inject", finalMap);
    }

    private Map<String, Object> readMapConfigFromFile(String filePath) throws IOException {
        log.info("reading config from {}", filePath);
        String suffix = StringUtils.lowerCase(FilenameUtils.getExtension(filePath));
        MapConfigFileReader reader = mapConfigFileReaderRegistry.get(suffix);
        if (reader == null) {
            throw new IllegalArgumentException(
                MessageFormat.format("Could not find {0} reader for {1}", suffix, filePath));
        }
        return reader.readFromFile(filePath);
    }
}
