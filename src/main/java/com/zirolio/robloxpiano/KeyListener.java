package com.zirolio.robloxpiano;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class KeyListener implements NativeKeyListener {
    public static void init() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        GlobalScreen.addNativeKeyListener(new KeyListener());
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int mods = e.getModifiers();
        int code = e.getKeyCode();

        if ((mods & NativeKeyEvent.ALT_MASK) != 0) {
            if (code == NativeKeyEvent.VC_P) {
                InputStream is = Main.class.getResourceAsStream("/melody.txt");
                if (is == null) throw new RuntimeException("Файл не найден!");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String[] content = reader.lines().collect(Collectors.joining("\n")).split("\n", 2);
                    System.out.println(Arrays.toString(content));
                    SongPlayer.getInstance().play(content[1], Integer.parseInt(content[0].trim()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }

            if ((mods & NativeKeyEvent.CTRL_MASK) != 0 && code == NativeKeyEvent.VC_S) SongPlayer.getInstance().stop();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        //  System.out.println("Отпущено: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}
}
