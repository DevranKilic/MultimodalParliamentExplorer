<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Rede Portfolio</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="/style_index_ftl.css">

</head>
<body>
<a class="StartseiteButton" href="/index">
    <button>Startseite</button>
</a>
<a class="ZumAbgeordneten" href="/RedePortfolio/${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}">
    <button>Zurück zum Abgeordneten Redeportfolio</button>
</a>
<br>
<h1>${abgeordneter.getVorname()} ${abgeordneter.getName()}</h1>

<img class="Bild" src="${abgeordneter.getHq_picture()}" width="400" alt="Füge ein Bild hinzu">

<div class="Bildändern">
    <p>Gebe eine URL zu einem Bild des Abgeordneten ein</p>
    <form action="/Bild/${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}" method="post">
        <label for="NeueURL">URL: </label>
        <input type="text" name="NeueURL" id="NeueURLid" placeholder="Bsp. https://bilddatenbank.bundestag.de/fotos/file7c4h1vf8jf8thcic3bj.jpg">
        <button type="button" id="Bild-reset-buttonid">Zurücksetzen</button>
        <button type="submit" id="Bild-ändernid">Bild aktualisieren</button>
    </form>
</div>

<#if abgeordneterMongo.getBildHistorie_Impl()?has_content>
    <div class="Historie">
        <h2>Historie</h2>
        <#list abgeordneterMongo.getBildHistorie_Impl()?reverse as Historie>
            <h3>${Historie.getDate()} ${Historie.getTime()}</h3>
            <p>Alter Wert: ${Historie.getOldValue1()}</p>
            <p>Neuer Wert: ${Historie.getNewValue1()}</p>
        </#list>
    </div>
</#if>

<script src="/jQuery.js"></script>
<#--script>
    $("#Bild-ändernid").on("click", function(event) {
        event.preventDefault();
        let NeueURL = $("#NeueURLid").val();
        if (NeueURL) {
            $.ajax({
                type: "PUT",
                url: "/Bild/" + "${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}",
                data: { NeueURL: NeueURL },
                success: function() {
                    alert("Erfolg");
                    location.reload();
                },
                error: function() {
                    alert("Fehler");
                    location.reload();
                }
            });
        } else {
            alert("Bitte gib eine gültige URL ein.");
        }
    });
</script-->

<script>
    $("#Bild-reset-buttonid").on("click", function(event) {
        event.preventDefault();
        $.ajax({
            type: "DELETE",
            url: "/Bild/" + "${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}",
            success: function() {
                alert("Erfolg");
                location.reload();
            },
            error: function() {
                location.reload();
            }
        });
    });
</script>
</body>
</html>