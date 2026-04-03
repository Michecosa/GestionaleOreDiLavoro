package com.workinghoursmanagement;

import java.util.Map;
import java.util.HashMap;
import com.workinghoursmanagement.model.Mese;
import com.workinghoursmanagement.service.TempoDAO;
import com.workinghoursmanagement.service.PersonaDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/get-turni")
public class GetTurniServlet extends HttpServlet {
    private TempoDAO tempoDAO = new TempoDAO();
    private PersonaDAO personaDAO = new PersonaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Leggiamo l'id del mese passato come parametro (es. get-turni?id=1)
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("{\"error\": \"ID mese mancante\"}");
            return;
        }

        try {
            int meseId = Integer.parseInt(idParam);
            Mese mese = tempoDAO.getMeseCompleto(meseId);

            if (mese != null) {
                // RECUPERIAMO IL NOME DEL LAVORATORE
                String nomeLavoratore = personaDAO.getNomeById(mese.getPersonaId());

                // Creiamo un oggetto di risposta "arricchito"
                Map<String, Object> risposta = new HashMap<>();
                risposta.put("nome", nomeLavoratore);
                risposta.put("meseId", mese.getId());
                risposta.put("giorni", mese.getGiorni());
                risposta.put("totaleOre", mese.calcolaOreTotali()); 

                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(risposta));
            }
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}