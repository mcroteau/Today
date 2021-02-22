<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Towns</title>
</head>
<body>
<h1>Locations</h1>
<a href="/z/admin/prospects/create" class="href-dotted">+ New Location</a>
<c:if test="${prospects.size() > 0}">
    <table>
        <tr>
            <th></th>
            <th>Town ID</th>
            <th>Count</th>
            <th></th>
        </tr>
        <c:forEach var="prospect" items="${prospects}">
            <tr>
                <td><a href="/z/admin/prospects/edit/${prospect.id}" class="href-dotted-black">${prospect.name}</a></td>
                <td class="center">${prospect.townId}</td>
                <td class="center"><a href="/z/admin/count/${prospect.id}" class="href-dotted-black">${prospect.count}</a></td>
                <td class="right">
                    <form action="/z/admin/prospects/delete/${prospect.id}" method="post">
                        <input type="submit" class="button small beauty" value="Delete" onclick="return confirm('Are you sure you want to delete Prospect?');"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${prospects.size() == 0}">
    <p>No towns created yet.</p>
</c:if>
</body>
</html>
