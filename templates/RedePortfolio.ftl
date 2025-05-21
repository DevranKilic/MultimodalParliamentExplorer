<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Rede Portfolio</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/style_redeportfolio_ftl.css">
    <link rel="stylesheet" href="/style_navbar_ftl.css">
    <script src="https://kit.fontawesome.com/acc5c3cbc3.js" crossorigin="anonymous"></script>
    <script src="/jQuery.js"></script>
    <script src="https://d3js.org/d3.v7.min.js"></script>
    <script src='/charts.js'></script>
    <script src='/functions.js'></script>
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
<div class="content">
    <div class="TopButtons">
        <a class="StartseiteButton" href="/index">
            <button>Startseite</button>
        </a>

        <form>
            <label for= "PDFWatchFor"></label>
            <button type="button" id="PDFWatchButtonID">PDF Format</button>
        </form>

        <form>
            <label for="XMIWatchFor"></label>
            <button type="button" id="XMIWatchButtonID">XMI Format</button>
        </form>

        <script>
            $("#PDFWatchButtonID").on("click", function (event) {
                let AbgeordneterSucheingabe = ${abgeordneter.getMongoID_asString()};
                event.preventDefault();
                $.ajax({
                    type: "GET",
                    url: "/RedePortfolioPDFDownload/" + AbgeordneterSucheingabe,
                    success(data){
                        <#-- https://stackoverflow.com/questions/40674532/how-to-display-base64-encoded-pdf -->
                        var base64 = data;
                        const blob = base64ToBlob( base64, 'application/pdf' );
                        const url = URL.createObjectURL( blob );
                        const pdfWindow = window.open("");
                        pdfWindow.document.write("<iframe width='100%' height='100%' src='" + url + "'></iframe>");

                        function base64ToBlob( base64, type = "application/octet-stream" ) {
                            const binStr = atob( base64 );
                            const len = binStr.length;
                            const arr = new Uint8Array(len);
                            for (let i = 0; i < len; i++) {
                                arr[ i ] = binStr.charCodeAt( i );
                            }
                            return new Blob( [ arr ], { type: type } );
                        }
                        base64ToBlob(base64)
                    }
                })
            });

            $("#XMIWatchButtonID").on("click", function (event) {
                let AbgeordneterSucheingabe = ${abgeordneter.getMongoID_asString()};
                event.preventDefault();
                $.ajax({
                    type: "GET",
                    url: "/RedeXMIAnalyse/" + AbgeordneterSucheingabe,
                    success(data){
                        var base64 = data;
                        const blob = base64ToBlob( base64, 'application/xmi+xml' );
                        const url = URL.createObjectURL( blob );
                        const link = document.createElement('a');
                        link.href = url;
                        link.download = '${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}.xmi';
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);

                        function base64ToBlob( base64, type = "application/octet-stream" ) {
                            const binStr = atob( base64 );
                            const len = binStr.length;
                            const arr = new Uint8Array(len);
                            for (let i = 0; i < len; i++) {
                                arr[ i ] = binStr.charCodeAt( i );
                            }
                            return new Blob( [ arr ], { type: type } );
                        }
                        base64ToBlob(base64)
                    }
                })
            });
        </script>
    </div>


    <br>
    <h1>${abgeordneter.getVorname()} ${abgeordneter.getName()}</h1>


    <div class="PictureAndBio">

        <div class="Picture">
            <#if abgeordneter.getHq_picture()?has_content>
                <a href="/Bild/${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}">
                    <img class="Bild" src="${abgeordneter.getHq_picture()}" alt="Füge ein Bild hinzu">
                </a>
            <#else >
                <a href="/Bild/${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}">
                    <img class="Bild" src="https://img.freepik.com/free-psd/contact-icon-illustration-isolated_23-2151903337.jpg?t=st=1734542429~exp=1734546029~hmac=0838c8337578358ba41f1198680bdbb9ca70ed5ffbecb38f37731cf3130e3501&w=826" width="400" alt="Füge ein Bild hinzu (Default)">
                </a>
            </#if>
        </div>

        <div class="Biografie">
            <#if abgeordneter.getID_String()?has_content><p>Abgeordneter ID: ${abgeordneter.getID_String()}</p></#if>
            <#if abgeordneter.getPartei_kurz()?has_content><p>Partei: ${abgeordneter.getPartei_kurz()}</p></#if>
            <#if abgeordneter.getVorname()?has_content><p>Vorname: ${abgeordneter.getVorname()}</p></#if>
            <#if abgeordneter.getName()?has_content><p>Nachname: ${abgeordneter.getName()}</p></#if>
            <#if abgeordneter.getGeschlecht()?has_content><p>Geschlecht: ${abgeordneter.getGeschlecht()}</p></#if>
            <#if abgeordneter.getGeburtsdatum_Mongo_Impl()?has_content><p>Geburtsdatum: ${abgeordneter.getGeburtsdatum_Mongo_Impl()}</p></#if>
            <#if abgeordneter.getGeburtsort()?has_content><p>Geburtsort: ${abgeordneter.getGeburtsort()}</p></#if>
            <#if abgeordneter.getFamilienstand()?has_content><p>Familienstand: ${abgeordneter.getFamilienstand()}</p></#if>
            <#if abgeordneter.getReligion()?has_content><p>Religion: ${abgeordneter.getReligion()}</p></#if>
            <#if abgeordneter.getBeruf()?has_content><p>Beruf: ${abgeordneter.getBeruf()}</p></#if>
            <#if abgeordneter.getVita()?has_content><p>Vita: ${abgeordneter.getVita()}</p></#if>
        </div>
    </div>

    <div class="PictureDescription">
        <#if abgeordneter.getPicture_BilddatenbankBeschreibung()?has_content>
            <p>${abgeordneter.getPicture_BilddatenbankBeschreibung()}</p>
        </#if>
    </div>

    <h1>Wahlperioden Übersicht</h1>

    <div class="Wahlperioden">
        <#list WahlperiodenInfos as wahlperiode>
            <div class="EineWahlperiode">
                <div class="Wahlperiode">
                    <#if wahlperiode.getWahlperiode().getNumber()?has_content><p>Wahlperiode: ${wahlperiode.getWahlperiode().getNumber()}</p></#if>
                    <#if wahlperiode.getWahlperiode().getStartDate_Mongo_Impl()?has_content><p>Von: ${wahlperiode.getWahlperiode().getStartDate_Mongo_Impl()}</p></#if>
                    <#if wahlperiode.getWahlperiode().getEndDate_Mongo_Impl()?has_content><p>Bis: ${wahlperiode.getWahlperiode().getEndDate_Mongo_Impl()}</p></#if>
                </div>
                <div class="Wahlkreis">
                    <h3>Wahlkreis</h3>
                    <#if wahlperiode.getWahlkreis().getNumber()?has_content><p>Wahlkreis Nummer: ${wahlperiode.getWahlkreis().getNumber()}</p></#if>
                    <#if wahlperiode.getWahlkreis().getWKR_Name()?has_content><p>Wahlkreis Name: ${wahlperiode.getWahlkreis().getWKR_Name()}</p></#if>
                    <#if wahlperiode.getWahlkreis().getWKR_Land()?has_content><p>Wahlkreis Land: ${wahlperiode.getWahlkreis().getWKR_Land()}</p></#if>
                    <#if wahlperiode.getWahlkreis().getWKR_Liste()?has_content><p>Wahlkreis Liste: ${wahlperiode.getWahlkreis().getWKR_Liste()}</p></#if>
                </div>
                <div class="Institutionen">
                    <h3>Institutionen</h3>
                    <#list wahlperiode.getInstitution() as institution>
                        <#if institution.getIns_Lang()?has_content><p>Beschreibung: ${institution.getIns_Lang()}</p></#if>
                        <#if institution.getMDBins_Von_Mongo_Impl()?has_content><p>Mitglied von: ${institution.getMDBins_Von_Mongo_Impl()}</p></#if>
                        <#if institution.getMDBins_Bis_Mongo_Impl()?has_content><p>Mitglied bis: ${institution.getMDBins_Bis_Mongo_Impl()}</p></#if>
                        <#if institution.getFkt_lang()?has_content><p>Funktion: ${institution.getFkt_lang()}</p></#if>
                        <#if institution.getFktins_Von_Mongo_Impl()?has_content><p>Funktion von: ${institution.getFktins_Von_Mongo_Impl()}</p></#if>
                        <#if institution.getFktins_Bis_Mongo_Impl()?has_content><p>Funktion bis: ${institution.getFktins_Bis_Mongo_Impl()}</p></#if>
                    </#list>
                </div>
            </div>
        </#list>
    </div>

    <#if AlleReden?has_content>
        <div class="Inhaltsverzeichnis">
            <h1>Inhaltsverzeichnis</h1>
            <select onchange="location.href=this.value">
                <option disabled selected>Bitte auswählen…</option>
                <#list AlleReden as rede>
                    <option value="#${rede.getRede_ID()}">
                        ${rede.getDate_Mongo_Impl()} - ${rede.getSitzungsnummer()}
                    </option>
                </#list>
            </select>
        </div>
    </#if>


    <#if AlleReden?has_content>
        <h1>Alle Reden und Anlagen</h1>
    </#if>

    <div class="Reden">
        <#list AlleReden as rede>
        <div class="Rede">
            <div class="Redeinfo" id="${rede.getRede_ID()}">
                <p>Wahlperiode: ${rede.getWahlperiodeNummer()}</p>
                <p>Sitzungsnummer: ${rede.getSitzungsnummer()}</p>
                <p>Rede ID: ${rede.getRede_ID()}</p>
                <p>Datum der Sitzung: ${rede.getDate_Mongo_Impl()}</p>
                <#if rede.getTagesordnungspunkt()?has_content><p>Tagesordnungspunkt: ${rede.getTagesordnungspunkt()}</p></#if>
                <#if rede.getTOP_Beschreibung()?has_content><p>Beschreibung: ${rede.getTOP_Beschreibung()}</p></#if>
            </div>

            <div class="VisualisierungButton">
                <form>
                    <label for= "VisualisierungFor"></label>
                    <button type="button" id="VisualisierungButtonID">Rede Visualisierung</button>
                </form>
                <script>
                    $("#VisualisierungButtonID").on("click", function (event) {
                        var RedeMongoID = ${rede.getMongoID_AsString()}
                        window.location.href = /visual/ + RedeMongoID;
                    });
                </script>
            </div>

            <#if rede.getCheckJCasRedeTextExists()>
            <div class="Charts" id="Charts${rede.getMongoID_AsString()?replace(".","")}">
                <div class="NamedEntityBubbleChart" id="BubbleChart${rede.getMongoID_AsString()?replace(".","")}">
                    <script>
                        $(document).ready(function(){
                            loadNamedEntityData("${rede.getMongoID_AsString()?replace(".","")}");
                        })
                    </script>
                </div>

                <div class="POSPieChart" id="PieChart${rede.getMongoID_AsString()?replace(".","")}">
                    <script>
                        $(document).ready(function(){
                            loadPOSData("${rede.getMongoID_AsString()?replace(".","")}");
                        })
                    </script>
                </div>
                </#if>
            </div>

            <div class="RedeVideo" id="RedeVideo${rede.getMongoID_AsString()?replace(".","")}">
                <#if rede.getVideoMimeType()?? && rede.getVideoBase64()??>
                    <video controls id="Video${rede.getMongoID_AsString()?replace(".","")}">
                        <source id="VideoSource${rede.getMongoID_AsString()?replace(".","")}" type="${rede.getVideoMimeType()}" src="data:${rede.getVideoMimeType()};base64,${rede.getVideoBase64()}">
                    </video>
                </#if>
            </div>

            <div class="RedeText" id="RedeText${rede.getMongoID_AsString()?replace(".","")}">
                <#if rede.getRedeText_WithEdit()??>
                    ${rede.getRedeText_WithEdit()}
                <#else>
                    <#list rede.getRedeAuszug_Objekte() as RedeAuszug>
                        <#if RedeAuszug.getIsKommentarMongoDB()>
                            <p>
                                <a href="/Kommentar/?Abgeordneter=${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}&RedeAuszugMongoID=${RedeAuszug.getMongoIDMongoDB_String()}">
                                    <b>${RedeAuszug.getAuszug()}</b>
                                </a>
                            </p>
                        <#else>
                            <#if RedeAuszug.getAuszug()?has_content><p>${RedeAuszug.getAuszug()}</p></#if>
                        </#if>
                    </#list>
                </#if>
            </div>
        </div>
    </#list>
    </div>
</div>
</body>
</html>
