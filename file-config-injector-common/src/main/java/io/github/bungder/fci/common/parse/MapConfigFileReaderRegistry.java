package io.github.bungder.fci.common.parse;

import io.github.bungder.fci.common.util.Registry;
import java.util.List;

/**
* @author bungder
 * @date 09/19/2021 12:20 AM
 */
public class MapConfigFileReaderRegistry extends Registry<String, MapConfigFileReader> {

    private List<MapConfigFileReader> readers;

    public MapConfigFileReaderRegistry(List<MapConfigFileReader> readers) {
        this.readers = readers;
        for (MapConfigFileReader reader : readers) {
            for (String name : reader.getSupportedFileTypes()) {
                register(name, reader);
            }
        }
    }
}
