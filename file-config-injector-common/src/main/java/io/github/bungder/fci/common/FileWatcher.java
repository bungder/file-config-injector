package io.github.bungder.fci.common;

import io.github.bungder.fci.common.event.FileChangeEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author by bungder
 */
@Slf4j
public class FileWatcher implements Runnable {

    private static Map<String, FileWatcher> watcherMap = new ConcurrentHashMap<>();
    private static ExecutorService exec = Executors.newCachedThreadPool();
    private String directory;
    private Map<String, Consumer<FileChangeEvent>> fileEventConsumerMap = new ConcurrentHashMap<>();

    private FileWatcher(String directoryPath, String fileName, Consumer<FileChangeEvent> consumer) {
        this.directory = directoryPath;
        fileEventConsumerMap.put(fileName, consumer);
    }

    public static synchronized void watch(String path, Consumer<FileChangeEvent> consumer)
        throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(path + " does not exist");
        }
        if (file.isDirectory()) {
            throw new IllegalAccessError(path + " is a directory");
        }
        Path parentPath = file.getParentFile().toPath();
        String parentPathStr = parentPath.toString();
        if (watcherMap.containsKey(parentPathStr)) {
            FileWatcher watcher = watcherMap.get(parentPathStr);
            watcher.addFile(file.getName(), consumer);
        } else {
            FileWatcher watcher = new FileWatcher(parentPathStr, file.getName(), consumer);
            watcherMap.put(parentPathStr, watcher);
            exec.submit(watcher);
        }
    }

    private void addFile(String fileName, Consumer<FileChangeEvent> consumer) {
        this.fileEventConsumerMap.put(fileName, consumer);
    }

    public void run() {
        try {
            watch();
        } catch (Throwable e) {
            log.warn("", e);
        }
    }

    public void watch() throws IOException {
        final Path path = new File(directory).toPath();
        log.info("start watching directory: {}", path);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        try {
            final WatchKey watchKey =
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE);
            while (true) {
                try {
                    final WatchKey wk = watchService.take();
                    for (WatchEvent<?> event : wk.pollEvents()) {
                        //we only register "ENTRY_MODIFY" so the context is always a Path.
                        log.info("count: {}, kind: {}. context: {}", event.count(), event.kind(), event.context());
                        final Path changed = (Path) event.context();
                        System.out.println(changed);
                        String fileName = changed.getFileName().toString();
                        Consumer<FileChangeEvent> consumer = fileEventConsumerMap.get(fileName);
                        if (consumer == null) {
                            continue;
                        }
                        consumer.accept(new FileChangeEvent()
                            .setTimestamp(System.currentTimeMillis())
                            .setPath(changed.toString()));
                    }
                    // reset the key
                    boolean valid = wk.reset();
                    if (!valid) {
                        log.warn("Key has been unregisterede");
                    }
                } catch (Throwable e) {
                    log.warn("", e);
                }
            }
        } finally {
            watchService.close();
        }
    }
}