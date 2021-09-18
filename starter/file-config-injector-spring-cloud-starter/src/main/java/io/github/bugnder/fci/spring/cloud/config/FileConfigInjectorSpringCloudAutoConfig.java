package io.github.bugnder.fci.spring.cloud.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author bungder
 */
@ConditionalOnProperty(name = "io.github.bungder.file-config.enabled", matchIfMissing = true)
@EnableConfigurationProperties(FileInjectorProperties.class)
public class FileConfigInjectorSpringCloudAutoConfig {
}
