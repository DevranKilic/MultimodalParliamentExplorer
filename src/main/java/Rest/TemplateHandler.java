package Rest;
import Class_Freemarker_Impl.Wahlperiode_Freemarker_Impl;
import Class_Interfaces.Rede;
import Class_MongoDB_Impl.*;
import Class_Impl.Historie_Impl;
import Evaluation.Evaluation_MongoDB_Impl;
import Export.PdfTex_Impl;
import Export.XmiExport;
import Helper.Hilfsmethoden;
import JCas_Extraction_Implementations.JCAS_Extractor_Impl;
import com.mongodb.Mongo;
import com.mongodb.client.FindIterable;
import database.MongoDBHandler;
import freemarker.template.Configuration;
import io.javalin.http.Context;
import io.javalin.openapi.*;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Diese Klasse beinhaltet alle Endpunkte welche Templates verwenden, sowie deren Logik.
 * Darüber hinaus befinden sich hier Caches und Methoden zum erstellen, Abrufen und Aktualisieren von den Caches.
 */
public class TemplateHandler {
    private Configuration fConfiguration = null;
    private RESTHandler pHandler = null;

    //Index
    private Map<String, List<Fraktion_MongoDB_Impl>> WP_AlleFraktionen = new HashMap<>();
    private Map<String, Map<String,Object>> Cache_Abgeordneter_Redenportfolio = new LinkedHashMap<>();
    private List<Document> AlleAbgeordneten = new ArrayList<>();
    private Boolean AllInCache = false;

    public TemplateHandler(RESTHandler pHandler, String Pfad) throws IOException {
        this.fConfiguration = new Configuration(Configuration.VERSION_2_3_33);
        fConfiguration.setDirectoryForTemplateLoading(new File(Pfad));
        this.pHandler = pHandler;
    }

    public Configuration getfConfiguration(){
        return this.fConfiguration;
    }


    /**
     * In diser Methode befinden sich alle Endpunkte.
     */
    public void init() {
        initCacheLoad();

        System.out.println("Check out ReDoc docs at http://localhost:7070/redoc");
        System.out.println("Check out Swagger UI docs at http://localhost:7070/swagger");

        //Dieser Endpunkt beinhaltet die Übersichtsseite
        this.pHandler.getHandler().get("/index", ctx -> route_IndexGet(ctx));

        //Dieser Endpunkt beinhaltet die Meldung dass eine URL eingabe nicht gefunden wurde.
        //Man gelangt hier hin wenn man z.B. /NotFound/loremipsum in die URL eingibt.
        this.pHandler.getHandler().get("/NotFound/{Suche}", ctx -> route_NotFoundGet(ctx));

        //Dieser Endpunkt beinhaltet das RedePortfolio von einem Abgeordneten
        //this.pHandler.getHandler().get("/RedePortfolio/<Vorname_Nachname_ID>", ctx -> route_RedePortfolioGet(ctx));
        this.pHandler.getHandler().get("/RedePortfolio/{Vorname_Nachname_ID}", ctx -> route_RedePortfolioGet(ctx));

        //Dieser Endpunkt beinhaltet die Seite in der man das Bild von einem Abgeordneten ändern kann und die Änderungshistorie sieht.
        this.pHandler.getHandler().get("/Bild/{Vorname_Nachname_ID}", ctx -> route_BildGet(ctx));

        //Dieser Endpunkt beinhaltet die Logik in der man das Bild von einem Abgeordneten ändern kann.
        this.pHandler.getHandler().put("/Bild/{Vorname_Nachname_ID}", ctx -> route_BildPut(ctx));

        //Dieser Endpunkt beinhaltet die Logik in der ein Bild von einem Abgeordneten gelöscht wird.
        this.pHandler.getHandler().delete("/Bild/{Vorname_Nachname_ID}", ctx -> route_BildDelete(ctx));

        //Dieser Post Endpoint beinhaltet die Logik wenn jemand ein Bild verändern möchte.
        this.pHandler.getHandler().post("/Bild/{Vorname_Nachname_ID}", ctx -> route_BildPost(ctx));

        //Dieser Endpunkt beinhaltet die Seite in der man die Zugehörigkeiten von einem Kommentar einsehen kann, diese verändern kann und die Änderungshistorie sieht
        this.pHandler.getHandler().get("/Kommentar/", ctx -> route_KommentarGet(ctx));

        //Dieser Post Endpoint beinhaltet die Logik wenn jemand die Zugehörigkeiten eines Kommentars verändern möchte.
        this.pHandler.getHandler().put("/Kommentar/", ctx -> route_KommentarPut(ctx));

        //Dieser Post Endpoint beinhaltet die Logik wenn jemand die Zugehörigkeiten eines Kommentars löschen möchte.
        this.pHandler.getHandler().delete("/Kommentar/", ctx -> route_KommentarDelete(ctx));

        //Dieser Post Endpoint beinhaltet die Logik wenn jemand die Zugehörigkeiten eines Kommentars verändern möchte.
        this.pHandler.getHandler().post("/Kommentar/", ctx -> route_KommentarPost(ctx));


        //Dieser Post Endpoint beinhaltet die Logik in der man einen Neuen Abgeordneten hinzufügen kann.
        this.pHandler.getHandler().post("/Add/Abgeordneter/", ctx -> route_AddAbgeordneterPost(ctx));

        //Dieser Get Endpoint beinhaltet die Logik einen Abgeordneten anhand eines Text Ausschnitt einer Rede zu finden.
        this.pHandler.getHandler().get("/RedeSuche/", ctx -> route_RedeSucheGet(ctx));

        //Dieser Get Endpoint beinhaltet die Logik einen Abgeordneten anhand von Vorname_Nachname_ID zu finden.
        this.pHandler.getHandler().get("/AbgeordneterSuche/", ctx -> route_AbgeordneterSucheGet(ctx));

        //Dieser Get Endpoint übermittelt die NamedEntity Daten von einer Rede MongoID um ein BubbleChart zu bilden.
        this.pHandler.getHandler().get("/NamedEntityData/{RedeMongoID}", ctx -> route_NamedEntityDataGet(ctx));

        //Dieser Get Endpoint übermittelt die POS Daten von einer Rede MongoID um ein PieChart zu bilden.
        this.pHandler.getHandler().get("/POSData/{RedeMongoIDs}", ctx -> route_POSDataGet(ctx));

        //Dieser Get Endpoint beinhaltet die Logik um die Ergebnisse der Evaluations darzustellen.
        this.pHandler.getHandler().get("/EvaluationsVideos/", ctx -> route_EvaluationsVideosGet(ctx));

        //Dieser Get Endpoint übermittelt die NamedEntity Daten von einer Evaluation MongoID um ein BubbleChart zu bilden.
        this.pHandler.getHandler().get("/NamedEntityDataEvaluation/{EvaluationMongoID}", ctx -> route_NamedEntityDataEvaluationGet(ctx));

        //Dieser Get Endpoint übermittelt die NamedEntity Daten von einer Evaluation MongoID um ein PieChart zu bilden.
        this.pHandler.getHandler().get("/POSDataEvaluation/{EvaluationMongoID}", ctx -> route_POSDataEvaluationGet(ctx));
        
        //Dieser Get Endpoint ruft die Graphen für eine bestimmte Rede auf
        this.pHandler.getHandler().get("/visual/{RedeMongoID}", ctx -> route_Visualization(ctx));

        //Dieser Get Endpoint übermittelt die Topic Daten für den Bubble Chart
        this.pHandler.getHandler().get("/TopicData/{RedeMongoIDs}", ctx -> route_TopicDataGet(ctx));

        //Dieser Get Endpoint übermittelt die Sentiment Daten für den Radar Chart
        this.pHandler.getHandler().get("/SentimentData/{RedeMongoIDs}", ctx -> route_SentimentDataGet(ctx));

        //Dieser Get Endpoint übermittelt die NamedEntity Daten für den Sunburst Chart
        this.pHandler.getHandler().get("/NamedEntityData2/{RedeMongoIDs}", ctx -> route_NamedEntityData2(ctx));
        
        //Dieser Get Endpoint nimmt die Abgeordneter MongoID an und stellt eine PDF über die Rede Analysen bereit.
        this.pHandler.getHandler().get("/RedePortfolioPDFDownload/{AbgeordneterMongoID}", ctx -> route_RedePortfolioPDFDownloadGet(ctx));

        //Dieser Get Endpoint übermittelt Daten für die Volltext Visualisierung von Reden
        this.pHandler.getHandler().get("/Volltext/{RedeMongoID}", ctx -> route_VolltextVisualization(ctx));

        //Dieser Get Endpoint führt zu der Hub Seite
        this.pHandler.getHandler().get("/hub", ctx -> route_hub(ctx));

        //Dieser Get Endpoint übermittelt Daten für den XMI Export
        this.pHandler.getHandler().get("/RedeXMIAnalyse/{AbgeordneterMongoID}", ctx -> route_RedePortfolioXMIDownloadGet(ctx));

        //Dieser Get Endpoint übermittelt Daten für den XMI Export
        this.pHandler.getHandler().get("/SitzungPDFExport", ctx -> route_SitzungPDFExportGet(ctx));
    }


