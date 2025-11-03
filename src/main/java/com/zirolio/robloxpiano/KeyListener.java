package com.zirolio.robloxpiano;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {

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
            if (code == NativeKeyEvent.VC_P) {
                String melodyName = "gravity-falls";
                Config.MelodyConfig melodyConfig = Config.get().get(melodyName);
                MelodyPlayer.getInstance().play(melodyConfig.tabs, melodyConfig.bpm);
            }

            if ((mods & NativeKeyEvent.CTRL_MASK) != 0 && code == NativeKeyEvent.VC_S) MelodyPlayer.getInstance().stop();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        //  System.out.println("Отпущено: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}
}
