package io.github.bungder.fci.common.event;

/**
 * @Created by tanshichang
 * @Date 2021-09-18 08:47
 */
public class FileChangeEvent {
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
}