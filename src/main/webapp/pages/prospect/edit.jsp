<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="sequence.model.Prospect" %>
<html>
<head>
    <title>Edit Prospect</title>
</head>
<body>
<h1>Edit Prospect</h1>
<form action="/z/prospects/update" method="post">

    <input type="hidden" name="id" value="${prospect.id}"/>

    <label>Name</label>
    <input type="text" name="name" value="${prospect.name}"/>

    <label>Phone</label>
    <input type="text" name="phone" value="${prospect.phone}" />

    <label>Email</label>
    <input type="text" name="email" value="${prospect.email}" />

    <label>Status</label>
    <select name="statusId" style="display: block">
        <c:forEach items="${statuses}" var="status">
            <%
                String selected = "";
                Prospect prospect = (Prospect) request.getAttribute("prospect");
            %>
            <c:if test="${prospect.statusId == status.id}">
                <%selected = "selected";%>
            </c:if>

            <option value="${status.id}" <%=selected%>>${status.name}</option>
        </c:forEach>
    </select>

    <input type="submit" class="button retro" value="Update" style="display:inline-block;margin:30px auto;"/>

</form>
</body>
</html>
