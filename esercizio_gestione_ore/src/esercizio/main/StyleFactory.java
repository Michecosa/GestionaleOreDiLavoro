package esercizio.main;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import static esercizio.main.AppColors.*;

/**
 * Factory per componenti Swing stilizzati.
 * Centralizza la creazione di bottoni, campi di testo, combo box,
 * tabelle ed etichette con lo stile grafico dell'applicazione.
 */
public final class StyleFactory {

    private StyleFactory() {} // Classe non istanziabile

    // ── Bottoni ─────────────────────────────────────────────────────────────

    /**
     * Crea un bottone per la sidebar con hover e stile flat.
     */
    public static JButton sidebarButton(String text) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setForeground(TEXT_MAIN);
        b.setBackground(PANEL_BG);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!b.getFont().isBold()) b.setBackground(new Color(0xF0, 0xF2, 0xF5));
            }
            public void mouseExited(MouseEvent e) {
                if (!b.getFont().isBold()) b.setBackground(PANEL_BG);
            }
        });
        return b;
    }

    /**
     * Crea un bottone accent colorato (sfondo pieno, testo bianco).
     */
    public static JButton accentButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(color); }
        });
        return b;
    }

    // ── Campi di testo ───────────────────────────────────────────────────────

    /**
     * Crea un JTextField con placeholder, bordo arrotondato e focus colorato.
     */
    public static JTextField styledField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setBackground(Color.WHITE);
        f.setForeground(TEXT_SUB);
        f.setCaretColor(TEXT_MAIN);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(TEXT_MAIN); }
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 2),
                        BorderFactory.createEmptyBorder(7, 11, 7, 11)));
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(TEXT_SUB); }
                f.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            }
        });
        return f;
    }

    // ── Combo Box ────────────────────────────────────────────────────────────

    /**
     * Applica lo stile standard a un JComboBox esistente.
     */
    public static void styleCombo(JComboBox<?> cb) {
        cb.setBackground(Color.WHITE);
        cb.setForeground(TEXT_MAIN);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBorder(BorderFactory.createLineBorder(BORDER));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setForeground(TEXT_MAIN);
                setBackground(isSelected ? new Color(0xE8, 0xF0, 0xFE) : Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    // ── Tabelle ──────────────────────────────────────────────────────────────

    /**
     * Applica lo stile standard (righe alternate, header, selezione) a una JTable.
     */
    public static void styleTable(JTable table) {
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_MAIN);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setGridColor(new Color(0xEE, 0xEE, 0xEE));
        table.setSelectionBackground(new Color(0xE0, 0xF2, 0xF1));
        table.setSelectionForeground(TEXT_MAIN);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0xF8, 0xF9, 0xFA));
        header.setForeground(TEXT_MAIN);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setPreferredSize(new Dimension(0, 40));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? CARD_BG : ROW_ALT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (v instanceof Double || v instanceof Integer) setHorizontalAlignment(SwingConstants.RIGHT);
                else setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
    }

    // ── Etichette ────────────────────────────────────────────────────────────

    /**
     * Crea un'etichetta titolo di sezione (grande, grassetto).
     */
    public static JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_MAIN);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return lbl;
    }
}