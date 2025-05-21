<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Startseite Portfolio`s</title>
    <link rel="stylesheet" href="/style_index_ftl.css">
    <link rel="stylesheet" href="/style_navbar_ftl.css">
    <script src="https://kit.fontawesome.com/acc5c3cbc3.js" crossorigin="anonymous"></script>
    <script src="jQuery.js"></script>
    <script src="https://d3js.org/d3.v7.min.js"></script>
    <script src='charts.js'></script>
    <script src='functions.js'></script>
</head>
<div class="navbar">
    <#include "navbar.ftl">
    <script>
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
</div>

<body>
<#--
<a class="EvaluationsVideoButton" href="/EvaluationsVideos">
    <button>EvaluationsVideo</button>
</a>
-->

<div class="content">
    <div class="Willkommen">
        <h1>Willkommen zum Rede Portfolio</h1>
        <p>Im Rede Portolio finden Sie die Stammdaten und Reden von allen Abgeordneten</p>
        <p>Klicke auf einen Abgeordneten, um seine Stammdaten und Reden einzusehen</p>
        <div class="Auswahl">

            <form action="/index" method="get">
                <label for="wahlperiode">Wahlperiode:</label>
                <select id="wahlperiode" name="wahlperiode">
                    <#list Wahlperiode as WP_Nummer>
                        <option value="${WP_Nummer}" <#if WP_Nummer == wahlperiode>selected</#if>>${WP_Nummer}.Wahlperiode</option>
                    </#list>
                </select>

                <label for="sortierung">Sortierung:</label>
                <select id="sortierung" name="sortierung">
                    <option value="A-Z" <#if Sortierungsart == "A-Z">selected</#if>>A-Z Sortierung</option>
                    <option value="Z-A" <#if Sortierungsart == "Z-A">selected</#if>>Z-A Sortierung</option>
                    <option value="highest" <#if Sortierungsart == "highest">selected</#if>>Höchste Anzahl an Reden</option>
                    <option value="lowest" <#if Sortierungsart == "lowest">selected</#if>>Niedrigste Anzahl an Reden</option>
                </select>
                <button type="submit">Auswählen</button>
            </form>

        </div>
        <div class ="Volltextsuche">
            <form>
                <label for= "AbgeordneterSuche">Abgeordneter Suche</label>
                <input type="text" id="AbgeordneterSucheID" name="Sucheingabe" placeholder="Vorname Nachname ID">
                <button type="button" id="AbgeordneterSucheButtonID">Suchen</button>
            </form>
        </div>
        <div class ="RedeSuche">
            <form>
                <label for= "RedeSuche">Rede Suche</label>
                <input type="text" id="RedeSucheID" name="RedeSuche" placeholder="Rede Ausschnitt">
                <button type="button" id="RedeSucheButtonID">Suchen</button>
            </form>
        </div>
        <div class ="SitzungSuche">
            <form>
                <label for= "SitzungSuche">Sitzungs Suche</label>
                <input type="text" id="SitzungSucheID" name="SitzungSuche" placeholder="Sitzungsnummer(n)[27 / 2,8]">
                <button type="button" id="SitzungSucheButtonID">Als PDF Exportieren</button>
            </form>
        </div>
    </div>


    <h2 id="WahlperiodeNumber">${wahlperiode}.Wahlperiode</h2>
    <#--h2>Sortierung nach: ${Sortierungsart_Anzeige}</h2-->

    <div class="AllFraktion">
        <#list Fraktionen as fraktion>
            <div class="Fraktion">
                <h4><strong>${fraktion.getName()}</strong></h4>
            </div>
            <div class="Mitglieder">
                <ol>
                    <#list fraktion.getMitglieder_DB() as mitglied>
                        <li>
                            <a href="/RedePortfolio/${mitglied.getVorname()}_${mitglied.getName()}_${mitglied.getID_String()}">
                                <button>${mitglied.getVorname()} ${mitglied.getName()} (${mitglied.getReden_Mongo_Impl()?size}) </button>
                            </a>
                        </li>
                    </#list>
                </ol>
            </div>
        </#list>
    </div>

    <script src="/jQuery.js"></script>
    <script>
        $("#AbgeordneterSucheButtonID").on("click", function (event) {
            let AbgeordneterSucheingabe = encodeURIComponent($("#AbgeordneterSucheID").val());
            event.preventDefault();
            $.ajax({
                type: "GET",
                url: "/AbgeordneterSuche/?Vorname_Nachname_ID=" + AbgeordneterSucheingabe,
                success: function (data) {
                    if (data === "NotFound") {
                        alert("Abgeordneter nicht gefunden.")
                    } else  {
                        window.location.href = "/RedePortfolio/"+data;
                    }
                },
                error: function () {
                    alert("Abgeordneter nicht gefunden")
                }
            })
        });
    </script>

    <script>
        $("#RedeSucheButtonID").on("click", function(event) {
            let RedeSucheingabe = encodeURIComponent($("#RedeSucheID").val());
            event.preventDefault();
            $.ajax({
                type: "GET",
                url: "/RedeSuche/?RedeAusschnitt=" + RedeSucheingabe,
                success: function(data) {
                    if (data === "NotFound") {
                        alert("Rede Ausschnitt nicht gefunden.")
                    } else  {
                        window.location.href = "/RedePortfolio/"+data;
                    }
                },
                error: function() {
                    alert("Rede Ausschnitt nicht gefunden.");
                }
            });
        });
    </script>

    <script>
        $("#SitzungSucheButtonID").on("click", function(event) {
            event.preventDefault();
            let Sitzungsnummer = encodeURIComponent($("#SitzungSucheID").val());


            $.ajax({
                type: "GET",
                url: "/SitzungPDFExport/?Sitzungsnummer=" + Sitzungsnummer,
                success: function(data) {
                    if (data === "NotFound") {
                        alert("Sitzung nicht gefunden.");
                    } else {
                        const link = document.createElement('a');
                        link.href = 'data:application/pdf;base64,' + data;
                        link.download = 'Sitzung_' + Sitzungsnummer + '.pdf';
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);
                    }
                },
                error: function() {
                    alert("Fehler beim Export der Sitzung.");
                }
            });
        });
    </script>
</div>
</body>
</html>