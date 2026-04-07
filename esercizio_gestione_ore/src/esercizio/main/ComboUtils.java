package esercizio.main;

import esercizio.database.PersonaDAO;
import esercizio.database.TempoDAO;

import javax.swing.*;
import java.util.List;

/**
 * Utility per la gestione e il ricaricamento delle combo box
 * relative a dipendenti e fogli ore (mesi).
 */
public final class ComboUtils {

    private ComboUtils() {} // Classe non istanziabile

    /**
     * Ricarica la combo dei dipendenti dal database e, dopo l'aggiornamento,
     * ricarica anche la combo dei mesi in base alla nuova selezione.
     * Tenta di ripristinare la selezione precedente se ancora presente.
     *
     * @param cbDip       combo box dei dipendenti da aggiornare
     * @param cbMese      combo box dei mesi da aggiornare di conseguenza
     * @param personaDAO  DAO per recuperare l'elenco dipendenti
     * @param tempoDAO    DAO per recuperare i mesi del dipendente selezionato
     */
    public static void ricaricaComboDipendenti(
            JComboBox<PersonaDAO.PersonaInfo> cbDip,
            JComboBox<TempoDAO.MeseInfo> cbMese,
            PersonaDAO personaDAO,
            TempoDAO tempoDAO) {
        if (cbDip == null) return;

        PersonaDAO.PersonaInfo precedente = (PersonaDAO.PersonaInfo) cbDip.getSelectedItem();
        cbDip.removeAllItems();
        for (PersonaDAO.PersonaInfo pi : personaDAO.getPersoneCompleto())
            cbDip.addItem(pi);

        // Ripristina la selezione precedente se ancora presente
        if (precedente != null) {
            for (int i = 0; i < cbDip.getItemCount(); i++) {
                if (cbDip.getItemAt(i).id == precedente.id) {
                    cbDip.setSelectedIndex(i);
                    break;
                }
            }
        }

        ricaricaComboMesi(cbDip, cbMese, tempoDAO);
    }

    /**
     * Ricarica la combo dei mesi in base al dipendente attualmente selezionato.
     * Se nessun mese è disponibile, disabilita la combo.
     *
     * @param cbDip    combo box dei dipendenti (fonte della selezione)
     * @param cbMese   combo box dei mesi da aggiornare
     * @param tempoDAO DAO per recuperare i mesi del dipendente selezionato
     */
    public static void ricaricaComboMesi(
            JComboBox<PersonaDAO.PersonaInfo> cbDip,
            JComboBox<TempoDAO.MeseInfo> cbMese,
            TempoDAO tempoDAO) {
        if (cbMese == null) return;
        cbMese.removeAllItems();
        PersonaDAO.PersonaInfo sel = (PersonaDAO.PersonaInfo) cbDip.getSelectedItem();
        if (sel == null) return;
        List<TempoDAO.MeseInfo> mesi = tempoDAO.getMesiPerPersona(sel.id);
        cbMese.setEnabled(!mesi.isEmpty());
        for (TempoDAO.MeseInfo mi : mesi) cbMese.addItem(mi);
    }
}