    /**
     * Ermöglicht es im Cache nach einem Abgeordneten mithilfe eines Vorname_Nachname_ID Strings zu suchen,
     * Dabei muss der Suchstring nicht eindeutig sein und könnte z.B. auch nur ,,Olaf´´ sein, dies würde zum Ergebnis
     * ,,Olaf_Feldmann_11000530´´ führen.
     * @param Vorname_Nachname_ID
     * @return Abgeordneter_MongoDB_Impl
     */
    public Abgeordneter_MongoDB_Impl SucheAbgeordnter(String Vorname_Nachname_ID){
        Abgeordneter_MongoDB_Impl Abgeordnter_Portfolio = null;
        Document WantedDocument = null;
        System.out.println("Suche nach: "+Vorname_Nachname_ID);
        for (Document document : AlleAbgeordneten) {
            String iterdocu_V_N_ID = document.getString("Vorname")+" "+document.getString("Nachname")+" "+String.valueOf(document.getInteger("ID"));
            String Button = document.getString("Vorname")+"_"+document.getString("Nachname")+"_"+String.valueOf(document.getInteger("ID"));
            if (iterdocu_V_N_ID.toLowerCase().contains(Vorname_Nachname_ID.toLowerCase()) || Button.equals(Vorname_Nachname_ID)){
                Abgeordnter_Portfolio = new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),document.getInteger("MongoID"));
                System.out.println("Suche erfolgreich: "+Abgeordnter_Portfolio.getVorname()+"_"+Abgeordnter_Portfolio.getName()+"_"+Abgeordnter_Portfolio.getID()+"");
                WantedDocument = document;
                break;
            }
        }
        //Gefundenes Dokument wird nach vorne in die Liste gesetzt, damit beim nächsten mal es schneller gefunden wird
        if (WantedDocument!=null){
            this.AlleAbgeordneten.remove(WantedDocument);
            this.AlleAbgeordneten.add(0,WantedDocument);
        }

        if (Abgeordnter_Portfolio==null){
            System.out.println("Suche nicht erfolgreich");
            return null;
        }
        return Abgeordnter_Portfolio;
    }

    /**
     * Ermöglicht es im Cache nach einem Abgeordneten mithile eines Vorname_Nachname_ID Strings zu suchen,
     * Dabei muss der Suchstinrg nicht eindeutig sein.
     * Falls ein Abgeordneter gefunden wurde, wird dieser mitsamt seinem bereits bestehendem RedePortfolio ausgegeben.
     * @param V_N_ID_found
     * @return Map<String, Object>
     */
    public Map<String, Object> getAbgeordneterResponseFromCache(String V_N_ID_found){
        Map<String,Object> response = null;
        String WantedResponse_Key = null;
        Map<String,Object> WantedResponse_Value = null;
        for (String V_N_ID : Cache_Abgeordneter_Redenportfolio.keySet()) {
            if (V_N_ID.equals(V_N_ID_found)){
                WantedResponse_Key = V_N_ID;
                WantedResponse_Value = Cache_Abgeordneter_Redenportfolio.get(V_N_ID);
                response = Cache_Abgeordneter_Redenportfolio.get(V_N_ID);
                break;
            }
        }

        //Element nach vorne bringen damit es nächstes mal schneller gefunden wird
        if (WantedResponse_Key!=null){
            Cache_Abgeordneter_Redenportfolio.remove(WantedResponse_Key);
            Cache_Abgeordneter_Redenportfolio.put(WantedResponse_Key, WantedResponse_Value);
        }
        return response;
    }

    /**
     * Hier wird für einen Abgeordneten das Response Object aufgebaut, dieses beinhaltet alle Informationen die es benötigt um
     * das RedePortfolio im Template abbilden zu können. Dieses Response Objekt wird dabei auch im Cache gespeichert.
     * @param Abgeordnter_Portfolio
     * @return Map<String, Object>
     */
    public Map<String, Object> CreateAbgeordneterResponse(Abgeordneter_MongoDB_Impl Abgeordnter_Portfolio) throws ResourceInitializationException, CASException, IOException {
        System.out.println("RedePortfolio wird neu generiert");
        Map<String, Object> response = new HashMap<>();
        List<Wahlperiode_Freemarker_Impl> WahlperiodenInfos = new ArrayList<>();
        List<Integer> Wahlperioden_MongoID = Abgeordnter_Portfolio.getWahlperioden_Mongo_Impl();
        if (Wahlperioden_MongoID!=null){
            Wahlperioden_MongoID.forEach(wp -> {
                WahlperiodenInfos.add(new Wahlperiode_Freemarker_Impl(this.pHandler.getMyMongoDBHandler(),wp));
            });
        }
        WahlperiodenInfos.sort((wp_klein,wp_groß) -> Integer.compare(wp_klein.getWahlperiode().getNumber(),wp_groß.getWahlperiode().getNumber()));

        List<Rede_MongoDB_Impl> AlleReden = new ArrayList<>();
        List<Integer> Rede_MongoID = Abgeordnter_Portfolio.getReden_Mongo_Impl();
        if (!Rede_MongoID.isEmpty()){
            Rede_MongoID.forEach(iter_MongoID ->{
                AlleReden.add(new Rede_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),iter_MongoID));
            });
        }

        for (Rede_MongoDB_Impl redeMongoDB : AlleReden) {
            redeMongoDB.setVideoBase64AndVideoMimeType_process();
            if (redeMongoDB.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText")!=null){
                redeMongoDB.RedeText_NoEdit_processing();
                redeMongoDB.RedeText_WithEdit_processing();
            } else {
                Hilfsmethoden.ListIntegerToListRedeAuszugMongoDBObjekt(this.pHandler.getMyMongoDBHandler(),redeMongoDB);
            }
        }

        AlleReden.sort((sitzungsnr_klein, sitzungsnr_gross) -> Integer.compare(sitzungsnr_klein.getSitzungsnummer(),sitzungsnr_gross.getSitzungsnummer()));
        response.put("WahlperiodenInfos", WahlperiodenInfos);
        response.put("AlleReden",AlleReden);
        response.put("abgeordneter",Abgeordnter_Portfolio);
        //System.out.println("Folgendes wurde zum Cache eingefügt: "+Abgeordnter_Portfolio.getVorname()+"_"+Abgeordnter_Portfolio.getName()+"_"+Abgeordnter_Portfolio.getID());
        Cache_Abgeordneter_Redenportfolio.put(Abgeordnter_Portfolio.getVorname()+"_"+Abgeordnter_Portfolio.getName()+"_"+Abgeordnter_Portfolio.getID(),response);

        return response;
    }

    /**
     * Falls ein Abgeordneter dessen Bild verändert wurde, so wird diese Veränderung auch im RedePortfolio, welches sich im Cache befindet aktualisiert.
     * @param Abgeordneter
     */
    public void UpdatePictureInCache(Abgeordneter_MongoDB_Impl Abgeordneter){
        String V_N_ID = Abgeordneter.getVorname()+"_"+Abgeordneter.getName()+"_"+Abgeordneter.getID();
        Map<String, Object> response = null;
        String CacheAbgeordneteRedenportfolio_Key = null;
        for (String V_N_ID_Cache : Cache_Abgeordneter_Redenportfolio.keySet()) {
            if (V_N_ID_Cache.equals(V_N_ID)){
                CacheAbgeordneteRedenportfolio_Key = V_N_ID_Cache;
                response = Cache_Abgeordneter_Redenportfolio.get(V_N_ID_Cache);
                //Der neue Abgeordneter enthält die neue URL zum hq_picture
                response.remove("abgeordneter");
                response.put("abgeordneter", Abgeordneter);
                break;
            }
        }
        //Cache wird aktualisiert
        if (response!=null){
            this.Cache_Abgeordneter_Redenportfolio.remove(CacheAbgeordneteRedenportfolio_Key);
            this.Cache_Abgeordneter_Redenportfolio.put(CacheAbgeordneteRedenportfolio_Key,response);
            System.out.println("Bild im Abgeordneter Rede Portfolio Cache aktualisiert");
        }
    }

    /**
     * Diese Methode erstellt für jeden Abgeordneten ein RedePortfolio und speichert diese im Cache.
     * Dies könnte lange dauern, würde jedoch dafür sorgen dass auf der Webseite alle RedePortfolios schnell aufrufbar sind.
     */
    public void AlleAbgeordnetenRedenportfoliosToCache(){
        for (Document Abgeordneter_docu : AlleAbgeordneten) {
            Map<String, Object> response = new HashMap<>();
            Abgeordneter_MongoDB_Impl Abgeordnter_Portfolio = new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),Abgeordneter_docu.getInteger("MongoID"));
            String V_N_ID = Abgeordnter_Portfolio.getVorname()+"_"+Abgeordnter_Portfolio.getName()+"_"+Abgeordnter_Portfolio.getID();
            List<Wahlperiode_Freemarker_Impl> WahlperiodenInfos = new ArrayList<>();
            List<Integer> Wahlperioden_MongoID = Abgeordnter_Portfolio.getWahlperioden_Mongo_Impl();
            if (Wahlperioden_MongoID!=null){
                Wahlperioden_MongoID.forEach(wp -> {
                    WahlperiodenInfos.add(new Wahlperiode_Freemarker_Impl(this.pHandler.getMyMongoDBHandler(),wp));
                });
            }
            WahlperiodenInfos.sort((wp_klein,wp_groß) -> Integer.compare(wp_klein.getWahlperiode().getNumber(),wp_groß.getWahlperiode().getNumber()));
            List<Rede_MongoDB_Impl> AlleReden = new ArrayList<>();
            List<Integer> Rede_MongoID = Abgeordnter_Portfolio.getReden_Mongo_Impl();
            if (!Rede_MongoID.isEmpty()){
                Rede_MongoID.forEach(iter_MongoID ->{
                    AlleReden.add(new Rede_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),iter_MongoID));
                });
            }
            AlleReden.sort((sitzungsnr_klein, sitzungsnr_gross) -> Integer.compare(sitzungsnr_klein.getSitzungsnummer(),sitzungsnr_gross.getSitzungsnummer()));
            response.put("WahlperiodenInfos", WahlperiodenInfos);
            response.put("AlleReden",AlleReden);
            response.put("abgeordneter",Abgeordnter_Portfolio);
            Cache_Abgeordneter_Redenportfolio.put(V_N_ID,response);
            System.out.println("RedePortfolio wurde ins Cache geladen: "+V_N_ID);
        }
        this.AllInCache = true;
    }


    /**
     * Diese Methode entimmt alle Abgeordneten Dokumente aus der DB und speichert diese in der Cache.
     * Dies ermöglicht eine schnellere Volltextsuche.
     * @param MyMongoDBHandler MyMongoDBHandler
     */
    public void AlleAbgeordneten_chache_load(MongoDBHandler MyMongoDBHandler){
        //System.out.println("Alle Abgeordneten werden für die Volltextsuche in den Cache geladen");
        FindIterable<Document> AbgeordneteDB = this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").find();
        for (Document document : AbgeordneteDB) {
            this.AlleAbgeordneten.add(document);
        }
        AlleAbgeordneten.sort((a,b)-> (a.getString("Vorname")+a.getString("Nachname")+String.valueOf(a.getInteger("ID"))).compareTo(b.getString("Vorname")+b.getString("Nachname")+String.valueOf(b.getInteger("ID"))));
    }


    /**
     * Dises Methode nimmt eine Wahlperiode an und speichert alle Fraktionen samt seiner Mitglieder in den Cache.
     * Dies ermöglicht das schnelle Laden der Index Seite für verschiedene Wahlperioden..
     * @param Wahlperiode
     * @return List<Fraktion_MongoDB_Impl>
     */
    public List<Fraktion_MongoDB_Impl> cache_AlleFraktionen(String Wahlperiode){
        System.out.println("Index für die "+Wahlperiode+".Wahlperiode wird geladen");
        MongoDBHandler MyMongoDBHandler = pHandler.getMyMongoDBHandler();
        List<Fraktion_MongoDB_Impl> AlleFraktionen = new ArrayList<>();
        FindIterable<Document> Fraktionen = this.pHandler.getMyMongoDBHandler().getCollection("Fraktion").find();
        for (Document document : Fraktionen) {
            Fraktion_MongoDB_Impl iterFraktion = new Fraktion_MongoDB_Impl(MyMongoDBHandler,document.getInteger("MongoID"));
            Map<String, List<String>> Abgeordneter_ID_WP_Nr = iterFraktion.getAbgeordneter_ID_WP_Nr_Mongo_Impl();
            Abgeordneter_ID_WP_Nr.forEach((k,v) -> {
                v.forEach(wp -> {
                    if (wp.equals(Wahlperiode) || Wahlperiode.equals("")){
                        if (k!=null){
                            //System.out.println(k);
                            Document Abgeordneter = MyMongoDBHandler.getCollection("Abgeordneter").find(new Document("ID",Integer.valueOf(k))).first();

                            if (Abgeordneter!=null){
                                Abgeordneter_MongoDB_Impl iterAbgeordneter = new Abgeordneter_MongoDB_Impl(Abgeordneter);
                                iterFraktion.AddMitglieder_DB(iterAbgeordneter);
                            }
                        }
                    }
                });
            });
            if (!iterFraktion.getMitglieder_DB().isEmpty()) {
                AlleFraktionen.add(iterFraktion);
            }
        }
        return AlleFraktionen;
    }

    /**
     * Initialisiert den Cache
     */
    public void initCacheLoad(){
        //Diese Methode füllt die Abgeordneten aus der DB in den Cache, für beschleunigte Abgeordnetensuche
        AlleAbgeordneten_chache_load(this.pHandler.getMyMongoDBHandler());

        //Diese Methode lädt alle RedePortfolios im Voraus
        if (Hilfsmethoden.Cache_load_Abfrage()){
            AlleAbgeordnetenRedenportfoliosToCache();
        }

        //Diese Methode füllt den Cache mit den Fraktionen der aktuellen Wahlperiode, dabei werden die Mitglieder auch gefiltert.
        if (Hilfsmethoden.WPCache_load_Abfrage()){
            long Anzahl_Wahlperioden = this.pHandler.getMyMongoDBHandler().getCollection("Wahlperiode").countDocuments();
            for (int i = 0; i < Anzahl_Wahlperioden; i++) {
                WP_AlleFraktionen.put(String.valueOf(i),cache_AlleFraktionen(String.valueOf(i)));
            }
        } else {
            WP_AlleFraktionen.put("20",cache_AlleFraktionen("20"));
        }
    }


    @OpenApi(
            summary = "Übersichtsseite",
            operationId = "route_IndexGet",
            path = "/index",
            methods = HttpMethod.GET,
            tags = {"Index"},
            queryParams = {
                    @OpenApiParam(name="wahlperiode", description = "Wähle die Wahlperiode aus, dessen Übersicht angezeigt werden soll", required = false),
                    @OpenApiParam(name="Sucheingabe", description = "Abgeordneter Volltext Suche. Man kann nach Vorname Nachname ID suchen, z.B. alice führt zu Alice_Weidel_11004930 ", required = false),
                    @OpenApiParam(name="sortierung", description = "Gebe folgendes ein A-Z, Z-A, highest, lowest", required = false)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Übersichtsseite HTML")
            }
    )
    public void route_IndexGet(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");

        //Hier werden alle verfügbaren Wahlperioden geladen, für die Wahlperioden Auswahl
        int Anzahl_Wahlperioden = (int) this.pHandler.getMyMongoDBHandler().getCollection("Wahlperiode").countDocuments();
        List<String> Wahlperiode_Nummer_String = new ArrayList<>();
        for (int i = Anzahl_Wahlperioden; i > 0; i--) {
            Wahlperiode_Nummer_String.add(String.valueOf(i));
        }

        Map<String, Object> response = new HashMap<>();
        String Wahlperiode = ctx.queryParam("wahlperiode") != null ? ctx.queryParam("wahlperiode") : String.valueOf(Anzahl_Wahlperioden);
        response.put("wahlperiode", Wahlperiode);

        List<Fraktion_MongoDB_Impl> AlleFraktionen = new ArrayList<>();
        for (String WP : WP_AlleFraktionen.keySet()) {
            if (WP.equals(Wahlperiode)){
                AlleFraktionen = WP_AlleFraktionen.get(WP);
            }
        }
        if (AlleFraktionen.isEmpty()){
            AlleFraktionen = cache_AlleFraktionen(Wahlperiode);
            WP_AlleFraktionen.put(Wahlperiode,AlleFraktionen);
        }

        String Sortierungsart = ctx.queryParam("sortierung") != null ? ctx.queryParam("sortierung") : "";
        AlleFraktionen.forEach(f -> {
            if (Sortierungsart.equals("A-Z")) {
                response.put("Sortierungsart","A-Z");
                response.put("Sortierungsart_Anzeige", "A-Z Sortierung");
                f.getMitglieder_DB().sort((erster, zweiter) -> erster.getVorname().compareTo(zweiter.getVorname()));
            } else if (Sortierungsart.equals("Z-A")) {
                response.put("Sortierungsart","Z-A");
                response.put("Sortierungsart_Anzeige", "Z-A Sortierung");
                f.getMitglieder_DB().sort((erster, zweiter) -> erster.getVorname().compareTo(zweiter.getVorname()));
                Collections.reverse(f.getMitglieder_DB());
            } else if (Sortierungsart.equals("highest")) {
                response.put("Sortierungsart","highest");
                response.put("Sortierungsart_Anzeige", "Höchste Anzahl an Reden");
                f.getMitglieder_DB().sort((erster, zweiter) -> Integer.compare(erster.getReden_Mongo_Impl().size(),zweiter.getReden_Mongo_Impl().size()));
                Collections.reverse(f.getMitglieder_DB());
            } else if(Sortierungsart.equals("lowest")){
                response.put("Sortierungsart","lowest");
                response.put("Sortierungsart_Anzeige", "Niedrigste Anzahl an Reden");
                f.getMitglieder_DB().sort((erster, zweiter) -> Integer.compare(erster.getReden_Mongo_Impl().size(),zweiter.getReden_Mongo_Impl().size()));
            } else{
                response.put("Sortierungsart","A-Z");
                response.put("Sortierungsart_Anzeige", "A-Z Sortierung");
                f.getMitglieder_DB().sort((erster, zweiter) -> erster.getVorname().compareTo(zweiter.getVorname()));
            }
        });
        response.put("Fraktionen",AlleFraktionen);
        response.put("Wahlperiode", Wahlperiode_Nummer_String);
        ctx.render("index.ftl",response);
    }

    @OpenApi(
            summary = "RedenPortfolio",
            operationId = "route_RedePortfolioGet",
            path = "/RedePortfolio/{Vorname_Nachname_ID}",
            methods = HttpMethod.GET,
            tags = {"RedenPortfolio"},
            pathParams = {
                    @OpenApiParam(name= "Vorname_Nachname_ID", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "RedenPortfolio eines Abgeordneten HTML")
            }
    )
    public void route_RedePortfolioGet(io.javalin.http.Context ctx) throws ResourceInitializationException, CASException, IOException {
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        Abgeordneter_MongoDB_Impl Abgeordnter_Portfolio = null;
        String Vorname_Nachname_ID = ctx.pathParam("Vorname_Nachname_ID");

        Abgeordnter_Portfolio = SucheAbgeordnter(Vorname_Nachname_ID);
        if (Abgeordnter_Portfolio!=null){
            String V_N_ID_found = Abgeordnter_Portfolio.getVorname()+"_"+Abgeordnter_Portfolio.getName()+"_"+Abgeordnter_Portfolio.getID();
            Map<String, Object> response_Cache = getAbgeordneterResponseFromCache(V_N_ID_found);
            if (response_Cache!=null){
                System.out.println("Abgeordneter im Cache gefunden");
                ctx.render("RedePortfolio.ftl",response_Cache);
            } else {
                System.out.println("Abgeordneter im Cache nicht gefunden");
                response = CreateAbgeordneterResponse(Abgeordnter_Portfolio);
                ctx.render("RedePortfolio.ftl",response);
            }
        } else{
            ctx.redirect("/NotFound/"+Vorname_Nachname_ID);
        }
    }

    @OpenApi(
            summary = "Falsche URL Eingaben",
            operationId = "route_NotFoundGet",
            path = "/NotFound/{Suche}",
            methods = HttpMethod.GET,
            tags = {"NotFound"},
            pathParams = {
                    @OpenApiParam(name="Suche", description = "Beliebige Query-URL Eingabe die kein Treffer ergab.", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Fehlgeschlagene URL Eingabe HTML")
            }
    )
    private void route_NotFoundGet(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        String NotFound_Suche = ctx.pathParam("Suche");
        response.put("Suchstring",NotFound_Suche);
        ctx.render("Not_Found.ftl",response);
    }

    @OpenApi(
            summary = "Bild Übersicht",
            operationId = "route_BildGet",
            path = "/Bild/{Vorname_Nachname_ID}",
            methods = HttpMethod.GET,
            tags = {"Bild"},
            pathParams = {
                    @OpenApiParam(name= "Vorname_Nachname_ID", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Einsicht des Bild und Änderungshistorie HTML")
            }
    )
    private void route_BildGet(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        Abgeordneter_MongoDB_Impl Abgeordnter = null;
        String Vorname_Nachname_ID = ctx.pathParam("Vorname_Nachname_ID");
        System.out.println("Suche nach Bild von: "+Vorname_Nachname_ID);
        Abgeordnter = SucheAbgeordnter(Vorname_Nachname_ID);
        if (Abgeordnter!=null){
            Abgeordneter_MongoDB_Impl Abgeordneter_Mongo = new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(), Abgeordnter.getMongoID());
            Hilfsmethoden.ListBildHistorieMongoIDsToListMongoObjekt(this.pHandler.getMyMongoDBHandler(),Abgeordneter_Mongo);

            response.put("abgeordneterMongo",Abgeordneter_Mongo);
            response.put("abgeordneter",Abgeordnter);
            ctx.render("BildUpdate.ftl",response);
        } else{
            ctx.redirect("/NotFound/"+Vorname_Nachname_ID);
        }
    }


    @OpenApi(
            summary = "Bild Löschen",
            operationId = "route_BildDelete",
            path = "/Bild/{Vorname_Nachname_ID}",
            methods = HttpMethod.DELETE,
            tags = {"Bild"},
            pathParams = {
                    @OpenApiParam(name= "Vorname_Nachname_ID", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Löschung des aktuellen Bild des Abgeordneten"),
                    @OpenApiResponse(status = "404", description = "Abgeordneter nicht gefunden.")
            }
    )
    private void route_BildDelete(io.javalin.http.Context ctx) {
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        Abgeordneter_MongoDB_Impl Abgeordneter = null;
        String Vorname_Nachname_ID = ctx.pathParam("Vorname_Nachname_ID");
        Historie_Impl Historie = new Historie_Impl();
        Abgeordneter = SucheAbgeordnter(Vorname_Nachname_ID);
        if (Abgeordneter != null) {
            if (this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").updateOne(new Document("ID", Abgeordneter.getID()), new Document("$set", new Document("hq_picture", ""))).wasAcknowledged()) {
                Historie.setOldValue1(Abgeordneter.getHq_picture());
                Historie.setNewValue1("");
                Abgeordneter.putDocu("hq_picture", "");
                UpdatePictureInCache(Abgeordneter);
                response.put("abgeordneter", new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(), Abgeordneter.getMongoID()));
            }
            this.pHandler.getMyMongoDBHandler().Historie_single_Uploader("Historie", Historie);

            Abgeordneter_MongoDB_Impl Abg_Mongo = new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(), Abgeordneter.getMongoID());
            List<Integer> HistorieMongoIDs_new = new ArrayList<>();
            if (Abg_Mongo.getBildhistorieMongoIDs() != null) {
                HistorieMongoIDs_new = Abg_Mongo.getBildhistorieMongoIDs();
                HistorieMongoIDs_new.add(Historie.getMongoID());
            } else {
                HistorieMongoIDs_new.add(Historie.getMongoID());
            }
            this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").updateOne(new Document("MongoID", Abgeordneter.getMongoID()), new Document("$set", new Document("Bildhistorie_MongoIDs", HistorieMongoIDs_new)));
            ctx.status(200);
        } else {
            ctx.status(404);
        }
    }


    @OpenApi(
            summary = "Bild Ändern über form",
            operationId = "route_BildPost",
            path = "/Bild/{Vorname_Nachname_ID}",
            methods = HttpMethod.POST,
            tags = {"Bild"},
            pathParams = {
                    @OpenApiParam(name = "Vorname_Nachname_ID", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930", required = true)
            },
            formParams = {
                    @OpenApiParam(name = "NeueURL", description = "Gebe eine URL ein, die zu einem Bild des Abgeordneten führt.", required = true),
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Änderung vom Bild eines bestimmten Abgeordneten")
            }
    )
    private void route_BildPost(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        Abgeordneter_MongoDB_Impl Abgeordneter = null;
        String Vorname_Nachname_ID = ctx.pathParam("Vorname_Nachname_ID");
        String NeueURL = ctx.formParam("NeueURL");
        Historie_Impl Historie = new Historie_Impl();

        Abgeordneter = SucheAbgeordnter(Vorname_Nachname_ID);
        System.out.println("Die URL Eingabe war: "+NeueURL);
        if (Abgeordneter!=null){

            if (this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").updateOne(new Document("ID", Abgeordneter.getID()),new Document("$set", new Document("hq_picture",NeueURL))).wasAcknowledged()){
                Historie.setOldValue1(Abgeordneter.getHq_picture());
                Historie.setNewValue1(NeueURL);
                Abgeordneter.putDocu("hq_picture",NeueURL);
                UpdatePictureInCache(Abgeordneter);
                response.put("UploadStatus","Bild erfolgreich aktualisiert");
                response.put("abgeordneter", new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),Abgeordneter.getMongoID()));
            } else{
                response.put("UploadStatus","Bild konnte nicht in der Datenbank aktualisiert werden");
            }
            this.pHandler.getMyMongoDBHandler().Historie_single_Uploader("Historie",Historie);

            Abgeordneter_MongoDB_Impl Abg_Mongo = new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),Abgeordneter.getMongoID());
            List<Integer> HistorieMongoIDs_new = new ArrayList<>();
            if (Abg_Mongo.getBildhistorieMongoIDs()!=null){
                HistorieMongoIDs_new = Abg_Mongo.getBildhistorieMongoIDs();
                HistorieMongoIDs_new.add(Historie.getMongoID());
            } else {
                HistorieMongoIDs_new.add(Historie.getMongoID());
            }

            this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").updateOne(new Document("MongoID",Abgeordneter.getMongoID()), new Document("$set", new Document("Bildhistorie_MongoIDs",HistorieMongoIDs_new)));

            ctx.redirect("/Bild/"+Vorname_Nachname_ID);
        } else{
            ctx.redirect("/NotFound/"+Vorname_Nachname_ID);
            response.put("UploadStatus","Es ist ein Fehler unterlaufen");
        }
    }


    @OpenApi(
            summary = "Bild Ändern über ajax",
            operationId = "route_BildPut",
            path = "/Bild/{Vorname_Nachname_ID}",
            methods = HttpMethod.PUT,
            tags = {"Bild"},
            pathParams = {
                    @OpenApiParam(name = "Vorname_Nachname_ID", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930", required = true)
            },
            formParams = {
                    @OpenApiParam(name = "NeueURL", description = "Gebe eine URL ein, die zu einem Bild des Abgeordneten führt.", required = true),
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Änderung vom Bild eines bestimmten Abgeordneten")
            }
    )
    private void route_BildPut(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        Abgeordneter_MongoDB_Impl Abgeordneter = null;
        String Vorname_Nachname_ID = ctx.pathParam("Vorname_Nachname_ID");
        String NeueURL = ctx.formParam("NeueURL");
        Historie_Impl Historie = new Historie_Impl();
        Abgeordneter = SucheAbgeordnter(Vorname_Nachname_ID);
        System.out.println("Die URL Eingabe war: "+NeueURL);
        if (Abgeordneter!=null){
            if (this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").updateOne(new Document("ID", Abgeordneter.getID()),new Document("$set", new Document("hq_picture",NeueURL))).wasAcknowledged()){
                Historie.setOldValue1(Abgeordneter.getHq_picture());
                Historie.setNewValue1(NeueURL);
                Abgeordneter.putDocu("hq_picture",NeueURL);
                UpdatePictureInCache(Abgeordneter);
                response.put("UploadStatus","Bild erfolgreich aktualisiert");
                response.put("abgeordneter", new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),Abgeordneter.getMongoID()));
            } else{
                response.put("UploadStatus","Bild konnte nicht in der Datenbank aktualisiert werden");
            }
            this.pHandler.getMyMongoDBHandler().Historie_single_Uploader("Historie",Historie);
            Abgeordneter_MongoDB_Impl Abg_Mongo = new Abgeordneter_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(),Abgeordneter.getMongoID());
            List<Integer> HistorieMongoIDs_new = new ArrayList<>();
            if (Abg_Mongo.getBildhistorieMongoIDs()!=null){
                HistorieMongoIDs_new = Abg_Mongo.getBildhistorieMongoIDs();
                HistorieMongoIDs_new.add(Historie.getMongoID());
            } else {
                HistorieMongoIDs_new.add(Historie.getMongoID());
            }
            this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").updateOne(new Document("MongoID",Abgeordneter.getMongoID()), new Document("$set", new Document("Bildhistorie_MongoIDs",HistorieMongoIDs_new)));
        } else{
            response.put("UploadStatus","Es ist ein Fehler unterlaufen");
        }
    }


    @OpenApi(
            summary = "Kommentar Übersicht",
            operationId = "route_KommentarGet",
            path = "/Kommentar/",
            methods = HttpMethod.GET,
            tags = {"Kommentar"},
            queryParams = {
                    @OpenApiParam(name= "Abgeordneter", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930, von wessem RedePortfolio stammt der Kommentar ", required = true),
                    @OpenApiParam(name= "RedeAuszugMongoID", description = "Gebe die MongoID des RedeAuszugs an.", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Übersicht der Abgeordneten- und Fraktionszugehörigkeit eines Kommentars, sowie dessen Änderungshistorie HTML")
            }
    )
    private void route_KommentarGet(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        Map<String, Object> response = new HashMap<>();
        String V_N_ID = ctx.queryParam("Abgeordneter");
        String RedeAuszugMongoID = ctx.queryParam("RedeAuszugMongoID");
        Abgeordneter_MongoDB_Impl abgeordneter_mongo = SucheAbgeordnter(V_N_ID);
        RedeAuszug_MongoDB_Impl RedeAuszug = new RedeAuszug_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(), Integer.parseInt(RedeAuszugMongoID));
        Iterable<Document> FraktionenAuswahl = this.pHandler.getMyMongoDBHandler().getCollection("Fraktion").find();
        List<String> Fraktionsnamen = new ArrayList<>();
        for (Document document : FraktionenAuswahl) {
            Fraktionsnamen.add(document.getString("Name"));
        }
        Hilfsmethoden.ListHistorieMongoIDsToListMongoObjekt(this.pHandler.getMyMongoDBHandler(),RedeAuszug);

        response.put("abgeordneter", abgeordneter_mongo);
        response.put("RedeAuszug", RedeAuszug);
        response.put("Fraktionsnamen", Fraktionsnamen);
        ctx.render("TextUpdate.ftl",response);
    }


    @OpenApi(
            summary = "Kommentar Zugehörigkeit ändern",
            operationId = "route_KommentarPut",
            path = "/Kommentar/",
            methods = HttpMethod.PUT,
            tags = {"Kommentar"},
            queryParams = {
                    @OpenApiParam(name = "AbgeordneterRedePortfolio", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930, von wessem RedePortfolio stammt der Kommentar.", required = true),
                    @OpenApiParam(name = "RedeAuszugMongoID", description = "Gebe die MongoID des RedeAuszugs an.", required = true)
            },
            formParams = {
                    @OpenApiParam(name = "AbgeordneterSucheingabe", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930, zu dessen Zugehörigkeit wird geändert.", required = true),
                    @OpenApiParam(name = "Fraktionsnamen", description = "Gebe den Vollständigen Fraktionsnamen ein", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Änderung der Zugehörigkeiten eines bestimmten Kommentars")
            }
    )
    private void route_KommentarPut(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");

        String AbgeordneterRedePortfolio = ctx.queryParam("AbgeordneterRedePortfolio");
        String RedeAuszugMongoID = ctx.queryParam("RedeAuszugMongoID");
        String AbgeordneterSucheingabe = ctx.formParam("AbgeordneterSucheingabe");
        String Fraktion_Name = ctx.formParam("Fraktionsnamen");

        RedeAuszug_MongoDB_Impl RedeAuszug = new RedeAuszug_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(), Integer.parseInt(RedeAuszugMongoID));
        Hilfsmethoden.ListHistorieMongoIDsToListMongoObjekt(this.pHandler.getMyMongoDBHandler(),RedeAuszug);
        Historie_Impl Historie = new Historie_Impl();
        if (RedeAuszug.getAbgeordneter()!=null){
            Historie.setOldValue1(RedeAuszug.getAbgeordneter().getVorname()+" "+RedeAuszug.getAbgeordneter().getName()+" "+RedeAuszug.getAbgeordneter().getID());
        }
        if (RedeAuszug.getFraktion()!=null){
            Historie.setOldValue2(RedeAuszug.getFraktion().getName());
        }

        Abgeordneter_MongoDB_Impl Abgeordneter = SucheAbgeordnter(AbgeordneterSucheingabe);
        if (Abgeordneter!=null && !AbgeordneterSucheingabe.isEmpty()) {
            RedeAuszug.UpdateMongoDBAbgeordnter(Abgeordneter.getMongoID());
            Historie.setNewValue1(Abgeordneter.getVorname()+" "+Abgeordneter.getName()+" "+Abgeordneter.getID());
        } else if (AbgeordneterSucheingabe.isEmpty()) {
            this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").updateOne(new Document("MongoID",RedeAuszug.getMongoIDMongoDB()), new Document("$set",new Document("Abgeordneter",-1)));
        }

        Document Fraktiondoc = this.pHandler.getMyMongoDBHandler().getCollection("Fraktion").find(new Document("Name", Fraktion_Name)).first();
        int FraktionMongoID = Fraktiondoc.getInteger("MongoID");

        if (Fraktion_Name!=null) {
            RedeAuszug.UpdateMongoDBFraktion(FraktionMongoID);
            Historie.setNewValue2(Fraktion_Name);
        }
        this.pHandler.getMyMongoDBHandler().Historie_single_Uploader("Historie",Historie);

        List<Integer> HistorieMongoIDs_new = new ArrayList<>();
        if (RedeAuszug.getHistorieMongoIDs()!=null){
            HistorieMongoIDs_new = RedeAuszug.getHistorieMongoIDs();
            HistorieMongoIDs_new.add(Historie.getMongoID());
        } else {
            System.out.println(Integer.valueOf(Historie.getMongoID()));
            HistorieMongoIDs_new.add(Integer.valueOf(Historie.getMongoID()));
        }

        this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").updateOne(new Document("MongoID",RedeAuszug.getMongoIDMongoDB()), new Document("$set", new Document("Historie_MongoIDs",HistorieMongoIDs_new)));
    }

    @OpenApi(
            summary = "Kommentar Zugehörigkeit ändern",
            operationId = "route_KommentarPost",
            path = "/Kommentar/",
            methods = HttpMethod.POST,
            tags = {"Kommentar"},
            queryParams = {
                    @OpenApiParam(name = "AbgeordneterRedePortfolio", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930, von wessem RedePortfolio stammt der Kommentar.", required = true),
                    @OpenApiParam(name = "RedeAuszugMongoID", description = "Gebe die MongoID des RedeAuszugs an.", required = true)
            },
            formParams = {
                    @OpenApiParam(name = "AbgeordneterSucheingabe", description = "Es findet eine Abgeordnetensuche statt. z.B. alice führt zu Alice_Weidel_11004930, zu dessen Zugehörigkeit wird geändert.", required = true),
                    @OpenApiParam(name = "Fraktionsnamen", description = "Gebe den Vollständigen Fraktionsnamen ein", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Änderung der Zugehörigkeiten eines bestimmten Kommentars")
            }
    )
    private void route_KommentarPost(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");

        String AbgeordneterRedePortfolio = ctx.queryParam("AbgeordneterRedePortfolio");
        String RedeAuszugMongoID = ctx.queryParam("RedeAuszugMongoID");
        String AbgeordneterSucheingabe = ctx.formParam("AbgeordneterSucheingabe");
        String Fraktion_Name = ctx.formParam("Fraktionsnamen");

        RedeAuszug_MongoDB_Impl RedeAuszug = new RedeAuszug_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(), Integer.parseInt(RedeAuszugMongoID));
        Hilfsmethoden.ListHistorieMongoIDsToListMongoObjekt(this.pHandler.getMyMongoDBHandler(),RedeAuszug);
        Historie_Impl Historie = new Historie_Impl();
        if (RedeAuszug.getAbgeordneter()!=null){
            Historie.setOldValue1(RedeAuszug.getAbgeordneter().getVorname()+" "+RedeAuszug.getAbgeordneter().getName()+" "+RedeAuszug.getAbgeordneter().getID());
        }
        if (RedeAuszug.getFraktion()!=null){
            Historie.setOldValue2(RedeAuszug.getFraktion().getName());
        }

        Abgeordneter_MongoDB_Impl Abgeordneter = SucheAbgeordnter(AbgeordneterSucheingabe);
        if (Abgeordneter!=null && !AbgeordneterSucheingabe.isEmpty()) {
            RedeAuszug.UpdateMongoDBAbgeordnter(Abgeordneter.getMongoID());
            Historie.setNewValue1(Abgeordneter.getVorname()+" "+Abgeordneter.getName()+" "+Abgeordneter.getID());
        } else if (AbgeordneterSucheingabe.isEmpty()) {
            this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").updateOne(new Document("MongoID",RedeAuszug.getMongoIDMongoDB()), new Document("$set",new Document("Abgeordneter",-1)));
        }

        Document Fraktiondoc = this.pHandler.getMyMongoDBHandler().getCollection("Fraktion").find(new Document("Name", Fraktion_Name)).first();
        int FraktionMongoID = Fraktiondoc.getInteger("MongoID");

        if (Fraktion_Name!=null) {
            RedeAuszug.UpdateMongoDBFraktion(FraktionMongoID);
            Historie.setNewValue2(Fraktion_Name);
        }

        this.pHandler.getMyMongoDBHandler().Historie_single_Uploader("Historie",Historie);

        List<Integer> HistorieMongoIDs_new = new ArrayList<>();
        if (RedeAuszug.getHistorieMongoIDs()!=null){
            HistorieMongoIDs_new = RedeAuszug.getHistorieMongoIDs();
            HistorieMongoIDs_new.add(Historie.getMongoID());
        } else {
            //System.out.println(Integer.valueOf(Historie.getMongoID()));
            HistorieMongoIDs_new.add(Integer.valueOf(Historie.getMongoID()));
        }

        this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").updateOne(new Document("MongoID",RedeAuszug.getMongoIDMongoDB()), new Document("$set", new Document("Historie_MongoIDs",HistorieMongoIDs_new)));

        ctx.redirect("/Kommentar/?Abgeordneter="+AbgeordneterRedePortfolio+"&RedeAuszugMongoID="+RedeAuszugMongoID);
    }

    @OpenApi(
            summary = "Kommentar Zugehörigkeiten löschen",
            operationId = "route_KommentarDelete",
            path = "/Kommentar/",
            methods = HttpMethod.DELETE,
            tags = {"Kommentar"},
            queryParams = {
                    @OpenApiParam(name = "RedeAuszugMongoID", description = "Gebe die MongoID des RedeAuszugs an.", required = true)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Löschung der Zugehörigkeiten eines bestimmten Kommentars")
            }
    )
    private void route_KommentarDelete(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");

        String RedeAuszugMongoID = ctx.queryParam("RedeAuszugMongoID");
        RedeAuszug_MongoDB_Impl RedeAuszug = new RedeAuszug_MongoDB_Impl(this.pHandler.getMyMongoDBHandler(), Integer.parseInt(RedeAuszugMongoID));
        Hilfsmethoden.ListHistorieMongoIDsToListMongoObjekt(this.pHandler.getMyMongoDBHandler(),RedeAuszug);
        Historie_Impl Historie = new Historie_Impl();
        if (RedeAuszug.getAbgeordneter()!=null){
            Historie.setOldValue1(RedeAuszug.getAbgeordneter().getVorname()+" "+RedeAuszug.getAbgeordneter().getName()+" "+RedeAuszug.getAbgeordneter().getID());
        } else {
            Historie.setOldValue1("");
        }
        if (RedeAuszug.getFraktion()!=null){
            Historie.setOldValue2(RedeAuszug.getFraktion().getName());
        } else {
            Historie.setOldValue2("");
        }

        Historie.setNewValue1("");
        Historie.setNewValue2("");
        this.pHandler.getMyMongoDBHandler().Historie_single_Uploader("Historie",Historie);

        List<Integer> HistorieMongoIDs_new = new ArrayList<>();
        if (RedeAuszug.getHistorieMongoIDs()!=null){
            HistorieMongoIDs_new = RedeAuszug.getHistorieMongoIDs();
            HistorieMongoIDs_new.add(Historie.getMongoID());
        } else {
            System.out.println(Integer.valueOf(Historie.getMongoID()));
            HistorieMongoIDs_new.add(Integer.valueOf(Historie.getMongoID()));
        }

        this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").updateOne(new Document("MongoID",RedeAuszug.getMongoIDMongoDB()), new Document("$set", new Document("Historie_MongoIDs",HistorieMongoIDs_new)));

        this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").updateOne(new Document("MongoID",RedeAuszug.getMongoIDMongoDB()), new Document("$set",new Document("Abgeordneter",-1)));
        this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").updateOne(new Document("MongoID",RedeAuszug.getMongoIDMongoDB()), new Document("$set",new Document("Fraktion",-1)));
    }

    @OpenApi(
            summary = "Neuen Abgeordneten hinzufügen",
            operationId = "route_AddAbgeordneterPost",
            path = "/Add/Abgeordneter/",
            methods = HttpMethod.POST,
            tags = {"Abgeordneter"},
            queryParams = {
                    @OpenApiParam(name = "Vorname", type = String.class , description = "Gebe einen Vornamen ein", required = true, allowEmptyValue = false),
                    @OpenApiParam(name = "Nachname", type = String.class, description = "Gebe einen Nachnamen ein", required = true, allowEmptyValue = false)
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Es wird ein neuer Abgeordneter in die Datenbank unter der Collection AbgeordneterPost hochgeladen")
            }
    )
    public void route_AddAbgeordneterPost(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        String Vorname = ctx.queryParam("Vorname");
        String Nachname = ctx.queryParam("Nachname");
        int hash = hashCode();

        Document NewAbgeordneter = new Document();
        NewAbgeordneter.append("Vorname", Vorname);
        NewAbgeordneter.append("Nachname", Nachname);
        NewAbgeordneter.append("MongoID", hash);

        pHandler.getMyMongoDBHandler().getCollection("AbgeordneterPost").insertOne(NewAbgeordneter);
    }


    @OpenApi(
            summary = "Finde Abgeordneten anhand von einen Redeausschnitt",
            operationId = "route_RedeSucheGet",
            path = "/RedeSuche/",
            methods = HttpMethod.GET,
            tags = {"Abgeordneter"},
            queryParams = {
                    @OpenApiParam(name = "RedeAusschnitt", type = String.class , description = "Gebe einen RedeAusschnitt ein", required = true, allowEmptyValue = false),
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Das Abgeordneter RedePortfolio wird ausgegeben."),
                    @OpenApiResponse(status = "200", description = "Falls kein Abgeordneter gefunden wurde, wird NotFound zurückgegeben")
            }
    )
    public void route_RedeSucheGet(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        String SuchEingabe = ctx.queryParam("RedeAusschnitt");
        System.out.println("Sucheingabe lautet: "+ SuchEingabe);

        //Man soll bei den Abgeordneten in deren Reden suchen!!!!
        Iterable<Document> RedeAuszuge = this.pHandler.getMyMongoDBHandler().getCollection("RedeAuszug").find();
        Document RedeAuszugFound = null;
        for (Document document : RedeAuszuge) {
            if (document.getString("Auszug").toLowerCase().trim().contains(SuchEingabe.toLowerCase().trim())){
                RedeAuszugFound = document;
                break;
            }
        }

        if (RedeAuszugFound!=null){
            System.out.println("Found RedeAuszug: "+RedeAuszugFound.getString("Auszug"));
            System.out.println("RedeAuszug MongoID: "+RedeAuszugFound.getInteger("MongoID"));

            Integer RedeAuszugMongoID = RedeAuszugFound.getInteger("MongoID");

            Document RedeDoc = this.pHandler.getMyMongoDBHandler().getCollection("Rede").find(new Document("RedeAuszugMongoIDs", RedeAuszugMongoID)).first();

            if (RedeDoc!=null){
                System.out.println("Rede MongoID: "+RedeDoc.getInteger("MongoID"));
                System.out.println("AbgeordneterMongoID: "+RedeDoc.getInteger("Abgeordneter"));
                Integer AbgeordneterMongoID = RedeDoc.getInteger("Abgeordneter");
                Document AbgeordneterDoc = this.pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").find(new Document("MongoID", AbgeordneterMongoID)).first();
                if (AbgeordneterDoc!=null){
                    System.out.println("Abgeordnter lautet: "+AbgeordneterDoc.getString("Vorname")+" "+AbgeordneterDoc.getString("Nachname"));
                    String V_N_ID = AbgeordneterDoc.getString("Vorname")+"_"+AbgeordneterDoc.getString("Nachname")+"_"+AbgeordneterDoc.getInteger("ID");
                    ctx.result(V_N_ID);
                } else {
                    ctx.result("NotFound");
                }
            } else {
                ctx.result("NotFound");
            }
        } else {
            ctx.result("NotFound");
        }
    }



    @OpenApi(
            summary = "Finde Abgeordneten anhand von Vorname Nachname ID",
            operationId = "route_AbgeordneterSucheGet",
            path = "/AbgeordneterSuche/",
            methods = HttpMethod.GET,
            tags = {"Abgeordneter"},
            queryParams = {
                    @OpenApiParam(name = "Vorname_Nachname_ID", type = String.class , description = "Suche mit Vorname_Nachname_ID", required = true, allowEmptyValue = false),
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Es wird die Sucheingabe zurückgegeben falls ein Abgeordneter gefunden wurde."),
                    @OpenApiResponse(status = "200", description = "Falls kein Abgeordneter gefunden wurde, wird NotFound zurückgegeben.")
            }
    )
    private void route_AbgeordneterSucheGet(io.javalin.http.Context ctx){
        System.out.println(ctx.path());
        ctx.res().setCharacterEncoding("UTF-8");
        String SuchEingabe = ctx.queryParam("Vorname_Nachname_ID");
        System.out.println("Sucheingabe lautet: "+ SuchEingabe);
        Abgeordneter_MongoDB_Impl Abgeordneter = SucheAbgeordnter(SuchEingabe);

        if (Abgeordneter!= null){
            ctx.result(SuchEingabe);
        } else {
            ctx.result("NotFound");
        }
    }


    @OpenApi(
            summary = "Nimmt die Rede MongoID und übergibt eine JSON mit NamedEntity Daten",
            operationId = "route_NamedEntityDataGet",
            path = "/NamedEntityData/{RedeMongoID}",
            methods = HttpMethod.GET,
            tags = {"JSON"},
            queryParams = {
                    @OpenApiParam(name = "RedeMongoID", type = String.class , description = "Gebe die RedeMongoID an", required = true, allowEmptyValue = false),
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Es wird die JSON mit NamedEntity Daten für die Visualisierung zurückgegben.")
            }
    )
    private void route_NamedEntityDataGet(io.javalin.http.Context ctx) throws ResourceInitializationException, CASException {
        System.out.println(ctx.path());
        String SuchEingabe = ctx.pathParam("RedeMongoID");

        Rede_MongoDB_Impl Rede = new Rede_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(SuchEingabe));
        JCas RedeJCas = Rede.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
        JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(RedeJCas);

        String Rede_mit_Kommentaren = Rede.getRede_mit_Kommentaren();


        int size = JCasExtractor.namedEntityExtraction().getAllNamedEntity().size();
        List<String> AllNamedEntity = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int begin = JCasExtractor.namedEntityExtraction().getNamedEntity_Begin(i);
            int end = JCasExtractor.namedEntityExtraction().getNamedEntity_End(i);
            String Excerpt = Rede_mit_Kommentaren.substring(begin, end);
            AllNamedEntity.add(Excerpt);
        }

        Map<String, Integer> NamedEntity_Counter = new HashMap<>();
        for (String NamedEntity : AllNamedEntity) {
            if (NamedEntity_Counter.get(NamedEntity)!=null){
                Integer counter = NamedEntity_Counter.get(NamedEntity);
                counter++;
                NamedEntity_Counter.put(NamedEntity, counter);
            } else {
                NamedEntity_Counter.put(NamedEntity, 1);
            }
        }


        JSONArray rArray = new JSONArray();
        for (String NamedEntityKey : NamedEntity_Counter.keySet()) {
            //System.out.println("Key: "+NamedEntityKey);
            //System.out.println("Value: "+NamedEntity_Counter.get(NamedEntityKey));

            JSONObject IterData = new JSONObject();
            IterData.put("label", NamedEntityKey);
            IterData.put("value", NamedEntity_Counter.get(NamedEntityKey));
            rArray.put(IterData);
        }

        ctx.contentType("application/json");
        System.out.println("NamedEntity Endpoint fertig geladen");
        ctx.json(rArray.toString());
    }

    @OpenApi(
            summary = "Nimmt die Rede MongoID und übergibt eine JSON mit POS Daten",
            operationId = "route_POSDataGet",
            path = "/POSData/{RedeMongoID}",
            methods = HttpMethod.POST,
            tags = {"JSON"},
            queryParams = {
                    @OpenApiParam(name = "RedeMongoID", type = String.class , description = "Gebe die RedeMongoID an", required = true, allowEmptyValue = false),
            },
            responses = {
                    @OpenApiResponse(status = "200", description = "Es wird die JSON mit NamedEntity Daten für die Visualisierung zurückgegben.")
            }
    )
    /**
     * Diese Route bereitet die POS Daten für eine Menge von Reden auf
     * @param ctx
     */
    private void route_POSDataGet(io.javalin.http.Context ctx) throws ResourceInitializationException, CASException {
        System.out.println(ctx.path());
        String idsString = ctx.pathParam("RedeMongoIDs");
        String[] RedeMongoIDs = idsString.split(",");

        Map<String, Integer> combinedPOSCounter = new HashMap<>(); // Store combined POS counts

        for (String RedeMongoID : RedeMongoIDs) {
            Rede_MongoDB_Impl Rede = new Rede_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(RedeMongoID));
            JCas RedeJCas = Rede.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
            JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(RedeJCas);

            String Rede_mit_Kommentaren = Rede.getRede_mit_Kommentaren();

            int size = JCasExtractor.posExtraction().getAllPOS().size();

            for (int i = 0; i < size; i++) {
                int begin = JCasExtractor.posExtraction().getPOS_Begin(i);
                int end = JCasExtractor.posExtraction().getPOS_End(i);
                //String Excerpt = Rede_mit_Kommentaren.substring(begin, end);
                String POS_Value = JCasExtractor.posExtraction().getPOS_PosValue(i);

                combinedPOSCounter.put(POS_Value, combinedPOSCounter.getOrDefault(POS_Value, 0) + 1);
            }
        }

        JSONArray rArray = new JSONArray();
        for (Map.Entry<String, Integer> entry : combinedPOSCounter.entrySet()) {
            JSONObject IterData = new JSONObject();
            IterData.put("label", entry.getKey());
            IterData.put("value", entry.getValue());
            rArray.put(IterData);
        }

        ctx.contentType("application/json");
        System.out.println("POS Endpoint fertig geladen");
        ctx.json(rArray.toString());
    }


    @OpenApi(
            summary = "Übergibt alle Evaluationsvideo Objekte mitsamt Video in Base64 und MimeType",
            operationId = "route_EvaluationsVideosGet",
            path = "/EvaluationsVideos/",
            methods = HttpMethod.GET,
            tags = {"Evaluation_MongoDB_Impl"},
            responses = {
                    @OpenApiResponse(status = "200", description = "Übergibt alle Evaluationsvideo Objekte mitsamt Video in Base64 und MimeType für die Freemarker FTL.")
            }
    )
    private void route_EvaluationsVideosGet(io.javalin.http.Context ctx) throws IOException {
        System.out.println(ctx.path());
        Map<String, Object> response = new HashMap<>();

        FindIterable<Document> MongoDB_Dokumente = pHandler.getMyMongoDBHandler().getCollection("Evaluation").find();
        List<Evaluation_MongoDB_Impl> AlleEvaluationen = new ArrayList<>();

        for (Document document : MongoDB_Dokumente) {
            if (document.getInteger("MongoID")!=null){
                int MongoID = document.getInteger("MongoID");
                Evaluation_MongoDB_Impl EvaluationIter = new Evaluation_MongoDB_Impl(pHandler.getMyMongoDBHandler(), MongoID);
                if (EvaluationIter!=null){
                    EvaluationIter.setVideoBase64AndVideoMimeType_process();
                    AlleEvaluationen.add(EvaluationIter);
                }
            }
        }
        response.put("AlleEvaluationen", AlleEvaluationen);
        ctx.render("EvaluationVideo.ftl",response);
    }


    @OpenApi(
            summary = "Berechnet die JSON mit NamedEntity Daten für ein Evaluationsvideo",
            operationId = "route_NamedEntityDataEvaluationGet",
            path = "/NamedEntityDataEvaluation/{EvaluationMongoID}",
            methods = HttpMethod.GET,
            tags = {"JSON"},
            responses = {
                    @OpenApiResponse(status = "200", description = "Es wird die JSON mit NamedEntity Daten für die Visualisierung vom Evaluationsvideo zurückgegben.")
            }
    )
    private void route_NamedEntityDataEvaluationGet(io.javalin.http.Context ctx) throws ResourceInitializationException, CASException {
        System.out.println(ctx.path());
        String SuchEingabe = ctx.pathParam("EvaluationMongoID");


        Evaluation_MongoDB_Impl Evaluation = new Evaluation_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(SuchEingabe));
        JCas aJCas = Evaluation.toJCasFromDatabaseDocumentJSON("JCas_JSON_VideoAnalyse");
        JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(aJCas);

        String Transcript = Evaluation.getTranscript();


        int size = JCasExtractor.namedEntityExtraction().getAllNamedEntity().size();
        List<String> AllNamedEntity = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int begin = JCasExtractor.namedEntityExtraction().getNamedEntity_Begin(i);
            int end = JCasExtractor.namedEntityExtraction().getNamedEntity_End(i);
            String Excerpt = Transcript.substring(begin, end);
            AllNamedEntity.add(Excerpt);
        }

        Map<String, Integer> NamedEntity_Counter = new HashMap<>();
        for (String NamedEntity : AllNamedEntity) {
            if (NamedEntity_Counter.get(NamedEntity)!=null){
                Integer counter = NamedEntity_Counter.get(NamedEntity);
                counter++;
                NamedEntity_Counter.put(NamedEntity, counter);
            } else {
                NamedEntity_Counter.put(NamedEntity, 1);
            }
        }
        JSONArray rArray = new JSONArray();
        for (String NamedEntityKey : NamedEntity_Counter.keySet()) {
            //System.out.println("Key: "+NamedEntityKey);
            //System.out.println("Value: "+NamedEntity_Counter.get(NamedEntityKey));

            JSONObject IterData = new JSONObject();
            IterData.put("label", NamedEntityKey);
            IterData.put("value", NamedEntity_Counter.get(NamedEntityKey));
            rArray.put(IterData);
        }

        ctx.contentType("application/json");
        System.out.println("NamedEntity Endpoint fertig geladen");
        ctx.json(rArray.toString());
    }


    @OpenApi(
            summary = "Berechnet die JSON mit POS Daten für ein Evaluationsvideo",
            operationId = "route_POSDataEvaluationGet",
            path = "/POSDataEvaluation/{EvaluationMongoID}",
            methods = HttpMethod.GET,
            tags = {"JSON"},
            responses = {
                    @OpenApiResponse(status = "200", description = "Es wird die JSON mit POS Daten für die Visualisierung vom Evaluationsvideo zurückgegben.")
            }
    )
    private void route_POSDataEvaluationGet(io.javalin.http.Context ctx) throws ResourceInitializationException, CASException {
        System.out.println(ctx.path());
        String SuchEingabe = ctx.pathParam("EvaluationMongoID");

        Evaluation_MongoDB_Impl Evaluation = new Evaluation_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(SuchEingabe));
        JCas aJCas = Evaluation.toJCasFromDatabaseDocumentJSON("JCas_JSON_VideoAnalyse");
        JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(aJCas);


        int size = JCasExtractor.posExtraction().getAllPOS().size();
        List<String> AllPOS = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String POS_Value = JCasExtractor.posExtraction().getPOS_PosValue(i);
            AllPOS.add(POS_Value);
        }

        Map<String, Integer> POS_Counter = new HashMap<>();
        for (String POS : AllPOS) {
            if (POS_Counter.get(POS)!=null){
                Integer counter = POS_Counter.get(POS);
                counter++;
                POS_Counter.put(POS, counter);
            } else {
                POS_Counter.put(POS, 1);
            }
        }

        JSONArray rArray = new JSONArray();
        for (String POS : POS_Counter.keySet()) {
            //System.out.println("Key: "+POS);
            //System.out.println("Value: "+POS_Counter.get(NamedEntityKey));

            JSONObject IterData = new JSONObject();
            IterData.put("label", POS);
            IterData.put("value", POS_Counter.get(POS));
            rArray.put(IterData);
        }
        ctx.contentType("application/json");
        System.out.println("POS Endpoint fertig geladen");
        ctx.json(rArray.toString());
    }

    /**
     * Diese Route rendert das Template für die Startseite
     * @param ctx
     */
    private void route_hub(io.javalin.http.Context ctx) {
        ctx.render("hub.ftl");
    }

    /**
     * Diese Route ist zuständig für das rendern der Charts
     * @param ctx
     */
    private void route_Visualization(Context ctx) {
        String SuchEingabe = ctx.pathParam("RedeMongoID");

        Map<String, Object> model = new HashMap<>();
        model.put("SuchEingabe", SuchEingabe);

        ctx.render("Chart_Visualizations.ftl", model);
    }

    /**
     * Diese Route bereitet die Topic Daten für eine Menge von Reden auf
     * @param ctx
     */
    private void route_TopicDataGet(Context ctx) throws ResourceInitializationException, CASException {
        String idsString = ctx.pathParam("RedeMongoIDs");
        String[] RedeMongoIDs = idsString.split(",");

        Map<String, Double> TopicScores = new HashMap<>();

        for (String RedeMongoID : RedeMongoIDs) {
            Rede_MongoDB_Impl Rede = new Rede_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(RedeMongoID));
            JCas RedeJCas = Rede.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
            JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(RedeJCas);

            int size = JCasExtractor.topicExtraction().getAllTopics().size();

            for (int i = 0; i < size; i++) {
                String Topic_Value = JCasExtractor.topicExtraction().getTopic_Topic(i);
                String Topic_Score = JCasExtractor.topicExtraction().getTopic_Score(i);

                double score = Double.parseDouble(Topic_Score);
                double prevScore = TopicScores.getOrDefault(Topic_Value, 0.0);
                TopicScores.put(Topic_Value, prevScore + score);
            }
        }

        JSONArray rArray = new JSONArray();
        for (Map.Entry<String, Double> entry : TopicScores.entrySet()) {
            JSONObject IterData = new JSONObject();
            IterData.put("label", entry.getKey());
            IterData.put("value", entry.getValue());
            rArray.put(IterData);
        }

        ctx.contentType("application/json");
        System.out.println("Topic Endpoint fertig geladen");
        ctx.json(rArray.toString());
    }


    /**
     * Diese Route bereitet die Named-Entity Daten für eine Menge von Reden auf
     * @param ctx
     */
    private void route_NamedEntityData2(Context ctx) throws ResourceInitializationException, CASException {
        String idsString = ctx.pathParam("RedeMongoIDs");
        String[] RedeMongoIDs = idsString.split(",");

        List<Map<String, Object>> allNamedEntities = new ArrayList<>();

        for (String RedeMongoID : RedeMongoIDs) {
            Rede_MongoDB_Impl Rede = new Rede_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(RedeMongoID));
            JCas RedeJCas = Rede.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
            JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(RedeJCas);

            String Rede_mit_Kommentaren = Rede.getRede_mit_Kommentaren();

            int size = JCasExtractor.namedEntityExtraction().getAllNamedEntity().size();
            Map<String, Map<String, Integer>> namedEntitiesMap = new HashMap<>();

            for (int i = 0; i < size; i++) {
                int begin = JCasExtractor.namedEntityExtraction().getNamedEntity_Begin(i);
                int end = JCasExtractor.namedEntityExtraction().getNamedEntity_End(i);
                String excerpt = Rede_mit_Kommentaren.substring(begin, end);
                String value = JCasExtractor.namedEntityExtraction().getNamedEntity_Value(i);

                namedEntitiesMap.putIfAbsent(value, new HashMap<>());

                namedEntitiesMap.get(value).put(excerpt, namedEntitiesMap.get(value).getOrDefault(excerpt, 0) + 1);
            }

            //Named-Entities werden in eine Hierarchie für das Sunburst Diagramm angeordnet
            Map<String, Object> rootStructure = new HashMap<>();
            rootStructure.put("name", "root");

            List<Map<String, Object>> children = new ArrayList<>();
            for (Map.Entry<String, Map<String, Integer>> entry : namedEntitiesMap.entrySet()) {
                String category = entry.getKey();
                Map<String, Integer> excerpts = entry.getValue();

                Map<String, Object> categoryNode = new HashMap<>();
                categoryNode.put("name", category);

                List<Map<String, Object>> excerptNodes = new ArrayList<>();
                for (Map.Entry<String, Integer> excerptEntry : excerpts.entrySet()) {
                    String excerpt = excerptEntry.getKey();
                    int count = excerptEntry.getValue();

                    Map<String, Object> excerptNode = new HashMap<>();
                    excerptNode.put("name", excerpt);
                    excerptNode.put("value", count);
                    excerptNodes.add(excerptNode);
                }

                categoryNode.put("children", excerptNodes);
                children.add(categoryNode);
            }

            rootStructure.put("children", children);

            List<Map<String, Object>> responseArray = new ArrayList<>();
            responseArray.add(rootStructure);
            allNamedEntities.add(rootStructure);

        }

        ctx.contentType("application/json");
        System.out.println("NamedEntity Endpoint fertig geladen");
        ctx.json(allNamedEntities);

    }

    /**
     * Diese Route bereitet die Sentiment Daten für eine Menge von Reden auf
     * @param ctx
     */
    private void route_SentimentDataGet(Context ctx) throws ResourceInitializationException, CASException {
        String idsString = ctx.pathParam("RedeMongoIDs");
        String[] RedeMongoIDs = idsString.split(",");

        Map<String, Integer> SentimentRanges = new HashMap<>();
        SentimentRanges.put("Very Negative", 0);
        SentimentRanges.put("Negative", 0);
        SentimentRanges.put("Neutral", 0);
        SentimentRanges.put("Positive", 0);
        SentimentRanges.put("Very Positive", 0);

        for (String RedeMongoID : RedeMongoIDs) {
            Rede_MongoDB_Impl Rede = new Rede_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(RedeMongoID));
            JCas RedeJCas = Rede.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
            JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(RedeJCas);

            int size = JCasExtractor.sentimentExtraction().getAllSentiment().size();

            for (int i = 0; i < size; i++) {
                double sentimentValue = JCasExtractor.sentimentExtraction().getSentiment_Sentiment(i);

                if (sentimentValue <= -0.8) {
                    SentimentRanges.put("Very Negative", SentimentRanges.get("Very Negative") + 1);
                } else if (sentimentValue <= -0.4) {
                    SentimentRanges.put("Negative", SentimentRanges.get("Negative") + 1);
                } else if (sentimentValue < 0.4) {
                    SentimentRanges.put("Neutral", SentimentRanges.get("Neutral") + 1);
                } else if (sentimentValue < 0.8) {
                    SentimentRanges.put("Positive", SentimentRanges.get("Positive") + 1);
                } else {
                    SentimentRanges.put("Very Positive", SentimentRanges.get("Very Positive") + 1);
                }
            }
        }

        JSONArray rArray = new JSONArray();
        for (Map.Entry<String, Integer> entry : SentimentRanges.entrySet()) {
            JSONObject IterData = new JSONObject();
            IterData.put("label", entry.getKey());
            IterData.put("value", entry.getValue());
            rArray.put(IterData);
        }

        ctx.contentType("application/json");
        System.out.println("Sentiment Endpoint fertig geladen");
        ctx.json(rArray.toString());
    }

    private void route_RedePortfolioPDFDownloadGet(io.javalin.http.Context ctx) throws IOException, ResourceInitializationException, CASException {

        System.out.println(ctx.path());
        String SuchEingabe = ctx.pathParam("AbgeordneterMongoID");
        System.out.println("Sucheingabe lautet: "+SuchEingabe);

        Document Abgeordneter_docu = pHandler.getMyMongoDBHandler().getCollection("Abgeordneter").find(new Document("MongoID", Integer.parseInt(SuchEingabe))).first();

        if (Abgeordneter_docu!= null){
            Abgeordneter_MongoDB_Impl Abgeordneter = new Abgeordneter_MongoDB_Impl(Abgeordneter_docu);
            List<Integer> RedeMongoIDs = Abgeordneter.getReden_Mongo_Impl();
            Integer AbgID = Abgeordneter.getMongoID();
            System.out.println(RedeMongoIDs);
            PdfTex_Impl exp = new PdfTex_Impl(pHandler.getMyMongoDBHandler());


            String PfadPDF = "src/main/resources/RedePDFAnalyse/" + AbgID + ".pdf";
            StringBuilder abgRede = exp.abgRedeTex(AbgID);
            exp.exportLatexToPDF(abgRede.toString(), PfadPDF);

            File PDF_file = new File(PfadPDF);
            String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(PDF_file));
            ctx.result(encoded);
        } else {
            System.out.println("Abgeordneter nicht gefunden");
        }
    }

    /**
     * Diese Route gibt Daten für die Volltext Visualisierung einer Rede an das Frontend weiter
     * @param ctx
     */
    private void route_VolltextVisualization(Context ctx) throws ResourceInitializationException, CASException {
        String SuchEingabe = ctx.pathParam("RedeMongoID");

        Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(pHandler.getMyMongoDBHandler(), Integer.valueOf(SuchEingabe));
        List<Integer> redeAuszugMongoIDs = rede.getRedeAuszugMongoIDs();

        JCas RedeJCas = rede.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
        JCAS_Extractor_Impl JCasExtractor = new JCAS_Extractor_Impl(RedeJCas);

        String Rede_mit_Kommentaren = rede.getRede_mit_Kommentaren();

        //Sentiment Daten aufbereiten
        List<Map<String, Object>> sentimentDataList = new ArrayList<>();
        int sentimentSize = JCasExtractor.sentimentExtraction().getAllSentiment().size();

        for (int i = 0; i < sentimentSize; i++) {
            int sentimentBegin = JCasExtractor.sentimentExtraction().getSentiment_Begin(i);
            int sentimentEnd = JCasExtractor.sentimentExtraction().getSentiment_End(i);
            String sentenceText = Rede_mit_Kommentaren.substring(sentimentBegin, sentimentEnd);

            double sentimentValue = JCasExtractor.sentimentExtraction().getSentiment_Sentiment(i);

            Map<String, Object> sentimentData = new HashMap<>();
            sentimentData.put("sentimentBegin", sentimentBegin);
            sentimentData.put("sentimentEnd", sentimentEnd);
            sentimentData.put("sentimentValue", sentimentValue);
            sentimentData.put("sentenceText", sentenceText);
            sentimentDataList.add(sentimentData);
        }

        //Named-Entity Daten aufbereiten
        List<Map<String, Object>> NamedEntitiesData = new ArrayList<>();
        int namedEntitySize = JCasExtractor.namedEntityExtraction().getAllNamedEntity().size();
        for (int i = 0; i < namedEntitySize; i++) {
            int begin = JCasExtractor.namedEntityExtraction().getNamedEntity_Begin(i);
            int end = JCasExtractor.namedEntityExtraction().getNamedEntity_End(i);
            String excerpt = Rede_mit_Kommentaren.substring(begin, end);
            String value = JCasExtractor.namedEntityExtraction().getNamedEntity_Value(i);

            Map<String, Object> NamedEntity = new HashMap<>();
            NamedEntity.put("begin", begin);
            NamedEntity.put("end", end);
            NamedEntity.put("excerpt", excerpt);
            NamedEntity.put("value", value);
            NamedEntitiesData.add(NamedEntity);
        }

        //POS-Daten aufbereiten
        Map<String, List<Map<String, Object>>> posData = new HashMap<>();
        int posSize = JCasExtractor.posExtraction().getAllPOS().size();
        for (int i = 0; i < posSize; i++) {
            int Begin = JCasExtractor.posExtraction().getPOS_Begin(i);
            int End = JCasExtractor.posExtraction().getPOS_End(i);
            String POS_Value = JCasExtractor.posExtraction().getPOS_PosValue(i);
            String POS_Text = Rede_mit_Kommentaren.substring(Begin, End);

            Map<String, Object> pos = new HashMap<>();
            pos.put("text", POS_Text);
            pos.put("begin", Begin);
            pos.put("end", End);

            posData.computeIfAbsent(POS_Value, k -> new ArrayList<>()).add(pos);
        }

        //Redner infos aufbereiten
        int abgeordneterID = rede.getRedner_Mongo_Impl();
        Abgeordneter_MongoDB_Impl abgeordneter = new Abgeordneter_MongoDB_Impl(pHandler.getMyMongoDBHandler(), abgeordneterID);
        String nachname = abgeordneter.getName();
        String vorname = abgeordneter.getVorname();
        String partei = abgeordneter.getPartei_kurz();
        String geburtsdatum = abgeordneter.getGeburtsdatum_Mongo_Impl();
        String hqPicture = abgeordneter.getHq_picture();

        // Rede mit Kommentaren vorbereiten
        List<Map<String, Object>> AuszugList = new ArrayList<>();
        for (Integer auszugID : redeAuszugMongoIDs) {
            RedeAuszug_MongoDB_Impl redeAuszug = new RedeAuszug_MongoDB_Impl(pHandler.getMyMongoDBHandler(), auszugID);
            String text = redeAuszug.getAuszug();
            boolean isKommentar = redeAuszug.getIsKommentarMongoDB();

            Map<String, Object> Auszug = new HashMap<>();
            Auszug.put("text", text);
            Auszug.put("isComment", isKommentar);
            AuszugList.add(Auszug);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("nachname", nachname);
        model.put("vorname", vorname);
        model.put("partei", partei);
        model.put("geburtsdatum", geburtsdatum);
        model.put("hqPicture", hqPicture);
        model.put("AuszugList", AuszugList);
        model.put("NamedEntitiesData", NamedEntitiesData);
        model.put("posData", posData);
        model.put("sentimentDataList", sentimentDataList);

        ctx.render("Volltext_Visualizations.ftl", model);
    }


    private void route_RedePortfolioXMIDownloadGet(io.javalin.http.Context ctx) throws IOException, ResourceInitializationException, CASException {

        System.out.println(ctx.path());
        String SuchEingabe = ctx.pathParam("AbgeordneterMongoID");
        System.out.println("Sucheingabe lautet: " + SuchEingabe);

        Document Abgeordneter_docu = pHandler.getMyMongoDBHandler().getCollection("Abgeordneter")
                .find(new Document("MongoID", Integer.parseInt(SuchEingabe))).first();

        if (Abgeordneter_docu != null) {
            Abgeordneter_MongoDB_Impl Abgeordneter = new Abgeordneter_MongoDB_Impl(Abgeordneter_docu);

            Integer AbgID = Abgeordneter.getMongoID();

            XmiExport xmiExport = new XmiExport(pHandler.getMyMongoDBHandler());
            StringBuilder xmiString = xmiExport.abgRedeXmi(AbgID);
            String PfadXMI = "src/main/resources/RedeXMIAnalyse/" + AbgID + ".xmi";
            xmiExport.exportXmiToFile(xmiString.toString(), PfadXMI);

            File XMI_file = new File(PfadXMI);
            String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(
                    org.apache.commons.io.FileUtils.readFileToByteArray(XMI_file));

            ctx.result(encoded);
        } else {
            System.out.println("Abgeordneter nicht gefunden");
        }
    }


    /**
     * Route for the Sitzung-to-PDF download
     * @param ctx
     * @throws IOException
     * @throws ResourceInitializationException
     * @throws CASException
     */
    private void route_SitzungPDFExportGet(io.javalin.http.Context ctx) throws IOException, ResourceInitializationException, CASException {
        String Sitzungsnummer = ctx.queryParam("Sitzungsnummer");
        System.out.println("Sitzungsnummer lautet: " + Sitzungsnummer);

        if (Sitzungsnummer == null || Sitzungsnummer.isEmpty()) {
            ctx.status(400).result("Sitzungsnummer is required");
            return;
        }

        List<Integer> Sitzungsnummern = new ArrayList<>();
        if (Sitzungsnummer.contains(",")) {
            String[] nummern = Sitzungsnummer.split(",");
            for (String nummer : nummern) {
                try {
                    Sitzungsnummern.add(Integer.parseInt(nummer.trim()));
                } catch (NumberFormatException e) {
                    ctx.status(400).result("Ungültige Sitzungsnummer: " + nummer);
                    return;
                }
            }
        } else {
            try {
                Sitzungsnummern.add(Integer.parseInt(Sitzungsnummer.trim()));
            } catch (NumberFormatException e) {
                ctx.status(400).result("Ungültige Sitzungsnummer: " + Sitzungsnummer);
                return;
            }
        }

        List<Integer> MongoIDs = new ArrayList<>();
        for (Integer nummer : Sitzungsnummern) {
            Document Sitzung_docu = pHandler.getMyMongoDBHandler().getCollection("Sitzung")
                    .find(new Document("Sitzungsnr", nummer)).first();

            if (Sitzung_docu == null) {
                ctx.status(404).result("Sitzung nicht gefunden: " + nummer);
                return;
            }

            Integer MongoID = Sitzung_docu.getInteger("MongoID");
            MongoIDs.add(MongoID);
        }
        //System.out.println(MongoIDs);

        PdfTex_Impl exp = new PdfTex_Impl(pHandler.getMyMongoDBHandler());
        String PfadPDF = "src/main/resources/SitzungPDFExport/" + Sitzungsnummer.replace(",", "_") + ".pdf";

        StringBuilder sitzungRede;
        if (MongoIDs.size() == 1) {
            sitzungRede = exp.singleSitzungTex(MongoIDs.get(0));
        } else {
            sitzungRede = exp.multipleSitzungenTex(MongoIDs);
        }

        if (sitzungRede == null || sitzungRede.toString().isEmpty()) {
            ctx.status(500).result("Fehler beim Generieren des PDF-Inhalts");
            return;
        }

        exp.exportLatexToPDF(sitzungRede.toString(), PfadPDF);

        File PDF_file = new File(PfadPDF);
        if (!PDF_file.exists()) {
            System.out.println("Fehler: PDF-Datei wurde nicht erstellt.");
            ctx.status(500).result("Fehler beim Erstellen der PDF-Datei");
            return;
        }

        String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(PDF_file));
        ctx.result(encoded);
        System.out.println("PDF erfolgreich als Base64 zurückgegeben.");
    }

}

