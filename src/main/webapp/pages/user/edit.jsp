<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.strongperched.Parakeet" %>

	<c:if test="${not empty error}">
	    <div class="notify">${error}</div>
	</c:if>

    <h1>Your Profile</h1>


    <a href="/${pageContext.request.contextPath}/user/edit_password/${user.id}" class="href-dotted" style="display:inline-block;margin-top:60px;">Update Password</a>
	

