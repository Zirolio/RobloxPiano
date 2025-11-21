package com.zirolio.robloxpiano;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zirolio.robloxpiano.ui.PlayerUI;

import javax.management.RuntimeErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;


public class Config extends ArrayList<Config.MelodyConfig> {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static Config INSTANCE = new Config();
    private static String configSavePath;

    public static void load(String path) {
        configSavePath = path;

        try {
            INSTANCE =  gson.fromJson(Files.readString(Path.of(configSavePath)), Config.class);
        } catch (Exception ignored) {
            try {
                Config.save(INSTANCE);
            } catch (Exception e) {
                throw new RuntimeException("Can't create config file!");
            }
        }
    }

    public static void save(Config config) throws IOException {
        Files.writeString(Path.of(configSavePath), gson.toJson(config));
    }

    public static void refreshAll() throws Error {
        Config refreshed = new Config();

        for (MelodyConfig config : INSTANCE) {
            String url = config.url != null ? config.url :
                    "https://virtualpiano.net/music-sheet/" + config.name
                            .replaceAll("\\(", "")
                            .replaceAll("\\)", "")
                            .toLowerCase().replaceAll("\\s+", "-") + "/";

            try { refreshed.add(MelodyDownloader.load(url));
            } catch (Exception e) { throw new RuntimeException("Failed to get: (" + config.name + ") " + url); }
        }

        // Save
        try { save(refreshed); INSTANCE = refreshed;
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static Config get() {
        return INSTANCE;
    }
    public static MelodyConfig getMelodyConfig(String name) {
        for (MelodyConfig melodyConfig : INSTANCE) if (melodyConfig.name.equals(name)) return melodyConfig;
        return null;
    }

    public static void addMelody(MelodyConfig melodyConfig) {
        try {
            INSTANCE.removeIf(e -> e.name.equals(melodyConfig.name));
            INSTANCE.add(melodyConfig);
            INSTANCE.sort(Comparator.comparing(e -> e.name));
            PlayerUI.getInstance().listPanel.refreshMelodyList();
            Config.save(INSTANCE);
        } catch (IOException ignored) {}
    }

    public static class MelodyConfig {
        public String name = "";
        public int tempo = 100;
        public int trans = 0;
        public String tabs = "";
        public String author = "";
        public String date = "???";
        public String playTime = "??:??";
        public String url = "";


        public MelodyConfig() {}
        public MelodyConfig(String name, String author, String date, String playTime, String url, String tabs, Integer tempo, Integer trans) {
            this.name = Objects.requireNonNullElse(name, this.name);;
            this.tabs = Objects.requireNonNullElse(tabs, this.tabs);;
            this.tempo = Objects.requireNonNullElse(tempo, this.tempo);;
            this.author = Objects.requireNonNullElse(author, this.author);;
            this.date = Objects.requireNonNullElse(date, this.date);;
            this.trans = Objects.requireNonNullElse(trans, this.trans);;
            this.playTime = Objects.requireNonNullElse(playTime, this.playTime);;
            this.url = Objects.requireNonNullElse(url, this.url);;
        }
    }

}
