package Threading;

import Class_MongoDB_Impl.Abgeordneter_MongoDB_Impl;
import com.mongodb.client.FindIterable;
import database.MongoDBHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;

/**
 * Diese Klasse läuft als Thread und wiederholt sich täglich
 * Diese Klasse greif auf die Webseite des Bundestag zu und fügt den Abgeordneten in der Datenbank eine BildURL und zudem die Meta Daten hinzu.
 */
public class AbgeordnetenFotosUpdaterRunnable implements Runnable{

    private MongoDBHandler MyMongoDBHandler;

    public AbgeordnetenFotosUpdaterRunnable(MongoDBHandler MyMongoDBHandler){
        this.MyMongoDBHandler = MyMongoDBHandler;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("AbgeordnetenFotos werden aktualisiert");


            FindIterable<org.bson.Document> Abgeordnete = MyMongoDBHandler.getCollection("Abgeordneter").find();

            for (org.bson.Document Abgeordneter_Docu : Abgeordnete) {
                Abgeordneter_MongoDB_Impl Abgeordneter = new Abgeordneter_MongoDB_Impl(Abgeordneter_Docu);
                String Vorname = Abgeordneter.getVorname();
                String Nachname = Abgeordneter.getName();

                if (!(Abgeordneter.getHq_picture().isEmpty() || Abgeordneter.getPicture_BilddatenbankBeschreibung().isEmpty())){
                    //System.out.println("Folgender Abgeordneter hat bereits ein Bild mit Beschreibung: "+Abgeordneter.getMongoID()+" "+Abgeordneter.getVorname()+" "+Abgeordneter.getName());
                    continue;
                }

                String URL = "https://bilddatenbank.bundestag.de/search/picture-result?query="+Vorname+"+"+Nachname;

                try {
                    Document document = Jsoup.connect(URL).get();
                    //System.out.println(document.html());

                    //document.select("div").forEach(div -> System.out.println(div.classNames()));

                    Element divtable = document.selectFirst("div.col-xs-12.col-sm-12.rowGridContainer");
                    //System.out.println(divtable.html()); Hier sind alle Bilder

                    if (divtable==null){
                        //System.out.println("Folgendes divtable war null: "+Vorname+" "+Nachname);
                        continue;
                    }

                    Element firstitem = divtable.selectFirst("div.item");
                    if (firstitem==null){
                        //System.out.println("Folgendes firstitem war null (kein Bild gefunden): "+Vorname+" "+Nachname);
                        continue;
                    }


                    StringBuilder caption = new StringBuilder(firstitem.toString());
                    //System.out.println(firstitem.toString());
                    int index_begin = caption.indexOf("<div class='col-md-9 col-sm-12'>");
                    int index_end = caption.indexOf("</div>");

                    int index_begin_ort = caption.indexOf("Ort:");
                    int index_end_ort = caption.indexOf("Ort:");
                    caption.replace(index_begin_ort, index_end_ort, "    ");
                    String caption_string = caption.substring(index_begin+32,index_end);

                    StringBuilder caption_modify = new StringBuilder(caption_string);

                    String final_caption = caption_string;
                    if (caption_string.contains("<b>Dieses Bild")){
                        int indexOfBildRechteBegin = caption_modify.indexOf("<b>");
                        int indexOfBildrechteEnd = caption_modify.indexOf("</b>");
                        final_caption = caption_modify.replace(indexOfBildRechteBegin,indexOfBildrechteEnd+8,"").toString();
                    } else {
                        final_caption = caption_string;
                    }

                    //System.out.println(final_caption);





                    Element img = firstitem.selectFirst("img");
                    String Picture_path = img.attr("src");
                    //System.out.println(Picture_path);

                    String Picture_URL = "https://bilddatenbank.bundestag.de"+Picture_path;
                    //System.out.println(Picture_URL);

                    MyMongoDBHandler.getCollection("Abgeordneter").findOneAndUpdate(new org.bson.Document("MongoID", Abgeordneter.getMongoID()), new org.bson.Document("$set", new org.bson.Document("BilddatenbankBeschreibung",final_caption)));
                    MyMongoDBHandler.getCollection("Abgeordneter").findOneAndUpdate(new org.bson.Document("MongoID", Abgeordneter.getMongoID()), new org.bson.Document("$set", new org.bson.Document("hq_picture",Picture_URL)));
                    System.out.println("Folgendem Abgeordneten wurde ein Bild mit Beschreibung hinzugefügt: "+Abgeordneter.getMongoID()+" "+Abgeordneter.getVorname()+" "+Abgeordneter.getName());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("Aktualisierung von Abgeordneten Fotos abgeschlossen");
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
}
