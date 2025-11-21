package com.zirolio.robloxpiano;

import com.zirolio.robloxpiano.ui.PlayerUI;

import javax.security.auth.callback.Callback;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class MelodyPlayer {
    public static ArrayList<String> NOTES_LINE = new ArrayList<>(List.of(
            "1_", "2_", "3_", "4_", "5_", "6_", "7_", "8_", "9_", "0_", "q_", "w_", "e_", "r_", "t_",
                    "1", "!", "2", "@", "3", "#", "4", "$", "5", "%", "6", "^", "7", "&", "8", "*", "9", "(", "0",
                    "q", "Q", "w", "W", "e", "E", "r", "t", "T", "y", "Y", "u", "i", "I", "o", "O", "p", "P", "a", "s", "S", "d", "D", "f", "g", "G", "h", "H", "j", "J", "k", "l", "L", "z", "Z", "x", "c", "C", "v", "V", "b", "B", "n", "m",
                    "y_", "u_", "i_", "o_", "p_", "a_", "s_", "d_", "f_", "g_", "h_", "j_"
    ));

    private final Robot robot;
    private boolean playing = false;
    private Config.MelodyConfig currentMelodyConfig = null;
    private Thread playThread;

    public MelodyPlayer() throws AWTException {
        this.robot = new Robot();
    }

    public void stop() {
        this.playing = false;
        this.currentMelodyConfig = null;
        if (this.playThread != null && this.playThread.isAlive()) {
            // this.playThread.interrupt();
            try {
                this.playThread.join();
            } catch (Exception ignored) {}
        }
    }

    /**
     * Проигрывает песню.
     * @param config melody config
     */
    public void play(Config.MelodyConfig config) { this.play(config, null); }
    /**
     * Проигрывает песню.
     * @param config melody config
     * @param callback callback function
     */
    public void play(Config.MelodyConfig config, Runnable callback) {
        stop();
        if (config == null) return;
        this.currentMelodyConfig = config;
        this.playing = true;

        System.out.println(this.normalizeTabs(config.tabs));

        this.playThread = new Thread(() -> {
            try { // Pre play pause
                Thread.sleep((long) (3 * 1000));
            } catch (InterruptedException ignored) {
                this.playing = false;
                if (callback != null) SwingUtilities.invokeLater(callback);
                return;
            }

            if (this.playing) {
                // Start playing
                System.out.println("Play " + config.name);

                try {
                    this.playProcessor(this.normalizeTabs(config.tabs), config.tempo, config.trans);
                } catch (InterruptedException ignored) {}
            }

            this.playing = false;
            this.currentMelodyConfig = null;
            if (callback != null) SwingUtilities.invokeLater(callback);
        });

        this.playThread.start();
    }

    private String normalizeTabs(String tabs) {
        return tabs.replaceAll("\\s+", "").replaceAll("\\|", "-").replaceAll("\\s{2,}", "");
    }

    @SuppressWarnings("BusyWait")
    private void playProcessor(String tabs, int tempo, int trans) throws InterruptedException {
        if (tabs.isEmpty()) return;
        // PlayerUI.getInstance().notesLogger.clear();

        TabPlayer tabPlayer = new TabPlayer(this.robot, tempo, trans);
        String iterator = tabs;

        while (!iterator.isEmpty() && this.playing) {

            if (iterator.startsWith("~")) {
                iterator = iterator.substring(1);
                // tabPlayer.space();
                // tabPlayer.shortPause();
            } else if (iterator.startsWith("--")) { // --
                iterator = iterator.substring(2);
                tabPlayer.longPause();
            } else if (iterator.startsWith("-")) { // -
                iterator = iterator.substring(1);
                tabPlayer.pause();
            } else if (iterator.startsWith("[")) { // [asd]
                int endIndex = iterator.indexOf(']');
                String nextItem = iterator.substring(0, endIndex + 1);
                iterator = iterator.substring(endIndex + 1);

                PlayerUI.getInstance().notesLogger.log(nextItem);
                tabPlayer.playAccord(nextItem);
            } else if (iterator.startsWith("{")) { // {asd}
                int endIndex = iterator.indexOf('}');
                String nextItem = iterator.substring(0, endIndex + 1);
                iterator = iterator.substring(endIndex + 1);

                PlayerUI.getInstance().notesLogger.log(nextItem);
                tabPlayer.playSeq(nextItem);
            } else {  // a
                String nextItem = iterator.substring(0, 1);
                iterator = iterator.substring(1);

                PlayerUI.getInstance().notesLogger.log(nextItem);
                tabPlayer.playNote(nextItem);
            }

            iterator = iterator.strip();
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public Config.MelodyConfig getCurrentMelodyConfig() {
        return currentMelodyConfig;
    }

    private static final MelodyPlayer instance;
    static {
        try {
            instance = new MelodyPlayer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MelodyPlayer getInstance() {
        return instance;
    }

    public static class TabPlayer {
        private final Robot robot;

        final int trans;

        final float beat;      // a or [aa]
        final float minimalBeat;      // a or [aa]
        final float shortPause;      // "-"
        final float midPause;        // "--"

        public TabPlayer(Robot robot, int tempo, int trans) {
            this.robot = robot;
            this.trans = trans;
            this.beat = 60f / tempo / 2;
            this.minimalBeat = beat / 10;
            this.shortPause = beat / 2;
            this.midPause = beat;
        }

        private String getNoteWithTrans(String note) {
            if (trans != 0) {
                int newIndex = NOTES_LINE.indexOf(String.valueOf(note)) + trans;
                if (newIndex < 0) newIndex += NOTES_LINE.size();
                else if (newIndex >= NOTES_LINE.size()) newIndex -= NOTES_LINE.size();

                note = NOTES_LINE.get(newIndex);
            }

            return note;
        }

        private void pressNote(char note) {
            int keyCode;
            boolean isUpper = false, isCtrl = false;

            String noteToPlay = getNoteWithTrans(String.valueOf(note));
            note = noteToPlay.charAt(0);

            if (noteToPlay.length() >= 2 && noteToPlay.charAt(1) == '_') isCtrl = true;

            if ("!@#$%^&*()".contains(String.valueOf(note))) {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(
                        "1234567890".charAt("!@#$%^&*()".indexOf(note))
                );
                isUpper = true;
            } else {
                keyCode = KeyEvent.getExtendedKeyCodeForChar(Character.toLowerCase(note));
                isUpper = Character.isUpperCase(note);
            }

            if (keyCode == KeyEvent.VK_UNDEFINED) return;
            if (isUpper) this.robot.keyPress(KeyEvent.VK_SHIFT);
            if (isCtrl) this.robot.keyPress(KeyEvent.VK_CONTROL);
            this.robot.keyPress(keyCode);
            if (isUpper) this.robot.keyRelease(KeyEvent.VK_SHIFT);
            if (isCtrl) this.robot.keyRelease(KeyEvent.VK_CONTROL);
        }

        private void releaseNote(char note) {
            int keyCode;

            String noteToPlay = getNoteWithTrans(String.valueOf(note));
            note = noteToPlay.charAt(0);

            if ("!@#$%^&*()".contains(String.valueOf(note)))
                keyCode = KeyEvent.getExtendedKeyCodeForChar(
                        "1234567890".charAt("!@#$%^&*()".indexOf(note))
                );
            else keyCode = KeyEvent.getExtendedKeyCodeForChar(Character.toLowerCase(note));

            if (keyCode == KeyEvent.VK_UNDEFINED) return;
            this.robot.keyRelease(keyCode);
        }

        public void playNote(String note) throws InterruptedException { this.playNote(note.charAt(0)); }
        public void playNote(char note) throws InterruptedException {
            this.pressNote(note);
            Thread.sleep((long) (this.beat * 1000 / 2));
            this.releaseNote(note);
            Thread.sleep((long) (this.beat * 1000 / 2));
        }

        public void playAccord(String accord) throws InterruptedException {
            accord = accord.substring(1, accord.length() - 1);

            for (char note : accord.toCharArray()) this.pressNote(note);
            Thread.sleep((long) (this.beat * 1000 / 2));
            for (char note : accord.toCharArray()) this.releaseNote(note);
            Thread.sleep((long) (this.beat * 1000 / 2));
        }

        public void playSeq(String seq) throws InterruptedException {
            seq = seq.substring(1, seq.length() - 1);

            for (char note : seq.toCharArray()) this.pressNote(note);
            Thread.sleep((long) (this.minimalBeat * 1000));
            for (char note : seq.toCharArray()) this.releaseNote(note);
        }

        public void pause() throws InterruptedException {
            // this.robot.keyPress(KeyEvent.VK_SPACE);
            Thread.sleep((long) (this.shortPause * 1000));
            // this.robot.keyRelease(KeyEvent.VK_SPACE);
        }

        public void longPause() throws InterruptedException {
            // this.robot.keyPress(KeyEvent.VK_SPACE);
            Thread.sleep((long) (this.midPause * 1000));
            // this.robot.keyRelease(KeyEvent.VK_SPACE);
        }

        public void space() throws InterruptedException {
            this.robot.keyPress(KeyEvent.VK_SPACE);
            Thread.sleep((long) (this.minimalBeat / 2 * 1000));
            this.robot.keyRelease(KeyEvent.VK_SPACE);
        }
    }
}

/*public void play(Config.MelodyConfig config, Runnable callback) {
        stop();
        if (config == null) return;
        this.currentMelodyConfig = config;
        this.playing = true;

        this.playThread = new Thread(() -> {
            try { // Pre play pause
                Thread.sleep((long) (3 * 1000));
            } catch (InterruptedException ignored) {
                this.playing = false;
                if (callback != null) SwingUtilities.invokeLater(callback);
                return;
            }

            if (this.playing) {
                // Start playing
                System.out.println("Play " + config.name);

                String[] tabs = config.tabs.split("\\s+");
                for (String tab : tabs) {
                    if (!this.playing) break;
                    try {
                        this.playTab(tab, config.bpm);
                    } catch (InterruptedException e) {
                        // playing = false;
                        break;
                    }
                }
            }

            this.playing = false;
            this.currentMelodyConfig = null;
            if (callback != null) SwingUtilities.invokeLater(callback);
        });

        this.playThread.start();
    }

    @SuppressWarnings("BusyWait")
    private void playTab(String tabText, int tempoBPM) throws InterruptedException {
        if (tabText.isEmpty()) return;
        System.out.println("Playing: " + tabText);

        final float beat = 60f / tempoBPM;      // длительность одного удара
        final float shortPause = beat / 4;      // короткая пауза между нотами
        final float midPause = beat / 2;        // пауза при '|'

        TabIterator iterator = new TabIterator(tabText);

        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.isEmpty()) continue;

            System.out.println("- " + item);

            if (item.equals("|") || item.equals("-")) {
                Thread.sleep((long) (midPause * 1000));
                continue;
            }

            String preArr = item.endsWith("|") || item.endsWith("-") ? item.substring(0, item.length() - 1) : item;
            String[] itemArr = (preArr.startsWith("[") ? preArr.substring(1, preArr.length() - 1) : preArr).split("");

            for (String keyChar : itemArr) {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(keyChar.charAt(0));
                if (keyCode == KeyEvent.VK_UNDEFINED) continue;
                this.robot.keyPress(keyCode);
            }
            Thread.sleep((long) (shortPause * 1000));

            if (item.endsWith("|") || item.endsWith("-")) Thread.sleep((long) (midPause * 1000));
            for (String keyChar : itemArr) {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(keyChar.charAt(0));
                if (keyCode == KeyEvent.VK_UNDEFINED) continue;
                this.robot.keyRelease(keyCode);
            }
            if (!item.endsWith("|") && !item.endsWith("-")) Thread.sleep((long) (shortPause * 1000));

        }
    }*/