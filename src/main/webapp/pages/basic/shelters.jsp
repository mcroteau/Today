<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Select an Organization</title>
</head>
<body>

    <style>
        h3{
            margin:40px auto 4px !important;
        }
        table{
            margin:0px auto 40px auto;
        }
    </style>

    <p class="yellow" style="display: inline-block">Please help! <strong class="highlight">${count} in need!</strong></p>

    <h1>Organizations</h1>

    <p>Please select an organization:</p>

    <c:forEach var="town" items="${prospects}">
        <h3>${town.name}</h3>
        <table>
            <c:forEach var="prospect" items="${town.prospects}">
                <tr>
                    <td class="center" style="padding-left:10px;width:50%">
                        <a href="/z/donate/${prospect.id}" class="href-dotted">${prospect.name}</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:forEach>

    <p>or</p>

    <p>Give to Dynamics <strong>+Gain</strong></p>

    <div style="margin-bottom:70px;">
        <a href="/z/donate" class="button light small">Give Now &hearts;</a>
    </div>

</body>
</html>
