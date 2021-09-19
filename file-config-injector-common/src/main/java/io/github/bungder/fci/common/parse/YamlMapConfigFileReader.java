package io.github.bungder.fci.common.parse;

import com.google.common.collect.Streams;
import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.configuration2.YAMLConfiguration;

/**
 * @author bungder
 * @date 09/18/2021 11:56 PM
 */
public class YamlMapConfigFileReader implements MapConfigFileReader {

    @Override
    public Map<String, Object> readFromBinary(byte[] configContent) throws Exception {
        YAMLConfiguration yamlConfiguration = new YAMLConfiguration();
        yamlConfiguration.read(new ByteArrayInputStream(configContent));
        return Streams.stream(yamlConfiguration.getKeys())
            .collect(Collectors.toMap(s -> s, s -> yamlConfiguration.getString(s)));
    }

    @Override
    public String[] getSupportedFileTypes() {
        return new String[] {"yml", "yaml"};
    }
}
