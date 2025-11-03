package com.zirolio.robloxpiano;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class Config extends HashMap<String, Config.MelodyConfig> {
    private static final Config INSTANCE = loadConfig("/config.json");

    public static Config loadConfig(String path) {
        try (InputStream is = Config.class.getResourceAsStream(path)) {
            if (is == null)
                throw new RuntimeException("Config file not found: " + path);

            return new Gson().fromJson(new InputStreamReader(is), Config.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public static class MelodyConfig {
        public int bpm = 100;
        public String tabs = "";
    }
}
