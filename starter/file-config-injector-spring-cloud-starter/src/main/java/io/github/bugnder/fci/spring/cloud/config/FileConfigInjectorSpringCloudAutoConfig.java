package io.github.bugnder.fci.spring.cloud.config;

import io.github.bugnder.fci.spring.cloud.FileSourceLocator;
import io.github.bungder.fci.common.parse.MapConfigFileReaderRegistry;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author bungder
 */
@ConditionalOnProperty(name = "io.github.bungder.file-config.enabled", matchIfMissing = true)
@EnableConfigurationProperties(FileInjectorProperties.class)
public class FileConfigInjectorSpringCloudAutoConfig {

    @Resource
    private FileInjectorProperties fileInjectorProperties;
    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public FileSourceLocator fileSourceLocator(MapConfigFileReaderRegistry mapConfigFileReaderRegistry) {
        return new FileSourceLocator(fileInjectorProperties, mapConfigFileReaderRegistry, applicationContext);
    }
}
