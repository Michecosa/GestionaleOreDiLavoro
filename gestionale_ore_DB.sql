CREATE DATABASE IF NOT EXISTS GestionaleOre;
USE GestionaleOre;

-- Tabella base: Persona
CREATE TABLE Persona (
    id       INT PRIMARY KEY AUTO_INCREMENT,
    nome     VARCHAR(100) NOT NULL,
    eta      INT          NOT NULL
);

-- Tabella: Sviluppatore (estende Persona)
CREATE TABLE Sviluppatore (
    id         INT PRIMARY KEY,
    linguaggio VARCHAR(100) NOT NULL,
    FOREIGN KEY (id) REFERENCES Persona(id)
);

-- Tabella: Designer (estende Persona)
CREATE TABLE Designer (
    id   INT PRIMARY KEY,
    tool VARCHAR(100) NOT NULL,
    FOREIGN KEY (id) REFERENCES Persona(id)
);

-- Tabella: Tester (estende Persona)
CREATE TABLE Tester (
    id         INT PRIMARY KEY,
    tipo_test  VARCHAR(100) NOT NULL,
    FOREIGN KEY (id) REFERENCES Persona(id)
);

-- Tabella: Mese
CREATE TABLE Mese (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    numero_giorni   INT NOT NULL
);

-- Tabella: Giorno
CREATE TABLE Giorno (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    numero_giorno   INT             NOT NULL,
    ore_lavorate    DOUBLE          NOT NULL DEFAULT 0,
    note            VARCHAR(255),
    mese_id         INT             NOT NULL,
    FOREIGN KEY (mese_id) REFERENCES Mese(id)
);