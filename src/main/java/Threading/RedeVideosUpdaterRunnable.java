package Threading;

import Class_MongoDB_Impl.Rede_MongoDB_Impl;
import Helper.Hilfsmethoden;
import Http.HttpManager;
import NLP.NLP_Impl;
import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import database.MongoDBHandler;
import org.apache.commons.io.FileUtils;
import org.bson.BsonMaximumSizeExceededException;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Diese Klasse läuft als Thread und wiederholt sich täglich
 * Es mithilfe vom Datum und Tagesordnungspunkt, das Video mit dem Tagesordnungspunkt in dem die Rede enthalten auf der Webseite vom Bundestag
 * heruntergeladen.
 */
public class RedeVideosUpdaterRunnable implements Runnable{

    private MongoDBHandler MyMongoDBHandler;

    public RedeVideosUpdaterRunnable(MongoDBHandler MyMongoDBHandler){
        this.MyMongoDBHandler = MyMongoDBHandler;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("RedeVideos werden aktualisiert");

            HttpManager MyHttpManager = new HttpManager();
            FindIterable<Document> Reden =  MyMongoDBHandler.getCollection("Rede").find();

            for (Document rede_iter : Reden) {
                Rede_MongoDB_Impl Rede = new Rede_MongoDB_Impl(MyMongoDBHandler, rede_iter.getInteger("MongoID"));

                if (!Rede.getBundestagVideoID().isEmpty()){
                    System.out.println("Rede mit der ID: "+ Rede.getRede_ID()+" besitzt schon eine BundestagVideoID: "+Rede.getBundestagVideoID());
                    try {
                        DownloadBundestagRedeVideos(Rede);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                /*
                System.out.println(Rede.getRede_ID());
                System.out.println(Rede.getVorname());
                System.out.println(Rede.getNachname());
                System.out.println(Rede.getDate_Mongo_Impl());
                System.out.println(Rede.getSitzungsnummer());
                System.out.println(Rede.getTagesordnungspunkt());

                 */

                String Tagesordnungspunkt = Rede.getTagesordnungspunkt();
                StringBuilder Tagesordnungspunkt_Searchkey = new StringBuilder("");
                if (Tagesordnungspunkt.contains("Tagesordnungspunkt")){

                } else if (Tagesordnungspunkt.contains("Zusatzpunkt")) {
                    Tagesordnungspunkt_Searchkey.append("ZP ");
                } else {
                    //System.out.println("Kein gültiger Tagesordnungspunkt: "+Tagesordnungspunkt);
                    continue;
                }

                String[] splitted = Tagesordnungspunkt.split(" ");
                Integer Zahl = -1;
                for (String split : splitted) {
                    try{
                        Zahl = Integer.parseInt(split);
                    } catch (NumberFormatException n){

                    }
                }
                if (Zahl==-1){
                    //System.out.println("Keine gültige Zahl "+Zahl);
                    continue;
                } else {
                    //System.out.println("Gültige Zahl "+Zahl);
                    Tagesordnungspunkt_Searchkey.append(String.valueOf(Zahl));
                }
                //System.out.println("Der Tagespunkt SearchKey lautet: "+ Tagesordnungspunkt_Searchkey);

                String Anfang = "01.01.1970";
                String RedeDatum = Rede.getDate_Mongo_Impl();

                LocalDate Anfang_date = Hilfsmethoden.String_to_LocalDate(Anfang);
                LocalDate Ende_date = Hilfsmethoden.String_to_LocalDate(RedeDatum);
                long AnzahlTage = ChronoUnit.DAYS.between(Anfang_date, Ende_date);
                long Datum_searchkey = ((AnzahlTage)*86400000 - 3600000);
                //System.out.println("Milisekunden: "+ Datum_searchkey);

                String Sitzungsnummer_searchkey = String.valueOf(Rede.getSitzungsnummer());
                String URL_Situngsnummer_Tagesordnungspunkt = "https://www.bundestag.de/ajax/filterlist/de/mediathek/536668-536668?limit=70&mediaCategory=442350%23Plenarsitzungen&noFilterSet=false&sitzung=536680%23%22"+Sitzungsnummer_searchkey+"%22&visibleAgendaItemNumber=536682%23%22"+Tagesordnungspunkt_Searchkey+"%22";


                //System.out.println(URL_Situngsnummer_Tagesordnungspunkt);
                //System.out.println("\n");
                try {
                    org.jsoup.nodes.Document document = Jsoup.connect(URL_Situngsnummer_Tagesordnungspunkt).get();

                    Elements info_div = document.select(".bt-slide-content");
                    for (Element element_info_div : info_div) {
                        Element a = element_info_div.selectFirst(".bt-open-in-overlay");
                        StringBuilder href = new StringBuilder(a.attr("href"));
                        String href_string = href.toString();
                        //System.out.println(href_string);
                        int indexVideoID_begin = href.indexOf("videoid=");
                        int indexVideoID_End = href.indexOf("#");
                        String VideoID = href.substring(indexVideoID_begin+8, indexVideoID_End);
                        //System.out.println(VideoID);

                        Element datum_div = a.selectFirst(".bt-teaser-text");
                        Element p = datum_div.selectFirst("p");
                        if (datum_div.text().contains(RedeDatum)){
                            //System.out.println("Gleiches Rededatum gefunden");
                            MyMongoDBHandler.getCollection("Rede").findOneAndUpdate(new Document("MongoID", Rede.getMongoID()), new Document("$set", new Document("BundestagVideoID", VideoID)));

                            String JSON_Link = "https://webtv.bundestag.de/player/macros/_x_s-144277506/shareData.json?contentId="+VideoID;
                            String json_string = MyHttpManager.SendGetRequest(JSON_Link);
                            //System.out.println(JSON_Link);
                            JSONObject json = new JSONObject(json_string);
                            String DownloadURL = json.getString("downloadUrl");
                            //System.out.println("DownloadURL: "+DownloadURL);
                            MyMongoDBHandler.getCollection("Rede").findOneAndUpdate(new Document("MongoID", Rede.getMongoID()), new Document("$set", new Document("BundestagVideoDownloadLink", DownloadURL)));


                            //Iframe kann mit Jsoup nicht entnommen werden. Selenium notwendig. Einbettungscode
                            //System.out.println("Hier der href link");
                            //System.out.println(href_string);

                            System.out.println("Folgender Rede wurde ein Video hinzugefügt: "+Rede.getRede_ID());
                            DownloadBundestagRedeVideos(Rede);
                        } else {
                            //System.out.println("Datum unterschiedlich. RedeDatum ist : "+RedeDatum+" und gefundenes Datum ist: "+p.text());
                        }


                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("Aktualisierung von RedeVideos abgeschlossen");
            //Jeden Tag
            // 1 Sekunde = 1000 Millisekunden
            // 24 Stunden x 60 Minuten x 60 Sekunden x 1000 Millisekunden
            try {
                Thread.sleep(24*60*60*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Diese Methode nimmt eine Rede entgegen und lädt das Video ins BundestagRedeVideos herunter
     * Für diese Funktion muss der Rede vorher der VideoDownload link hinzugefügt werden.
     * @param RedeObjekt
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    private void DownloadBundestagRedeVideos(Rede_MongoDB_Impl RedeObjekt) throws Exception {

        NLP_Impl myNlp = new NLP_Impl(MyMongoDBHandler);
        Document Rede_document =  MyMongoDBHandler.getCollection("Rede").find(new Document("MongoID", RedeObjekt.getMongoID())).first();

        Rede_MongoDB_Impl Rede = new Rede_MongoDB_Impl(MyMongoDBHandler, Rede_document.getInteger("MongoID"));
        File videofile = new File(Rede.getBundestagVideoLocalPath());

        if (Rede.getBundestagVideoDownloadLink().isEmpty() || videofile.exists()){
            return;
        }

        String DownloadLink = Rede.getBundestagVideoDownloadLink();


        String SaveLocation = "src/main/resources/BundestagRedeVideos/";
        String FullSavePath = SaveLocation+Rede.getRede_ID()+".mp4";
        File FullSaveLocation = new File(FullSavePath);

        if (FullSaveLocation.exists()) {
            System.out.println("Folgende Datei existiert bereits: "+ FullSavePath);
            return;
        } else {
            URL link_url = new URL(DownloadLink.toString());
            System.out.println("Folgende Datei wird heruntergeladen (Download könnte dauern): "+ FullSavePath);
            if (videofile.exists()){
                return;
            }
            FileUtils.copyURLToFile(link_url, FullSaveLocation);
            System.out.println("Folgende Datei wurde heruntergeladen: "+ FullSavePath);
            MyMongoDBHandler.getCollection("Rede").findOneAndUpdate(new Document("MongoID", Rede.getMongoID()), new Document("$set", new Document("BundestagVideoLocalPath", FullSavePath)));
        }

        try{
            File videoFile = new File(FullSavePath);
            myNlp.analyseAndUploadVideo(videoFile, Rede_document.getString("Rede_ID"));
            System.out.println("Rede wurde Analisiert");
        }catch (BsonMaximumSizeExceededException e){
            System.out.println("BsonMaximumSizeExceededException");
        }catch (MongoCommandException e){
            System.out.println("MongoCommandException");
        }catch (InvalidObjectException e){
            System.out.println("InvalidObjectException");
        }catch (OutOfMemoryError e){
        System.out.println("OutOfMemoryError");
    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}