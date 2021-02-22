<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="parakeet" uri="/META-INF/tags/parakeet.tld"%>

<script src="https://www.google.com/recaptcha/api.js" async defer></script>

<div id="signup-form-container">

    <c:if test="${not empty message}">
        <div class="notify alert-info">${message}</div>
    </c:if>


    <h2>Dynamics <br/><strong class="gain">+Gain</strong></h2>

    <p style="text-align: left; margin-top:30px;">Are you already a member?
        <a href="${pageContext.request.contextPath}/signin" class="href-dotted">Signin!</a>
    </p>


    <form action="/z/register" modelAttribute="user" method="post" enctype="multipart/form-data" autocomplete="false" class="pure-form pure-form-stacked" id="registration-form">
        <fieldset style="text-align: left">

            <h2 style="margin-bottom:20px;">Signup</h2>

            <label>Email</label>
            <input id="username" type="email" placeholder="Email Address" name="username" style="width:100%;">

            <label>Password</label>
            <input id="password" type="password" placeholder="Password &#9679;&#9679;&#9679;" name="password" style="width:100%;">

            <div class="g-recaptcha" data-sitekey="6Leyol0aAAAAAOfr3raS1YXd9qcGczhcxTTQPVQb" style="margin-top:30px;"></div>

        </fieldset>
    </form>


    <div style="width:100%;margin-top:41px;text-align:center;margin-bottom:30px; ">
        <input type="submit" class="button retro" id="signup-button" value="Signup!" style="width:100%;"/>
    </div>

    <br class="clear"/>

</div>


<script>


    var processing = false

    //var pass = "You may proceed..."

    var email = document.getElementById("username")
    var password = document.getElementById("password")
    var form = document.getElementById("registration-form")
    var signupButton = document.getElementById("signup-button")

    /**
    var summationP = document.getElementById("summation")
    var summationInput = document.getElementById("value")
    var verdictP = document.getElementById("verdict")

    signupButton.setAttribute("disabled", true)

    var numOne = getRandom()
    var numTwo = getRandom()

    var z = numOne + numTwo

    var summationText = numOne + " + " + numTwo + " = ";
    summationP.innerHTML = summationText

    summationInput.addEventListener("input", function(event){
        verdictP.style.display = "block"
        if(z == summationInput.value){
            verdictP.innerHTML = pass
            signupButton.removeAttribute("disabled")
        }else{
            verdictP.style.display = "none"
            signupButton.setAttribute("disabled", disabled)
        }
    })
    **/

    signupButton.addEventListener("click", function(event){
        event.preventDefault();
        if(!processing){
            processing = true;
            form.submit();
        }
    })

    setTimeout(function(){
        password.value = ""
        email.value = ""
    }, 1000)


    function getRandom(){
        return Math.ceil(Math.random()* 20);
    }

</script>
