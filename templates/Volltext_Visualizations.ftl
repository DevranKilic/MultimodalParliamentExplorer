<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://kit.fontawesome.com/acc5c3cbc3.js" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="/style_volltext_ftl.css">
    <link rel="stylesheet" href="/style_navbar_ftl.css">
    <title>Volltext Visualization</title>
</head>
<#include "navbar.ftl">
<body>
<main>
    <h2>Volltext Visualisierung</h2>
    <div>
        <img src="${hqPicture}" alt="${vorname} ${nachname}" style="max-width: 150px;">
        <p>
            <strong>Speaker:</strong> ${vorname} ${nachname}
            <strong>Partei:</strong> ${partei}
            <strong>Geburtsdatum:</strong> ${geburtsdatum}
        </p>
    </div>

    <div class="toggle-checkbox">
        <input type="checkbox" id="toggle-named-entities" checked onchange="toggleNamedEntities()">
        <label for="toggle-named-entities">Show Named Entities</label>
    </div>
    <div class="toggle-checkbox">
        <input type="checkbox" id="toggle-pos" checked onchange="togglePOS()">
        <label for="toggle-pos">Show POS</label>
    </div>

    <div class="legend" id="named-entities-legend">
        <strong>Named Entities:</strong>
        <div class="legend-item">
            <span class="legend-color loc"></span> LOC (Location)
        </div>
        <div class="legend-item">
            <span class="legend-color per"></span> PER (Person)
        </div>
        <div class="legend-item">
            <span class="legend-color org"></span> ORG (Organization)
        </div>
        <div class="legend-item">
            <span class="legend-color misc"></span> MISC (Miscellaneous)
        </div>
    </div>

    <div class="legend" id="pos-legend">
        <strong>POS (Part of Speech):</strong>
        <div class="legend-item">
            <span class="legend-color nn"></span> nn
        </div>
        <div class="legend-item">
            <span class="legend-color adjd"></span> adjd
        </div>
        <div class="legend-item">
            <span class="legend-color prf"></span> prf
        </div>
        <div class="legend-item">
            <span class="legend-color pper"></span> pper
        </div>
        <div class="legend-item">
            <span class="legend-color vvinf"></span> vvinf
        </div>
        <div class="legend-item">
            <span class="legend-color art"></span> art
        </div>
        <div class="legend-item">
            <span class="legend-color adja"></span> adja
        </div>
        <div class="legend-item">
            <span class="legend-color pws"></span> pws
        </div>
        <div class="legend-item">
            <span class="legend-color vvfin"></span> vvfin
        </div>
        <div class="legend-item">
            <span class="legend-color vainf"></span> vainf
        </div>
        <div class="legend-item">
            <span class="legend-color adv"></span> adv
        </div>
        <div class="legend-item">
            <span class="legend-color apprart"></span> apprart
        </div>
        <div class="legend-item">
            <span class="legend-color appr"></span> appr
        </div>
        <div class="legend-item">
            <span class="legend-color proav"></span> proav
        </div>
        <div class="legend-item">
            <span class="legend-color pdat"></span> pdat
        </div>
        <div class="legend-item">
            <span class="legend-color ne"></span> ne
        </div>
        <div class="legend-item">
            <span class="legend-color ptkant"></span> ptkant
        </div>
        <div class="legend-item">
            <span class="legend-color vvpp"></span> vvpp
        </div>
        <div class="legend-item">
            <span class="legend-color vafin"></span> vafin
        </div>
        <div class="legend-item">
            <span class="legend-color kon"></span> kon
        </div>
        <div class="legend-item">
            <span class="legend-color piat"></span> piat
        </div>
        <div class="legend-item">
            <span class="legend-color pposat"></span> pposat
        </div>
        <div class="legend-item">
            <span class="legend-color card"></span> card
        </div>
        <div class="legend-item">
            <span class="legend-color pwav"></span> pwav
        </div>
        <div class="legend-item">
            <span class="legend-color pds"></span> pds
        </div>
        <div class="legend-item">
            <span class="legend-color vmfin"></span> vmfin
        </div>
        <div class="legend-item">
            <span class="legend-color vapp"></span> vapp
        </div>
        <div class="legend-item">
            <span class="legend-color kous"></span> kous
        </div>
        <div class="legend-item">
            <span class="legend-color kokom"></span> kokom
        </div>
        <div class="legend-item">
            <span class="legend-color vvizu"></span> vvizu
        </div>
        <div class="legend-item">
            <span class="legend-color ptkzu"></span> ptkzu
        </div>
        <div class="legend-item">
            <span class="legend-color prels"></span> prels
        </div>
    </div>

    <div class="speech">
        <#list AuszugList as Auszug>
            <#if Auszug.isComment>
                <div class="comment">
                    <div>
                        <i class="fas fa-comment comment-icon"></i>
                        <strong>Comment</strong>
                    </div>

                    <#assign commentText = Auszug.text>

                    <#list sentimentDataList as sentiment>
                        <#if sentiment.sentenceText??>
                            <#assign sentimentIndex = commentText?index_of(sentiment.sentenceText)>
                            <#if sentimentIndex != -1>
                                <#assign commentText = commentText?substring(0, sentimentIndex + sentiment.sentenceText?length) +
                                ' <i class="fas fa-circle-info sentiment-icon" title="Sentiment: ' + sentiment.sentimentValue + '"></i> ' +
                                commentText?substring(sentimentIndex + sentiment.sentenceText?length)>
                            </#if>
                        </#if>
                    </#list>

                    <span>${commentText}</span>
                </div>
            <#else>
                <#assign text = Auszug.text>

                <#list sentimentDataList as sentiment>
                    <#if sentiment.sentenceText??>
                        <#assign sentimentIndex = text?index_of(sentiment.sentenceText)>
                        <#if sentimentIndex != -1>
                            <#assign text = text?substring(0, sentimentIndex + sentiment.sentenceText?length) +
                            ' <i class="fas fa-circle-info sentiment-icon" title="Sentiment: ' + sentiment.sentimentValue + '"></i> ' +
                            text?substring(sentimentIndex + sentiment.sentenceText?length)>
                        </#if>
                    </#if>
                </#list>

                ${text}
            </#if>
        </#list>
    </div>
