<%@ page import="sequence.model.Town" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="sequence.model.Prospect" %>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="sequence.common.Constants" %>
<%@ page import="sequence.model.Prospect" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Location</title>
</head>
<body>
<h1>Edit Location</h1>
<form action="/z/admin/prospects/update" method="post">

    <input type="hidden" name="id" value="${prospect.id}"/>
    <input type="hidden" name="count" value="${prospect.count}"/>

    <label>Name</label>
    <input type="text" name="name" value="${prospect.name}"/>

    <label>Uri</label>
    <input type="text" name="locationUri" value="${prospect.locationUri}" />

    <label>Town</label>
    <select name="townId" style="display: block">
        <c:forEach items="${towns}" var="town">
            <%
                String selected = "";
                Prospect prospect = (Prospect) request.getAttribute("prospect");
            %>
            <c:if test="${prospect.townId == town.id}">
                <%selected = "selected";%>
            </c:if>

            <option value="${town.id}" <%=selected%>>${town.name}</option>
        </c:forEach>
    </select>

    <label>Description</label>
    <textarea name="description">${prospect.description}</textarea>

<%--    <label>Needs</label>--%>
<%--    <textarea name="needs">${prospect.needs}</textarea>--%>

    <label>Stripe Dev Key</label>
    <input type="text" name="devKey" value="${prospect.devKey}"/>

    <label>Stripe Live Key</label>
    <input type="text" name="liveKey" value="${prospect.liveKey}"/>

    <input type="submit" class="button retro" value="Update" style="display:inline-block;margin:30px auto;"/>

<%--    <% if(Parakeet.hasRole(Constants.ROLE_ADMIN)){ %>--%>
<%--        <ul>--%>
<%--            <c:forEach items="${userLocations}" var="userLocation">--%>
<%--                <li>${userLocation.username}--%>
<%--                    <c:if test="${userLocation.approved}">--%>
<%--                        <form action="/z/prospect/revoke/${userLocation.locationId}" method="post">--%>
<%--                            --%>
<%--                        </form>--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${!userLocation.approved}">--%>
<%--                        <form action="/z/prospect/approve/${userLocation.locationId}" method="post">--%>

<%--                        </form>--%>
<%--                    </c:if>--%>
<%--                </li>--%>
<%--            </c:forEach>--%>
<%--        </ul>--%>
<%--    <%}%>--%>

</form>
</body>
</html>
