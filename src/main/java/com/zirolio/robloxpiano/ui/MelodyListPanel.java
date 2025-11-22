package com.zirolio.robloxpiano.ui;

import com.zirolio.robloxpiano.Config;

import javax.swing.*;
import java.awt.*;

public class MelodyListPanel extends JPanel {
    public MelodyListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setBackground(new Color(40, 40, 40));
    }

    public void refreshMelodyList() {
        removeAll();
        int width = getParent() instanceof JViewport viewport
                ? viewport.getWidth()
                : 350;

        boolean first = true;
        for (Config.MelodyConfig cfg : Config.get()) {
            if (first) first = false;
            else add(Box.createVerticalStrut(8));

            MelodyButton btn = new MelodyButton(cfg.name, cfg.author, cfg.date, cfg.playTime, cfg.trans);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.onParentResize(width);
            add(btn);
        }

        revalidate();
        repaint();
    }
}