</main>
</body>
<script>
    function applyHighlighting() {
        <#list NamedEntitiesData as NamedEntity>
        <#if NamedEntity.begin?? && NamedEntity.end?? && NamedEntity.value?? && NamedEntity.excerpt??>
        highlightText("${NamedEntity.excerpt?js_string}", "${NamedEntity.value?lower_case}");
        </#if>
        </#list>

        <#list posData?keys as posCategory>
        <#assign posList = posData[posCategory]>
        <#list posList as pos>
        <#if pos.begin?? && pos.end?? && pos.text??>
        highlightText("${pos.text?js_string}", "${posCategory?lower_case}");
        </#if>
        </#list>
        </#list>
    }

    function highlightText(text, className) {
        const textNodes = getAllTextNodes(document.querySelector('.speech'));
        for (let i = 0; i < textNodes.length; i++) {
            const node = textNodes[i];
            const content = node.textContent;

            if (content.includes(text)) {
                const span = document.createElement('span');
                span.className = className;

                const index = content.indexOf(text);
                const matchedText = content.substring(index, index + text.length);

                const before = content.substring(0, index);
                const after = content.substring(index + text.length);

                const beforeNode = document.createTextNode(before);
                const afterNode = document.createTextNode(after);

                span.textContent = matchedText;

                const parent = node.parentNode;
                parent.insertBefore(beforeNode, node);
                parent.insertBefore(span, node);
                parent.insertBefore(afterNode, node);
                parent.removeChild(node);

                textNodes.splice(i, 1, beforeNode, afterNode);
                i++;
            }
        }
    }

    function getAllTextNodes(element) {
        const result = [];
        const walk = document.createTreeWalker(element, NodeFilter.SHOW_TEXT, null, false);
        let node;
        while (node = walk.nextNode()) {
            result.push(node);
        }
        return result;
    }

    function toggleNamedEntities() {
        const toggleCheckbox = document.getElementById('toggle-named-entities');
        const spans = document.querySelectorAll('.loc, .per, .org, .misc');
        const legendDiv = document.getElementById('named-entities-legend');

        if (toggleCheckbox.checked) {
            spans.forEach(span => {
                span.style.backgroundColor = '';
            });
            legendDiv.style.display = 'block';
        } else {
            spans.forEach(span => {
                span.style.backgroundColor = 'transparent';
            });
            legendDiv.style.display = 'none';
        }
    }

    function togglePOS() {
        const toggleCheckbox = document.getElementById('toggle-pos');
        const spans = document.querySelectorAll('.nn, .adjd, .prf, .pper, .vvinf, .art, .adja, .pws, ' +
            '.vvfin, .vainf, .adv, .apprart, .appr, .proav, .pdat, .ne,.ptkant,.vvpp,.' +
            'vafin,.kon,.viat,.pposat,.card,.pwav,.pds,.vmfin,.vapp,.kous,.kokom,.vvizu,.ptkzu,.prels,.piat');
        const legendDiv = document.getElementById('pos-legend');

        if (toggleCheckbox.checked) {
            spans.forEach(span => {
                span.style.backgroundColor = '';
            });
            legendDiv.style.display = 'block';
        } else {
            spans.forEach(span => {
                span.style.backgroundColor = 'transparent';
            });
            legendDiv.style.display = 'none';
        }
    }

    $(document).ready(function() {
        applyHighlighting();

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
</html>