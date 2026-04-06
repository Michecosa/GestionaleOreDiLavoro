package service;
// Definisce il package in cui si trova questa classe

import dao.WorkerDAO;
// Importa la classe WorkerDAO per interagire con il database dei lavoratori

import model.Worker;
// Importa il modello Worker che rappresenta un lavoratore

import java.util.List;
// Importa la classe List per gestire elenchi di lavoratori

public class WorkerService {
    // Classe di servizio che gestisce la logica di business relativa ai lavoratori
    // Fa da intermediario tra il controller/servlet ed il DAO (accesso al database)

    private WorkerDAO dao = new WorkerDAO();
    // Crea un'istanza di WorkerDAO per comunicare con il database

    public List<Worker> getAllWorkers() {
        return dao.getAll();
        // Richiama il metodo DAO per ottenere tutti i lavoratori dal database
        // Restituisce una lista di oggetti Worker
    }

    public void addWorker(String name, int specId) {
        dao.insert(name, specId);
        // Aggiunge un nuovo lavoratore al database
        // name: nome del lavoratore
        // specId: id della specializzazione
    }

    public void deleteWorker(int id) {
        dao.delete(id);
        // Elimina un lavoratore dal database tramite il suo id
    }
}