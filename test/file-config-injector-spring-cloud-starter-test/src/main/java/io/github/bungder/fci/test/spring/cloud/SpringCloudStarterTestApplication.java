package io.github.bungder.fci.test.spring.cloud;

import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author bungder
 * @date 09/18/2021 5:24 PM
 */
@Slf4j
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class SpringCloudStarterTestApplication {


    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SpringApplication app = new SpringApplication(SpringCloudStarterTestApplication.class);
        ConfigurableApplicationContext applicationContext = app.run(args);
        Environment env = applicationContext.getEnvironment();
        stopwatch.stop();
        String urlPrefix = getUrlPrefix(env);
        String applicationName = env.getProperty("spring.application.name");
        log.info(
            "server {} started after {} seconds, profiles: {}, address: {}",
            StringUtils.isBlank(applicationName) ? "" : " '" + applicationName + "' ",
            stopwatch.elapsed(TimeUnit.SECONDS),
            env.getActiveProfiles(),
            urlPrefix
        );
    }

    public static String getUrlPrefix(Environment env) {
        String address = "127.0.0.1";
        String serverAddress = env.getProperty("server.address");
        if (StringUtils.isNoneBlank(serverAddress)) {
            address = serverAddress;
        }
        String urlPrefix = "http://" + address;
        String port = env.getProperty("server.port");
        if (!"80".equalsIgnoreCase(port)) {
            urlPrefix = urlPrefix + ":" + port;
        }
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isNoneBlank(contextPath)) {
            urlPrefix += "/" + contextPath;
        }
        return urlPrefix;
    }
}
