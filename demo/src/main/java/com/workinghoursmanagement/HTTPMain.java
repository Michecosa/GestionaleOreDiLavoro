package com.workinghoursmanagement;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/hello")
public class HTTPMain extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain"); // Buona pratica
        response.getWriter().println("Ciao dal Servlet!");
    }
}