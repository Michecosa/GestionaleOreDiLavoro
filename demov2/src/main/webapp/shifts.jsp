<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Calendario Turni</title>
    <link rel="stylesheet" href="css/homepageStyle.css">
</head>
<body>
    <h1>Calendario Turni</h1>

    <a href="shifts?month=${calendar.previousMonth}">←</a>
    ${calendar.yearMonth}
    <a href="shifts?month=${calendar.nextMonth}">→</a>

    <!-- DEBUG: mostra numero di chiavi nella mappa -->
    <p>Numero di date con turni: <c:out value="${fn:length(shiftsByDate)}"/></p>

    <table border="1">
        <tr>
            <th>Lun</th><th>Mar</th><th>Mer</th><th>Gio</th>
            <th>Ven</th><th>Sab</th><th>Dom</th>
        </tr>
        <c:forEach var="week" items="${calendar.weeks}">
            <tr>
                <c:forEach var="day" items="${week}">
                    <td>
                        <c:if test="${day != null}">
                            <strong>${day.dayOfMonth}</strong>
                            <!-- DEBUG: mostra lista dei turni per la data -->
                            <c:out value="Numero turni: ${fn:length(shiftsByDate[day])}"/><br/>

                            <c:forEach var="s" items="${shiftsByDate[day]}">
                                <div>${s.worker.name} (${s.shiftType})</div>
                            </c:forEach>
                        </c:if>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</body>
</html>