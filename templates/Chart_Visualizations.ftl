<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chart Visualization</title>
    <script src="https://d3js.org/d3.v7.min.js"></script>
    <link rel="stylesheet" href="/style_charts_ftl.css">
    <link rel="stylesheet" href="/style_navbar_ftl.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://kit.fontawesome.com/acc5c3cbc3.js" crossorigin="anonymous"></script>
</head>
<body>
<#include "navbar.ftl">
<main>
    <h2>Rede ${SuchEingabe} Chart Visualization</h2>
    <div id="bubble-chart"></div>
    <div id="pos-chart"></div>
    <div id="radar-chart"></div>
    <div id="sunburst-chart"></div>
</main>

<script>
    async function fetchTopicData() {
        const response = await fetch("/TopicData/${SuchEingabe}");
        return await response.json();
    }

    async function fetchPOSData() {
        const response = await fetch('/POSData/${SuchEingabe}');
        return await response.json();
    }

    async function fetchSentimentData() {
        const response = await fetch('/SentimentData/${SuchEingabe}');
        return await response.json();
    }

    async function fetchNamedEntityData() {
        const response = await fetch('/NamedEntityData2/${SuchEingabe}');
        return await response.json();
    }

    async function BarChart() {
        const data = await fetchPOSData();

        data.forEach(d => d.value = +d.value);
        data.sort((a, b) => d3.descending(a.value, b.value));

        const margin = { top: 40, right: 30, bottom: 120, left: 60 };
        const width = 700 - margin.left - margin.right;
        const height = 450 - margin.top - margin.bottom;

        d3.select("#pos-chart svg").remove();

        const svg = d3.select("#pos-chart").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        const x = d3.scaleBand()
            .domain(data.map(d => d.label))
            .range([0, width])
            .padding(0.5);

        const y = d3.scaleLinear()
            .domain([0, d3.max(data, d => d.value)])
            .nice()
            .range([height, 0]);

        svg.selectAll(".bar")
            .data(data)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", d => x(d.label))
            .attr("width", x.bandwidth())
            .attr("y", d => y(d.value))
            .attr("height", d => height - y(d.value))
            .attr("fill", "steelblue");

        svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x))
            .selectAll("text")
            .style("text-anchor", "end")
            .attr("transform", "rotate(-45)")
            .attr("dx", "-0.8em")
            .attr("dy", "0.15em")
            .style("font-size", "12px");

        svg.append("g")
            .call(d3.axisLeft(y).ticks(5));

        svg.selectAll(".label")
            .data(data)
            .enter().append("text")
            .attr("x", d => x(d.label) + x.bandwidth() / 2)
            .attr("y", d => y(d.value) - 5)
            .attr("text-anchor", "middle")
            .style("font-size", "12px")
            .text(d => d.value);

        svg.append("text")
            .attr("x", width / 2)
            .attr("y", height + 80)
            .attr("text-anchor", "middle")
            .style("font-size", "14px")
            .text("Labels");

        svg.append("text")
            .attr("x", -height / 2)
            .attr("y", -40)
            .attr("text-anchor", "middle")
            .attr("transform", "rotate(-90)")
            .style("font-size", "14px")
            .text("Values");

    }

    async function RadarChart() {
        const data = await fetchSentimentData();

        const margin = { top: 50, right: 50, bottom: 50, left: 50 };
        const width = 600 - margin.left - margin.right;
        const height = 600 - margin.top - margin.bottom;

        const svg = d3.select("#radar-chart")
            .append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + (margin.left + width / 2) + "," + (margin.top + height / 2) + ")");

        const labels = data.map(function(d) { return d.label; });
        const maxValue = d3.max(data, function(d) { return d.value; });

        const angleSlice = (Math.PI * 2) / labels.length;

        const radiusScale = d3.scaleLinear()
            .domain([0, maxValue])
            .range([0, width / 2]);

        const levels = 5;
        const circleGroup = svg.append("g")
            .attr("class", "grid-circles");

        for (let i = 0; i < levels; i++) {
            const radius = radiusScale(maxValue * (i + 1) / levels);

            circleGroup.append("circle")
                .attr("cx", 0)
                .attr("cy", 0)
                .attr("r", radius)
                .attr("class", "grid-circle")
                .style("fill", "none")
                .style("stroke", "#CDCDCD")
                .style("stroke-width", "0.5px");
        }

        const axes = svg.selectAll(".axis")
            .data(labels)
            .enter()
            .append("g")
            .attr("class", "axis");

        axes.append("line")
            .attr("x1", 0)
            .attr("y1", 0)
            .attr("x2", function(d, i) { return Math.cos(angleSlice * i - Math.PI / 2) * radiusScale(maxValue); })
            .attr("y2", function(d, i) { return Math.sin(angleSlice * i - Math.PI / 2) * radiusScale(maxValue); })
            .attr("class", "line")
            .style("stroke", "#CDCDCD")
            .style("stroke-width", "1px");

        axes.append("text")
            .attr("x", function(d, i) { return Math.cos(angleSlice * i - Math.PI / 2) * (radiusScale(maxValue) + 10); })
            .attr("y", function(d, i) { return Math.sin(angleSlice * i - Math.PI / 2) * (radiusScale(maxValue) + 10); })
            .text(function(d) { return d; })
            .attr("class", "legend")
            .style("font-size", "11px")
            .attr("text-anchor", function(d, i) {
                const angle = angleSlice * i - Math.PI / 2;
                return (Math.cos(angle) < 0) ? "end" : (Math.cos(angle) > 0) ? "start" : "middle";
            })
            .attr("dy", function(d, i) {
                const angle = angleSlice * i - Math.PI / 2;
                return (Math.sin(angle) < 0) ? "-0.5em" : "1em";
            });

        const radarArea = d3.radialLine()
            .angle(function(d, i) { return i * angleSlice; })
            .radius(function(d) { return radiusScale(d.value); })
            .curve(d3.curveLinearClosed);

        svg.append("path")
            .datum(data)
            .attr("class", "radar-area")
            .attr("d", radarArea)
            .style("fill", "steelblue")
            .style("fill-opacity", 0.6)
            .style("stroke", "#00366e")
            .style("stroke-width", "2px");

        svg.selectAll(".radar-dot")
            .data(data)
            .enter()
            .append("circle")
            .attr("class", "radar-dot")
            .attr("cx", function(d, i) { return Math.cos(angleSlice * i - Math.PI / 2) * radiusScale(d.value); })
            .attr("cy", function(d, i) { return Math.sin(angleSlice * i - Math.PI / 2) * radiusScale(d.value); })
            .attr("r", 4)
            .style("fill", "#fff")
            .style("stroke", "#00366e")
            .style("stroke-width", "2px");
    }

    async function SunburstChart() {
        const dataArray = await fetchNamedEntityData();

        const data = dataArray[0];

        const margin = { top: 50, right: 50, bottom: 50, left: 50 };
        const width = 600 - margin.left - margin.right;
        const height = 600 - margin.top - margin.bottom;
        const radius = Math.min(width, height) / 2;

        const color = d3.scaleOrdinal(d3.quantize(d3.interpolateRainbow, data.children.length + 1));

        const partition = function(data) {
            return d3.partition()
                .size([2 * Math.PI, radius])
                (d3.hierarchy(data)
                    .sum(function(d) { return d.value; })
                    .sort(function(a, b) { return b.value - a.value; }));
        };

        const arc = d3.arc()
            .startAngle(function(d) { return d.x0; })
            .endAngle(function(d) { return d.x1; })
            .padAngle(function(d) { return Math.min((d.x1 - d.x0) / 2, 0.003); })
            .padRadius(radius / 2)
            .innerRadius(function(d) { return d.y0; })
            .outerRadius(function(d) { return d.y1; });

        const root = partition(data);
        console.log("Root:", root);

        const svg = d3.create("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .style("max-width", "100%")
            .style("height", "auto");

        const g = svg.append("g")
            .attr("transform", "translate(" + (margin.left + width / 2) + "," + (margin.top + height / 2) + ")");

        const paths = g.selectAll("path")
            .data(root.descendants().filter(function(d) { return d.depth; }))
            .enter().append("path")
            .attr("fill", function(d) {
                while (d.depth > 1) d = d.parent;
                return color(d.data.name);
            })
            .attr("d", arc)
            .on("mouseover", function(event, d) {
                d3.select(this).attr("opacity", 0.8);
                g.selectAll("text")
                    .filter(function(textD) { return textD === d; })
                    .style("visibility", "visible");
            })
            .on("mouseout", function(event, d) {
                d3.select(this).attr("opacity", 1);
                g.selectAll("text")
                    .filter(function(textD) { return textD === d; })
                    .style("visibility", "hidden");
            })
            .append("title")
            .text(function(d) {
                return d.ancestors().map(function(d) { return d.data.name; }).reverse().join("/") + "\n" + d.value;
            });

        const labels = g.selectAll("text")
            .data(root.descendants().filter(function(d) {
                return d.depth && (d.y0 + d.y1) / 2 * (d.x1 - d.x0) > 10;
            }))
            .enter().append("text")
            .attr("transform", function(d) {
                const x = (d.x0 + d.x1) / 2 * 180 / Math.PI;
                const y = (d.y0 + d.y1) / 2;
                return "rotate(" + (x - 90) + ") translate(" + y + ",0) rotate(" + (x < 180 ? 0 : 180) + ")";
            })
            .attr("dy", "0.35em")
            .style("font-size", "12px")
            .style("text-anchor", function(d) {
                return (d.x0 + d.x1) / 2 * 180 / Math.PI < 180 ? "start" : "end";
            })
            .style("visibility", "hidden")
            .text(function(d) { return d.data.name; });

        const container = document.getElementById("sunburst-chart");
        container.appendChild(svg.node());

    }

    async function BubbleChart() {
        const data = await fetchTopicData();

        const margin = { top: 40, right: 40, bottom: 40, left: 40 };
        const width = 1000 - margin.left - margin.right;
        const height = 600 - margin.top - margin.bottom;

        d3.select("#bubble-chart svg").remove();

        const svg = d3.select("#bubble-chart").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        const maxScore = d3.max(data, d => d.value);
        const minScore = d3.min(data, d => d.value);

        console.log("Min score:", minScore, "Max score:", maxScore); // Debug log

        const sizeScale = d3.scaleSqrt()
            .domain([minScore || 0, maxScore || 1])
            .range([15, 100]);

        const color = d3.scaleOrdinal(d3.schemeCategory10);

        const simulation = d3.forceSimulation(data)
            .force("charge", d3.forceManyBody().strength(5))
            .force("center", d3.forceCenter(width / 2, height / 2))
            .force("collision", d3.forceCollide().radius(d => sizeScale(d.value) + 2))
            .on("tick", ticked);

        const bubbles = svg.selectAll(".bubble")
            .data(data)
            .enter().append("circle")
            .attr("class", "bubble")
            .attr("r", d => sizeScale(d.value))
            .attr("fill", (d, i) => color(i))
            .attr("stroke", "#333")
            .attr("stroke-width", 1)
            .attr("fill-opacity", 0.8);

        const labels = svg.selectAll(".category-label")
            .data(data)
            .enter().append("text")
            .attr("class", "category-label")
            .attr("text-anchor", "middle")
            .attr("dominant-baseline", "middle")
            .attr("font-size", d => Math.min(2 * sizeScale(d.value) / 3, 16))
            .attr("font-weight", "bold")
            .attr("fill", "#333")
            .text(d => d.label);

        const valueLabels = svg.selectAll(".value-label")
            .data(data)
            .enter().append("text")
            .attr("class", "value-label")
            .attr("text-anchor", "middle")
            .attr("font-size", d => Math.min(sizeScale(d.value) / 3, 14))
            .attr("fill", "#333")
            .text(d => d.value.toFixed(2));

        const labelSimulation = d3.forceSimulation(data)
            .force("x", d3.forceX(d => d.x).strength(0.1))
            .force("y", d3.forceY(d => d.y).strength(0.1))
            .force("collide", d3.forceCollide().radius(d => sizeScale(d.value) + 10))
            .on("tick", () => {
                labels
                    .attr("x", d => d.x)
                    .attr("y", d => d.y);

                valueLabels
                    .attr("x", d => d.x)
                    .attr("y", d => d.y + 15);
            });

        function ticked() {
            bubbles
                .attr("cx", d => d.x)
                .attr("cy", d => d.y);

            labels
                .attr("x", d => d.x)
                .attr("y", d => d.y);

            valueLabels
                .attr("x", d => d.x)
                .attr("y", d => d.y + 15);

            labelSimulation.force("x", d3.forceX(d => d.x).strength(0.1))
                .force("y", d3.forceY(d => d.y).strength(0.1))
                .alpha(1).restart();
        }

    }

    BubbleChart();
    SunburstChart();
    RadarChart();
    BarChart();

    $(document).ready(function() {
        $("#home").click(function() {
            window.location.href = "/hub";
        });

        $("#redenportfolios").click(function() {
            window.location.href = "/index";
        });

        $("#visualizations").click(function() {
            $("main").hide();

            $("#chartSearchContainer").show();
            $("#volltextSearchContainer").hide();
            $("#chartSearchInput").focus();
        });

        $("#speeches").click(function() {
            $("main").hide();

            $("#volltextSearchContainer").show();
            $("#chartSearchContainer").hide();
            $("#volltextSearchInput").focus();
        });

        $("#chartSearchInput").keypress(function(event) {
            if (event.which === 13) {
                let RedeID = $(this).val().trim();
                if (RedeID) {
                    window.location.href = "/visual/" + RedeID;
                }
            }
        });

        $("#volltextSearchInput").keypress(function(event) {
            if (event.which === 13) {
                let RedeID = $(this).val().trim();
                if (RedeID) {
                    window.location.href = "/Volltext/" + RedeID;
                }
            }
        });
    });

</script>

</body>
</html>
