package io.github.bungder.fci.common;

import com.google.common.io.Files;
import com.google.gson.Gson;
import io.github.bungder.fci.common.event.FileChangeEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * @author bungder
 * @date 09/18/2021 12:42 PM
 */
@Slf4j
public class FileWatcherTest {

    @SneakyThrows
    @Test
    public void test() {
        String filePath = System.getProperty("user.home")+"/tmp/file-injector/001.txt";
        new File(filePath).getParentFile().mkdirs();
        String anotherPath = System.getProperty("user.home")+"/tmp/file-injector/" + RandomStringUtils.randomAlphanumeric(8) + ".txt";
        Files.write("123123".getBytes(StandardCharsets.UTF_8), new File(filePath));
        Consumer<FileChangeEvent> consumer = (event) -> {
            log.info("changed: {}", new Gson().toJson(event));
        };
        log.info("start watching {}", filePath);
        FileWatcher.watch(filePath, consumer);
        TimeUnit.SECONDS.sleep(5);
        log.info("writing to {}", anotherPath);
        Files.write(RandomStringUtils.randomAlphanumeric(128).getBytes(StandardCharsets.UTF_8),
            new File(anotherPath));
        Files.write("321321".getBytes(StandardCharsets.UTF_8), new File(filePath));
        TimeUnit.MINUTES.sleep(2);
    }
}
