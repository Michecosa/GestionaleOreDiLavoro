package esercizio.main;

import esercizio.database.PersonaDAO;
import esercizio.database.TempoDAO;
import esercizio.persona.Designer;
import esercizio.persona.Sviluppatore;
import esercizio.persona.Tester;
import esercizio.tempo.Mese;

import javax.swing.*;
//import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
//import java.util.ArrayList;
import java.time.YearMonth;

public class Main {

    // ── Palette colori ───────────────────────────
    private static final Color BG = new Color(0xF4, 0xF7, 0xF6); // Sfondo molto chiaro, quasi bianco
    private static final Color PANEL_BG = Color.WHITE; // Pannelli bianchi per stacco
    private static final Color CARD_BG = Color.WHITE; // Schede bianche
    private static final Color ACCENT = new Color(0x00, 0x56, 0xD2); // Blu scuro e saturo (accessibile)
    private static final Color ACCENT2 = new Color(0x62, 0x00, 0xEA); // Viola scuro (accessibile)
    private static final Color TEXT_MAIN = new Color(0x1A, 0x1D, 0x21); // Testo quasi nero
    private static final Color TEXT_SUB = new Color(0x5F, 0x63, 0x68); // Grigio medio-scuro per sottotitoli
    private static final Color DANGER = new Color(0xD3, 0x2F, 0x2F); // Rosso scuro
    private static final Color SUCCESS = new Color(0x1B, 0x5E, 0x20); // Verde scuro
    private static final Color ROW_ALT = new Color(0xF8, 0xF9, 0xFA); // Grigio chiarissimo per righe alterne
    private static final Color BORDER = new Color(0xDA, 0xDF, 0xE4); // Colore bordi standard

    private static PersonaDAO personaDAO = new PersonaDAO();
    private static TempoDAO   tempoDAO   = new TempoDAO();

