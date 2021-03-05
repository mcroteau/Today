<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Overview</title>
</head>
<body>
<style>

</style>
<div id="overview-wrapper">

    <c:if test="${not empty message}">
        <p class="notify">${message}</p>
    </c:if>


    <h1>Overview</h1>
    <div class="go-left">
    <c:forEach items="${efforts}" var="effort">
        <c:if test="${effort.prospectActivities.size() > 0}">
            <p>
                <c:forEach items="${effort.prospectActivities}" var="activity">
                   ${activity.name} >
                </c:forEach>
            </p>

        </c:if>
    </c:forEach>
    </div>
    <div class="go-right">
        <c:forEach items="${prospectCounts}" var="prospectCount">
            <p>${prospectCount.status.name} : ${prospectCount.count}</p>
        </c:forEach>
    </div>
    <br class="clear"/>

    <svg id="demo1" width=1040 height=400>
        <g transform="translate(0,10)">
            <g class="links"></g>
            <g class="nodes"></g>
            <g class="text"></g>
        </g>
    </svg>

    <script type="text/javascript">
        $(document).ready(function(){
            <%--$.ajax({--%>
            <%--    url: "${pageContext.request.contextPath}/tree",--%>
            <%--    success: visualize,--%>
            <%--    error: function(e){--%>
            <%--        console.log('error', e);--%>
            <%--    }--%>
            <%--});--%>

            visualize();
            function visualize(resp) {

                var data = {"name": "A", "children": [
                        {"name": "C"},
                        {"name": "D", "children": [
                                {"name": "F"},
                                {"name": "E"}
                            ]},
                        {"name": "B"}
                    ]};


                var root = d3.hierarchy(data)
                    .sort((a,b) => b.height - a.height || a.data.name.localeCompare(b.data.name));

                var treeLayout = d3.tree()
                    .size([580, 80]);


                var tree = treeLayout(root);
                console.log(tree, root)

                var svg = d3.select("#demo1");


                var links = svg.select('g.links')
                    .selectAll('line.link')
                    .data(root.links())
                    .enter()
                    .append('line')
                    .attr('x1', function(d) {return d.source.x;})
                    .attr('y1', function(d) {return d.source.y;})
                    .attr('x2', function(d) {return d.target.x;})
                    .attr('y2', function(d) {return d.target.y;})
                    .attr('stroke', "#999")
                    .attr('stroke-width', 0.5);

                console.log('links', links);

                var nodes = svg.select('g.nodes')
                    .selectAll('circle.node')
                    .data(root.descendants())
                    .enter()
                    .append('circle')
                    .attr('cx', function(d) {return d.x;})
                    .attr('cy', function(d) {return d.y;})
                    .attr('r', 1)
                    .attr("fill", "white")
                    .attr('stroke', "#999")
                    .attr('stroke-width', 1);

                console.log('nodes', nodes);

                var texts = svg.select('g.nodes')
                    .selectAll('text.node')
                    .data(root.descendants())
                    .enter()
                    .append("text")
                    .attr("color", "#000")
                    .attr('x', function(d) {return d.x;})
                    .attr('y', function(d) {return d.y;})
                    .attr("text-anchor", "middle")
                    .text(function(d) {
                        console.log('name', d.data.name);
                        return d.data.name
                    })
            }
        });

        width = 954



    </script>
</div>
</body>