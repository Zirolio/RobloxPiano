package com.zirolio.robloxpiano;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class SongPlayer {
    private final Robot robot;
    private boolean playing = false;
    private Thread playThread;

    public SongPlayer() throws AWTException {
        this.robot = new Robot();
    }

    public void stop() {
        this.playing = false;
        if (this.playThread != null && this.playThread.isAlive()) {
            this.playThread.interrupt();
        }
    }

    /**
     * Проигрывает песню.
     *
     * @param songTabs строка с нотами/аккордами, разделёнными пробелами, аккорды в [abc]
     * @param tempoBPM темп в ударах в минуту (если <=0, игнорируется)
     */
    public void play(String songTabs, int tempoBPM) {
        stop();
        this.playing = true;

        this.playThread = new Thread(() -> {
            String[] tabs = songTabs.split("\\s+");
            for (String tab : tabs) {
                if (!this.playing) break;
                try {
                    this.playTab(tab, tempoBPM);
                } catch (InterruptedException e) {
                    playing = false;
                    break;
                }
            }
            this.playing = false;
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

            if (item.equals("|")) {
                Thread.sleep((long) (midPause * 1000));
                continue;
            }

            String preArr = item.endsWith("|") ? item.substring(0, item.length() - 1) : item;
            String[] itemArr = (preArr.startsWith("[") ? preArr.substring(1, preArr.length() - 1) : preArr).split("");

            System.out.println("-- " + preArr + " " + Arrays.toString(itemArr));

            for (String keyChar : itemArr) {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(keyChar.charAt(0));
                if (keyCode == KeyEvent.VK_UNDEFINED) continue;
                this.robot.keyPress(keyCode);
            }
            Thread.sleep((long) (shortPause * 1000));

            if (item.endsWith("|")) Thread.sleep((long) (midPause * 1000));
            for (String keyChar : itemArr) {
                int keyCode = KeyEvent.getExtendedKeyCodeForChar(keyChar.charAt(0));
                if (keyCode == KeyEvent.VK_UNDEFINED) continue;
                this.robot.keyRelease(keyCode);
            }
            if (!item.endsWith("|")) Thread.sleep((long) (shortPause * 1000));

        }
    }



    private static final SongPlayer instance;
    static {
        try {
            instance = new SongPlayer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SongPlayer getInstance() {
        return instance;
    }
}
