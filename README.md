# Multimodal Parliament Explorer 🏛️

[![ ](https://img.youtube.com/vi/Ds2fyq_sJRo/hqdefault.jpg)](https://www.youtube.com/watch?v=Ds2fyq_sJRo)


# Analyse und Darstellung von Bundestagsreden

Dieses Projekt wurde im Rahmen des Moduls *Programmierpraktikum* an der Goethe-Universität Frankfurt entwickelt und stellt eine leistungsstarke Java-Anwendung zur Analyse und Präsentation von Bundestagsreden dar.

Ziel des Projekts war es, umfassende Redeprofile für Abgeordnete des Deutschen Bundestags zu erstellen. Hierzu werden automatisch öffentlich zugängliche Daten – wie Stammdaten, Redebeiträge und zugehörige Videos – aus Quellen wie dem Open-Data-Portal des Bundestags und der Parlamentsmediathek extrahiert und in einer MongoDB-Datenbank gespeichert. Eine benutzerfreundliche Weboberfläche ermöglicht es, diese Daten effizient zu durchsuchen und nach verschiedenen Kriterien wie Legislaturperioden oder Fraktionen zu filtern.

Die Analyse der Reden erfolgt durch den Einsatz moderner NLP-Methoden aus dem **Texttechnologylab** der Goethe-Universität. Tools wie **Spacy**, **GerVader** und **ParlBERT** liefern präzise Ergebnisse zur Sentiment-Analyse, thematischen Klassifikation und Identifikation der häufigsten Wortarten in den Reden. Für eine umfassende Analyse werden auch die Redevideos mit **WhisperX** transkribiert und mit den Redetexten synchronisiert, wodurch gesprochene Inhalte direkt im Text hervorgehoben werden.

Die Anwendung stellt sicher, dass alle Daten kontinuierlich aktualisiert werden, um stets aktuelle Informationen bereitzustellen. Darüber hinaus können Nutzer sowohl einzelne Reden als auch vollständige Sitzungen oder Abgeordnetenprofile in verschiedenen Formaten, wie PDF oder LaTeX, exportieren.

Dieses Projekt kombiniert fortschrittliche Datenverarbeitung, Webtechnologien und NLP, um einen transparenten und strukturierten Zugang zu parlamentarischen Reden zu ermöglichen. Nutzer:innen erhalten einen umfassenden Überblick über die Arbeit einzelner Abgeordneter, können deren Redebeiträge im Kontext einsehen und sich eigenständig ein fundiertes Bild über politische Positionen und Entwicklungen verschaffen.

---

## Datenquellen und Verarbeitung

Alle verwendeten Informationen stammen aus offiziellen und frei zugänglichen Quellen:

- Die **Stammdaten der Abgeordneten** sowie die **Plenarprotokolle** werden über das Open-Data-Portal des Deutschen Bundestags bezogen.
- **Redevideos** werden aus der Parlamentsmediathek geladen.
- **Porträtbilder** stammen aus der offiziellen Bilderdatenbank.

Die Inhalte werden per Webscraping (mittels JSoup) ausgelesen, in Java-Objektstrukturen überführt und anschließend in einer **MongoDB** gespeichert. Die Anwendung erkennt automatisch neue Veröffentlichungen (z. B. neue Sitzungen oder Änderungen bei den Abgeordneten) und aktualisiert den Datenbestand entsprechend. So bleibt die Datenbasis fortlaufend aktuell.

---

## Webanwendung

Die Anwendung stellt ein Webinterface bereit, das mit dem Java-Webframework **Javalin** umgesetzt wurde. Über die Benutzeroberfläche können Nutzer:innen:

- Legislaturperioden auswählen,
- Fraktionen anzeigen und filtern,
- Abgeordnete nach Name oder Anzahl der gehaltenen Reden sortieren,
- auf das **Redeprofil** einzelner Abgeordneter zugreifen.

Das Redeprofil zeigt unter anderem:

- Ein Foto und die Stammdaten des/der Abgeordneten,
- Eine Liste der übernommenen Ämter und Mitgliedschaften,
- Übersicht über alle gehaltenen Reden (chronologisch),
- NLP-basierte Auswertungen zu den Redebeiträgen,
- Synchronisierte Videoabschnitte mit markiertem Redetext.

---

## Sprach- und Videoanalyse

Zur Analyse der Redetexte kommen Werkzeuge des **Texttechnologylab** der Goethe-Universität zum Einsatz:

- **SpaCy** zur Tokenisierung und linguistischen Annotation,
- **GerVADER** zur Sentimentanalyse im politischen Kontext,
- **ParlBERT** zur thematischen und semantischen Verarbeitung von Reden.

Diese NLP-Komponenten ermöglichen die automatische Klassifizierung und Bewertung jeder Rede. Es werden unter anderem:

- Das **Sentiment** jedes Redebeitrags visualisiert,
- Die **meistverwendeten Themen** extrahiert und grafisch dargestellt,
- Die **Häufigkeit von Wortarten** (Nomen, Verben, Adjektive etc.) analysiert.

Zusätzlich werden die Plenarvideos mit **WhisperX** transkribiert. Die Transkription wird mit den Protokollen synchronisiert, sodass im Webinterface automatisch markiert wird, welcher Abschnitt des Textes in einem bestimmten Videoabschnitt gesprochen wird.

---

## Exportmöglichkeiten

Sowohl einzelne Reden als auch komplette Plenarsitzungen oder Abgeordnetenprofile können exportiert werden – wahlweise:

- als **PDF-Dokument**, oder
- als **LaTeX-Datei** (z. B. für wissenschaftliche Weiterverwendung).

Alle Visualisierungen (z. B. Sentiment-Graphen oder Wortartenhäufigkeit) sind im Export enthalten.

---

## API-Dokumentation

Die Anwendung stellt eine REST-API bereit. Zur Dokumentation der API-Endpunkte wird **OpenAPI** verwendet. Eine automatisch generierte API-Dokumentation ist über die Anwendung aufrufbar und listet alle verfügbaren Endpunkte sowie deren Parameternutzung.

---

## Hinweise zur Ausführung und Speicherbedarf

Beim Start der Anwendung sollte in IntelliJ (oder einer anderen Umgebung) in den **VM Options** folgende Einstellung gesetzt werden:  
-Xmx6G --add-opens java.base/java.util=ALL-UNNAMED
