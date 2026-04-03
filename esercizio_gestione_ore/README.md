# Gruppo
- Michela
- Donato
- Dennis

## Progetto
Creare un gestionale per le ore di lavoro con grafica

### Classe Astratta: Persona
- Attributi: nome (String), id (int), tariffaOraria (double).

- Metodo Astratto: calcolaStipendio(double oreTotale) -> Ogni ruolo potrebbe avere bonus diversi


#### Classi Figlie: Sviluppatore, Tester, Designer
Ereditano da Persona

- Override di descriviRuolo() per personalizzare l'interfaccia


### Classe: Giorno (La singola "Casella")
Invece di usare solo un numero, creiamo un oggetto per ogni cella del calendario.

- Attributi: numeroGiorno (int), oreLavorate (double), note (String - es. "Ferie", "Lavoro straordinario").

- Metodi: aggiungiOre(double ore), resetta()


### Classe: Mese (Il contenitore)
Questa è la classe che gestisce l'array di "caselle".

- Attributi: nomeMese (String), giorni (un Array di oggetti Giorno).

- Metodi: * getTotaleOre(): Cicla l'array e somma le ore.

- stampaProspetto(): Crea una stringa gigante con tutti i giorni per la JOptionPane


### Classe: Main



pacchetti: persona, tempo, main