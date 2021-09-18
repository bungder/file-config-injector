package io.github.bungder.fci.common;

import io.github.bungder.fci.common.event.FileChangeEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @Created by bungder
 * @Date 2021-09-18 08:27
 */
public class FileWatcher implements Runnable {

    private FileWatcher(String directoryPath, String fileName, Consumer<FileChangeEvent> consumer) {
        this.directory = directoryPath;
        fileEventConsumerMap.put(fileName, consumer);
    }

    private String directory;
    private Map<String, Consumer<FileChangeEvent>> fileEventConsumerMap = new ConcurrentHashMap<>();
    private static Map<String, FileWatcher> watcherMap = new ConcurrentHashMap<>();

    private static ExecutorService exec = Executors.newCachedThreadPool();

    public static synchronized void watch(String path, Consumer<FileChangeEvent> consumer) throws NoSuchFieldException {
        File file = new File(path);
        if (!file.exists()) {
            throw new NoSuchFieldException(path + " does not exist");
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
            aa();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void aa() throws IOException {
        final Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Desktop");
        System.out.println(path);
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
                try {
                    final WatchKey wk = watchService.take();
                    for (WatchEvent<?> event : wk.pollEvents()) {
                        //we only register "ENTRY_MODIFY" so the context is always a Path.
                        final Path changed = (Path) event.context();
                        System.out.println(changed);
                        if (changed.endsWith("myFile.txt")) {
                            System.out.println("My file has changed");
                        }
                    }
                    // reset the key
                    boolean valid = wk.reset();
                    if (!valid) {
                        System.out.println("Key has been unregisterede");
                    }
                } catch (Throwable e) {

                }
            }
        }
    }
}