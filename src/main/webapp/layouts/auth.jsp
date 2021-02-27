<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="Parakeet" uri="/META-INF/tags/parakeet.tld"%>
<%@ page import="xyz.strongperched.Parakeet" %>
<%@ page import="today.common.Constants" %>
<%@ page import="today.service.UiService" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%
    ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    UiService uiService = (UiService) context.getBean("uiService");
%>
<html>
<head>
    <title><%=uiService.getBusinessName()%> : <decorator:title default="One day at a time."/></title>

    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/media/icon.png?v=<%=System.currentTimeMillis()%>">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/packages/grid.css?v=<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/default.css?v=<%=System.currentTimeMillis()%>">

    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/packages/jquery.js"></script>

</head>
<body>
<div class="container">

    <div class="row">
        <div class="col-sm-12">
            <div id="header-wrapper">
                <a href="${pageContext.request.contextPath}" class="logo">
                    <img src="${pageContext.request.contextPath}/assets/media/icon.png" style="width:50px;"/><br/>
                    Today<br/>
                    <span class="tagline"></span>
                </a>

                <div id="navigation">
                    <span id="welcome">Hello <a href="/z/user/edit/${sessionScope.userId}" class="href-dotted-black zero"><strong>${sessionScope.username}</strong></a>!</span>
                </div>
                <br class="clear"/>
            </div>

            <decorator:body />

            <div id="footer-navigation" style="margin-top:30px;">
                <%if(Parakeet.hasRole(Constants.SUPER_ROLE)){%>
                    <a href="/z/prospects" class="href-dotted">Prospects</a>
                <%}%>
            </div>
        </div>
    </div>


    <div class="go-right">
        <a href="/z/signout" class="href-dotted">Signout</a>
    </div>

    <br class="clear"/>

    <div id="contact" style="margin:30px auto 100px; text-align: center">
        <p>Support: <a href="mailto:<%=uiService.getBusinessName()%> " class="href-dotted-black"><%=uiService.getBusinessEmail()%> </a></p>
    </div>

</body>
</html>