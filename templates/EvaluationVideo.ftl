<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Evaluationsergebnisse</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/style_index_ftl.css">
    <script src="jQuery.js"></script>
    <script src="https://d3js.org/d3.v7.min.js"></script>
    <script src='charts.js'></script>
    <script src='functions.js'></script>
</head>
<body>
<a class="StartseiteButton" href="/index">
    <button>Startseite</button>
</a>

<h1> Willkommen zu den Ergebnissen der Evaluation</h1>

<#list AlleEvaluationen as EvaluationIter>
    <div class="Evaluation" id="Evaluation${EvaluationIter.getMongoID()?replace(".","")}">
    <#if EvaluationIter.getCheckJCasVideoAnalyseExists()>
        <li>
            <div class="Charts" id="Charts${EvaluationIter.getMongoID()?replace(".","")}">
                <div class="NamedEntityBubbleChartEval" id="BubbleChart${EvaluationIter.getMongoID()?replace(".","")}">
                    <script>
                        $(document).ready(function(){
                            loadNamedEntityDataEvaluation("${EvaluationIter.getMongoID()?replace(".","")}");
                        })
                    </script>
                </div>

                <div class="POSPieChartEval" id="PieChart${EvaluationIter.getMongoID()?replace(".","")}">
                    <script>
                        $(document).ready(function(){
                            loadPOSDataEvaluation("${EvaluationIter.getMongoID()?replace(".","")}");
                        })
                    </script>
                </div>
            </div>

            <div class="EvaluationsVideo" id="EvaluationsVideo${EvaluationIter.getMongoID()?replace(".","")}">
                <#if EvaluationIter.getVideoMimeType()?? && EvaluationIter.getVideoBase64()??>
                <video controls id="Video${EvaluationIter.getMongoID()?replace(".","")}">
                    <source id="VideoSource${EvaluationIter.getMongoID()?replace(".","")}" type="${EvaluationIter.getVideoMimeType()}" src="data:${EvaluationIter.getVideoMimeType()};base64,${EvaluationIter.getVideoBase64()}">
                </video>
                </#if>
            </div>

            <p>MongoID: ${EvaluationIter.getMongoID()?replace(".","")}</p>
            <p>VideoName: ${EvaluationIter.getVideoName()}</p>
            <p>Transcript: ${EvaluationIter.getTranscript()}</p>
        </li>
    </#if>
    </div>
</#list>
</body>
</html>