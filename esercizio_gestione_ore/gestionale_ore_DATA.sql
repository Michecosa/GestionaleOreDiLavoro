-- Inserimento Sviluppatore
INSERT INTO Persona (nome, eta) VALUES ('Mario Rossi', 30);
INSERT INTO Sviluppatore (id, linguaggio) VALUES (1, 'Java');

-- Inserimento Designer
INSERT INTO Persona (nome, eta) VALUES ('Elena Bianchi', 28);
INSERT INTO Designer (id, tool) VALUES (2, 'Figma');

-- Inserimento Tester
INSERT INTO Persona (nome, eta) VALUES ('Luca Verdi', 35);
INSERT INTO Tester (id, tipo_test) VALUES (3, 'Automated Selenium');

-- Inserimento Mese
INSERT INTO Mese (numero_giorni) VALUES 
(31), -- Gennaio (ID 1)
(28), -- Febbraio (ID 2 - 29 se bisestile)
(31), -- Marzo (ID 3)
(30), -- Aprile (ID 4)
(31), -- Maggio (ID 5)
(30), -- Giugno (ID 6)
(31), -- Luglio (ID 7)
(31), -- Agosto (ID 8)
(30), -- Settembre (ID 9)
(31), -- Ottobre (ID 10)
(30), -- Novembre (ID 11)
(31); -- Dicembre (ID 12)

-- Es. mese_id = 4 Aprile (30 giorni)
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(1, 8.0, 'Sviluppo interfacce DAO', 4),
(2, 8.0, 'Debug connessione JDBC', 4),
(3, 9.5, 'Straordinari per migrazione DB', 4),
(4, 8.0, 'Documentazione tecnica', 4),
(5, 6.0, 'Permesso pomeridiano', 4),
(6, 0.0, 'Sabato - Riposo', 4),
(7, 0.0, 'Domenica - Riposo', 4);

-- Registrazioni per Mario Rossi (Sviluppatore) nel mese ID 1
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(1, 8.0, 'Sviluppo CRUD Database', 1),
(2, 7.5, 'Fix bug su login', 1),
(3, 4.0, 'Meeting tecnico (mezza giornata)', 1);

-- Registrazioni per Elena Bianchi (Designer) nel mese ID 1
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(1, 8.0, 'Wireframe nuova Dashboard', 1),
(2, 0.0, 'Ferie', 1);