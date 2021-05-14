package io.empyre.io;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class Data {

    public static final String PATH = "plugins/Empyre3";

    public static void start() {
        List<String> paths = List.of("/", "/users/", "/armor/");
        paths.forEach(s -> {
            File f = new File(PATH + s);
            f.mkdir();
        });
    }

    public abstract void save() throws IOException;
    protected abstract boolean writeInitial();

    protected final File file;
    protected YamlConfiguration config;
    protected Data(String path) {
        file = new File(path);
        config = YamlConfiguration.loadConfiguration(file);
    }
    public File getFile() {
        return file;
    }
    public YamlConfiguration getConfig() {
        return config;
    }
    public void create() throws IOException {
        if (!file.exists()) {
            boolean b = file.createNewFile();
            config = YamlConfiguration.loadConfiguration(file);
            writeInitial();
        }
    }
    public void delete() throws IOException {
        if (file.exists()) {
            boolean b = file.delete();
        } else throw new IOException("File does not exist so it can't be deleted");
    }

}
