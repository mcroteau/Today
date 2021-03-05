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


    <div id="chart" style="height:700px;width:900px;"></div>

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
            font: 7px sans-serif;
        }

        .link {
            fill: none;
            stroke: #ccc;
            stroke-width: 1.5px;
        }
    </style>

    <!--https://codepen.io/fernoftheandes/pen/pcoFz-->

    <script type="text/javascript">
        $(document).ready(function(){
            $.ajax({
                url: "${pageContext.request.contextPath}/tree",
                success: visualize,
                error: function(e){
                    console.log('error', e);
                }
            });

            // visualize();
            function visualize(resp) {

                pubs = JSON.parse(resp)

                var diameter = 800;

                var margin = {top: 20, right: 120, bottom: 20, left: 120},
                    width = diameter,
                    height = diameter;

                var i = 0,
                    duration = 350,
                    root;

                var tree = d3.layout.tree()
                    .size([360, diameter / 2 - 80])
                    .separation(function(a, b) { return (a.parent == b.parent ? 4 : 10) / a.depth; });

                var diagonal = d3.svg.diagonal.radial()
                    .projection(function(d) { return [d.y, d.x / 180 * Math.PI]; });

                var svg = d3.select("#chart").append("svg")
                    .attr("width", width )
                    .attr("height", height )
                    .append("g")
                    .attr("transform", "translate(" + diameter / 2 + "," + diameter / 2 + ")");

                root = pubs;
                root.x0 = height / 2;
                root.y0 = 0;

//root.children.forEach(collapse); // start with all children collapsed
                update(root);

                d3.select(self.frameElement).style("height", "800px");

                function update(source) {

                    // Compute the new tree layout.
                    var nodes = tree.nodes(root),
                        links = tree.links(nodes);

                    // Normalize for fixed-depth.
                    nodes.forEach(function(d) { d.y = d.depth * 80; });

                    // Update the nodes…
                    var node = svg.selectAll("g.node")
                        .data(nodes, function(d) { return d.id || (d.id = ++i); });

                    // Enter any new nodes at the parent's previous position.
                    var nodeEnter = node.enter().append("g")
                        .attr("class", "node")
                        //.attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
                        .on("click", click);

                    nodeEnter.append("circle")
                        .attr("r", 1e-6)
                        .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

                    nodeEnter.append("text")
                        .attr("x", 10)
                        .attr("dy", ".35em")
                        .attr("text-anchor", "start")
                        //.attr("transform", function(d) { return d.x < 180 ? "translate(0)" : "rotate(180)translate(-" + (d.name.length * 8.5)  + ")"; })
                        .text(function(d) { return d.name + " " + d.index; })
                        .style("fill-opacity", 1e-6);

                    // Transition nodes to their new position.
                    var nodeUpdate = node.transition()
                        .duration(duration)
                        .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })

                    nodeUpdate.select("circle")
                        .attr("r", 4.5)
                        .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

                    nodeUpdate.select("text")
                        .style("fill-opacity", 1)
                        .attr("transform", function(d) { return d.x < 180 ? "translate(0)" : "rotate(180)translate(-" + (d.name.length + 50)  + ")"; });

                    // TODO: appropriate transform
                    var nodeExit = node.exit().transition()
                        .duration(duration)
                        //.attr("transform", function(d) { return "diagonal(" + source.y + "," + source.x + ")"; })
                        .remove();

                    nodeExit.select("circle")
                        .attr("r", 1e-6);

                    nodeExit.select("text")
                        .style("fill-opacity", 1e-6);

                    // Update the links…
                    var link = svg.selectAll("path.link")
                        .data(links, function(d) {
                            console.log(d)
                            return d.target.id;
                        });

                    // Enter any new links at the parent's previous position.
                    link.enter().insert("path", "g")
                        .attr("class", "link")
                        .attr("d", function(d) {
                            var o = {x: source.x0, y: source.y0};
                            return diagonal({source: o, target: o});
                        });

                    // Transition links to their new position.
                    link.transition()
                        .duration(duration)
                        .attr("d", diagonal);

                    // Transition exiting nodes to the parent's new position.
                    link.exit().transition()
                        .duration(duration)
                        .attr("d", function(d) {
                            var o = {x: source.x, y: source.y};
                            return diagonal({source: o, target: o});
                        })
                        .remove();

                    // Stash the old positions for transition.
                    nodes.forEach(function(d) {
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

// Collapse nodes
                function collapse(d) {
                    if (d.children) {
                        d._children = d.children;
                        d._children.forEach(collapse);
                        d.children = null;
                    }
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