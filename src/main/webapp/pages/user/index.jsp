<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>Accounts</title>
</head>

<body>

	<h1>Admin Accounts</h1>
	
	<c:if test="${not empty message}">
		<div class="notify">
			${message}
		</div>
	</c:if>


	<c:choose>
		<c:when test="${people.size() > 0}">
					
			<div class="span12">

				<table class="table table-condensed">
					<thead>
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>Username</th>
							<th>Status</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${people}">
							<tr>
								<td>${user.id}</td>
								<td>${user.name}</td>
								<td>${user.username}</td>
								<td>
	                                <c:if test="${user.disabled}">
	                                    <span class="beauty-light">Disabled</span>
	                                </c:if>
	                         	</td>

								<td><a href="${pageContext.request.contextPath}/user/edit/${user.id}" title="Edit" class="button sky">Edit</a>
							</tr>									
						</c:forEach>
					</tbody>
				</table>
				
			</div>
			
		</c:when>
		<c:when test="${people.size() == 0}">
			<p>No people created yet.</p>
		</c:when>
	</c:choose>
</body>
</html>