<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>Today's Count</h1>
<p>${prospect.name}</p>

<c:if test="${not empty message}">
    <div class="notify">${message}</div>
</c:if>

<form action="${pageContext.request.contextPath}/admin/count/update" id="count-form" modelAttribute="dailyCount" method="post">

    <input type="hidden" name="id" value="${dailyCount.id}">
    <input type="hidden" name="locationId" value="${prospect.id}">

    <div class="form-group" style="text-align: center">
        <input type="text" name="count" id="daily-count" placeholder="170" value="${dailyCount.count}" style="font-size:27px;text-align: center">
    </div>

</form>

<div id="edit-actions-container" style="width:100%;">
    <input type="submit" class="button retro" id="save-button" value="Save Count" style="width:100% !important;"/>
</div>

<script>
    var countInput = document.getElementById("daily-count");
    var countForm = document.getElementById("count-form");
    var saveButton = document.getElementById("save-button");

    saveButton.addEventListener("click", function(event){
        if(countInput.value == "" || isNaN(countInput.value)){
            alert("Please enter number for count!")
        }else{
            countForm.submit();
        }
    })
</script>
