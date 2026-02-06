package com.zirolio.robloxpiano.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TransToggle extends JToggleButton {

    private final Color baseColor = new Color(45, 45, 45);
    private final Color hoverColor = new Color(70, 70, 70);
    private final Color activeColor = new Color(255, 170, 0);
    private Color currentColor = baseColor;

    private boolean hovered = false;

    private boolean choiced = false;
    public boolean isChoiced() { return choiced; }
    public void setChoiced(boolean choiced) { this.choiced = choiced; }

    public TransToggle() { this(false); }
    public TransToggle(boolean isActive) {
        super("Auto trans");
        choiced = isActive;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                updateColor();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                updateColor();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                setChoiced(!isChoiced());
                updateColor();
            }
        });

        updateColor();
    }

    private void updateColor() {
        if (isChoiced()) {
            currentColor = activeColor;
        } else if (hovered) {
            currentColor = hoverColor;
        } else {
            currentColor = baseColor;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = getHeight() / 2;
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        super.paintComponent(g);
    }
}
