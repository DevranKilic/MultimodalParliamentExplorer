package Threading;

import Factories.Factory;
import Helper.Hilfsmethoden;
import database.MongoDBHandler;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Diese Klasse läuft als Thread und wiederholt sich täglich
 * Diese Klasse greif auf die Webseite des Bundestag zu und lädt die Plenarprotokolle der aktuellen Legislaturperiode herunter und lädt diese in die Datenbank hoch
 */
public class PlenarprotokollUpdaterRunnable implements Runnable {

    private MongoDBHandler MyMongoDBHandler;

    public PlenarprotokollUpdaterRunnable(MongoDBHandler MyMongoDBHandler){
        this.MyMongoDBHandler = MyMongoDBHandler;
    }

    @Override
    public void run() {
        while (true){
            System.out.println("Plenarprotokolle werden aktualisiert");
            boolean EndReached = false;
            int offset_counter = 0;
            while (!EndReached){
                String URL = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/866354-866354?limit=10&noFilterSet=true&offset="+String.valueOf(offset_counter);
                try {
                    Document document = Jsoup.connect(URL).get();

                    Element tbody = document.selectFirst("tbody");
                    Elements tr = tbody.select("tr");
                    //Element p = tr.selectFirst("p");

                    for (Element tr_iter : tr) {
                        //System.out.println(tr_iter.html());
                        Element a = tr_iter.selectFirst(".bt-link-dokument");
                        //System.out.println(a.attr("href"));
                        StringBuilder Link = new StringBuilder(a.attr("href"));
                        //System.out.println(Link);
                        int index_of_slash = Link.lastIndexOf("/");
                        int index_of_point = Link.lastIndexOf(".");
                        String filename = Link.substring(index_of_slash+3, index_of_point);
                        //System.out.println(filename);

                        String filename_withoutZeros = String.valueOf(Integer.parseInt(filename));
                        String SaveLocation = "src/main/resources/20/";
                        String FullSavePath = SaveLocation+filename_withoutZeros+".xml";
                        File Plenarprotokoll = new File(FullSavePath);


                        if (MyMongoDBHandler.getCollection("Plenarprotokoll").find(new org.bson.Document(new org.bson.Document("PlenarprotokollNummer", filename_withoutZeros))).first()!=null) {
                            System.out.println("Folgendes Plenarprotokoll existiert bereits in der Datenbank: "+ filename_withoutZeros);
                            continue;
                        } else {
                            URL link_url = new URL(Link.toString());
                            FileUtils.copyURLToFile(link_url, Plenarprotokoll);

                            String PlenarprotokollEncoded = org.apache.commons.codec.binary.Base64.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(Plenarprotokoll));

                            org.bson.Document Plenarprotokoll_docu = new org.bson.Document();
                            Plenarprotokoll_docu.put("MongoID",  hashCode());
                            Plenarprotokoll_docu.put("PlenarprotokollNummer", filename_withoutZeros);
                            Plenarprotokoll_docu.put("Base64XML", PlenarprotokollEncoded);
                            Plenarprotokoll_docu.put("Verarbeitet", false);
                            MyMongoDBHandler.getCollection("Plenarprotokoll").insertOne(Plenarprotokoll_docu);
                            Plenarprotokoll.delete();
                            System.out.println("Folgendes Plenarprotokoll wurde in die Datenbank hochgeladen: "+ filename_withoutZeros);
                        }
                    }

                    if (tr.size()<10){
                        EndReached = true;
                    } else {
                        offset_counter += 10;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                UpdateMDBAndDTD();
                Factory factory = new Factory(MyMongoDBHandler);
                //Dies die Datenbank verändern.
                //Hilfsmethoden.UploadNewData(MyMongoDBHandler, factory);

                System.out.println("Aktualisierung von Plenarprotokolle abgeschlossen");

                //Jeden Tag
                // 1 Sekunde = 1000 Millisekunden
                // 24 Stunden x 60 Minuten x 60 Sekunden x 1000 Millisekunden
                Thread.sleep(24*60*60*1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Diese Methode entnimmt von Webseite des Bundestag den aktuellen MdB-Stammdaten und DTD-Plenarprotokoll
     */
    public void UpdateMDBAndDTD() throws IOException {

        System.out.println("Aktualisiere MdB-Stammdaten und Plenarprotokoll DTD");

        String BundestagURL = "https://www.bundestag.de";
        String URL = "https://www.bundestag.de/services/opendata";
        org.jsoup.nodes.Document document = Jsoup.connect(URL).get();

        Element btlinkliste = document.selectFirst("ul.bt-linkliste");
        Elements btlinkdokumente = btlinkliste.select("a.bt-link-dokument");

        for (Element element : btlinkdokumente) {

            if (element.attr("title").contains("xml-dtd")){
                //System.out.println("xml-dtd Vorhanden");
                //System.out.println(element);
                //System.out.println(element.attr("href"));

                String FullSavePath = "src/main/resources/20/dbtplenarprotokoll.dtd";
                File SaveFile = new File(FullSavePath);
                String href = element.attr("href");
                java.net.URL link_url = new URL( BundestagURL+href.toString());
                FileUtils.copyURLToFile(link_url, SaveFile);
                System.out.println("Aktualisiert: dbtplenarprotokoll.dtd");
            } else if (element.attr("title").contains("Strukturdefinition")) {
                //System.out.println("Strukturdefinition Vorhanden");
                //System.out.println(element);
                //System.out.println(element.attr("href"));

                String FullSavePath = "src/main/resources/PlenarprotokollDokumentation/dbtplenarprotokoll_kommentiert.pdf";
                File SaveFile = new File(FullSavePath);
                String href = element.attr("href");
                java.net.URL link_url = new URL(BundestagURL+href.toString());
                FileUtils.copyURLToFile(link_url, SaveFile);
                System.out.println("Aktualisiert: dbtplenarprotokoll_kommentiert.pdf");
            } else if (element.attr("title").contains("Stammdaten")) {
                //System.out.println("Stammdaten Vorhanden");
                //System.out.println(element);
                //System.out.println(element.attr("href"));

                String FullSavePath = "src/main/resources/MdB-Stammdaten/MdB-Stammdaten.zip";
                File SaveFile = new File(FullSavePath);
                String href = element.attr("href");
                java.net.URL link_url = new URL(BundestagURL+href.toString());
                FileUtils.copyURLToFile(link_url, SaveFile);
                //https://www.youtube.com/watch?v=KC9xcuLqiS8
                if (SaveFile.exists()){
                    byte[] buffer = new byte[1024];
                    FileInputStream fileInputStream = new FileInputStream(FullSavePath);
                    ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    while (zipEntry!=null){
                        String filepath = "src/main/resources/MdB-Stammdaten"+File.separator + zipEntry.getName();
                        System.out.println("Aktualisiert: "+zipEntry.getName());
                        if (!zipEntry.isDirectory()){
                            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
                            int len;
                            while((len= zipInputStream.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, len);
                            }
                            fileOutputStream.close();
                        } else {
                            File dir = new File(filepath);
                            dir.mkdir();
                        }
                        zipInputStream.closeEntry();
                        zipEntry = zipInputStream.getNextEntry();
                    }
                    zipInputStream.closeEntry();
                    zipInputStream.close();
                    fileInputStream.close();
                    SaveFile.delete();
                }
            }
        }
        System.out.println("Aktualisierung von MdB-Stammdaten und Plenarprotokoll DTD abgeschlossen");
    }
}
