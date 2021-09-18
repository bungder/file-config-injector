package io.github.bugnder.fci.spring.cloud;

import io.github.bugnder.fci.spring.cloud.config.FileInjectorProperties;
import io.github.bungder.fci.common.parse.MapConfigFileReader;
import io.github.bungder.fci.common.parse.MapConfigFileReaderRegistry;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
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

    public FileSourceLocator(FileInjectorProperties fileInjectorProperties,
                             MapConfigFileReaderRegistry mapConfigFileReaderRegistry) {
        this.fileInjectorProperties = fileInjectorProperties;
        this.mapConfigFileReaderRegistry = mapConfigFileReaderRegistry;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        Map<String, Object> finalMap = new HashMap<>();
        fileInjectorProperties.getFiles().forEach(file -> {
            try {
                Map<String, Object> map = this.readMapConfigFromFile(file);
                finalMap.putAll(map);
            } catch (Throwable e) {
                throw new IllegalArgumentException(
                    MessageFormat.format("Could not read config from file {0}", file),
                    e
                );
            }
        });
        return new MapPropertySource("file-inject", finalMap);
    }

    private Map<String, Object> readMapConfigFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        String suffix = StringUtils.lowerCase(FilenameUtils.getExtension(filePath));
        MapConfigFileReader reader = mapConfigFileReaderRegistry.get(suffix);
        if (reader == null) {
            throw new IllegalArgumentException("Could not find reader for " + filePath);
        }
        return reader.readFromFile(filePath);
    }
}
