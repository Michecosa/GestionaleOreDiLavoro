package esercizio.main;

import esercizio.database.PersonaDAO;
import esercizio.database.TempoDAO;
import esercizio.persona.Designer;
import esercizio.persona.Sviluppatore;
import esercizio.persona.Tester;
import esercizio.tempo.Mese;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.YearMonth;

import static esercizio.main.AppColors.*;
import static esercizio.main.StyleFactory.*;
import static esercizio.main.FormUtils.*;
import static esercizio.main.ComboUtils.*;
import static esercizio.main.DialogUtils.*;

/**
 * Responsabile della costruzione di tutti i pannelli (schede) dell'applicazione:
 * Dashboard, Dipendenti, Aggiungi e Turni (con le sue sotto-schede).
 */
public final class PanelBuilder {

    private final PersonaDAO personaDAO;
    private final TempoDAO   tempoDAO;

    public PanelBuilder(PersonaDAO personaDAO, TempoDAO tempoDAO) {
        this.personaDAO = personaDAO;
        this.tempoDAO   = tempoDAO;
    }

    // ── Dashboard ────────────────────────────────────────────────────────────

    public JPanel buildDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);
        p.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Benvenuto nel Gestionale Ore");
        title.setForeground(TEXT_MAIN);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel sub = new JLabel(
                "Seleziona una sezione dalla barra laterale per iniziare a gestire dipendenti e ore lavorate.");
        sub.setForeground(TEXT_SUB);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(title);
        top.add(Box.createVerticalStrut(12));
        top.add(sub);

        p.add(top, BorderLayout.NORTH);
        return p;
    }

    // ── Dipendenti ───────────────────────────────────────────────────────────

    public JPanel buildDipendentiPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // Barra superiore: titolo + ricerca
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel title = sectionTitle("Elenco Dipendenti");
        topBar.add(title, BorderLayout.WEST);

        JTextField searchField = styledField("Cerca per nome...");
        searchField.setPreferredSize(new Dimension(260, 40));
        topBar.add(searchField, BorderLayout.EAST);

        outer.add(topBar, BorderLayout.NORTH);

        // Tabella
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(CARD_BG);
        tableContainer.setBorder(BorderFactory.createLineBorder(BORDER));

        String[] cols = {"ID", "Nome", "Età", "Ruolo"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 2) return Integer.class;
                return String.class;
            }
        };

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        tableContainer.add(scroll, BorderLayout.CENTER);

        outer.add(tableContainer, BorderLayout.CENTER);

        // Pulsanti azioni
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnRefresh  = accentButton("Aggiorna lista", ACCENT);
        JButton btnModifica = accentButton("Modifica età",   ACCENT2);
        JButton btnElimina  = accentButton("Elimina",        DANGER);

        actions.add(btnRefresh);
        actions.add(btnModifica);
        actions.add(btnElimina);
        outer.add(actions, BorderLayout.SOUTH);

        // Carica dati
        Runnable loadDipendenti = () -> {
            model.setRowCount(0);
            for (PersonaDAO.PersonaInfo p : personaDAO.getPersoneCompleto())
                model.addRow(new Object[]{p.id, p.nome, p.eta, p.ruolo});
        };

        btnRefresh.addActionListener(e -> loadDipendenti.run());
        loadDipendenti.run();

        // Ricerca live
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            void filter() {
                String q = searchField.getText().toLowerCase().replace("cerca per nome...", "").trim();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                table.setRowSorter(sorter);
                if (q.isEmpty()) { sorter.setRowFilter(null); return; }
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + q, 1));
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        // Modifica età
        btnModifica.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { showInfo(outer, "Seleziona un dipendente dalla tabella per modificarlo."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int id       = (int) model.getValueAt(modelRow, 0);
            String nome  = model.getValueAt(modelRow, 1).toString();
            showModificaDialog(outer, personaDAO, id, nome, loadDipendenti);
        });

        // Elimina
        btnElimina.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { showInfo(outer, "Seleziona un dipendente da eliminare."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int id       = (int) model.getValueAt(modelRow, 0);
            String nome  = model.getValueAt(modelRow, 1).toString();

            int conf = JOptionPane.showConfirmDialog(outer,
                    "Sei sicuro di voler eliminare definitivamente " + nome + " (ID " + id + ")?",
                    "Conferma eliminazione",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf == JOptionPane.YES_OPTION) {
                if (personaDAO.delete(id)) {
                    loadDipendenti.run();
                    showSuccess(outer, "Dipendente eliminato con successo.");
                } else {
                    showError(outer, "Impossibile eliminare il dipendente. Verificare se ha turni registrati.");
                }
            }
        });

        return outer;
    }

    // ── Aggiungi Dipendente ───────────────────────────────────────────────────

    public JPanel buildAggiungiPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        outer.add(sectionTitle("Registra Nuovo Dipendente"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JTextField fNome  = styledField("Es. Mario Rossi");
        JTextField fEta   = styledField("Es. 30");
        JTextField fExtra = styledField("");

        String[] ruoli = {"Sviluppatore", "Designer", "Tester"};
        JComboBox<String> cbRuolo = new JComboBox<>(ruoli);
        styleCombo(cbRuolo);

        JLabel lblExtra = new JLabel("Linguaggio:");
        lblExtra.setForeground(TEXT_MAIN);
        lblExtra.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cbRuolo.addActionListener(e -> {
            String r = (String) cbRuolo.getSelectedItem();
            fExtra.setText("");
            fExtra.setForeground(TEXT_SUB);
            if ("Sviluppatore".equals(r)) {
                lblExtra.setText("Linguaggio principale:");
                fExtra.setToolTipText("Es. Java, Python");
            } else if ("Designer".equals(r)) {
                lblExtra.setText("Tool di design:");
                fExtra.setToolTipText("Es. Figma, Adobe XD");
            } else {
                lblExtra.setText("Tipo di testing:");
                fExtra.setToolTipText("Es. Unit, Functional, UI");
            }
        });
        cbRuolo.getActionListeners()[0].actionPerformed(null);

        int r = 0;
        addFormRow(form, gc, r++, "Nome Completo:",   fNome);
        addFormRow(form, gc, r++, "Età:",              fEta);
        addFormRow(form, gc, r++, "Ruolo Aziendale:", cbRuolo);
        addFormRow(form, gc, r++, lblExtra,            fExtra);

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        gc.insets = new Insets(25, 10, 10, 10);
        JButton btnAdd = accentButton("Aggiungi Dipendente", ACCENT);
        form.add(btnAdd, gc);

        btnAdd.addActionListener(e -> {
            try {
                String nome   = fNome.getText().equals("Es. Mario Rossi") ? "" : fNome.getText().trim();
                String etaStr = fEta.getText().equals("Es. 30")           ? "" : fEta.getText().trim();
                String extra  = fExtra.getText().trim();
                String ruolo  = (String) cbRuolo.getSelectedItem();

                if (nome.isEmpty() || etaStr.isEmpty() || extra.isEmpty()) {
                    showInfo(outer, "Compila tutti i campi obbligatori."); return;
                }

                int eta = Integer.parseInt(etaStr);
                if (eta < 18 || eta > 100) { showInfo(outer, "Inserisci un'età valida (18-100)."); return; }

                boolean success = false;
                switch (ruolo) {
                    case "Sviluppatore" -> success = personaDAO.createSviluppatore(new Sviluppatore(0, nome, eta, extra));
                    case "Designer"     -> success = personaDAO.createDesigner(new Designer(0, nome, eta, extra));
                    case "Tester"       -> success = personaDAO.createTester(new Tester(0, nome, eta, extra));
                }

                if (success) {
                    showSuccess(outer, ruolo + " \"" + nome + "\" aggiunto con successo!");
                    fNome.setText(""); fNome.setForeground(TEXT_SUB); fNome.setText("Es. Mario Rossi");
                    fEta.setText("");  fEta.setForeground(TEXT_SUB);  fEta.setText("Es. 30");
                    fExtra.setText("");
                } else {
                    showError(outer, "Errore durante il salvataggio del dipendente.");
                }
            } catch (NumberFormatException ex) {
                showInfo(outer, "Inserisci un valore numerico valido per l'età.");
            }
        });

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.setOpaque(false);
        center.add(form);
        outer.add(center, BorderLayout.CENTER);
        return outer;
    }

    // ── Turni ────────────────────────────────────────────────────────────────

    public JPanel buildTurniPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        outer.add(sectionTitle("Gestione Turni e Ore lavorate"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(CARD_BG);
        tabs.setForeground(TEXT_MAIN);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.setBorder(BorderFactory.createLineBorder(BORDER));

        @SuppressWarnings("unchecked")
        JComboBox<PersonaDAO.PersonaInfo>[] cbOre = new JComboBox[1];
        @SuppressWarnings("unchecked")
        JComboBox<PersonaDAO.PersonaInfo>[] cbVis = new JComboBox[1];
        @SuppressWarnings("unchecked")
        JComboBox<TempoDAO.MeseInfo>[] cbMeseOre = new JComboBox[1];
        @SuppressWarnings("unchecked")
        JComboBox<TempoDAO.MeseInfo>[] cbMeseVis = new JComboBox[1];

        tabs.addTab("  Inizializza Mese  ",         buildCreaMesePanel());
        tabs.addTab("  Registra Ore Giorniere  ",    buildInserisciOrePanel(cbOre, cbMeseOre));
        tabs.addTab("  Visualizza Resoconto  ",      buildVisualizzaPanel(cbVis, cbMeseVis));

        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 1) ricaricaComboDipendenti(cbOre[0], cbMeseOre[0], personaDAO, tempoDAO);
            if (idx == 2) ricaricaComboDipendenti(cbVis[0], cbMeseVis[0], personaDAO, tempoDAO);
        });

        outer.add(tabs, BorderLayout.CENTER);
        return outer;
    }

    // ── Sotto-scheda: Crea Mese ───────────────────────────────────────────────

    private JPanel buildCreaMesePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<PersonaDAO.PersonaInfo> cbDipendente = new JComboBox<>();
        styleCombo(cbDipendente);
        cbDipendente.setRenderer(personaInfoRenderer());
        for (PersonaDAO.PersonaInfo pi : personaDAO.getPersoneCompleto())
            cbDipendente.addItem(pi);

        JTextField fMese = styledField("1-12");
        JTextField fAnno = styledField("Es. 2026");

        JLabel lblGiorni = new JLabel("–");
        lblGiorni.setForeground(ACCENT);
        lblGiorni.setFont(new Font("Segoe UI", Font.BOLD, 14));

        Runnable aggiornaGiorni = () -> {
            try {
                int mese = Integer.parseInt(fMese.getText().replace("1-12", "").trim());
                int anno = Integer.parseInt(fAnno.getText().replace("Es. 2026", "").trim());
                if (mese >= 1 && mese <= 12 && anno >= 1900) {
                    lblGiorni.setText(YearMonth.of(anno, mese).lengthOfMonth() + " giorni");
                    lblGiorni.setForeground(ACCENT);
                } else {
                    lblGiorni.setText("mese/anno non validi");
                    lblGiorni.setForeground(DANGER);
                }
            } catch (NumberFormatException ex) {
                lblGiorni.setText("–");
                lblGiorni.setForeground(TEXT_SUB);
            }
        };

        javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { aggiornaGiorni.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { aggiornaGiorni.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aggiornaGiorni.run(); }
        };
        fMese.getDocument().addDocumentListener(dl);
        fAnno.getDocument().addDocumentListener(dl);

        addFormRow(p, gc, 0, "Dipendente:",     cbDipendente);
        addFormRow(p, gc, 1, "Mese (1-12):",    fMese);
        addFormRow(p, gc, 2, "Anno:",            fAnno);
        addFormRow(p, gc, 3, "Giorni nel mese:", lblGiorni);

        gc.gridx = 0; gc.gridy = 4; gc.gridwidth = 2;
        gc.insets = new Insets(25, 10, 10, 10);
        JButton btn = accentButton("Crea Foglio Ore Mese", ACCENT);
        p.add(btn, gc);

        btn.addActionListener(e -> {
            try {
                PersonaDAO.PersonaInfo sel = (PersonaDAO.PersonaInfo) cbDipendente.getSelectedItem();
                if (sel == null) { showInfo(p, "Seleziona un dipendente."); return; }

                int mese = Integer.parseInt(fMese.getText().replace("1-12", "").trim());
                int anno = Integer.parseInt(fAnno.getText().replace("Es. 2026", "").trim());

                if (mese < 1 || mese > 12) { showInfo(p, "Mese non valido (1-12)."); return; }
                if (anno < 1900)            { showInfo(p, "Anno non valido."); return; }

                int giorni = YearMonth.of(anno, mese).lengthOfMonth();
                Mese m = new Mese(mese, anno, giorni, sel.id);
                int newId = tempoDAO.creaMese(m);

                if (newId > 0) {
                    showSuccess(p, "Foglio ore creato per " + sel.nome + "!\n" +
                            YearMonth.of(anno, mese).getMonth()
                                    .getDisplayName(java.time.format.TextStyle.FULL,
                                                    java.util.Locale.ITALIAN)
                            + " " + anno + " — " + giorni + " giorni.");
                } else {
                    showError(p, "Impossibile creare il foglio ore. Il mese potrebbe esistere già per questo dipendente.");
                }
            } catch (NumberFormatException ex) {
                showInfo(p, "Compila Mese e Anno con valori numerici validi.");
            }
        });

        return p;
    }

    // ── Sotto-scheda: Inserisci Ore ───────────────────────────────────────────

    private JPanel buildInserisciOrePanel(
            JComboBox<PersonaDAO.PersonaInfo>[] cbDipRef,
            JComboBox<TempoDAO.MeseInfo>[] cbMeseRef) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<PersonaDAO.PersonaInfo> cbDipendente = new JComboBox<>();
        cbDipRef[0] = cbDipendente;
        styleCombo(cbDipendente);
        cbDipendente.setRenderer(personaInfoRendererConRuolo());
        for (PersonaDAO.PersonaInfo pi : personaDAO.getPersoneCompleto())
            cbDipendente.addItem(pi);

        JComboBox<TempoDAO.MeseInfo> cbMese = new JComboBox<>();
        cbMeseRef[0] = cbMese;
        styleCombo(cbMese);

        Runnable aggiornaMesi = () -> ricaricaComboMesi(cbDipendente, cbMese, tempoDAO);
        cbDipendente.addActionListener(e -> aggiornaMesi.run());
        aggiornaMesi.run();

        JTextField fGiorno = styledField("Es. 15");
        JTextField fOre    = styledField("Es. 8.0");
        JTextField fNote   = styledField("Es. Attività di sviluppo...");

        addFormRow(p, gc, 0, "Dipendente:",   cbDipendente);
        addFormRow(p, gc, 1, "Foglio Ore:",   cbMese);
        addFormRow(p, gc, 2, "Giorno:",        fGiorno);
        addFormRow(p, gc, 3, "Ore Lavorate:", fOre);
        addFormRow(p, gc, 4, "Note:",          fNote);

        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        gc.insets = new Insets(25, 10, 10, 10);
        JButton btn = accentButton("Salva Ore Giorno", ACCENT);
        p.add(btn, gc);

        btn.addActionListener(e -> {
            try {
                TempoDAO.MeseInfo meseInfo = (TempoDAO.MeseInfo) cbMese.getSelectedItem();
                if (meseInfo == null) { showInfo(p, "Nessun foglio ore disponibile per questo dipendente."); return; }

                int giorno    = Integer.parseInt(fGiorno.getText().replace("Es. 15", "").trim());
                double ore    = Double.parseDouble(fOre.getText().replace("Es. 8.0", "").trim());
                String note   = fNote.getText().equals("Es. Attività di sviluppo...") ? "" : fNote.getText().trim();

                if (giorno < 1 || giorno > 31) { showInfo(p, "Giorno non valido."); return; }
                if (ore < 0 || ore > 24)        { showInfo(p, "Numero ore non valido."); return; }

                if (tempoDAO.salvaOreGiorno(meseInfo.id, giorno, ore, note)) {
                    showSuccess(p, "Ore registrate per il giorno " + giorno + ".");
                    fGiorno.setText(""); fOre.setText(""); fNote.setText("");
                } else {
                    showError(p, "Impossibile salvare le ore.");
                }
            } catch (NumberFormatException ex) {
                showInfo(p, "Inserisci valori numerici validi per Giorno e Ore");
            }
        });

        return p;
    }

    // ── Sotto-scheda: Visualizza Resoconto ────────────────────────────────────

    private JPanel buildVisualizzaPanel(
            JComboBox<PersonaDAO.PersonaInfo>[] cbDipRef,
            JComboBox<TempoDAO.MeseInfo>[] cbMeseRef) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Barra selezione
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lblDip = new JLabel("Dipendente:");
        lblDip.setForeground(TEXT_MAIN);
        lblDip.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<PersonaDAO.PersonaInfo> cbDipendente = new JComboBox<>();
        cbDipRef[0] = cbDipendente;
        styleCombo(cbDipendente);
        cbDipendente.setPreferredSize(new Dimension(230, 38));
        cbDipendente.setRenderer(personaInfoRenderer());
        for (PersonaDAO.PersonaInfo pi : personaDAO.getPersoneCompleto())
            cbDipendente.addItem(pi);

        JLabel lblMese = new JLabel("Foglio Ore:");
        lblMese.setForeground(TEXT_MAIN);
        lblMese.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<TempoDAO.MeseInfo> cbMese = new JComboBox<>();
        cbMeseRef[0] = cbMese;
        styleCombo(cbMese);
        cbMese.setPreferredSize(new Dimension(260, 38));

        JButton btn = accentButton("Carica Resoconto", ACCENT);

        top.add(lblDip); top.add(cbDipendente);
        top.add(Box.createHorizontalStrut(8));
        top.add(lblMese); top.add(cbMese);
        top.add(Box.createHorizontalStrut(8));
        top.add(btn);
        p.add(top, BorderLayout.NORTH);

        // Tabella
        String[] cols = {"Giorno", "Ore Lavorate", "Note"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));

        JLabel lblTotale = new JLabel(" ");
        lblTotale.setForeground(SUCCESS);
        lblTotale.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotale.setBorder(BorderFactory.createEmptyBorder(15, 5, 0, 0));

        p.add(scroll, BorderLayout.CENTER);
        p.add(lblTotale, BorderLayout.SOUTH);

        cbDipendente.addActionListener(e -> ricaricaComboMesi(cbDipendente, cbMese, tempoDAO));
        ricaricaComboMesi(cbDipendente, cbMese, tempoDAO);

        btn.addActionListener(e -> {
            TempoDAO.MeseInfo meseInfo = (TempoDAO.MeseInfo) cbMese.getSelectedItem();
            if (meseInfo == null) { showInfo(p, "Seleziona un dipendente e un foglio ore"); return; }

            Mese m = tempoDAO.getMeseCompleto(meseInfo.id);
            model.setRowCount(0);
            lblTotale.setText(" ");

            if (m == null) { showInfo(p, "Nessun dato trovato"); return; }

            for (var g : m.getGiorni()) {
                model.addRow(new Object[]{
                        "Giorno " + g.getNumeroGiorno(),
                        String.format("%.1f h", g.getOreLavorate()),
                        g.getNote()
                });
            }
            lblTotale.setText("Totale ore nel mese: " + String.format("%.1f", m.calcolaOreTotali()) + " h");
        });

        return p;
    }

    // ── Renderer riutilizzabili ───────────────────────────────────────────────

    /** Renderer per PersonaInfo: mostra "id – nome" */
    private static DefaultListCellRenderer personaInfoRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PersonaDAO.PersonaInfo pi)
                    setText(pi.id + " – " + pi.nome);
                setBackground(isSelected ? new Color(0xE8, 0xF0, 0xFE) : Color.WHITE);
                setForeground(TEXT_MAIN);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        };
    }

    /** Renderer per PersonaInfo: mostra "id – nome (ruolo)" */
    private static DefaultListCellRenderer personaInfoRendererConRuolo() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PersonaDAO.PersonaInfo pi)
                    setText(pi.id + " – " + pi.nome + " (" + pi.ruolo + ")");
                setBackground(isSelected ? new Color(0xE8, 0xF0, 0xFE) : Color.WHITE);
                setForeground(TEXT_MAIN);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        };
    }
}