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

<div class="Textändern">
    <p>Der ausgewählte Kommentar besitzt aktuell folgende Eigenschaften</p>
    <#if RedeAuszug.getAuszug()?has_content><p>Kommentar Text: ${RedeAuszug.getAuszug()}</p></#if>

    <#if RedeAuszug.getAbgeordneter()??>
        <p>Zugehörig zum folgenden Abgeordneten: ${RedeAuszug.getAbgeordneter().getVorname()} ${RedeAuszug.getAbgeordneter().getName()} ${RedeAuszug.getAbgeordneter().getID_String()}</p>
    <#else>
        <p>Dieser Kommenter wurde noch keinem Abgeordneten zugeordnet</p>
    </#if>

    <#if RedeAuszug.getFraktion()??>
        <p>Zugehörig zur folgenden Fraktion: ${RedeAuszug.getFraktion().getName()}</p>
    <#else>
        <p>Dieser Kommenter wurde noch keiner Fraktion zugeordnet</p>
    </#if>

    <form action="/Kommentar/?RedeAuszugMongoID=${RedeAuszug.getMongoIDMongoDB_String()}&AbgeordneterRedePortfolio=${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}" method="post">
        <label for="NeueZuordnung">Abgeordneter: </label>
        <input type="text" name="AbgeordneterSucheingabe" id="AbgeordneterSucheingabeid" placeholder="Vorname Nachname ID">

        <label for="Fraktionsnamen">Fraktionen:</label>
        <select id="Fraktionsnamen" name="Fraktionsnamen">
            <#list Fraktionsnamen as fname>
                <option value="${fname}">${fname}</option>
            </#list>
        </select>
        <button type="button" id="reset-buttonid">Zurücksetzen</button>
        <button type="submit" id="NeueZuordnungid">Kommentar Eigenschaften aktualisieren</button>
    </form>
</div>

<#if RedeAuszug.getHistorie_Impl()?has_content>
    <div class="Historie">
        <h2>Historie</h2>
        <#list RedeAuszug.getHistorie_Impl()?reverse as Historie>
            <h3>${Historie.getDate()} ${Historie.getTime()}</h3>
            <p>(Abgeordneter)</p>
            <p>Alter Wert: ${Historie.getOldValue1()}</p>
            <p>Neuer Wert: ${Historie.getNewValue1()}</p>
            <p>(Fraktion)</p>
            <p>Alter Wert: ${Historie.getOldValue2()} </p>
            <p>Neuer Wert: ${Historie.getNewValue2()}</p>
        </#list>
    </div>
</#if>

<script src="/jQuery.js"></script>
<#--script>
    $("#NeueZuordnungid").on("click", function(event) {
        let AbgeordneterSucheingabe = $("#AbgeordneterSucheingabeid").val();
        event.preventDefault();
        $.ajax({
            type: "PUT",
            url: "/Kommentar/" + "?RedeAuszugMongoID=${RedeAuszug.getMongoIDMongoDB_String()}&AbgeordneterRedePortfolio=${abgeordneter.getVorname()}_${abgeordneter.getName()}_${abgeordneter.getID_String()}",
            data: { AbgeordneterSucheingabe: AbgeordneterSucheingabe },
            success: function() {
                alert("Erfolg");
            },
            error: function() {
            }
        });
    });
</script-->

<script>
    $("#reset-buttonid").on("click", function(event) {
        event.preventDefault();
        $.ajax({
            type: "DELETE",
            url: "/Kommentar/" + "?RedeAuszugMongoID=${RedeAuszug.getMongoIDMongoDB_String()}",
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