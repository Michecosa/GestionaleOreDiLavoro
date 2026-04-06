<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Lavoratori</title>
    <link rel="stylesheet" href="css/homepageStyle.css">
</head>
<body>
    <h2>Lavoratori</h2>
    <a href="worker-form.html">Nuovo lavoratore</a>

    <!-- DEBUG: mostra numero di lavoratori -->
    <p>Numero di lavoratori: <c:out value="${fn:length(workers)}"/></p>

    <table border="1">
        <tr>
            <th>Nome</th>
            <th>Azioni</th>
        </tr>
        <c:forEach var="w" items="${workers}">
            <tr>
                <td>${w.name}</td>
                <td><a href="workers?action=delete&id=${w.id}">Elimina</a></td>
            </tr>
        </c:forEach>
    </table>

    <!-- DEBUG: stampa lista completa dei lavoratori -->
    <pre>
        <c:forEach var="w" items="${workers}">
            ${w.id} - ${w.name} - ${w.specializationId}
        </c:forEach>
    </pre>
</body>
</html>