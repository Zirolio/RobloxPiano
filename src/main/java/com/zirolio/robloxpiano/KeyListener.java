package com.zirolio.robloxpiano;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KeyListener implements NativeKeyListener {
    private static boolean spin = false;

    public static void init() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        GlobalScreen.addNativeKeyListener(new KeyListener());
        System.out.println("KeyListener initialized!");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int mods = e.getModifiers();
        int code = e.getKeyCode();

        if ((mods & NativeKeyEvent.ALT_MASK) != 0) {
            if ((mods & NativeKeyEvent.CTRL_MASK) != 0 && code == NativeKeyEvent.VC_S) MelodyPlayer.getInstance().stop();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}
}
