package io.github.bungder.fci.common.util;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author bungder
 * @date 09/19/2021 12:16 AM
 */
public class Registry<K, V> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();

    public synchronized void register(K key, V value) {
        if (map.containsKey(key)) {
            String msg = MessageFormat.format("key {0} is already exists", key);
            log.warn(msg);
            throw new IllegalStateException(msg);
        }
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }
}
