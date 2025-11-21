package com.zirolio.robloxpiano;

import com.zirolio.robloxpiano.ui.PlayerUI;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        if (!argsList.isEmpty())
            Config.load(!argsList.getLast().startsWith("-") ? argsList.getLast() : "./._roblox_piano_config.json");

        if (argsList.contains("-r")) {
            try {
                Config.refreshAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        System.out.println("Program started, config loaded!");
        KeyListener.init();
        PlayerUI.Init();
    }
}