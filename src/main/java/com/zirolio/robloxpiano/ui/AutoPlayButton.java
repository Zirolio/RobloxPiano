package com.zirolio.robloxpiano.ui;

import com.zirolio.robloxpiano.Config;
import com.zirolio.robloxpiano.MelodyPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class AutoPlayButton extends JPanel {
    private boolean pressed = false;

    public boolean isPressed() {
        return pressed;
    }

    public AutoPlayButton() {
        setLayout(new BorderLayout(5, 0));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton button = new JButton("Auto Play");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        add(button);
        /* setFont(new Font("Consolas", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(Color.DARK_GRAY);
        setFocusPainted(false);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setOpaque(true); */

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(isPressed() ? Color.YELLOW.darker() : new Color(60, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(isPressed() ? Color.YELLOW : new Color(50, 50, 50));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                AutoPlayButton.this.pressed = !AutoPlayButton.this.pressed;
                if (AutoPlayButton.this.pressed) {
                    button.setBackground(Color.YELLOW);
                    generateRandomSeq();
                    onPlayerStop();
                } else {
                    MelodyPlayer.getInstance().stop();
                    button.setBackground(Color.DARK_GRAY);
                }
            }


            private ArrayList<Config.MelodyConfig> seq;
            private void generateRandomSeq() {
                seq = new ArrayList<>(Config.get());
                Collections.shuffle(seq);
            }

            private void onPlayerStop() {
                if (!AutoPlayButton.this.pressed || seq == null) return;
                if (seq.isEmpty()) generateRandomSeq();
                MelodyPlayer.getInstance().play(seq.removeFirst(), this::onPlayerStop);
            }
        });
    }
}

