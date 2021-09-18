package io.github.bungder.fci.common.parse;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * reader which can read map from file
 *
 * @author bungder
 * @date 09/18/2021 5:48 PM
 */
public interface MapConfigFileReader {

    default Map<String, Object> readFromFile(String filePath) throws IOException {
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
    Map<String, Object> readFromBinary(byte[] configContent) throws IOException;


    public String[] getSupportedFileTypes();
}
