package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * UI Constants and styling helpers with custom painted, high-contrast components
 * that look beautiful and legible across all operating systems and Look & Feels.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class UIConstants {

    // Modern High-Contrast Color Palette
    public static final Color PRIMARY = new Color(24, 49, 118);       // Deep Navy Blue #183176
    public static final Color PRIMARY_HOVER = new Color(37, 99, 235); // Royal Blue #2563EB
    public static final Color PRIMARY_DARK = new Color(15, 28, 77);   // #0F1C4D
    public static final Color PRIMARY_LIGHT = new Color(96, 165, 250);// Soft Blue #60A5FA

    public static final Color ACCENT = new Color(13, 148, 136);       // Teal #0D9488
    public static final Color ACCENT_HOVER = new Color(15, 118, 110); // Darker Teal
    public static final Color ACCENT_LIGHT = new Color(204, 251, 241);

    public static final Color BG_MAIN = new Color(241, 245, 249);     // Light Slate #F1F5F9
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(203, 213, 225); // #CBD5E1
    public static final Color BORDER_DARK = new Color(148, 163, 184);  // #94A3B8

    public static final Color TEXT_MAIN = new Color(15, 23, 42);       // #0F172A (Deep Slate Black)
    public static final Color TEXT_MUTED = new Color(71, 85, 105);    // #475569
    public static final Color TEXT_LIGHT = Color.WHITE;

    public static final Color COLOR_SUCCESS = new Color(21, 128, 61);  // #15803D
    public static final Color COLOR_SUCCESS_HOVER = new Color(22, 101, 52);
    public static final Color COLOR_DANGER = new Color(185, 28, 28);   // #B91C1C
    public static final Color COLOR_DANGER_HOVER = new Color(153, 27, 27);
    public static final Color COLOR_WARNING = new Color(180, 83, 9);   // #B45309

    // Modern High-Legibility Typography
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_BOLD_SMALL = new Font("Segoe UI", Font.BOLD, 11);

    /**
     * Custom painted modern button that guarantees high-contrast, crisp text
     * on all platforms regardless of Windows Look and Feel overrides.
     */
    public static class CustomStyledButton extends JButton {
        private Color normalBg;
        private Color hoverBg;
        private Color pressedBg;
        private Color normalFg;
        private Color borderColor;
        private int radius = 8;
        private boolean hovered = false;
        private boolean pressed = false;

        public CustomStyledButton(String text, Color bg, Color hover, Color fg, Color border) {
            super(text);
            this.normalBg = bg;
            this.hoverBg = hover;
            this.pressedBg = hover.darker();
            this.normalFg = fg;
            this.borderColor = border;

            setFont(FONT_HEADER);
            setForeground(fg);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBorder(new EmptyBorder(8, 16, 8, 16));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    pressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Select background color based on state
            Color currentBg;
            if (!isEnabled()) {
                currentBg = new Color(226, 232, 240);
            } else if (pressed) {
                currentBg = pressedBg;
            } else if (hovered) {
                currentBg = hoverBg;
            } else {
                currentBg = normalBg;
            }

            // Fill rounded background
            g2.setColor(currentBg);
            g2.fillRoundRect(0, 0, width - 1, height - 1, radius, radius);

            // Draw border
            if (borderColor != null) {
                g2.setColor(!isEnabled() ? new Color(203, 213, 225) : (hovered ? borderColor.darker() : borderColor));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);
            }

            // Draw text
            g2.setFont(getFont());
            Color currentFg = !isEnabled() ? new Color(148, 163, 184) : normalFg;
            g2.setColor(currentFg);

            FontMetrics fm = g2.getFontMetrics();
            Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
            int textX = (width - stringBounds.width) / 2;
            int textY = (height - stringBounds.height) / 2 + fm.getAscent();

            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            return new Dimension(Math.max(size.width + 16, 80), Math.max(size.height, 36));
        }
    }

    /**
     * Creates a high-contrast Primary button (Deep Navy with Crisp White text).
     */
    public static JButton createPrimaryButton(String text) {
        return new CustomStyledButton(text, PRIMARY, PRIMARY_HOVER, Color.WHITE, PRIMARY_DARK);
    }

    /**
     * Creates a high-contrast Secondary button (Teal with Crisp White text).
     */
    public static JButton createSecondaryButton(String text) {
        return new CustomStyledButton(text, ACCENT, ACCENT_HOVER, Color.WHITE, new Color(17, 94, 89));
    }

    /**
     * Creates a high-contrast Danger button (Crimson Red with Crisp White text).
     */
    public static JButton createDangerButton(String text) {
        return new CustomStyledButton(text, COLOR_DANGER, COLOR_DANGER_HOVER, Color.WHITE, new Color(127, 29, 29));
    }

    /**
     * Creates a high-contrast Outline/Light button (Pure White with Dark Slate Navy text).
     */
    public static JButton createLightButton(String text) {
        return new CustomStyledButton(text, Color.WHITE, new Color(241, 245, 249), TEXT_MAIN, BORDER_DARK);
    }

    /**
     * Generic button factory.
     */
    public static JButton createButton(String text, Color bg, Color fg) {
        return new CustomStyledButton(text, bg, bg.brighter(), fg, bg.darker());
    }

    /**
     * Creates a standard styled text field with high contrast.
     */
    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        styleInputComponent(tf);
        return tf;
    }

    public static JPasswordField createPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        styleInputComponent(pf);
        return pf;
    }

    public static void styleInputComponent(JComponent comp) {
        comp.setFont(FONT_REGULAR);
        comp.setBackground(Color.WHITE);
        comp.setForeground(TEXT_MAIN);
        comp.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_DARK, 1, true),
                new EmptyBorder(7, 10, 7, 10)
        ));
    }

    /**
     * Creates a styled card container panel with clean borders and white background.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    /**
     * Creates top header banner for frames.
     */
    public static JPanel createHeaderBanner(String title, String subtitle, String userBadge) {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(TEXT_LIGHT);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(FONT_SMALL);
        lblSub.setForeground(new Color(224, 231, 255)); // Light Indigo

        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        header.add(titlePanel, BorderLayout.WEST);

        if (userBadge != null && !userBadge.isEmpty()) {
            JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            badgePanel.setOpaque(false);

            JLabel lblBadge = new JLabel(userBadge);
            lblBadge.setFont(FONT_HEADER);
            lblBadge.setForeground(TEXT_LIGHT);
            lblBadge.setOpaque(true);
            lblBadge.setBackground(PRIMARY_DARK);
            lblBadge.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_LIGHT, 1, true),
                    new EmptyBorder(6, 12, 6, 12)
            ));

            badgePanel.add(lblBadge);
            header.add(badgePanel, BorderLayout.EAST);
        }

        return header;
    }

    /**
     * Styles standard JTable for clean data representation.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(219, 234, 254)); // Soft blue highlight
        table.setSelectionForeground(TEXT_MAIN);
        table.setGridColor(new Color(226, 232, 240));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        table.getTableHeader().setFont(FONT_HEADER);
        table.getTableHeader().setBackground(new Color(226, 232, 240));
        table.getTableHeader().setForeground(TEXT_MAIN);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setReorderingAllowed(false);
    }
}
