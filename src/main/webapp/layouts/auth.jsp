<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="sequence.common.Constants" %>

<html>
<head>
    <title>Dynamics + Gain: <decorator:title default="The Goal to Remove Barriers"/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="/z/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">

    <link rel="stylesheet" href="/z/assets/css/packages/grid.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/base.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/z/assets/css/app.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="/z/assets/js/packages/jquery.js"></script>

</head>
<body>

<div class="container">

    <div class="row">
        <div class="col-sm-12">
            <div id="header-wrapper">
                <a href="/z" class="logo">
                    <img src="/z/assets/media/icon.png" style="width:50px;"/><br/>
                    Sequence<br/>
                    <span class="tagline"></span>
                </a>

                <div id="navigation">
                    <span id="welcome">Hello <a href="/z/user/edit/${sessionScope.userId}" class="href-dotted-black zero"><strong>${sessionScope.username}</strong></a>!</span>
                </div>
                <br class="clear"/>
            </div>

            <decorator:body />

            <div id="footer-navigation" style="margin-top:30px;">
                <%if(Parakeet.hasRole(Constants.ADMIN_ROLE)){%>
                    <a href="/z/prospects" class="href-dotted">Prospects</a>
                <%}%>
            </div>
        </div>
    </div>

    <div class="go-left">
        <a href="/z/home" class="href-dotted">Home</a>&nbsp;&nbsp;
    </div>
    <div class="go-right">
        <a href="/z/signout" class="href-dotted">Signout</a>
    </div>

    <br class="clear"/>

    <div id="contact" style="text-align: center">
        <p><a href="mailto:mail@dynamicsgain.org" class="href-dotted-black">mail@dynamicsgain.org</a></p>
        <span class="regular">&copy; 2021 <br/>Dynamics<br/> <strong class="gain">+Gain</strong></span>
    </div>

</body>
</html>