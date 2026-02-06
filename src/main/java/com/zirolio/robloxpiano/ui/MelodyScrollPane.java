package com.zirolio.robloxpiano.ui;

import javax.swing.*;
import java.awt.*;

public class MelodyScrollPane extends JScrollPane {
    public MelodyScrollPane(MelodyListPanel listPanel) {
        super(listPanel);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(16);
        setBorder(BorderFactory.createEmptyBorder());
        getViewport().setBackground(listPanel.getBackground());

        JScrollBar scrollBar = getVerticalScrollBar();
        scrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(100, 100, 100);
                trackColor = new Color(45, 45, 45);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);

                int ARC = 8;
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, ARC, ARC);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0,0));
                b.setMinimumSize(new Dimension(0,0));
                b.setMaximumSize(new Dimension(0,0));
                return b;
            }
        });

    }
}
