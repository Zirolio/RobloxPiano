package com.zirolio.robloxpiano.ui;

import com.zirolio.robloxpiano.Config;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PlayerUI extends JFrame {
    public final MelodyListPanel listPanel;
    public final MelodyScrollPane scrollPane;
    public final MelodyDownloaderButton downloader;
    public final NotesLogger notesLogger;

    private PlayerUI() {
        super("Roblox Piano");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(true);

        // CONTENT
        JPanel content = new JPanel();
        content.setBackground(new Color(0, 0, 0));
        content.setBorder(new MatteBorder(0, 1, 1, 1, Color.WHITE));
        content.setLayout(new BorderLayout());

        listPanel = new MelodyListPanel();
        scrollPane = new MelodyScrollPane(listPanel);
        downloader = new MelodyDownloaderButton();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        notesLogger = new NotesLogger();
        topPanel.add(notesLogger, BorderLayout.NORTH);
        AutoPlayButton autoButton = new AutoPlayButton();
        topPanel.add(autoButton, BorderLayout.SOUTH);

        content.add(topPanel, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(downloader, BorderLayout.SOUTH);

        listPanel.refreshMelodyList();

        setContentPane(content);
        setSize(350, 500);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - getWidth(), 0);
        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Insets insets = listPanel.getBorder().getBorderInsets(null);
                int width = scrollPane.getViewport().getWidth() - insets.right - insets.left;

                for (Component comp : listPanel.getComponents()) {
                    if (comp instanceof MelodyButton btn) {
                        btn.onParentResize(width);
                    }
                }

                listPanel.revalidate();
                listPanel.repaint();
            }
        });
    }

    // Globalize
    private static PlayerUI INSTANCE;
    public static void Init() { INSTANCE = new PlayerUI(); }
    public static PlayerUI getInstance() { return INSTANCE; }
}
