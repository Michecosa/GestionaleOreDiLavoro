package esercizio.main;

import esercizio.database.PersonaDAO;

import javax.swing.*;
import java.awt.*;

import static esercizio.main.AppColors.*;
import static esercizio.main.StyleFactory.*;

/**
 * Utility per la visualizzazione di dialog e messaggi all'utente.
 * Raccoglie showSuccess/showInfo/showError e dialog di modifica dati.
 */
public final class DialogUtils {

    private DialogUtils() {} // Classe non istanziabile

    // ── Messaggi ─────────────────────────────────────────────────────────────

    public static void showSuccess(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Operazione Completata", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showInfo(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Informazione", JOptionPane.WARNING_MESSAGE);
    }

    public static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    // ── Dialog modifica dipendente ────────────────────────────────────────────

    /**
     * Apre il dialog modale per modificare l'età di un dipendente.
     *
     * @param parent        componente genitore per la centratura
     * @param personaDAO    DAO per l'aggiornamento sul database
     * @param id            ID del dipendente da modificare
     * @param nomeCorrente  nome visualizzato nell'intestazione del dialog
     * @param onDone        callback eseguita dopo un salvataggio andato a buon fine
     */
    public static void showModificaDialog(Component parent, PersonaDAO personaDAO,
                                          int id, String nomeCorrente, Runnable onDone) {
        JDialog dlg = new JDialog(
                (JFrame) SwingUtilities.getWindowAncestor(parent),
                "Modifica Età", true);
        dlg.setSize(380, 240);
        dlg.setLocationRelativeTo(parent);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Intestazione
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        JLabel lblInfo = new JLabel("Modifica età per: " + nomeCorrente + " (ID: " + id + ")");
        lblInfo.setForeground(TEXT_MAIN);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        p.add(lblInfo, gc);

        // Campo età
        gc.gridy = 1; gc.gridwidth = 1;
        JLabel lblEta = new JLabel("Nuova età:");
        lblEta.setForeground(TEXT_MAIN);
        lblEta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(lblEta, gc);

        gc.gridx = 1;
        JTextField fEta = styledField("");
        p.add(fEta, gc);

        // Bottone salva
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);
        JButton btnSalva = accentButton("Salva modifiche", ACCENT);
        btnSalva.addActionListener(e -> {
            try {
                if (fEta.getText().isBlank()) { showInfo(dlg, "Inserisci la nuova età"); return; }
                int nuovaEta = Integer.parseInt(fEta.getText().trim());
                if (nuovaEta < 18 || nuovaEta > 100) {
                    showInfo(dlg, "Inserisci un'età valida (18-100)"); return;
                }
                if (personaDAO.updateEta(id, nuovaEta)) {
                    onDone.run();
                    showSuccess(parent, "Età aggiornata con successo.");
                    dlg.dispose();
                } else {
                    showError(dlg, "Errore durante l'aggiornamento nel database");
                }
            } catch (NumberFormatException ex) {
                showInfo(dlg, "Inserisci un valore numerico valido per l'età");
            }
        });
        p.add(btnSalva, gc);

        dlg.setContentPane(p);
        dlg.setVisible(true);
    }
}