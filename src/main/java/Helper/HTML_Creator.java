package Helper;

import Class_MongoDB_Impl.*;
import database.MongoDBHandler;
import org.bson.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Diese Klasse befasst sich mit dem erstellen der HTML Dateien.
 * Hier werden statische HTML Seiten generiert.
 * Es kann der Index, sowie auch Redeportfolio`s für Abgeordnete erstellt werden.
 */
public abstract class HTML_Creator {


    /**
     * Hier wird die Index HTML Datei im entsprechenden Pfad erstellt
     * @param Index
     */
    public static void IndexHTMLWrite(StringBuilder Index){
        String Pfad = "src/main/HTML/index.html";
        File Datei = new File(Pfad);

        try{
            FileWriter DateiSchreiber = new FileWriter(Datei);
            BufferedWriter BufferedSchreiber = new BufferedWriter(DateiSchreiber);
            BufferedSchreiber.write(Index.toString());
            BufferedSchreiber.close();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Hier wird die Abgeordneten Redeportfolio HTML im entsprechenden Pfad erstellt
     * @param Abgeordneter
     * @param Dateiname
     */
    public static void AbgeordneterHTMLWrite(StringBuilder Abgeordneter, String Dateiname){


        String Pfad = "src/main/HTML/RedePortfolios";
        Pfad = Pfad+Dateiname;
        File Datei = new File(Pfad);

        try{
            FileWriter DateiSchreiber = new FileWriter(Datei);
            BufferedWriter BufferedSchreiber = new BufferedWriter(DateiSchreiber);
            BufferedSchreiber.write(Abgeordneter.toString());
            BufferedSchreiber.close();

        }catch (IOException e){
            e.printStackTrace();
        }


    }


    /**
     * Hier wird die HTML Inhalt und Struktur für den Index mittels eines Stringbuilder aufgebaut
     * Zudem wird nach A-Z oder Z-A sortiert und nach Wahlperiode gefiltert
     * @param MyMongoDBHandler
     * @param Sortiert_AZ_Abgeordnete
     * @param Wahlperiode
     * @return
     */
    public static StringBuilder IndexCreate(MongoDBHandler MyMongoDBHandler, Boolean Sortiert_AZ_Abgeordnete, Integer Wahlperiode){
        StringBuilder Fraktionen = new StringBuilder();
        StringBuilder Index_Willkommen = new StringBuilder();

        Index_Willkommen.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Startseite Portfolio`s</title>\n" +
                "<link rel=\"stylesheet\" href=\"style_index.css\">\n" +
                "<meta http-equiv=\"refresh\" content=\"30\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Willkommen\">\n" +
                "<h1>Willkommen zum Rede Portfolio</h1>\n" +
                "<p>Im Rede Portolio finden Sie die Stammdaten und Reden von allen Abgeordneten</p>\n" +
                "<p>Klicke auf einen Abgeordneten, um seine Stammdaten und Reden einzusehen</p>\n");

        Index_Willkommen.append("<div class=\"SortierungUbersicht\">\n" +
                "<p>Folgende Sortierungen stehen zur verfügung</p>\n" +
                "<ul>\n" +
                "<li>\n" +
                "<a href=\"indexAZ.html\"><button>A-Z Sortiertung</button></a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href=\"indexZA.html\"><button>Z-A Sortiertung</button></a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href=\"index_SpeechesCount.html\"><button>Anzahl Reden</button></a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n");


        Fraktionen.append(Index_Willkommen);
        Long Anzahl_Fraktionen = MyMongoDBHandler.countDocuments("Fraktion");
        //Boolean Sortiert_AZ_Abgeordnete = true;

        for (int i = 0; i < Anzahl_Fraktionen; i++) {
            Document iter_Fraktion = MyMongoDBHandler.query_output("Fraktion","MongoID",i);
            int MongoID = iter_Fraktion.getInteger("MongoID");
            //System.out.println(MongoID);
            Fraktion_MongoDB_Impl current_fraktion = new Fraktion_MongoDB_Impl(MyMongoDBHandler,MongoID);
            System.out.println("Aktuell lädt: "+current_fraktion.getName());
            //Fraktion Uberschrift
            //System.out.println(current_fraktion.toHTML());
            Map<String, List<String>> ID_WPs = current_fraktion.getAbgeordneter_ID_WP_Nr_Mongo_Impl();
            List<StringBuilder> Vor_Nachname_ID_counter_Liste = new ArrayList<>();
            String Wahlperiode_String = String.valueOf(Wahlperiode);
            List<String> isAlreadyCaptured = new ArrayList<>();
            for (String ID : ID_WPs.keySet()) {
                Boolean captured = false;
                List<String> Wahlperioden_des_abg = ID_WPs.get(ID);
                for (String WP : Wahlperioden_des_abg) {
                    if (WP.equals(Wahlperiode_String) || Wahlperiode_String.equals("0")){
                        for (String IsItCaptured : isAlreadyCaptured) {
                            if (IsItCaptured.equals(ID)){
                                captured = true;
                                break;
                            }
                        }
                        if (!captured){
                            int ID_int = Integer.valueOf(ID);
                            Document iter_Abgeordneter = MyMongoDBHandler.query_output("Abgeordneter","ID",ID_int);
                            Integer MongoID_Reden = iter_Abgeordneter.getInteger("MongoID");
                            List<Integer> Reden_IDs = (List<Integer>) iter_Abgeordneter.get("Reden");
                            String rede_size = String.valueOf(Reden_IDs.size());
                            StringBuilder Vor_Nachname_ID_counter = new StringBuilder();
                            Vor_Nachname_ID_counter.append(iter_Abgeordneter.getString("Vorname")+"_"+iter_Abgeordneter.getString("Nachname")+"_"+ID+"#"+rede_size);
                            Vor_Nachname_ID_counter_Liste.add(Vor_Nachname_ID_counter);
                            isAlreadyCaptured.add(ID);
                            //ID_WPs.remove(ID);
                        }

                    }
                }
            }
            if (Vor_Nachname_ID_counter_Liste.isEmpty()){
                continue;
            }
            Fraktionen.append("<div class=\"AllFraktion\">\n");

            //Hier wird sortiert A-Z (Bonusaufgabe)
            if (Sortiert_AZ_Abgeordnete){
                Collections.sort(Vor_Nachname_ID_counter_Liste);
            } else {
                Collections.sort(Vor_Nachname_ID_counter_Liste);
                Collections.reverse(Vor_Nachname_ID_counter_Liste);
            }



            StringBuilder Mitglieger_DIV = new StringBuilder();
            Mitglieger_DIV.append("<div class=\"Mitglieder\">\n");
            Mitglieger_DIV.append("<ol>\n");

            for (StringBuilder s : Vor_Nachname_ID_counter_Liste) {
                Mitglieger_DIV.append("<li>\n");
                //System.out.println(s);
                int index_first = s.indexOf("_");
                int index_last = s.lastIndexOf("_");
                String Vorname = s.substring(0,index_first);
                //System.out.println(Vorname);
                String Nachname = s.substring(index_first+1,index_last);
                //System.out.println(Nachname);
                int hashtag_index = s.lastIndexOf("#");
                int ID = Integer.valueOf(s.substring(index_last+1,hashtag_index));
                //System.out.println(ID);
                int counter = Integer.valueOf(s.substring(hashtag_index+1));
                //System.out.println(counter);

                Document iter_Abgeordneter = MyMongoDBHandler.query_output("Abgeordneter","ID",ID);
                int iterMongoID = iter_Abgeordneter.getInteger("MongoID");

                //Hier in dieser Methode wird der Stringbuilder für das RedenPortfolio des Abgeordneten aufgebaut
                Create_Abgeordneter_RedePortfolio(MyMongoDBHandler,iterMongoID);


                String href = "RedePortfolios/"+Vorname+"_"+Nachname+"_"+ID+".html";

                Mitglieger_DIV.append("<a href=\""+href+"\">\n");


                Mitglieger_DIV.append("<button>"+Vorname+" "+Nachname+" ("+counter+")</button>\n");

                Mitglieger_DIV.append("</a>\n");
                Mitglieger_DIV.append("</li>\n");
            }
            Mitglieger_DIV.append("</ol>\n");
            Mitglieger_DIV.append("</div>\n");


            Fraktionen.append(current_fraktion.toHTML());
            Fraktionen.append(Mitglieger_DIV);
        }

        Fraktionen.append("</div>\n");
        Fraktionen.append("</body>\n" +
                "</html>\n");



        IndexHTMLWrite(Fraktionen);
        return Fraktionen;
    }


    /**
     * Hier wird die HTML Inhalt und Struktur für den Index mittels eines Stringbuilder aufgebaut.
     * Jedoch wird hier nur nach der Anzahl der Reden sortiert und nach Wahlperiode gefiltert.
     * @param MyMongoDBHandler
     * @param Wahlperiode
     * @return
     */
    public static StringBuilder IndexCreate_SortedByRedenAnzahl(MongoDBHandler MyMongoDBHandler, Integer Wahlperiode){
        StringBuilder Fraktionen = new StringBuilder();
        StringBuilder Index_Willkommen = new StringBuilder();

        Index_Willkommen.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Startseite Portfolio`s</title>\n" +
                "<link rel=\"stylesheet\" href=\"style_index.css\">\n" +
                "<meta http-equiv=\"refresh\" content=\"30\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"Willkommen\">\n" +
                "<h1>Willkommen zum Rede Portfolio</h1>\n" +
                "<p>Im Rede Portolio finden Sie die Stammdaten und Reden von allen Abgeordneten</p>\n" +
                "<p>Klicke auf einen Abgeordneten, um seine Stammdaten und Reden einzusehen</p>\n");

        Index_Willkommen.append("<div class=\"SortierungUbersicht\">\n" +
                "<p>Folgende Sortierungen stehen zur verfügung</p>\n" +
                "<ul>\n" +
                "<li>\n" +
                "<a href=\"indexAZ.html\"><button>A-Z Sortiertung</button></a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href=\"indexZA.html\"><button>Z-A Sortiertung</button></a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href=\"index_SpeechesCount.html\"><button>Anzahl Reden</button></a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n");


        Fraktionen.append(Index_Willkommen);
        Long Anzahl_Fraktionen = MyMongoDBHandler.countDocuments("Fraktion");
        //Boolean Sortiert_AZ_Abgeordnete = true;

        for (int i = 0; i < Anzahl_Fraktionen; i++) {
            Document iter_Fraktion = MyMongoDBHandler.query_output("Fraktion","MongoID",i);
            int MongoID = iter_Fraktion.getInteger("MongoID");

            Fraktion_MongoDB_Impl current_fraktion = new Fraktion_MongoDB_Impl(MyMongoDBHandler,MongoID);
            System.out.println("Aktuell lädt: "+current_fraktion.getName());
            List<Document> Abgeordnete = new ArrayList<>();
            Map<String, List<String>> MongoID_abg = current_fraktion.getAbgeordneter_ID_WP_Nr_Mongo_Impl();
            String Wahlperiode_String = String.valueOf(Wahlperiode);
            List<String> CapturedList = new ArrayList<>();
            for (String IDs : MongoID_abg.keySet()) {
                Boolean IsItCaptured = false;
                Document abg = MyMongoDBHandler.query_output("Abgeordneter","ID",Integer.valueOf(IDs));
                List<String> Wahlperioden_des_Abg = MongoID_abg.get(IDs);
                for (String WP : Wahlperioden_des_Abg) {
                    if (WP.equals(Wahlperiode_String) || Wahlperiode_String.equals("0")){
                        for (String Captured : CapturedList) {
                            if (Captured.equals(IDs)){
                                IsItCaptured = true;
                                break;
                            }
                        }
                        if (!IsItCaptured){
                            Abgeordnete.add(abg);
                            CapturedList.add(IDs);
                            //MongoID_abg.remove(IDs);
                        }

                    }


                }
            }
            if (Abgeordnete.isEmpty()){
                continue;
            }
            Fraktionen.append("<div class=\"AllFraktion\">\n");

            StringBuilder Mitglieger_DIV = new StringBuilder();
            Mitglieger_DIV.append("<div class=\"Mitglieder\">\n");
            Mitglieger_DIV.append("<ol>\n");

            List<Document> Abg_sorted_Rede = new ArrayList<>();

            Boolean isFull = true;
            while (isFull) {
                int highest_rede_anzahl = 0;
                int highest_rede_anzahl_MongoID = 0;
                Document highest_iter_abg = null;
                for (int j = 0; j < Abgeordnete.size(); j++) {
                    Document abgeordneter = Abgeordnete.get(j);
                    List<Integer> Reden_IDs = (List<Integer>) abgeordneter.get("Reden");
                    int rede_size = Reden_IDs.size();
                    if (rede_size >= highest_rede_anzahl) {
                        highest_rede_anzahl = rede_size;
                        highest_rede_anzahl_MongoID = abgeordneter.getInteger("MongoID");
                        highest_iter_abg = abgeordneter;
                    }

                }
                Document abgeordneter_docu = MyMongoDBHandler.query_output("Abgeordneter","MongoID",highest_rede_anzahl_MongoID);

                Mitglieger_DIV.append("<li>\n");
                String href = "RedePortfolios/" + abgeordneter_docu.getString("Vorname") + "_" + abgeordneter_docu.getString("Nachname") + "_" + abgeordneter_docu.getInteger("ID") + ".html";
                Mitglieger_DIV.append("<a href=\"" + href + "\">\n");

                Mitglieger_DIV.append("<button>" + abgeordneter_docu.getString("Vorname") + " " + abgeordneter_docu.getString("Nachname") + " (" + highest_rede_anzahl + ")</button>\n");
                //System.out.println(Mitglieger_DIV);
                Mitglieger_DIV.append("</a>\n");
                Mitglieger_DIV.append("</li>\n");

                //Hier in dieser Methode wird der Stringbuilder für das RedenPortfolio des Abgeordneten aufgebaut
                Create_Abgeordneter_RedePortfolio(MyMongoDBHandler,highest_rede_anzahl_MongoID);

                Abgeordnete.remove(highest_iter_abg);

                if (Abgeordnete.isEmpty()) {
                    isFull = false;
                }

            }
            Mitglieger_DIV.append("</ol>\n");
            Mitglieger_DIV.append("</div>\n");

            Fraktionen.append(current_fraktion.toHTML());
            Fraktionen.append(Mitglieger_DIV);
        }

        Fraktionen.append("</div>\n");
        Fraktionen.append("</body>\n");
        Fraktionen.append("</html>\n");


        IndexHTMLWrite(Fraktionen);
        return Fraktionen;
    }


    /**
     * Hier wird mithilfe eines Stringbuilders der Inhalt und die Struktur des HTML Redeportfolio für einen Abgeordneten
     * aufgebaut. Dabei wird kaskadieren die entsprechenden Objekt ToHTML Methoden aufgerufen.
     * @param MyMongoDBHandler
     * @param MongoID
     * @return
     */
    public static StringBuilder Create_Abgeordneter_RedePortfolio(MongoDBHandler MyMongoDBHandler,int MongoID){
        StringBuilder RedePortfolio = new StringBuilder();
        RedePortfolio.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<title>Rede Portfolio</title>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<link rel=\"stylesheet\" href=\"../style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<a class=\"StartseiteButton\" href=\"../indexAZ.html\">\n" +
                "<button>Startseite</button>\n" +
                "</a>\n" +
                "<br>\n");


        RedePortfolio.append("<div class=\"OverSpeeches\">\n");
        Abgeordneter_MongoDB_Impl Abgeordneter_impl = new Abgeordneter_MongoDB_Impl(MyMongoDBHandler, MongoID);
        System.out.println("Es wird eine HTML für folgenden Abgeordneten erstellt: "+Abgeordneter_impl.getVorname()+" "+Abgeordneter_impl.getName()+" "+Abgeordneter_impl.getID()+" "+MongoID);
        //Biografie
        RedePortfolio.append(Abgeordneter_impl.toHTML());



        RedePortfolio.append("<div class=\"AllWahlperioden\">\n");
        //Wahlperioden
        Boolean full_WP = true;
        Integer MongoID_withHighestWPNummer = 0;
        List<Integer> Wahlperioden = Abgeordneter_impl.getWahlperioden_Mongo_Impl();

        while(full_WP){
            RedePortfolio.append("<div class=\"EineWahlperiode\">\n");
            int highest = 0;
            for (int i = 0; i < Wahlperioden.size(); i++) {
                Wahlperioden_MongoDB_Impl iter_WP = new Wahlperioden_MongoDB_Impl(MyMongoDBHandler, Wahlperioden.get(i));
                int WP_Nummer = iter_WP.getNumber();
                if (WP_Nummer>highest){
                    highest = WP_Nummer;
                    MongoID_withHighestWPNummer = iter_WP.getMongoID();
                }
            }

            Wahlperioden_MongoDB_Impl Wahlperiode = new Wahlperioden_MongoDB_Impl(MyMongoDBHandler,MongoID_withHighestWPNummer);
            //Wahlperiode
            RedePortfolio.append(Wahlperiode.toHTML());
            //System.out.println(Wahlperiode.toHTML());


            //Wahlkreis
            int Wahlkreis_MongoID = Wahlperiode.getWahlkreis_Mongo_Impl();
            Wahlkreis_MongoDB_Impl iter_Wahlkreis = new Wahlkreis_MongoDB_Impl(MyMongoDBHandler,Wahlkreis_MongoID);
            //System.out.println(iter_Wahlkreis.toHTML());
            RedePortfolio.append(iter_Wahlkreis.toHTML());


            List<Integer> Institutionen_in_WP = Wahlperiode.getInstitutionen_Mongo_Impl();

            //Fraktion
            RedePortfolio.append("<div class=\"Fraktion\">\n");
            RedePortfolio.append("<h2 >Fraktion/Gruppe</h2>\n");
            for (int i = 0; i < Institutionen_in_WP.size(); i++) {
                Institution_MongoDB_Impl iter_Institution = new Institution_MongoDB_Impl(MyMongoDBHandler,Institutionen_in_WP.get(i));
                if (iter_Institution.getInsart_Lang().equals("Fraktion/Gruppe")){

                    RedePortfolio.append(iter_Institution.Fraktion_GruppeToHTML());
                    Institutionen_in_WP.remove(i);
                }
            }
            RedePortfolio.append("</div>\n");



            //Ausschüsse
            RedePortfolio.append("<div class=\"Ausschuss\">\n");
            Boolean ausschuss_exists = false;
            for (int i = 0; i < Institutionen_in_WP.size(); i++) {
                Institution_MongoDB_Impl iter_Institution = new Institution_MongoDB_Impl(MyMongoDBHandler,Institutionen_in_WP.get(i));
                if (iter_Institution.getInsart_Lang().contains("Ausschuss")){
                    if (!ausschuss_exists){
                        RedePortfolio.append("<h2 >Ausschüsse</h2>\n");
                        ausschuss_exists = true;
                    }
                    RedePortfolio.append(iter_Institution.AusschussToHTML());
                    Institutionen_in_WP.remove(i);
                }
            }
            RedePortfolio.append("</div>\n");



            //Sonstiges

            Boolean sonstiges_exists = false;
            for (int i = 0; i < Institutionen_in_WP.size(); i++) {
                Institution_MongoDB_Impl iter_Institution = new Institution_MongoDB_Impl(MyMongoDBHandler,Institutionen_in_WP.get(i));
                if (!(iter_Institution.getInsart_Lang().equals("Fraktion/Gruppe"))&&!(iter_Institution.getInsart_Lang().contains("Ausschuss"))){
                    if (!sonstiges_exists) {
                        RedePortfolio.append("<h2 >Sonstiges</h2>\n");
                        sonstiges_exists=true;
                    }
                    RedePortfolio.append("<div class=\"Sonstiges\">\n");
                    RedePortfolio.append(iter_Institution.SonstigesToHTML());
                    RedePortfolio.append("</div>\n");
                    Institutionen_in_WP.remove(i);
                }
            }

            Wahlperioden.remove(MongoID_withHighestWPNummer);
            RedePortfolio.append("</div>\n");
            if (Wahlperioden.isEmpty()){
                full_WP = false;
            }
        }
        RedePortfolio.append("</div>\n");
        RedePortfolio.append("</div>\n");

        if (!Abgeordneter_impl.getReden_Mongo_Impl().isEmpty()) {

            //Inhaltsverzeichnis
            RedePortfolio.append("<div class=\"Inhaltsverzeichnis\">\n");
            RedePortfolio.append("<h2>Inhaltsverzeichnis</h2>\n");
            RedePortfolio.append("<dl>\n");

            Boolean full_reden = true;
            List<Integer> Reden = Abgeordneter_impl.getReden_Mongo_Impl();

            Map<Integer, Integer> Sitzungsnr_MongoID = new HashMap<>();
            List<Integer> Reden_MongoID_sorted_sitzungsnummer_map = new ArrayList<>();

            for (Integer redeMongoID : Reden) {
                Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(MyMongoDBHandler, redeMongoID);
                Sitzungsnr_MongoID.put(rede.getSitzungsnummer(), redeMongoID);
            }


            while (full_reden) {

                int highest_map_key = 0;
                int MongoID_highest_map_key = 0;
                for (Integer sitzungsnr : Sitzungsnr_MongoID.keySet()) {
                    if (sitzungsnr > highest_map_key) {
                        highest_map_key = sitzungsnr;
                        MongoID_highest_map_key = Sitzungsnr_MongoID.get(sitzungsnr);
                    }
                }
                Reden_MongoID_sorted_sitzungsnummer_map.add(MongoID_highest_map_key);
                Sitzungsnr_MongoID.remove(highest_map_key);

                if (Sitzungsnr_MongoID.isEmpty()) {
                    full_reden = false;
                }
            }

            Collections.reverse(Reden_MongoID_sorted_sitzungsnummer_map);


            if (Reden_MongoID_sorted_sitzungsnummer_map.contains(0) && !(Abgeordneter_impl.getID() == 11004325)) {
                Reden_MongoID_sorted_sitzungsnummer_map.remove(0);
            }

            for (Integer mongoid_rede_sorted : Reden_MongoID_sorted_sitzungsnummer_map) {
                Rede_MongoDB_Impl iter_Rede = new Rede_MongoDB_Impl(MyMongoDBHandler, mongoid_rede_sorted);
                RedePortfolio.append("<dt><a href=\"#" + iter_Rede.getRede_ID() + "\" Rede> Datum/Sitzungsnummer: " + iter_Rede.getDate_Mongo_Impl() + " - " + iter_Rede.getSitzungsnummer() + "</a></dt>\n");
            }

            RedePortfolio.append("</dl>\n");
            RedePortfolio.append("</div>\n");
            RedePortfolio.append("</div>\n");


            //Reden
            for (Integer redeMongoID : Reden_MongoID_sorted_sitzungsnummer_map) {
                Rede_MongoDB_Impl iter_Rede = new Rede_MongoDB_Impl(MyMongoDBHandler, redeMongoID);
                RedePortfolio.append(iter_Rede.toHTML());
            }
        }


        String Vorname_Nachname_ID = "/"+Abgeordneter_impl.getVorname()+"_"+Abgeordneter_impl.getName()+"_"+Abgeordneter_impl.getID()+".html";
        //System.out.println(Vorname_Nachname_ID);
        RedePortfolio.append("\n</body>\n");
        RedePortfolio.append("</html>\n");
        AbgeordneterHTMLWrite(RedePortfolio, Vorname_Nachname_ID);
        return RedePortfolio;
    }
}
