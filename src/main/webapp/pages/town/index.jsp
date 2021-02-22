<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Dynamics +Gain: ${town.name} Locations</title>
</head>
<body>

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <p style="font-size:17px;">Please help!</p>
    <br/>
    <p id="welcome-text">
        <strong class="yellow" style="line-height: 1.3em;">${town.countZero}</strong><br/> <span>Homeless <span class="header-information"> in
            <strong class="highlight">${town.name}</strong></span></span>
    </p>

    <p class="open-text left">Dynamics +Gain is a non profit designed with
        the sole purpose of removing barriers that prevent people from
        giving time, money and resources to those in need!</p>

    <p class="left">Select an organization to get more information or to donate to them.</p>


    <h3>Organizations &amp; Shelters</h3>
    <table>
        <c:forEach var="prospect" items="${prospects}">
            <tr>
                <td class="center">
                    <a href="/z/donate/${prospect.id}" class="href-dotted">${prospect.name}</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <p>or</p>

    <p>Give to Dynamics <strong>+Gain</strong></p>

    <div style="margin-bottom:70px;">
        <a href="/z/donate" class="button beauty small">Give +</a>
    </div>

</body>
</html>
