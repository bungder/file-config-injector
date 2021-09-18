package io.github.bungder.fci.core.config;
import io.github.bungder.fci.common.parse.MapConfigFileReader;
import io.github.bungder.fci.common.parse.MapConfigFileReaderRegistry;
import io.github.bungder.fci.common.parse.PropertiesMapConfigFileReader;
import io.github.bungder.fci.common.parse.YamlMapConfigFileReader;
import java.util.List;
import org.springframework.context.annotation.Bean;

/**
 * @author bungder
 * @date 09/19/2021 12:13 AM
 */
public class FileConfigInjectorCoreAutoConfig {

    @Bean
    public PropertiesMapConfigFileReader propertiesMapConfigFileReader() {
        return new PropertiesMapConfigFileReader();
    }

    @Bean
    public YamlMapConfigFileReader yamlMapConfigFileReader() {
        return new YamlMapConfigFileReader();
    }

    @Bean
    public MapConfigFileReaderRegistry mapConfigFileReaderRegistry(List<MapConfigFileReader> readers) {
        return new MapConfigFileReaderRegistry(readers);
    }
}
