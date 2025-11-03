package com.zirolio.robloxpiano;

public class Main {
    public static void main(String[] args) {
        Config config = Config.get();
        System.out.println("Program started, config loaded!");
        KeyListener.init();
    }
}