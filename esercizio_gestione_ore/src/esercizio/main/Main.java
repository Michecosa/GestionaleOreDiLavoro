package esercizio.main;

import esercizio.database.PersonaDAO;
import esercizio.database.TempoDAO;

import javax.swing.*;
import java.awt.*;

import static esercizio.main.AppColors.*;
import static esercizio.main.StyleFactory.*;

/**
 * Punto di ingresso dell'applicazione.
 * Si occupa unicamente di costruire la finestra principale (frame, sidebar,
 * CardLayout) e di delegare la costruzione delle singole schede a PanelBuilder.
 */
public class Main {

    private static final PersonaDAO personaDAO = new PersonaDAO();
    private static final TempoDAO   tempoDAO   = new TempoDAO();

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(Main::buildMainWindow);
    }

    private static void buildMainWindow() {
        JFrame frame = new JFrame("Gestionale Ore");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 680);
        frame.setLocationRelativeTo(null);
        frame.setBackground(BG);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ── Sidebar ──────────────────────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setBackground(PANEL_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER),
                BorderFactory.createEmptyBorder(28, 16, 16, 16)
        ));

        JLabel logo = new JLabel("Gestionale Ore");
        logo.setForeground(ACCENT);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(30));

        // ── CardLayout (schede) ───────────────────────────────────────────────
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG);

        PanelBuilder builder = new PanelBuilder(personaDAO, tempoDAO);

        cardPanel.add(builder.buildDashboard(),       "DASHBOARD");
        cardPanel.add(builder.buildDipendentiPanel(), "DIPENDENTI");
        cardPanel.add(builder.buildAggiungiPanel(),   "AGGIUNGI");
        cardPanel.add(builder.buildTurniPanel(),      "TURNI");

        // ── Bottoni sidebar ───────────────────────────────────────────────────
        String[][] navItems = {
            {"Dashboard",  "DASHBOARD"},
            {"Dipendenti", "DIPENDENTI"},
            {"Aggiungi",   "AGGIUNGI"},
            {"Turni",      "TURNI"},
        };

        for (String[] item : navItems) {
            JButton btn = sidebarButton(item[0]);
            final String card = item[1];
            btn.addActionListener(e -> {
                for (Component c : sidebar.getComponents()) {
                    if (c instanceof JButton && c != e.getSource()) {
                        c.setBackground(PANEL_BG);
                        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    }
                }
                btn.setBackground(new Color(0xE8, 0xF0, 0xFE));
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                cardLayout.show(cardPanel, card);
            });
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(6));
        }

        sidebar.add(Box.createVerticalGlue());

        // Bottone Esci
        JButton btnEsci = sidebarButton("Esci");
        btnEsci.setForeground(DANGER);
        btnEsci.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(frame, "Uscire dall'applicazione?",
                    "Conferma", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0);
        });
        sidebar.add(btnEsci);

        // Seleziona Dashboard all'avvio (componente 2 = primo bottone nav)
        ((JButton) sidebar.getComponent(2)).doClick();

        root.add(sidebar,   BorderLayout.WEST);
        root.add(cardPanel, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}