    // Punto di ingresso
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(Main::buildMainWindow);
    }

    // Finestra principale
    private static void buildMainWindow() {
        JFrame frame = new JFrame("Gestionale Ore");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 680);
        frame.setLocationRelativeTo(null);
        frame.setBackground(BG);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // Sidebar
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

        // Panel principale (CardLayout)
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG);

        // Schede
        JPanel panelDashboard = buildDashboard();
        JPanel panelDipendenti = buildDipendentiPanel();
        JPanel panelAggiungi = buildAggiungiPanel();
        JPanel panelTurni = buildTurniPanel();

        cardPanel.add(panelDashboard, "DASHBOARD");
        cardPanel.add(panelDipendenti, "DIPENDENTI");
        cardPanel.add(panelAggiungi,  "AGGIUNGI");
        cardPanel.add(panelTurni, "TURNI");

        // Bottoni sidebar 
        String[][] navItems = {
            {"Dashboard", "DASHBOARD"},
            {"Dipendenti", "DIPENDENTI"},
            {"Aggiungi", "AGGIUNGI"},
            {"Turni", "TURNI"},
        };

        for (String[] item : navItems) {
            JButton btn = sidebarButton(item[0]);
            final String card = item[1];
            btn.addActionListener(e -> {
                // Reset background di tutti i bottoni e imposta quello attivo
                for (Component c : sidebar.getComponents()) {
                    if (c instanceof JButton && c != e.getSource()) {
                        c.setBackground(PANEL_BG);
                        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    }
                }
                btn.setBackground(new Color(0xE8, 0xF0, 0xFE)); // Azzurro chiarissimo di selezione
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                cardLayout.show(cardPanel, card);
            });
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(6));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton btnEsci = sidebarButton("Esci");
        btnEsci.setForeground(DANGER);
        btnEsci.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(frame, "Uscire dall'applicazione?",
                    "Conferma", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) System.exit(0);
        });
        sidebar.add(btnEsci);

        // Seleziona dashboard all'avvio
        ((JButton)sidebar.getComponent(2)).doClick(); // Il logo è componente 0, strut 1, primo bottone 2

        root.add(sidebar,  BorderLayout.WEST);
        root.add(cardPanel, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    // Dashboard
    private static JPanel buildDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);
        p.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Benvenuto nel Gestionale Ore");
        title.setForeground(TEXT_MAIN);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel sub = new JLabel("Seleziona una sezione dalla barra laterale per iniziare a gestire dipendenti e ore lavorate.");
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

    // Pannello Dipendenti
    private static JPanel buildDipendentiPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        // -- Titolo + barra ricerca
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel title = sectionTitle("Elenco Dipendenti");
        topBar.add(title, BorderLayout.WEST);

        JTextField searchField = styledField("Cerca per nome...");
        searchField.setPreferredSize(new Dimension(260, 40));
        topBar.add(searchField, BorderLayout.EAST);

        outer.add(topBar, BorderLayout.NORTH);

        // -- Tabella
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

        // -- Pulsanti azioni
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton btnRefresh  = accentButton("Aggiorna lista", ACCENT);
        JButton btnModifica = accentButton("Modifica età", ACCENT2);
        JButton btnElimina  = accentButton("Elimina",       DANGER);

        actions.add(btnRefresh);
        actions.add(btnModifica);
        actions.add(btnElimina);
        outer.add(actions, BorderLayout.SOUTH);

        // -- Carica dati
        Runnable loadDipendenti = () -> {
            model.setRowCount(0);
            for (PersonaDAO.PersonaInfo p : personaDAO.getPersoneCompleto()) {
                model.addRow(new Object[]{p.id, p.nome, p.eta, p.ruolo});
            }
        };

        btnRefresh.addActionListener(e -> loadDipendenti.run());
        loadDipendenti.run(); // carica subito

        // -- Ricerca live
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            void filter() {
                String q = searchField.getText().toLowerCase().replace("cerca per nome...", "").trim();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                table.setRowSorter(sorter);
                if (q.isEmpty()) { sorter.setRowFilter(null); return; }
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + q, 1)); // Filtra su colonna Nome
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        // -- Modifica
        btnModifica.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { showInfo(outer, "Seleziona un dipendente dalla tabella per modificarlo."); return; }
            int modelRow = table.convertRowIndexToModel(row);
            int id       = (int) model.getValueAt(modelRow, 0);
            String nome  = model.getValueAt(modelRow, 1).toString();
            showModificaDialog(outer, id, nome, loadDipendenti);
        });

        // -- Elimina
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

    // Dialog modifica dipendente
    private static void showModificaDialog(Component parent, int id, String nomeCorrente, Runnable onDone) {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parent), "Modifica Età", true);
        dlg.setSize(380, 240);
        dlg.setLocationRelativeTo(parent);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0;
        JLabel lblInfo = new JLabel("Modifica età per: " + nomeCorrente + " (ID: " + id + ")");
        lblInfo.setForeground(TEXT_MAIN);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gc.gridwidth = 2;
        p.add(lblInfo, gc);

        gc.gridy = 1; gc.gridwidth = 1;
        JLabel lblEta = new JLabel("Nuova età:");
        lblEta.setForeground(TEXT_MAIN);
        lblEta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(lblEta, gc);

        gc.gridx = 1;
        JTextField fEta = styledField("");
        p.add(fEta, gc);

        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2;
        gc.insets = new Insets(20, 8, 8, 8);
        JButton btnSalva = accentButton("Salva modifiche", ACCENT);
        btnSalva.addActionListener(e -> {
            try {
                if (fEta.getText().isBlank()) { showInfo(dlg, "Inserisci la nuova età"); return; }
                int nuovaEta = Integer.parseInt(fEta.getText().trim());
                if (nuovaEta < 18 || nuovaEta > 100) { showInfo(dlg, "Inserisci un'età valida (18-100)"); return; }

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

    // Pannello Aggiungi Dipendente
    private static JPanel buildAggiungiPanel() {
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

        // Campi comuni
        JTextField fNome = styledField("Es. Mario Rossi");
        JTextField fEta  = styledField("Es. 30");

        // Campo specifico ruolo
        JTextField fExtra = styledField("");

        // Selezione ruolo
        String[] ruoli = {"Sviluppatore", "Designer", "Tester"};
        JComboBox<String> cbRuolo = new JComboBox<>(ruoli);
        styleCombo(cbRuolo);

        JLabel lblExtra = new JLabel("Linguaggio:");
        lblExtra.setForeground(TEXT_MAIN);
        lblExtra.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cbRuolo.addActionListener(e -> {
            String r = (String) cbRuolo.getSelectedItem();
            fExtra.setText("");
            fExtra.setForeground(TEXT_SUB); // Ripristina colore placeholder
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
        addFormRow(form, gc, r++, "Nome Completo:", fNome);
        addFormRow(form, gc, r++, "Età:", fEta);
        addFormRow(form, gc, r++, "Ruolo Aziendale:", cbRuolo);
        addFormRow(form, gc, r++, lblExtra, fExtra);

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        gc.insets = new Insets(25, 10, 10, 10);
        JButton btnAdd = accentButton("Aggiungi Dipendente", ACCENT);
        form.add(btnAdd, gc);

        btnAdd.addActionListener(e -> {
            try {
                // Rimuovo placeholder se presenti
                String nome  = fNome.getText().equals("Es. Mario Rossi") ? "" : fNome.getText().trim();
                String etaStr = fEta.getText().equals("Es. 30") ? "" : fEta.getText().trim();
                String extra = fExtra.getText().trim();
                String ruolo = (String) cbRuolo.getSelectedItem();

                if (nome.isEmpty() || etaStr.isEmpty() || extra.isEmpty()) {
                    showInfo(outer, "Compila tutti i campi obbligatori."); return;
                }

                int eta = Integer.parseInt(etaStr);
                if (eta < 18 || eta > 100) { showInfo(outer, "Inserisci un'età valida (18-100)."); return; }

                boolean success = false;
                switch (ruolo) {
                    case "Sviluppatore" -> success = personaDAO.createSviluppatore(new Sviluppatore(0, nome, eta, extra));
                    case "Designer" -> success = personaDAO.createDesigner(new Designer(0, nome, eta, extra));
                    case "Tester" -> success = personaDAO.createTester(new Tester(0, nome, eta, extra));
                }

                if (success) {
                    showSuccess(outer, ruolo + " \"" + nome + "\" aggiunto con successo!");
                    // Reset form
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

    // Pannello Turni
    private static JPanel buildTurniPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);
        outer.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        JLabel title = sectionTitle("Gestione Turni e Ore lavorate");
        outer.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(CARD_BG);
        tabs.setForeground(TEXT_MAIN);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.setBorder(BorderFactory.createLineBorder(BORDER));

        // Teniamo riferimento alle combo per poterle ricaricare
        JComboBox<PersonaDAO.PersonaInfo>[] cbOre = new JComboBox[1];
        JComboBox<PersonaDAO.PersonaInfo>[] cbVis = new JComboBox[1];
        JComboBox<TempoDAO.MeseInfo>[] cbMeseOre = new JComboBox[1];
        JComboBox<TempoDAO.MeseInfo>[] cbMeseVis = new JComboBox[1];

        tabs.addTab("  Inizializza Mese  ", buildCreaMesePanel());
        tabs.addTab("  Registra Ore Giorniere  ", buildInserisciOrePanel(cbOre, cbMeseOre));
        tabs.addTab("  Visualizza Resoconto  ", buildVisualizzaPanel(cbVis, cbMeseVis));

        // Ogni volta che si entra nella tab Ore o Resoconto, ricarica i dipendenti
        tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
            if (idx == 1) ricaricaComboDipendenti(cbOre[0], cbMeseOre[0]);
            if (idx == 2) ricaricaComboDipendenti(cbVis[0], cbMeseVis[0]);
        });

        outer.add(tabs, BorderLayout.CENTER);
        return outer;
    }

    // Ricarica la combo dipendenti e aggiorna di conseguenza quella dei mesi
    private static void ricaricaComboDipendenti(
            JComboBox<PersonaDAO.PersonaInfo> cbDip,
            JComboBox<TempoDAO.MeseInfo> cbMese) {
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
        // Aggiorna i mesi in base al dipendente ora selezionato
        ricaricaComboMesi(cbDip, cbMese);
    }

    private static void ricaricaComboMesi(
            JComboBox<PersonaDAO.PersonaInfo> cbDip,
            JComboBox<TempoDAO.MeseInfo> cbMese) {
        if (cbMese == null) return;
        cbMese.removeAllItems();
        PersonaDAO.PersonaInfo sel = (PersonaDAO.PersonaInfo) cbDip.getSelectedItem();
        if (sel == null) return;
        var mesi = tempoDAO.getMesiPerPersona(sel.id);
        cbMese.setEnabled(!mesi.isEmpty());
        for (TempoDAO.MeseInfo mi : mesi) cbMese.addItem(mi);
    }

    private static JPanel buildCreaMesePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Combo dipendenti
        JComboBox<PersonaDAO.PersonaInfo> cbDipendente = new JComboBox<>();
        styleCombo(cbDipendente);
        cbDipendente.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PersonaDAO.PersonaInfo pi)
                    setText(pi.id + " – " + pi.nome + " (" + pi.ruolo + ")");
                setBackground(isSelected ? new Color(0xE8, 0xF0, 0xFE) : Color.WHITE);
                setForeground(TEXT_MAIN);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        for (PersonaDAO.PersonaInfo pi : personaDAO.getPersoneCompleto())
            cbDipendente.addItem(pi);

        JTextField fMese = styledField("1-12");
        JTextField fAnno = styledField("Es. 2026");

        // Label che mostra i giorni calcolati automaticamente
        JLabel lblGiorni = new JLabel("–");
        lblGiorni.setForeground(ACCENT);
        lblGiorni.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Aggiorna il conteggio giorni in tempo reale
        Runnable aggiornaGiorni = () -> {
            try {
                int mese = Integer.parseInt(fMese.getText().replace("1-12","").trim());
                int anno = Integer.parseInt(fAnno.getText().replace("Es. 2026","").trim());
                if (mese >= 1 && mese <= 12 && anno >= 1900) {
                    int giorni = YearMonth.of(anno, mese).lengthOfMonth();
                    lblGiorni.setText(giorni + " giorni");
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

        fMese.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { aggiornaGiorni.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { aggiornaGiorni.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aggiornaGiorni.run(); }
        });
        fAnno.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { aggiornaGiorni.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { aggiornaGiorni.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aggiornaGiorni.run(); }
        });

        addFormRow(p, gc, 0, "Dipendente:", cbDipendente);
        addFormRow(p, gc, 1, "Mese (1-12):", fMese);
        addFormRow(p, gc, 2, "Anno:", fAnno);
        addFormRow(p, gc, 3, "Giorni nel mese:", lblGiorni);

        gc.gridx = 0; gc.gridy = 4; gc.gridwidth = 2;
        gc.insets = new Insets(25, 10, 10, 10);
        JButton btn = accentButton("Crea Foglio Ore Mese", ACCENT);
        p.add(btn, gc);

        btn.addActionListener(e -> {
            try {
                PersonaDAO.PersonaInfo sel = (PersonaDAO.PersonaInfo) cbDipendente.getSelectedItem();
                if (sel == null) { showInfo(p, "Seleziona un dipendente."); return; }

                int mese = Integer.parseInt(fMese.getText().replace("1-12","").trim());
                int anno = Integer.parseInt(fAnno.getText().replace("Es. 2026","").trim());

                if (mese < 1 || mese > 12) { showInfo(p, "Mese non valido (1-12)."); return; }
                if (anno < 1900) { showInfo(p, "Anno non valido."); return; }

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

    private static JPanel buildInserisciOrePanel(
            JComboBox<PersonaDAO.PersonaInfo>[] cbDipRef,
            JComboBox<TempoDAO.MeseInfo>[] cbMeseRef) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Combo dipendenti
        JComboBox<PersonaDAO.PersonaInfo> cbDipendente = new JComboBox<>();
        cbDipRef[0] = cbDipendente;
        styleCombo(cbDipendente);
        cbDipendente.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PersonaDAO.PersonaInfo pi)
                    setText(pi.id + " – " + pi.nome + " (" + pi.ruolo + ")");
                setBackground(isSelected ? new Color(0xE8, 0xF0, 0xFE) : Color.WHITE);
                setForeground(TEXT_MAIN);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        for (PersonaDAO.PersonaInfo pi : personaDAO.getPersoneCompleto())
            cbDipendente.addItem(pi);

        // Combo mesi (si aggiorna al cambio dipendente)
        JComboBox<TempoDAO.MeseInfo> cbMese = new JComboBox<>();
        cbMeseRef[0] = cbMese;
        styleCombo(cbMese);

        Runnable aggiornaMesi = () -> ricaricaComboMesi(cbDipendente, cbMese);

        cbDipendente.addActionListener(e -> aggiornaMesi.run());
        aggiornaMesi.run(); // carica subito

        JTextField fGiorno = styledField("Es. 15");
        JTextField fOre = styledField("Es. 8.0");
        JTextField fNote = styledField("Es. Attività di sviluppo...");

        addFormRow(p, gc, 0, "Dipendente:", cbDipendente);
        addFormRow(p, gc, 1, "Foglio Ore:", cbMese);
        addFormRow(p, gc, 2, "Giorno:", fGiorno);
        addFormRow(p, gc, 3, "Ore Lavorate:", fOre);
        addFormRow(p, gc, 4, "Note:", fNote);

        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        gc.insets = new Insets(25, 10, 10, 10);
        JButton btn = accentButton("Salva Ore Giorno", ACCENT);
        p.add(btn, gc);

        btn.addActionListener(e -> {
            try {
                TempoDAO.MeseInfo meseInfo = (TempoDAO.MeseInfo) cbMese.getSelectedItem();
                if (meseInfo == null) { showInfo(p, "Nessun foglio ore disponibile per questo dipendente."); return; }

                int giorno = Integer.parseInt(fGiorno.getText().replace("Es. 15","").trim());
                double ore = Double.parseDouble(fOre.getText().replace("Es. 8.0","").trim());
                String note = fNote.getText().equals("Es. Attività di sviluppo...") ? "" : fNote.getText().trim();

                if (giorno < 1 || giorno > 31) { showInfo(p, "Giorno non valido."); return; }
                if (ore < 0 || ore > 24) { showInfo(p, "Numero ore non valido."); return; }

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

    private static JPanel buildVisualizzaPanel(
            JComboBox<PersonaDAO.PersonaInfo>[] cbDipRef,
            JComboBox<TempoDAO.MeseInfo>[] cbMeseRef) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // ── Barra selezione ──
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
        cbDipendente.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof PersonaDAO.PersonaInfo pi)
                    setText(pi.id + " – " + pi.nome);
                setBackground(isSelected ? new Color(0xE8, 0xF0, 0xFE) : Color.WHITE);
                setForeground(TEXT_MAIN);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
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

        // ── Tabella ──
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

        // Aggiorna combo mesi al cambio dipendente
        cbDipendente.addActionListener(e -> ricaricaComboMesi(cbDipendente, cbMese));
        // Carica subito i mesi del primo dipendente
        ricaricaComboMesi(cbDipendente, cbMese);

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

    // ── Utility: componenti styled (con colori ad alto contrasto) ─────────────

    private static JButton sidebarButton(String text) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setForeground(TEXT_MAIN); // Testo scuro su fondo bianco
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
            public void mouseExited(MouseEvent e)  { 
                if (!b.getFont().isBold()) b.setBackground(PANEL_BG); 
            }
        });
        return b;
    }

    private static JButton accentButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE); // Testo bianco su bottone colorato scuro
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

    private static JTextField styledField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setBackground(Color.WHITE);
        f.setForeground(TEXT_SUB); // Colore placeholder (grigio scuro)
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

    private static void styleCombo(JComboBox<?> cb) {
        cb.setBackground(Color.WHITE);
        cb.setForeground(TEXT_MAIN);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBorder(BorderFactory.createLineBorder(BORDER));
        // Rende scuro il testo anche nel popup
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setForeground(TEXT_MAIN);
                setBackground(isSelected ? new Color(0xE8, 0xF0, 0xFE) : Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
    }

    private static void styleTable(JTable table) {
        table.setBackground(CARD_BG);
        table.setForeground(TEXT_MAIN); // Testo scuro
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setGridColor(new Color(0xEE, 0xEE, 0xEE));
        table.setSelectionBackground(new Color(0xE0, 0xF2, 0xF1)); // Teal chiarissimo per selezione
        table.setSelectionForeground(TEXT_MAIN);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0xF8, 0xF9, 0xFA));
        header.setForeground(TEXT_MAIN); // Testo scuro
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setPreferredSize(new Dimension(0, 40));

        // Righe alternate
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v,
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

    private static JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_MAIN);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return lbl;
    }

    private static void addFormRow(JPanel p, GridBagConstraints gc, int row, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_MAIN);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addFormRow(p, gc, row, lbl, field);
    }

    private static void addFormRow(JPanel p, GridBagConstraints gc, int row, JComponent label, JComponent field) {
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        p.add(label, gc);
        gc.gridx = 1; gc.weightx = 1;
        field.setPreferredSize(new Dimension(250, 38));
        p.add(field, gc);
    }

    private static void showSuccess(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Operazione Completata", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showInfo(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Informazione", JOptionPane.WARNING_MESSAGE);
    }

    private static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}