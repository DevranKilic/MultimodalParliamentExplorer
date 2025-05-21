<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Multimodal Parliament Explorer</title>
    <link rel="stylesheet" href="/style_hub_ftl.css">
    <link rel="stylesheet" href="/style_navbar_ftl.css">
    <script src="https://kit.fontawesome.com/acc5c3cbc3.js" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<#include "navbar.ftl">
<main>
    <h1>Multimodal Parliament Explorer</h1>
    <figure class="test2">
        <img src="/hub-logo.png" alt="" class="test1">
    </figure>
</main>
</body>
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
</html>