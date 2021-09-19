package io.github.bungder.fci.test.spring.cloud.config;

import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author bungder
 */

@Slf4j
@RefreshScope
@Data
@ConfigurationProperties(prefix = "test.spring-cloud.yaml")
public class SpringCloudYamlTestConfig {
    private String name;
    private Long id;
    private String date;

    @PostConstruct
    private void init() {
        log.info("init {}: {}", SpringCloudYamlTestConfig.class.getSimpleName(), toString());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("name", name)
            .append("id", id)
            .append("date", date)
            .toString();
    }
}


