package com.zirolio.robloxpiano.ui;

import com.zirolio.robloxpiano.Config;
import com.zirolio.robloxpiano.MelodyDownloader;
import com.zirolio.robloxpiano.MelodyPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MelodyDownloaderButton extends JPanel {

    public MelodyDownloaderButton() {
        setLayout(new BorderLayout(5, 0));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ===================== INPUT FIELD =====================
        JTextField inputField = new JTextField();
        inputField.setBackground(new Color(40, 40, 40));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // ===================== TEMPO FIELD =====================
        JSpinner tempoSpinner = new JSpinner(
                new SpinnerNumberModel(100, 10, 999, 1)
        );
        tempoSpinner.setBackground(new Color(40, 40, 40));
        tempoSpinner.setForeground(Color.WHITE);
        tempoSpinner.setFont(new Font("Arial", Font.PLAIN, 14));

        JComponent editor = tempoSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor ed) {
            ed.getTextField().setBackground(new Color(40, 40, 40));
            ed.getTextField().setForeground(Color.WHITE);
            ed.getTextField().setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        tempoSpinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // ===================== PLAY BUTTON =====================
        JButton button = new JButton("Play");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        // hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getBackground().equals(new Color(0, 150, 0)) &&
                        !button.getBackground().equals(new Color(150, 0, 0)))
                    button.setBackground(new Color(60, 60, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getBackground().equals(new Color(0, 150, 0)) &&
                        !button.getBackground().equals(new Color(150, 0, 0)))
                    button.setBackground(new Color(50, 50, 50));
            }
        });

        // ===================== LAYOUT =====================
        add(inputField, BorderLayout.CENTER);
        add(tempoSpinner, BorderLayout.WEST);
        add(button, BorderLayout.EAST);

        // ===================== LOGIC =====================
        button.addMouseListener(new MouseAdapter() {
            private boolean playing = false;

            private void onStopPlaying() {
                this.playing = false;
                button.setBackground(new Color(50, 50, 50));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (this.playing) {
                    MelodyPlayer.getInstance().stop();
                    return;
                }

                if (!inputField.getText().isEmpty()) {
                    try {
                        int tempo = (int) tempoSpinner.getValue();
                        button.setBackground(new Color(0, 120, 120)); // loading

                        Config.MelodyConfig melodyConfig;

                        if (inputField.getText().startsWith("https://")) {
                            melodyConfig = MelodyDownloader.load(inputField.getText());
                            Config.addMelody(melodyConfig);
                        } else {
                            melodyConfig = Config.MelodyConfig.from(
                                    inputField.getText(),
                                    tempo
                            );
                        }

                        button.setBackground(new Color(0, 150, 0)); // play
                        this.playing = true;
                        MelodyPlayer.getInstance().play(melodyConfig, this::onStopPlaying);

                    } catch (Exception ex) {
                        button.setBackground(new Color(150, 0, 0)); // error
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}

