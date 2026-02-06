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
    public final TransToggle transToggle;

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
        transToggle = new TransToggle(true);
        notesLogger = new NotesLogger();
        AutoPlayButton autoButton = new AutoPlayButton();

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(transToggle);
        topPanel.add(notesLogger);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.add(autoButton, BorderLayout.NORTH);
        bottomPanel.add(downloader, BorderLayout.SOUTH);

        content.add(topPanel, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(bottomPanel, BorderLayout.SOUTH);

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
