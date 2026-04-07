package esercizio.main;

import javax.swing.*;
import java.awt.*;

import static esercizio.main.AppColors.*;

/**
 * Utility per la costruzione di form con layout a griglia (GridBagLayout).
 * Fornisce metodi per aggiungere righe label+campo in modo uniforme.
 */
public final class FormUtils {

    private FormUtils() {} // Classe non istanziabile

    /**
     * Aggiunge una riga (etichetta testo + componente campo) al pannello form.
     *
     * @param p      pannello con GridBagLayout
     * @param gc     GridBagConstraints da riutilizzare
     * @param row    indice di riga nella griglia
     * @param label  testo dell'etichetta
     * @param field  componente del campo (TextField, ComboBox, ecc.)
     */
    public static void addFormRow(JPanel p, GridBagConstraints gc,
                                  int row, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_MAIN);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addFormRow(p, gc, row, lbl, field);
    }

    /**
     * Aggiunge una riga (componente etichetta + componente campo) al pannello form.
     * Usato quando l'etichetta ha bisogno di stile personalizzato o è dinamica.
     *
     * @param p      pannello con GridBagLayout
     * @param gc     GridBagConstraints da riutilizzare
     * @param row    indice di riga nella griglia
     * @param label  componente etichetta personalizzato
     * @param field  componente del campo
     */
    public static void addFormRow(JPanel p, GridBagConstraints gc,
                                  int row, JComponent label, JComponent field) {
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        p.add(label, gc);
        gc.gridx = 1; gc.weightx = 1;
        field.setPreferredSize(new Dimension(250, 38));
        p.add(field, gc);
    }
}