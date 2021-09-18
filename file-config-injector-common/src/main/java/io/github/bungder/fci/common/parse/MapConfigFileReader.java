package io.github.bungder.fci.common.parse;

import java.util.Map;

/**
 * reader which can read map from file
 * @author bungder
 * @date 09/18/2021 5:48 PM
 */
public interface MapConfigFileReader {

    Map<String, Object> readFromFile(String filePath);

    /**
     *
     * @param configContent file content
     * @return
     */
    Map<String, Object> readFromBinary(byte[] configContent);
}
