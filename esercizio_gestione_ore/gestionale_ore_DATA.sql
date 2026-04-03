USE GestionaleOre;

-- ==========================================
-- 1. POPOLAMENTO TABELLA PERSONA
-- ==========================================
INSERT INTO Persona (nome, eta) VALUES 
('Alice Rossi', 28),   -- Diventerà ID 1
('Marco Bianchi', 34), -- Diventerà ID 2
('Giulia Verdi', 25);  -- Diventerà ID 3

-- ==========================================
-- 2. POPOLAMENTO RUOLI (TABELLE FIGLIE)
-- ==========================================
INSERT INTO Sviluppatore (id, linguaggio) VALUES 
(1, 'Java');

-- Marco (ID 2) è Designer
INSERT INTO Designer (id, tool) VALUES 
(2, 'Figma');

-- Giulia (ID 3) è Tester
INSERT INTO Tester (id, tipo_test) VALUES 
(3, 'Automated Testing');

-- ==========================================
-- 3. POPOLAMENTO MESI
-- ==========================================
-- mese_calendario (1-12) e anno (es. 2026)
INSERT INTO Mese (mese_calendario, anno, numero_giorni, persona_id) VALUES 
(1, 2026, 31, 1), -- Mese ID 1: Gennaio 2026 per Alice (Persona ID 1)
(1, 2026, 31, 2), -- Mese ID 2: Gennaio 2026 per Marco (Persona ID 2)
(2, 2026, 28, 3); -- Mese ID 3: Febbraio 2026 per Giulia (Persona ID 3)

-- inserisco Febbraio per Alice e Marco, e Marzo per tutti.
INSERT INTO Mese (mese_calendario, anno, numero_giorni, persona_id) VALUES 
(2, 2026, 28, 1), -- Mese ID 4: Febbraio 2026 per Alice (Sviluppatrice)
(2, 2026, 28, 2), -- Mese ID 5: Febbraio 2026 per Marco (Designer)

(3, 2026, 31, 1), -- Mese ID 6: Marzo 2026 per Alice (Sviluppatrice)
(3, 2026, 31, 2), -- Mese ID 7: Marzo 2026 per Marco (Designer)
(3, 2026, 31, 3); -- Mese ID 8: Marzo 2026 per Giulia (Tester)

-- ==========================================
-- 4. POPOLAMENTO GIORNI
-- ==========================================
-- Inserisco le ore lavorate collegate agli ID della tabella Mese (1, 2 e 3)

-- Giorni lavorati da Alice a Gennaio 2026 (Mese ID 1)
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(1, 8.0, 'Sviluppo backend API', 1),
(2, 7.5, 'Bug fixing su database', 1),
(3, 4.0, 'Riunione e code review', 1);

-- Giorni lavorati da Marco a Gennaio 2026 (Mese ID 2)
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(1, 6.0, 'Creazione wireframe', 2),
(2, 8.0, 'Prototipazione UI', 2),
(3, 8.0, 'Revisione grafica con il cliente', 2);

-- Giorni lavorati da Giulia a Febbraio 2026 (Mese ID 3)
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(1, 8.0, 'Scrittura test E2E', 3),
(2, 4.0, 'Esecuzione test manuali', 3);

-- --- FEBBRAIO (Alice, Mese ID 4) ---
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(5, 8.0, 'Integrazione nuovo gateway di pagamento', 4),
(6, 8.0, 'Test unitari e documentazione API', 4),
(10, 4.0, 'Permesso medico mattinata, pomeriggio sviluppo', 4),
(14, 8.0, 'Risoluzione bug critico in produzione', 4),
(20, 0.0, 'Ferie', 4); -- Si può anche inserire 0 per tracciare le ferie o assenze

-- --- FEBBRAIO (Marco, Mese ID 5) ---
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(5, 6.5, 'Design nuova dashboard utente', 5),
(6, 8.0, 'Creazione icone personalizzate in Figma', 5),
(12, 8.0, 'Riunione col reparto marketing e revisione bozze', 5),
(25, 5.0, 'Ottimizzazione asset grafici per il web', 5);

-- --- MARZO (Alice, Mese ID 6) ---
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(2, 8.0, 'Kick-off nuovo progetto: architettura DB', 6),
(3, 8.0, 'Scrittura query SQL ottimizzate', 6),
(15, 9.5, 'Straordinario per rilascio versione 2.0', 6),
(16, 8.0, 'Monitoraggio post-rilascio', 6);

-- --- MARZO (Marco, Mese ID 7) ---
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(2, 8.0, 'Brainstorming interfaccia mobile', 7),
(10, 7.0, 'Aggiornamento design system aziendale', 7),
(11, 8.0, 'Passaggio mockup agli sviluppatori', 7);

-- --- MARZO (Giulia, Mese ID 8) ---
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(4, 8.0, 'Creazione suite di test per il nuovo progetto', 8),
(5, 8.0, 'Test di carico sul server', 8),
(16, 8.0, 'Test di regressione dopo il rilascio della V2.0', 8),
(17, 6.0, 'Apertura ticket bug e meeting con Alice', 8);