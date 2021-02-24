<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Overview</title>
</head>
<body>
<style>

</style>
<div id="overview-wrapper">

    <c:if test="${not empty message}">
        <p class="notify">${message}</p>
    </c:if>


    <h1>Overview</h1>
    <c:forEach items="${efforts}" var="effort">
        <c:if test="${effort.prospectActivities.size() > 0}">
            <p>
                <c:forEach items="${effort.prospectActivities}" var="activity">
                   ${activity.name} >
                </c:forEach>
            </p>

        </c:if>
    </c:forEach>

</div>
</body>