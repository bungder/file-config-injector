package io.github.bungder.fci.test.spring.cloud.service;

import io.github.bungder.fci.test.spring.cloud.config.SpringCloudYamlTestConfig;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author bungder
 */
@Slf4j
@Service
public class LoopService {

    @Resource
    private SpringCloudYamlTestConfig springCloudYamlTestConfig;

    private ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);

    public void doSome() {
        log.info(springCloudYamlTestConfig.toString());
    }

    @PostConstruct
    private void init() {
        exec.scheduleAtFixedRate(() -> doSome(), 1, 1, TimeUnit.SECONDS);
    }
}
