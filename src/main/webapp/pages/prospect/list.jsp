<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Prospects</title>
</head>
<body>
<h1>Prospects</h1>
<a href="${pageContext.request.contextPath}/prospects/create" class="href-dotted">+ New Prospect</a>
<br class="clear"/>
<br class="clear"/>
<c:if test="${prospects.size() > 0}">
    <table>
        <tr>
            <th></th>
            <th>Phone</th>
            <th>Email</th>
            <th></th>
        </tr>
        <c:forEach var="prospect" items="${prospects}">
            <tr>
                <td><a href="${pageContext.request.contextPath}/prospects/edit/${prospect.id}" class="href-dotted-black">${prospect.name}</a></td>
                <td class="center">${prospect.phone}</td>
                <td class="center">${prospect.email}</td>
                <td class="right">
                    <form action="/${pageContext.request.contextPath}/prospects/delete/${prospect.id}" method="post">
                        <input type="submit" class="button small beauty" value="Delete" onclick="return confirm('Are you sure you want to delete this prospect?');"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${prospects.size() == 0}">
    <p>No Prospects created yet.</p>
</c:if>
</body>
</html>
