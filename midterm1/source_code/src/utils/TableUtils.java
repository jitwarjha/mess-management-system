package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Custom table renderers and utilities for beautiful data representation.
 * 
 * Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
 */
public class TableUtils {

    /**
     * Alternating row color renderer with center/left alignment.
     */
    public static class AlternatingRowRenderer extends DefaultTableCellRenderer {
        private final int alignment;

        public AlternatingRowRenderer(int alignment) {
            this.alignment = alignment;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(alignment);
            setBorder(new EmptyBorder(0, 8, 0, 8));

            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(248, 250, 252));
                }
                c.setForeground(UIConstants.TEXT_MAIN);
            }
            return c;
        }
    }

    /**
     * Badge pill renderer for Status / Dietary Choice / Role.
     */
    public static class BadgeCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setFont(UIConstants.FONT_BOLD_SMALL);

            String text = value != null ? value.toString() : "";
            lbl.setText(text);

            Color bg = new Color(241, 245, 249);
            Color fg = UIConstants.TEXT_MAIN;
            Color border = UIConstants.BORDER_COLOR;

            switch (text.toUpperCase()) {
                case "ADMIN":
                    bg = new Color(238, 242, 255); // Indigo
                    fg = new Color(67, 56, 202);
                    border = new Color(199, 210, 254);
                    break;
                case "STUDENT":
                    bg = new Color(240, 253, 244); // Green
                    fg = new Color(22, 101, 52);
                    border = new Color(187, 247, 208);
                    break;
                case "VEG":
                    bg = new Color(240, 253, 244); // Light Green
                    fg = new Color(21, 128, 61);
                    border = new Color(134, 239, 172);
                    break;
                case "NON-VEG":
                    bg = new Color(254, 242, 242); // Red/Rose
                    fg = new Color(185, 28, 28);
                    border = new Color(254, 202, 202);
                    break;
                case "JAIN":
                    bg = new Color(254, 249, 195); // Amber / Yellow
                    fg = new Color(161, 98, 7);
                    border = new Color(253, 224, 71);
                    break;
                case "SPECIAL":
                    bg = new Color(243, 232, 255); // Purple
                    fg = new Color(126, 34, 206);
                    border = new Color(216, 180, 254);
                    break;
                case "NOT_EATING":
                case "NOT EATING":
                    bg = new Color(241, 245, 249); // Slate Gray
                    fg = new Color(71, 85, 105);
                    border = new Color(203, 213, 225);
                    break;
                case "ALL":
                    bg = new Color(239, 246, 255); // Blue
                    fg = new Color(29, 78, 216);
                    border = new Color(191, 219, 254);
                    break;
            }

            if (!isSelected) {
                lbl.setBackground(bg);
                lbl.setForeground(fg);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        new EmptyBorder(2, 6, 2, 6),
                        new LineBorder(border, 1, true)
                ));
            }

            return lbl;
        }
    }
}
