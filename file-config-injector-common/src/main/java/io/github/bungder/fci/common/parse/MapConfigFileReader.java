package io.github.bungder.fci.common.parse;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * reader which can read map from file
 *
 * @author bungder
 * @date 09/18/2021 5:48 PM
 */
public interface MapConfigFileReader {

    default Map<String, Object> readFromFile(String filePath) throws Exception {
        byte[] data = Files.toByteArray(new File(filePath));
        return readFromBinary(data);
    }

    default Map<String, Object> propertiesToMap(Properties properties) {
        Set<String> keys = properties.stringPropertyNames();
        Map<String, Object> result = keys.stream()
            .collect(Collectors.toMap(s -> s, s -> properties.get(s)));
        return result;
    }

    /**
     * @param configContent file content
     * @return
     */
    Map<String, Object> readFromBinary(byte[] configContent) throws Exception;


    public String[] getSupportedFileTypes();
}
