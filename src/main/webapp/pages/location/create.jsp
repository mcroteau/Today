<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Create Location</title>
</head>
<body>
<h1>Create Location</h1>
<form action="/z/admin/prospects/save" method="post">

    <label>Name</label>
    <input type="text" name="name" />

    <label>Uri</label>
    <input type="text" name="locationUri" />

    <label>Town</label>
    <select name="townId" style="display: block">
        <c:forEach items="${towns}" var="town">
            <option value="${town.id}">${town.name}</option>
        </c:forEach>
    </select>

    <input type="submit" class="button retro" value="Save" style="display:inline-block;margin:30px auto;"/>
</form>
</body>
</html>
