package controller;
// Definisce il package in cui si trova questa classe

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
// Importa le classi necessarie per creare servlet Jakarta EE

import java.io.IOException;
import java.util.List;
// Importa classi per gestione eccezioni I/O e collezioni

import model.Worker;
import service.WorkerService;
// Importa il modello Worker ed il servizio WorkerService

@WebServlet("/workers")
// Mappa la servlet all’URL /workers

public class WorkerController extends HttpServlet {
    // Servlet che gestisce le richieste HTTP relative ai lavoratori

    private WorkerService service = new WorkerService();
    // Istanza del service per gestire la logica di business dei lavoratori

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Gestisce le richieste GET (visualizzazione e cancellazione)

        String action = req.getParameter("action");
        // Legge il parametro "action" dalla richiesta

        if ("delete".equals(action)) {
            // Se l'azione è "delete", elimina il lavoratore

            int id = Integer.parseInt(req.getParameter("id"));
            // Recupera l'id del lavoratore da eliminare
            service.deleteWorker(id);
            // Chiama il service per eliminare il suddetto lavoratore
            resp.sendRedirect("workers");
            // Reindirizza di nuovo alla lista dei lavoratori
            return;
        }

        List<Worker> workers = service.getAllWorkers();
        // Recupera tutti i lavoratori dal service

        req.setAttribute("workers", workers);
        // Imposta la lista dei lavoratori come attributo della richiesta

        req.getRequestDispatcher("workers.jsp").forward(req, resp);
        // Invia la richiesta e la lista dei lavoratori alla JSP per la visualizzazione
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Gestisce le richieste POST (inserimento di un nuovo lavoratore)

        String name = req.getParameter("name");
        int specId = Integer.parseInt(req.getParameter("specId"));
        // Legge i parametri name e specId dal form

        service.addWorker(name, specId);
        // Chiama il service per aggiungere il nuovo lavoratore

        resp.sendRedirect("workers");
        // Reindirizza di nuovo alla lista dei lavoratori dopo l’inserimento
    }
}