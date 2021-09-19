package io.github.bugnder.fci.spring.cloud;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import io.github.bugnder.fci.spring.cloud.config.FileInjectorProperties;
import io.github.bungder.fci.common.FileWatcher;
import io.github.bungder.fci.common.parse.MapConfigFileReader;
import io.github.bungder.fci.common.parse.MapConfigFileReaderRegistry;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

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
    public ReloadablePropertySource locate(Environment environment) {
        try {
            return new ReloadablePropertySource("file-inject", new ConcurrentHashMap<>(),
                ListUtils.emptyIfNull(fileInjectorProperties.getFiles()));
        } catch (Throwable e) {
            throw new RuntimeException("Error on loading config file", e);
        }
    }

    private Map<String, Object> readMapConfigFromFile(String filePath) throws Exception {
        log.info("reading config from {}", filePath);
        String suffix = StringUtils.lowerCase(FilenameUtils.getExtension(filePath));
        MapConfigFileReader reader = mapConfigFileReaderRegistry.get(suffix);
        if (reader == null) {
            throw new IllegalArgumentException(
                MessageFormat.format("Could not find {0} reader for {1}", suffix, filePath));
        }
        return reader.readFromFile(filePath);
    }

    class ReloadablePropertySource extends MapPropertySource {

        private Map<String, Object> map;

        public ReloadablePropertySource(String name, Map<String, Object> map, List<String> filePathList)
            throws Exception {
            super(name, map);
            this.map = map;
            for (String path : filePathList) {
                Map<String, Object> curMap = readMapConfigFromFile(path);
                map.putAll(curMap);
                FileWatcher.watch(path, (p) -> {
                    try {
                        String msg = MessageFormat.format("refresh file config due to {0} changed: {1} {2}",
                            p.getPath(), String.valueOf(p.getSource().context()), p.getSource().kind().name());
                        log.info(msg);
                        log.info("reloading {}", p.getPath());
                        Map<String, Object> map2 = readMapConfigFromFile(p.getPath());
                        map.putAll(map2);
                        log.info("keys {} changed: {}", Joiner.on(", ").join(map2.keySet()), new Gson().toJson(map2));
                        applicationContext.publishEvent(new EnvironmentChangeEvent(map2.keySet()));
                    } catch (Throwable e) {
                        log.error("Error on reloading {}", p.getPath(), e);
                    }
                });
            }
        }

        @Override
        public Object getProperty(String s) {
            return map.get(s);
        }

    }
}
