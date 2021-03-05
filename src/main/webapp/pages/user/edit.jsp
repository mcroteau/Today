<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="xyz.strongperched.Parakeet" %>

	<c:if test="${not empty error}">
	    <div class="notify">${error}</div>
	</c:if>

    <h1>Your Profile</h1>

	<c:if test="${activityCounts.size() > 0}">
		<h3>Great Job!</h3>
		<p>You're always doing a great job.
		Let's see what you've been up to.</p>
		<c:forEach items="${activityCounts}" var="activityCount">
			<p>${activityCount.count} ${activityCount.name}s</p>
		</c:forEach>
		<p>Not bad...</p>
	</c:if>
	<c:if test="${activityCounts.size() == 0}">
		Nothing to show yet.
	</c:if>

    <a href="/${pageContext.request.contextPath}/user/edit_password/${user.id}" class="href-dotted" style="display:inline-block;margin-top:60px;">Update Password</a>
	

