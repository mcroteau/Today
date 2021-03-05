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

    <div id="chart"></div>
    <%--    <svg id="demo1" width=1040 height=400>--%>
    <%--        <g transform="translate(0,10)">--%>
    <%--            <g class="links"></g>--%>
    <%--            <g class="nodes"></g>--%>
    <%--            <g class="text"></g>--%>
    <%--        </g>--%>
    <%--    </svg>--%>

    <style>
        .node {
            cursor: pointer;
        }

        .node circle {
            fill: #fff;
            stroke: steelblue;
            stroke-width: 1.5px;
        }

        .node text {
            font: 10px sans-serif;
        }

        .link {
            fill: none;
            stroke: #ccc;
            stroke-width: 0.5px;
        }
    </style>

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

                var root = {
                    name : "Prospect",
                    type : "square",
                    count : 20,
                    children : [
                        {
                            name : "Call",
                            type : "triangle",
                            count : 10,
                            children : [
                                {
                                    name : "Call",
                                    type : "triangle",
                                    count : 10,
                                },
                                {
                                    name : "Email",
                                    type : "triangle",
                                    count : 7,
                                },
                                {
                                    name : "Demo",
                                    type : "triangle",
                                    count : 10,
                                },
                            ]
                        },
                        {
                            name : "Email",
                            type : "triangle",
                            count : 7,
                            children : [
                                {
                                    name : "Demo",
                                    type : "triangle",
                                    count : 10,
                                    children : [
                                        {
                                            name : "Call",
                                            type : "triangle",
                                            count : 10,
                                        },
                                        {
                                            name : "Email",
                                            type : "triangle",
                                            count : 7,
                                        },
                                        {
                                            name : "Demo",
                                            type : "triangle",
                                            count : 10,
                                        },
                                    ]
                                },
                            ]
                        },
                        {
                            name : "Demo",
                            type : "triangle",
                            count : 10,
                        },
                    ]
                }


                var margin = {
                        top: 20,
                        right: 120,
                        bottom: 20,
                        left: 120
                    },
                    width = 960 - margin.right - margin.left,
                    height = 800 - margin.top - margin.bottom;

                var i = 0,
                    duration = 750,
                    rectW = 60,
                    rectH = 20;

                function distance() {
                    return 110;
                }
                var tree = d3.layout.tree()
                    .nodeSize([65, 10]);
                var diagonal = d3.svg.diagonal()
                    .projection(function (d) {
                        return [d.x + rectW / 2, d.y + rectH / 2];
                    });

                var svg = d3.select("#chart").append("svg").attr("width", 1000).attr("height", 1000)
                    .call(zm = d3.behavior.zoom().scaleExtent([1,3]).on("zoom", redraw)).append("g")
                    .attr("transform", "translate(" + 350 + "," + 20 + ")");

//necessary so that zoom knows where to zoom and unzoom from
                zm.translate([350, 20]);

                root.x0 = 0;
                root.y0 = height / 2;

                function collapse(d) {
                    if (d.children) {
                        d._children = d.children;
                        d._children.forEach(collapse);
                        d.children = null;
                    }
                }

                root.children.forEach(collapse);
                update(root);

                d3.select("#body").style("height", "800px");

                function update(source) {

                    // Compute the new tree layout.
                    var nodes = tree.nodes(root).reverse(),
                        links = tree.links(nodes);

                    // Normalize for fixed-depth.
                    nodes.forEach(function (d) {
                        d.y = d.depth * 180;
                    });

                    // Update the nodes…
                    var node = svg.selectAll("g.node")
                        .data(nodes, function (d) {
                            return d.id || (d.id = ++i);
                        });

                    // Enter any new nodes at the parent's previous position.
                    var nodeEnter = node.enter().append("g")
                        .attr("class", "node")
                        .attr("transform", function (d) {
                            return "translate(" + source.x0 + "," + source.y0 + ")";
                        })
                        .on("click", click);

                    nodeEnter.append("rect")
                        .attr("width", rectW)
                        .attr("height", rectH)
                        .style("stroke", "#efefef")
                        .style("stroke-width", 1)
                        .style("fill", function (d) {
                            return d._children ? "#2175ff" : "#fff";
                        });

                    nodeEnter.append("text")
                        .attr("x", rectW / 2)
                        .attr("y", rectH / 2)
                        .attr("dy", ".35em")
                        .style("fill", "black")
                        .attr("text-anchor", "middle")
                        .text(function (d) {
                            return d.name + ( d.count ? " (" + d.count + ")" : "") ;
                        });

                    // Transition nodes to their new position.
                    var nodeUpdate = node.transition()
                        .duration(duration)
                        .attr("transform", function (d) {
                            return "translate(" + d.x + "," + d.y + ")";
                        });

                    nodeUpdate.select("rect")
                        .attr("width", rectW)
                        .attr("height", rectH)
                        .attr("stroke", "black")
                        .attr("stroke-width", 0.5)
                        .style("fill", function (d) {
                            return d._children ? "#2175ff" : "#fff";
                        });

                    nodeUpdate.select("text")
                        .style("fill-opacity", 1)
                        .style("fill", d => {
                            return d._children ? "#fff" : "#000";
                        });

                    // Transition exiting nodes to the parent's new position.
                    var nodeExit = node.exit().transition()
                        .duration(duration)
                        .attr("transform", function (d) {
                            return "translate(" + source.x + "," + source.y + ")";
                        })
                        .remove();

                    nodeExit.select("rect")
                        .attr("width", rectW)
                        .attr("height", rectH)
                        .attr("stroke", "black")
                        .attr("stroke-width", 1);

                    nodeExit.select("text");

                    // Update the links…
                    var link = svg.selectAll("path.link")
                        .data(links, function (d) {
                            return d.target.id;
                        });

                    // Enter any new links at the parent's previous position.
                    link.enter().insert("path", "g")
                        .attr("class", "link")
                        .attr("x", rectW / 2)
                        .attr("y", rectH / 2)
                        .attr("d", function (d) {
                            var o = {
                                x: source.x0,
                                y: source.y0 - 10
                            };
                            return diagonal({
                                source: o,
                                target: o
                            });
                        });

                    // Transition links to their new position.
                    link.transition()
                        .duration(duration)
                        .attr("d", diagonal);

                    // Transition exiting nodes to the parent's new position.
                    link.exit().transition()
                        .duration(duration)
                        .attr("d", function (d) {
                            var o = {
                                x: source.x,
                                y: source.y
                            };
                            return diagonal({
                                source: o,
                                target: o
                            });
                        })
                        .remove();

                    // Stash the old positions for transition.
                    nodes.forEach(function (d) {
                        d.x0 = d.x;
                        d.y0 = d.y;
                    });
                }

// Toggle children on click.
                function click(d) {
                    if (d.children) {
                        d._children = d.children;
                        d.children = null;
                    } else {
                        d.children = d._children;
                        d._children = null;
                    }
                    update(d);
                }

//Redraw for zoom
                function redraw() {
                    //console.log("here", d3.event.translate, d3.event.scale);
                    svg.attr("transform",
                        "translate(" + d3.event.translate + ")"
                        + " scale(" + d3.event.scale + ")");
                }
            }
        });

        width = 954
        margin = ({top: 10, right: 120, bottom: 10, left: 40})
        dx = 10
        dy = width / 6

    </script>
</div>
</body>