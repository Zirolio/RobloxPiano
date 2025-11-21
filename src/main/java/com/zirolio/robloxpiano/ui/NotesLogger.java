package com.zirolio.robloxpiano.ui;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayDeque;

public class NotesLogger extends JPanel {
    private final JLabel label;
    private final ArrayDeque<String> lines = new ArrayDeque<>();

    public NotesLogger() {
        setBackground(new Color(40, 40, 40));
        setBorder(new MatteBorder(1, 1, 1, 1, Color.WHITE));
        setLayout(new BorderLayout());

        label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Consolas", Font.BOLD, 14));
        label.setHorizontalAlignment(SwingConstants.CENTER); // <-- центр

        add(label, BorderLayout.CENTER);

        int lineHeight = label.getFontMetrics(label.getFont()).getHeight();
        setPreferredSize(new Dimension(0, lineHeight * 4));
    }

    public void log(String text) {
        lines.addLast(text);
        while (lines.size() > 3) lines.removeFirst();

        StringBuilder html = new StringBuilder("<html><div style='text-align:center;'>");
        for (String line : lines) {
            html.append(line).append("<br>");
        }
        html.append("</div></html>");

        label.setText(html.toString());
    }

    public void clear() {
        lines.clear();
        label.setText("");
    }
}
