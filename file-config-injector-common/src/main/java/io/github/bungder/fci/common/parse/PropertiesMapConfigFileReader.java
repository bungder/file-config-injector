package io.github.bungder.fci.common.parse;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
* @author bungder
 * @date 09/18/2021 11:57 PM
 */
public class PropertiesMapConfigFileReader implements MapConfigFileReader {

    @Override
    public Map<String, Object> readFromBinary(byte[] configContent) throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader(new String(configContent, StandardCharsets.UTF_8)));
        return this.propertiesToMap(properties);
    }

    @Override
    public String[] getSupportedFileTypes() {
        return new String[] {"properties"};
    }
}
