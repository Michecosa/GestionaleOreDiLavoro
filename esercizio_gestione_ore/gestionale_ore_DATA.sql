-- Disabilita temporaneamente i controlli per svuotare tutto
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Giorno;
TRUNCATE TABLE Mese;
TRUNCATE TABLE Sviluppatore;
TRUNCATE TABLE Designer;
TRUNCATE TABLE Tester;
TRUNCATE TABLE Persona;
SET FOREIGN_KEY_CHECKS = 1;

-- Persone 
INSERT INTO Persona (id, nome, eta) VALUES (1, 'Mario Rossi', 30);
INSERT INTO Sviluppatore (id, linguaggio) VALUES (1, 'Java');

INSERT INTO Persona (id, nome, eta) VALUES (2, 'Elena Bianchi', 28);
INSERT INTO Designer (id, tool) VALUES (2, 'Figma');

-- Mesi usando gli ID creati (1 e 2)
-- Qui inseriamo i primi 3 mesi per Mario e i primi 3 per Elena
INSERT INTO Mese (id, numero_giorni, persona_id) VALUES 
(1, 31, 1), (2, 28, 1), (3, 31, 1), -- Mesi di Mario
(4, 31, 2), (5, 28, 2), (6, 31, 2); -- Mesi di Elena

-- Giorni collegati ai Mesi esistenti
INSERT INTO Giorno (numero_giorno, ore_lavorate, note, mese_id) VALUES 
(1, 8.0, 'Sviluppo DAO', 1),   -- Giorno 1 del Mese 1 (Mario)
(1, 8.0, 'Design UI', 4);     -- Giorno 1 del Mese 4 (Elena)