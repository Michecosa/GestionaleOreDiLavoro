CREATE DATABASE IF NOT EXISTS GestionaleOre;
USE GestionaleOre;

-- Tabella base: Persona
CREATE TABLE Persona (
    id    INT PRIMARY KEY AUTO_INCREMENT,
    nome  VARCHAR(100) NOT NULL,
    eta   INT          NOT NULL
);

-- Tabella: Sviluppatore (estende Persona)
CREATE TABLE Sviluppatore (
    id         INT PRIMARY KEY,
    linguaggio VARCHAR(100) NOT NULL,
    CONSTRAINT fk_sviluppatore_persona
        FOREIGN KEY (id) REFERENCES Persona(id) ON DELETE CASCADE
);

-- Tabella: Designer (estende Persona)
CREATE TABLE Designer (
    id   INT PRIMARY KEY,
    tool VARCHAR(100) NOT NULL,
    CONSTRAINT fk_designer_persona
        FOREIGN KEY (id) REFERENCES Persona(id) ON DELETE CASCADE
);

-- Tabella: Tester (estende Persona)
CREATE TABLE Tester (
    id        INT PRIMARY KEY,
    tipo_test VARCHAR(100) NOT NULL,
    CONSTRAINT fk_tester_persona
        FOREIGN KEY (id) REFERENCES Persona(id) ON DELETE CASCADE
);

-- Tabella: Mese (Foglio Ore Mensile)
CREATE TABLE Mese (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    mese_calendario INT NOT NULL,
    anno            INT NOT NULL,
    numero_giorni   INT NOT NULL,
    persona_id      INT NOT NULL,
    CONSTRAINT fk_mese_persona
        FOREIGN KEY (persona_id) REFERENCES Persona(id) ON DELETE CASCADE
);

-- Tabella: Giorno
CREATE TABLE Giorno (
    id            INT PRIMARY KEY AUTO_INCREMENT,
    numero_giorno INT           NOT NULL,
    ore_lavorate  DOUBLE        NOT NULL DEFAULT 0,
    note          VARCHAR(255),
    mese_id       INT           NOT NULL,
    CONSTRAINT fk_giorno_mese
        FOREIGN KEY (mese_id) REFERENCES Mese(id) ON DELETE CASCADE
);