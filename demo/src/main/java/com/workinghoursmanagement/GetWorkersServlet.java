package com.workinghoursmanagement;

import com.workinghoursmanagement.service.PersonaDAO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/get-workers")
public class GetWorkersServlet extends HttpServlet {
    
    private PersonaDAO personaDAO = new PersonaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        try {
            ArrayList<String> nomi = personaDAO.getAllNames();
            
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < nomi.size(); i++) {
                json.append("\"").append(nomi.get(i)).append("\"");
                if (i < nomi.size() - 1) json.append(",");
            }
            json.append("]");
            
            out.print(json.toString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
        out.flush();
    }
}