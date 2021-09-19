package io.github.bungder.fci.test.spring.cloud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.bungder.fci.test.spring.cloud.config.SpringCloudYamlTestConfig;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author bungder
 */
@Slf4j
@Service
public class LoopService {

    @Resource
    private SpringCloudYamlTestConfig springCloudYamlTestConfig;

    @Value("${test.demo-yaml-file}")
    private String yamlPath;

    private ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);

    public void doSome() {
        springCloudYamlTestConfig.toString();
//        log.info(springCloudYamlTestConfig.toString());
        try {
            write();
        } catch (Throwable e) {
            log.error("", e);
        }
    }

    @PostConstruct
    private void init() {
        log.info("config file: {}", yamlPath);
        exec.scheduleAtFixedRate(() -> doSome(), 1, 4, TimeUnit.SECONDS);
    }

    public void write() throws IOException {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Map map = getRandom();
        om.writeValue(new File(yamlPath), map);
    }

    public Map getRandom() {
        Map map = new HashMap();
        map.put("name", RandomStringUtils.randomAlphanumeric(8));
        map.put("id", RandomUtils.nextInt(10000, 100000));
        map.put("date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(new Date()));
        Map result = new HashMap();
        result.put("test", new HashMap() {{
            put("spring-cloud", new HashMap() {{
                put("yaml", map);
            }});
        }});
        return result;
    }
}
