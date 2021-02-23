<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>The Goal to Inform.</title>
</head>
<body>
<style>
    #homepage-wrapper p{
        text-align: left;
    }
    h3{
        margin:30px auto 3px ;
    }
    #welcome-text{
        margin-bottom:10px;
    }
</style>
<div id="homepage-wrapper">

    <c:if test="${not empty message}">
        <p class="notify">${message}</p>
    </c:if>

    <p class="" style="display: inline-block;font-size:17px;">Please help!</p>

    <h1 id="welcome-text" class="">
        <strong class="" style="line-height: 1.0em;" id="count">${count}</strong> <span>Homeless <span class="header-information"> in
            &nbsp;<a href="/z/towns" class="counts href-dotted-black" id="cities">${towns.size()}</a>&nbsp;
            <c:if test="${towns.size() == 1}">City!</c:if>
            <c:if test="${towns.size() > 1 || towns.size() == 0}">Cities!</c:if>
    </span> <a href="/z/shelters" class="highlight blue" style="color:#fff;font-size:57px;" id="shelters">${prospects.size()}</a> shelters <br/>registered</span></span>
    </h1>

    <p class="open-text" style="margin-bottom:10px;">
        Dynamics <strong class="gain">+Gain</strong>
        is an organization designed to remove barriers
        that prevent people from giving time, money and resources to those
        in need! Our goal is to set up Booths with near real time homeless
        data displayed allowing people to stay up to date on the reality of
        homelessness in their community.
    </p>

    <h3 style="margin-top:20px;">How does it work?</h3>
    <p>Kiosks/booths would be set up at your local community shopping centers
        displaying the number of people in need in your community,
        allowing you to donate right there on the spot.
        100% of your donation goes toward the organization you select
        or to Dynamics <strong>+Gain</strong>, your choice. Each organization
        has gone through the process to sign up.
        </p>

    <p class="yellow" style="margin:20px 0px;">If you are unable to donate,
        we will leave contact information for each organization as some and
        starting out non of the organizations have gone through the process
        yet.</p>

    <h2 style="margin-top:50px;">Woa woa woa, wait a minute?</h2>
    <p>You mean to tell me you that the donations go to the Organization I choose?
        <strong class="yellow">That is correct!</strong> Just select the Organization
    you wish to donate to and if they have signed up, you will see a donate
    form on their page. Otherwise, feel free let them know about this
        service. We are just so excited to have the opportunity to do this.</p>


    <h3 id="towns">Towns/Cities</h3>
    <ul>
        <c:forEach var="town" items="${towns}">
            <li style="padding:4px 0px;"><a href="/z/towns/${town.townUri}" class="href-dotted" style="font-size:27px;">${town.name}
                <span style="display:block"><strong class="" style="line-height:1.4em;">${town.count}</strong> homeless!</span></a></li>
        </c:forEach>
    </ul>
    
</div>

    <script>
        $(document).ready(function(){
            var $count = $('#count'),
                end = $count.html(),
                clean = $count.html().replace(",", "")

            var $shelters = $('#shelters'),
                sheltersEnd = $shelters.html()

            var goal = 0,
                render = 0

            var sheltersGoal = 0,
                sheltersRender = 0;

            var interval = setInterval(function(){
                goal = goal + 1250
                render = goal
                $count.html(render.toLocaleString())

                sheltersGoal++
                sheltersRender = sheltersGoal
                $shelters.html(sheltersRender.toLocaleString())

                if(sheltersGoal >= sheltersEnd){
                    $shelters.html(sheltersEnd);
                }

                if(goal >= clean){
                    $count.html(end)
                    clearInterval(interval)
                    interval = 0
                }
            }, 50);
        })
    </script>

</body>