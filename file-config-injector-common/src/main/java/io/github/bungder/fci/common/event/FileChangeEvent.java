package io.github.bungder.fci.common.event;

import java.nio.file.WatchEvent;

/**
 * @Created by tanshichang
 * @Date 2021-09-18 08:47
 */
public class FileChangeEvent<T> {
    private WatchEvent<T> source;
    private long timestamp;
    private String path;

    public long getTimestamp() {
        return timestamp;
    }

    public FileChangeEvent setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getPath() {
        return path;
    }

    public FileChangeEvent setPath(String path) {
        this.path = path;
        return this;
    }

    public WatchEvent<T> getSource() {
        return source;
    }

    public FileChangeEvent setSource(WatchEvent<T> source) {
        this.source = source;
        return this;
    }
}