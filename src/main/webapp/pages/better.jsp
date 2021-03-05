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
            stroke-width: 1.5px;
        }
    </style>

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

                const treeDatad = {
                    "name": "Eve",
                    "value": 15,
                    "type": "black",
                    "level": "yellow",
                    "children": [
                        {
                            "name": "Cain",
                            "value": 10,
                            "type": "grey",
                            "level": "red"
                        },
                        {
                            "name": "Seth",
                            "value": 10,
                            "type": "grey",
                            "level": "red",
                            "children": [
                                {
                                    "name": "Enos",
                                    "value": 7.5,
                                    "type": "grey",
                                    "level": "purple"
                                },
                                {
                                    "name": "Noam",
                                    "value": 7.5,
                                    "type": "grey",
                                    "level": "purple"
                                }
                            ]
                        },
                        {
                            "name": "Abel",
                            "value": 10,
                            "type": "grey",
                            "level": "blue"
                        },
                        {
                            "name": "Awan",
                            "value": 10,
                            "type": "grey",
                            "level": "green",
                            "children": [
                                {
                                    "name": "Enoch",
                                    "value": 7.5,
                                    "type": "grey",
                                    "level": "orange"
                                }
                            ]
                        },
                        {
                            "name": "Azura",
                            "value": 10,
                            "type": "grey",
                            "level": "green"
                        }
                    ]
                };

                var treeData = JSON.parse(resp)

// set the dimensions and margins of the diagram
                const margin = {top: 20, right: 90, bottom: 30, left: 90},
                    width  = 660 - margin.left - margin.right,
                    height = 500 - margin.top - margin.bottom;

// declares a tree layout and assigns the size
                const treemap = d3.tree().size([height, width]);

//  assigns the data to a hierarchy using parent-child relationships
                let nodes = d3.hierarchy(treeData, d => d.children);

// maps the node data to the tree layout
                nodes = treemap(nodes);

// append the svg object to the body of the page
// appends a 'group' element to 'svg'
// moves the 'group' element to the top left margin
                const svg = d3.select("body").append("svg")
                        .attr("width", width + margin.left + margin.right)
                        .attr("height", height + margin.top + margin.bottom),
                    g = svg.append("g")
                        .attr("transform",
                            "translate(" + margin.left + "," + margin.top + ")");

// adds the links between the nodes
                const link = g.selectAll(".link")
                    .data( nodes.descendants().slice(1))
                    .enter().append("path")
                    .attr("class", "link")
                    .style("stroke", 0.5)
                    .attr("d", d => {
                        return "M" + d.y + "," + d.x
                            + "C" + (d.y + d.parent.y) / 2 + "," + d.x
                            + " " + (d.y + d.parent.y) / 2 + "," + d.parent.x
                            + " " + d.parent.y + "," + d.parent.x;
                    });

// adds each node as a group
                const node = g.selectAll(".node")
                    .data(nodes.descendants())
                    .enter().append("g")
                    .attr("class", d => "node" + (d.children ? " node--internal" : " node--leaf"))
                    .attr("transform", d => "translate(" + d.y + "," + d.x + ")");

// adds the circle to the node
                node.append("circle")
                    .attr("r", d => d.data.value)
                    .attr("stroke-width", 2)
                    .style("stroke", "2")
                    .style("fill", "#fff");

// adds the text to the node
                node.append("text")
                    .attr("dy", ".35em")
                    // .attr("x", d => d.children ? (d.data.value + 5) * -1 : d.data.value + 5)
                    // .attr("y", d => d.children && d.depth !== 0 ? -(d.data.value + 5) : d)
                    .attr("x", function(d){
                        console.log(d)
                        if(d.data.y){
                            return d
                        }
                    })
                    .attr("y", function(d){
                        console.log(d)
                        if(d.data.y){
                            return d
                        }
                    })
                    .style("text-anchor", d => d.children ? "end" : "start")
                    .text(d => d.data.name);

            }
        });

        width = 954
        margin = ({top: 10, right: 120, bottom: 10, left: 40})
        dx = 10
        dy = width / 6

    </script>
</div>
</body>