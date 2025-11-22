package com.zirolio.robloxpiano.ui;

import com.zirolio.robloxpiano.Config;
import com.zirolio.robloxpiano.MelodyPlayer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MelodyButton extends JPanel {
    private final String fullName;
    private final String shortName;

    private final String fullDate;
    private final String shortDate;

    private final JLabel nameLabel;
    private final JLabel subtitle;
    private final JLabel playIcon;
    private final JLabel transLabel;
    private final JLabel dateLabel;

    private final Color baseColor = new Color(45, 45, 45);
    private final Color hoverColor = new Color(60, 60, 60);
    private final Color activeColor = new Color(30, 110, 60);
    private final Border border = BorderFactory.createEmptyBorder(10, 12, 10, 12);
    private Color currentColor = baseColor;

    public MelodyButton(String name, String author, String date, String playTime, int trans) {
        this.fullName = name;
        this.shortName = extractShort(name);
        this.fullDate = date;
        this.shortDate = convertToShortDate(date);

        setOpaque(false);
        setLayout(new BorderLayout(10, 0));
        setBorder(border);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // LEFT ICON
        playIcon = new JLabel("▶");
        playIcon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        playIcon.setForeground(Color.WHITE);
        add(playIcon, BorderLayout.LINE_START);

        // CENTER BLOCK
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        nameLabel = new JLabel(fullName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        subtitle = new JLabel(author + " • " + playTime);
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        center.add(nameLabel);
        center.add(subtitle);
        add(center, BorderLayout.CENTER);

        // RIGHT BLOCK
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        transLabel = new JLabel("T: " + trans);
        transLabel.setForeground(new Color(255, 170, 0));
        transLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        transLabel.setAlignmentX(RIGHT_ALIGNMENT);

        dateLabel = new JLabel(date);
        dateLabel.setForeground(new Color(150, 150, 150));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setAlignmentX(RIGHT_ALIGNMENT);

        right.add(transLabel);
        right.add(dateLabel);
        right.setMinimumSize(new Dimension(0, 0));
        right.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        add(right, BorderLayout.LINE_END);

        // MOUSE EVENTS
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MelodyPlayer pl = MelodyPlayer.getInstance();
                if (pl.isPlaying() && pl.getCurrentMelodyConfig() != null
                        && pl.getCurrentMelodyConfig().name.equals(name)) {
                    pl.stop();
                    playIcon.setText("▶");
                    setInactive();
                    return;
                }

                setActive();
                playIcon.setText("■");

                pl.play(Config.getMelodyConfig(name), () -> SwingUtilities.invokeLater(() -> {
                    playIcon.setText("▶");
                    setInactive();
                }));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive()) {
                    currentColor = hoverColor;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive()) setInactive();
            }
        });

        setMinimumSize(new Dimension(0, getPreferredSize().height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
    }

    public void onParentResize(int maxWidth) {
        int rightWidth = 90;
        setPreferredSize(new Dimension(maxWidth, getPreferredSize().height));
        setMaximumSize(new Dimension(maxWidth, getPreferredSize().height));

        adaptTitle(maxWidth - rightWidth);

        revalidate();
        repaint();
    }

    private String extractShort(String t) {
        int s = t.indexOf('(');
        int e = t.indexOf(')');
        if (s != -1 && e != -1) return t.substring(s + 1, e);
        return null;
    }

    private void adaptTitle(int availableWidth) {
        if (availableWidth <= 0) return;

        FontMetrics fm = nameLabel.getFontMetrics(nameLabel.getFont());

        // --- Обработка имени в приоритет ---
        int fullNameWidth = fm.stringWidth(fullName);
        String nameToShow = fullName;

        if (fullNameWidth > availableWidth) {
            if (shortName != null && fm.stringWidth(shortName) <= availableWidth) {
                nameToShow = shortName;
            } else {
                String dots = "...";
                int dotsWidth = fm.stringWidth(dots);
                for (int i = fullName.length() - 1; i > 0; i--) {
                    String sub = fullName.substring(0, i);
                    if (fm.stringWidth(sub) + dotsWidth <= availableWidth) {
                        nameToShow = sub + dots;
                        break;
                    }
                }
            }
        }

        nameLabel.setText(nameToShow);

        if (dateLabel != null) {
            int nameWidthUsed = fm.stringWidth(nameToShow);
            int spaceLeft = availableWidth - nameWidthUsed;
            if (spaceLeft > 0) {
                int longDateWidth = fm.stringWidth(fullDate);
                int shortDateWidth = fm.stringWidth(shortDate);

                if (longDateWidth <= spaceLeft) {
                    dateLabel.setText(fullDate);
                } else if (shortDateWidth <= spaceLeft) {
                    dateLabel.setText(shortDate);
                } else {
                    dateLabel.setText("");
                }
            } else {
                dateLabel.setText("");
            }
        }
    }


    private void setActive() {
        currentColor = activeColor;
        repaint();
    }

    private void setInactive() {
        currentColor = baseColor;
        repaint();
    }

    private boolean isActive() {
        return currentColor == activeColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, currentColor.brighter(), 0, getHeight(), currentColor.darker());
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
    }

    private static String convertToShortDate(String longDate) {
        String shortDate = longDate;

        shortDate = shortDate.replace("January", "01");
        shortDate = shortDate.replace("February", "02");
        shortDate = shortDate.replace("March", "03");
        shortDate = shortDate.replace("April", "04");
        shortDate = shortDate.replace("May", "05");
        shortDate = shortDate.replace("June", "06");
        shortDate = shortDate.replace("July", "07");
        shortDate = shortDate.replace("August", "08");
        shortDate = shortDate.replace("September", "09");
        shortDate = shortDate.replace("October", "10");
        shortDate = shortDate.replace("November", "11");
        shortDate = shortDate.replace("December", "12");

        shortDate = shortDate.replace(" ", ".");
        return shortDate;
    }
}
