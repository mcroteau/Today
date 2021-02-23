<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Prospect</title>
</head>
<body>
<h1>Create Prospect</h1>
<form action="/z/prospects/save" method="post">

    <label>Name</label>
    <input type="text" name="name" />

    <label>Uri</label>
    <input type="text" name="locationUri" />

    <label>Status</label>
    <select name="statusId" style="display: block">
        <c:forEach items="${statuses}" var="status">
            <option value="${status.id}">${status.name}</option>
        </c:forEach>
    </select>

    <input type="submit" class="button retro" value="Save Prospect" style="display:inline-block;margin:30px auto;"/>
</form>
</body>
</html>
