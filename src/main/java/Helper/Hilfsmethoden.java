package Helper;

import Class_MongoDB_Impl.Abgeordneter_MongoDB_Impl;
import Class_MongoDB_Impl.Historie_MongoDB_Impl;
import Class_MongoDB_Impl.RedeAuszug_MongoDB_Impl;
import Class_MongoDB_Impl.Rede_MongoDB_Impl;
import Class_Impl.*;
import Factories.Factory;
import database.MongoDBHandler;
import org.apache.commons.io.FileUtils;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.XmlCasDeserializer;
import org.apache.uima.util.XmlCasSerializer;
import org.bson.*;
import org.xml.sax.SAXException;

import java.io.IOException;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Diese Klasse beinhaltet unterschiedliche Methoden, um repetitiven Code  zu vermeiden und um klarere Strukturen woanders im Code zu behalten.
 * Es sind verschiedene nützliche Hilfsmethoden hier enthalten
 */

public abstract class Hilfsmethoden {


    /**
     * Folgende MongoID Attribute ermöglichen eine eindeutige ID vergabe
     */
    private static int MongoID_counter_Partei = 0;
    private static int MongoID_counter_Fraktion = 0;
    private static int MongoID_counter_Wahlperiode = 0;
    private static int MongoID_counter_Ausschuss = 0;


    /**
     * Einige Attribute haben den Datentyp Date. Damit man diese schnell und einfach in ein String umwandeln kann, wurde diese Methode geschrieben.
     * @param Datum
     * @return
     */
    public static String Date_zu_String(Date Datum) {
       SimpleDateFormat Datum_format = new SimpleDateFormat("dd.MM.yyyy");
       String Datum_inString = Datum_format.format(Datum);
       return Datum_inString;
    }

    public static LocalDate String_to_LocalDate(String DateString){
        DateTimeFormatter Datum_format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(DateString, Datum_format);
    }

    /**
     * Hier wird ein Date Objekt in die Uhrzeit als String umgewandelt.
     * @param Uhrzeit
     * @return
     */
    public static String Uhrzeit_zu_String(Date Uhrzeit){
        SimpleDateFormat Datum_format = new SimpleDateFormat("HH:mm:ss");
        String Uhrzeit_inString = Datum_format.format(Uhrzeit);
        return Uhrzeit_inString;
    }

    /**
     * Ermöglicht einen Abgeordneten anhand seiner ID zu finden.
     * @param Alle
     * @param ID
     * @return
     */
    public static Abgeordneter_Impl getAbgeordneter(Set<Abgeordneter_Impl> Alle, int ID) {
        Boolean found = false;
        Abgeordneter_Impl Gesuchter = null;
        for (Abgeordneter_Impl iter_Abgeordneter : Alle) {
            int iter_ID = iter_Abgeordneter.getID();
            if (iter_ID == ID) {
                Gesuchter = iter_Abgeordneter;
                found = true;
            }
            if (found) {
                break;
            }
        }
        if (Gesuchter != null) {
            return Gesuchter;
        } else {
            return null;
        }
    }

    /**
     * Sucht nach Abgeordneten mit den gleichen Vornamen UND Nachnamen, danach erst nur mit gleichen Nachnamen, zum Schluss nur gleichen Vornamen
     * @param Alle
     * @param Vorname
     * @param Nachname
     * @return
     */
    public static Abgeordneter_Impl getAbgeordneter(Set<Abgeordneter_Impl> Alle, String Vorname, String Nachname) {
        Boolean found = false;
        Abgeordneter_Impl Gesuchter = null;
        for (Abgeordneter_Impl iter_Abgeordneter : Alle) {
            String iter_Vorname = iter_Abgeordneter.getVorname();
            String iter_Nachname = iter_Abgeordneter.getName();
            if (iter_Vorname.equals(Vorname) && iter_Nachname.equals(Nachname)) {
                Gesuchter = iter_Abgeordneter;
                found = true;
            }
            if (found) {
                break;
            }

        }
        if (!found) {
            Gesuchter = getAbgeordneter(Alle, Nachname);
        }
        if (Gesuchter == null) {
            Gesuchter = getAbgeordneter(Alle, Vorname);
        }
        if (Gesuchter != null) {
            return Gesuchter;
        } else {
            return null;
        }
    }


    /**
     * Sucht nach Abgeordneten mit folgenden Vor- oder Nachnamen.
     * @param Alle
     * @param Vor_oder_Nachname
     * @return
     */
    public static Abgeordneter_Impl getAbgeordneter(Set<Abgeordneter_Impl> Alle, String Vor_oder_Nachname) {
        Boolean found_mit_nachname = false;
        Abgeordneter_Impl Gesuchter = null;
        for (Abgeordneter_Impl iter_Abgeordneter : Alle) {
            String iter_Vorname = iter_Abgeordneter.getName();
            if (iter_Vorname.equals(Vor_oder_Nachname)) {
                Gesuchter = iter_Abgeordneter;
                found_mit_nachname = true;
            }
            if (found_mit_nachname) {
                break;
            }

        }
        if (Gesuchter != null) {
            return Gesuchter;
        } else {
            return null;
        }
    }


    /**
     * Erstellt Partei Objekte, sammelt alle PARTEI_KURZ und entfernt alle doppelten und fügt Abgeordneten als Mitglied hinzu
     * @param AlleAbgeordneten
     * @return
     */
    public static Set<Partei_Impl> MDB_Stammdaten_Einlese_Operation1(Set<Abgeordneter_Impl> AlleAbgeordneten) {
        Set<String> Parteien_keine_doppelten = new HashSet<>();
        for (Abgeordneter_Impl Abgeordneter : AlleAbgeordneten) {
            String iter_Partei_Name = Abgeordneter.getPartei_kurz();
            if (iter_Partei_Name.equals("")) {
                continue;
            }
            Parteien_keine_doppelten.add(iter_Partei_Name);
        }
        Set<Partei_Impl> AlleParteien = new HashSet<>();
        for (String Partei_String_zu_Objekt : Parteien_keine_doppelten) {
            Partei_Impl Partei = new Partei_Impl();
            //Partei.setMongoID(MongoID_counter_Partei);
            Partei.setMongoLabel("PARTEI");
            MongoID_counter_Partei++;
            Partei.setName(Partei_String_zu_Objekt);
            AlleParteien.add(Partei);
        }

        for (Abgeordneter_Impl Abgeordneter_jitter : AlleAbgeordneten) {
            for (Partei_Impl Partei : AlleParteien) {
                if (Abgeordneter_jitter.getPartei_kurz().equals(Partei.getName())) {
                    Partei.AddMitglied(Abgeordneter_jitter);
                    Abgeordneter_jitter.setPartei(Partei);
                }
            }
        }
        return AlleParteien;
    }

    /**
     * Hier werden Alle möglichen Frakionsnamen gesammelt und doppelte entfernt
     * @param AlleAbgeordneten
     * @return
     */
    public static Set<Fraktion_Impl> MDB_Stammdaten_Einlese_Operation2(Set<Abgeordneter_Impl> AlleAbgeordneten) {
        Set<String> FraktionsNamen = new HashSet<>();
        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordneten) {
            Set<Wahlperioden_Impl> WP_von_Abgeordneten = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : WP_von_Abgeordneten) {
                Set<Institution_Impl> Institutionen_in_WP = wahlperiodenImpl.getInstitutionen();
                for (Institution_Impl institutionImpl : Institutionen_in_WP) {
                    String Insart_Lang = institutionImpl.getInsart_Lang();
                    if (Insart_Lang.equals("Fraktion/Gruppe")) {
                        //System.out.println(institutionImpl.getInsart_Lang());
                        //System.out.println(institutionImpl.getIns_Lang());
                        String Fraktion_Name_iter = institutionImpl.getIns_Lang();
                        FraktionsNamen.add(Fraktion_Name_iter);
                    }
                }
            }
        }


        /**
         * Hier erstellen wir Objekte von der FraktionsClass und geben diesen erstmal nur den Namen
         */
        Set<Fraktion_Impl> AlleFraktionen = new HashSet<>();
        for (String fraktionsnaman : FraktionsNamen) {
            Fraktion_Impl Fraktion = new Fraktion_Impl();
            //Fraktion.setMongoID(MongoID_counter_Fraktion);
            Fraktion.setMongoLabel("FRAKTION");
            MongoID_counter_Fraktion++;
            Fraktion.setName(fraktionsnaman);
            AlleFraktionen.add(Fraktion);
        }


        /**
         * Hier füllen wir die Fraktionsobjekten mit Map<Abgeordneten_ID, Set<WahlperiodeNummern>>
         */
        Boolean found = false;
        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordneten) {
            Set<Wahlperioden_Impl> Wahlperiode_von_Abgeordneten = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : Wahlperiode_von_Abgeordneten) {
                Set<Institution_Impl> Institutionen_in_WP = wahlperiodenImpl.getInstitutionen();
                for (Institution_Impl institutionImpl : Institutionen_in_WP) {
                    String Insart_Lang = institutionImpl.getInsart_Lang();
                    if (Insart_Lang.equals("Fraktion/Gruppe")) {
                        for (Fraktion_Impl fraktionImpl : AlleFraktionen) {
                            String Fraktionsname = fraktionImpl.getName();
                            String Ins_Lang = institutionImpl.getIns_Lang();
                            if (Ins_Lang.equals(Fraktionsname)) {
                                Map<Integer, Set<Integer>> Abg_ID_WPNummer = fraktionImpl.getAbgeordneter_ID_WP_Nr();
                                found = false;
                                for (Integer ID : Abg_ID_WPNummer.keySet()) {
                                    //if (ID == abgeordneterImpl.getID()){
                                    if (ID.equals(abgeordneterImpl.getID())) {
                                        fraktionImpl.AddWahlperiode(abgeordneterImpl.getID(), wahlperiodenImpl.getNumber());
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    fraktionImpl.AddNeuenAbgeordneten(abgeordneterImpl.getID(), wahlperiodenImpl.getNumber());
                                }
                            }
                        }
                    }
                }
            }
        }

        return AlleFraktionen;

    }


    /**
     * Hier werden Objekte von Wahlperiode erstellt. Die Obejekte der Klasse sind einzigartig im Kontext bzw. existieren nur 20 Stück davon, da es aktuell nur
     * 20 Wahlperioden gibt.
     * @param AlleAbgeordneten
     * @return
     */
    public static Set<Wahlperiode_Impl> MDB_Stammdaten_Einlese_Operation3(Set<Abgeordneter_Impl> AlleAbgeordneten) {
        Set<Integer> WP_Nummern_Alle = new HashSet<>();
        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordneten) {
            Set<Wahlperioden_Impl> Alle_WP_von_Abgeordneten = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : Alle_WP_von_Abgeordneten) {
                Integer WP_Nummer = wahlperiodenImpl.getNumber();
                WP_Nummern_Alle.add(WP_Nummer);
            }
        }

        Set<Wahlperiode_Impl> AlleWahlperiode = new HashSet<>();
        for (Integer WP_Nummern_iter : WP_Nummern_Alle) {
            Wahlperiode_Impl Wahlperiode = new Wahlperiode_Impl();
            //Wahlperiode.setMongoID(MongoID_counter_Wahlperiode);
            Wahlperiode.setMongoLabel("WAHLPERIODE");
            MongoID_counter_Wahlperiode++;
            Wahlperiode.setWP_Nummer(WP_Nummern_iter);
            AlleWahlperiode.add(Wahlperiode);
        }

        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordneten) {
            Set<Wahlperioden_Impl> WP_von_Abgeordneten = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : WP_von_Abgeordneten) {
                int Iter_WP_Nummer = wahlperiodenImpl.getNumber();
                for (Wahlperiode_Impl wahlperiodeImpl : AlleWahlperiode) {
                    if (Iter_WP_Nummer == wahlperiodeImpl.getWP_Nummer()) {
                        wahlperiodeImpl.AddAbgeordneten_ID(abgeordneterImpl.getID());
                    }
                }
            }
        }
        return AlleWahlperiode;
    }


    /**
     * Hier werden Objekte von Ausschuss erstellt und mit Attributen eingelesen
     * @param AlleAbgeordneten
     * @return
     */
    public static Set<Ausschuss_Impl> MDB_Stammdaten_Einlese_Operation4(Set<Abgeordneter_Impl> AlleAbgeordneten) {

        //Hier werden alle möglichen Ausschüsse entnommen und doppelte gelöscht
        Set<String> Beschreibungen_Ausschüssen = new HashSet<>();
        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordneten) {
            Set<Wahlperioden_Impl> WP_von_Abgeordneten = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : WP_von_Abgeordneten) {
                Set<Institution_Impl> Institutionen_in_WP = wahlperiodenImpl.getInstitutionen();
                for (Institution_Impl institutionImpl : Institutionen_in_WP) {
                    String Insart_Lang = institutionImpl.getInsart_Lang();
                    if (Insart_Lang.toLowerCase().contains("ausschuss")) {
                        //System.out.println(institutionImpl.getInsart_Lang());
                        //System.out.println(institutionImpl.getIns_Lang());
                        String Beschreibung = institutionImpl.getIns_Lang();
                        Beschreibungen_Ausschüssen.add(Beschreibung);
                    }
                }
            }
        }


        /**
         * Hier werden Objekte erstellt und mit Beschreibung befüllt
         */
        Set<Ausschuss_Impl> AlleAusschüsse = new HashSet<>();
        for (String Beschreibung_iter : Beschreibungen_Ausschüssen) {
            String Beschreibung = Beschreibung_iter;
            Ausschuss_Impl Ausschuss = new Ausschuss_Impl();
            //Ausschuss.setMongoID(MongoID_counter_Ausschuss);
            Ausschuss.setMongoLabel("AUSSCHUSS");
            MongoID_counter_Ausschuss++;
            Ausschuss.setBeschreibung(Beschreibung);
            AlleAusschüsse.add(Ausschuss);
        }

        /**
         * Hier werden die Ausschuss Objekte mit Art gefüllt
         */
        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordneten) {
            Set<Wahlperioden_Impl> WP_von_Abgeordneten = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : WP_von_Abgeordneten) {
                Set<Institution_Impl> Institutionen_in_WP = wahlperiodenImpl.getInstitutionen();
                for (Institution_Impl institutionImpl : Institutionen_in_WP) {
                    String Insart_Lang = institutionImpl.getInsart_Lang();
                    if (Insart_Lang.toLowerCase().contains("ausschuss")) {
                        for (Ausschuss_Impl ausschussImpl : AlleAusschüsse) {
                            if (ausschussImpl.getBeschreibung().equals(institutionImpl.getIns_Lang())) {
                                ausschussImpl.setAusschuss_Art(Insart_Lang);
                            }
                        }
                    }
                }
            }
        }


        /**
         * Hier werden die Ausschuss Objekte mit Map<Integer, Set<Integer>> gefüllt
         */
        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordneten) {
            Set<Wahlperioden_Impl> WP_von_Abgeordneten = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : WP_von_Abgeordneten) {
                Set<Institution_Impl> Institutionen_in_WP = wahlperiodenImpl.getInstitutionen();
                for (Institution_Impl institutionImpl : Institutionen_in_WP) {
                    String Insart_Lang = institutionImpl.getInsart_Lang();
                    if (Insart_Lang.toLowerCase().contains("ausschuss")) {
                        for (Ausschuss_Impl ausschussImpl : AlleAusschüsse) {
                            if (ausschussImpl.getAusschuss_Art().equals(Insart_Lang) && ausschussImpl.getBeschreibung().equals(institutionImpl.getIns_Lang())) {
                                int Wahlperiode_Nummer = wahlperiodenImpl.getNumber();
                                int Abgeordneten_ID = abgeordneterImpl.getID();
                                ausschussImpl.AddWP_mit_Abgeordneten(Wahlperiode_Nummer, Abgeordneten_ID);
                            }
                        }
                    }
                }
            }
        }
        return AlleAusschüsse;
    }

    /**
     * Hier wird jeder Rede ein Tagesordnungspunkt zugeordnet
     * @param AlleTagesordnungspunkte
     * @param AlleReden
     */
    public static void Pleanarprotokoll_Einlese_Operation3(Set<Tagesordnungspunkt_Impl> AlleTagesordnungspunkte, Set<Rede_Impl> AlleReden) {

        for (Rede_Impl rede : AlleReden) {
            for (Tagesordnungspunkt_Impl tagesordnungspunkt : AlleTagesordnungspunkte) {
                Set<String> Rede_IDs = tagesordnungspunkt.getRede_IDs();
                for (String redeId : Rede_IDs) {
                    if (redeId.equals(rede.getRede_ID())) {
                        rede.setTagesordnungspunkt(tagesordnungspunkt.getTagesordnungsname());
                        rede.setTOP_Beschreibung(tagesordnungspunkt.getTOP_Beschreibung());
                    }
                }
            }
        }

    }


    /**
     * Hier werden die Anzahl der Fehltage zu den Abgeordneten Objekten hinzugefügt
     * @param AlleAbgeordneten
     * @param Nachname1_Vorname1_Fraktion2
     */
    public static void Pleanarprotokoll_Einlese_Operation2(Set<Abgeordneter_Impl> AlleAbgeordneten, Map<String, String> Nachname1_Vorname1_Fraktion2) {

        for (String Nachname_Vorname : Nachname1_Vorname1_Fraktion2.keySet()) {
            String change = Nachname_Vorname;
            //System.out.println(change);
            if (change.contains(",")) {
                int index_of_komma = change.indexOf(",");
                String Nachname = change.substring(0, index_of_komma);
                //System.out.println(Nachname);
                String Vorname = change.substring(index_of_komma + 2, change.length());
                //System.out.println(Vorname);
                Abgeordneter_Impl Abgeordneter_fehlender = Hilfsmethoden.getAbgeordneter(AlleAbgeordneten, Vorname, Nachname);
                if (Abgeordneter_fehlender != null) {
                    Abgeordneter_fehlender.AddFehltag_Sitzung();
                    //System.out.println(Abgeordneter_fehlender.getName());
                    String Fraktion = Nachname1_Vorname1_Fraktion2.get(Nachname_Vorname);
                    //System.out.println(Fraktion);

                    //Abgeordneter_fehlender.setFraktion(Fraktion);
                }
            }


        }

    }




    /**
     * Dies muss direkt danach Ausgeführt werden, wenn die Pleanarprotkolle Eingelesen wurden
     * Diese Methode ordnet jeder Rede einen Abgeordneten zu
     * Diese Methode ordnet jedem Abgeordnetet seine Reden zu
     * @param AlleReden
     * @param AllAbgeordneten
     */
    public static void Pleanarprotokoll_Einlese_Operation1(Set<Rede_Impl> AlleReden, Set<Abgeordneter_Impl> AllAbgeordneten) {
        for (Rede_Impl Rede : AlleReden) {
            int Redner_ID = Rede.getRedner_ID();
            Abgeordneter_Impl Abgeordneter_Gesuchter = Hilfsmethoden.getAbgeordneter(AllAbgeordneten, Redner_ID);
            if (Abgeordneter_Gesuchter == null) {
                continue;
            } else {
                Rede.setRedner(Abgeordneter_Gesuchter);
                Abgeordneter_Gesuchter.AddRede(Rede);
            }
        }
    }

    /*
    //Fügt den Abgeordneten Objekten all deren Abstimmungen hinzu
    public static void Entscheidung_Einlese_Operation1(Set<Abgeordneter_Impl> AlleAbgeordneten, Set<AbstimmungClass> AlleAbstimmungen){

        for (AbstimmungClass iter_Abstimmung : AlleAbstimmungen) {
            String Vorname = iter_Abstimmung.getVorname();
            String Nachname = iter_Abstimmung.getNachname();
            Abgeordneter_Impl Abgeordneter = Hilfsmethoden.getAbgeordneter(AlleAbgeordneten, Vorname,Nachname);
            if (Abgeordneter!=null){
                Abgeordneter.AddAbstimmungen(iter_Abstimmung);
            }
        }
    }
     */



    /**
     * Gibt die Zahl aus die am häufigsten im Set vorkommt, falls gleichhäufig Vorkommende, dann wird das letzte zurückgegeben
     * @param IntegerSet
     * @return
     */
    public static Integer HäufigsterImSet1(List<Integer> IntegerSet) {
        Integer Gesuchte_ID = 0;
        Map<Integer, Integer> ID_Häufigkeit = new HashMap<>();

        for (Integer i : IntegerSet) {
            if (ID_Häufigkeit.containsKey(i)) {
                Integer Neue_Häufigkeit = ID_Häufigkeit.get(i) + 1;
                ID_Häufigkeit.put(i, Neue_Häufigkeit);
            } else {
                ID_Häufigkeit.put(i, 1);
            }
        }

        for (Integer i : ID_Häufigkeit.keySet()) {
            //System.out.println(i);
            //System.out.println(ID_Häufigkeit.get(i));
        }

        int current_max = 0;
        for (Integer value : ID_Häufigkeit.values()) {
            if (value > current_max) {
                current_max = value;
                //System.out.println(current_max);
            }
        }
        for (Integer i : ID_Häufigkeit.keySet()) {
            //System.out.println(ID_Häufigkeit.get(i));
            if (current_max == ID_Häufigkeit.get(i)) {
                Gesuchte_ID = i;
                break;
            }
        }
        return Gesuchte_ID;
    }

    /**
     * Wandelt den Datentyp innerhalb der Map um
     * @param AlteMap
     * @return
     */
    public static Map<String, List<String>> MapFormatter1(Map<Integer, List<Integer>> AlteMap) {
        Map<String, List<String>> NeueMap = new HashMap<>();

        for (Integer altesKey : AlteMap.keySet()) {
            Integer alterKey = altesKey;
            String neuesKey = alterKey.toString();
            List<Integer> alteList = AlteMap.get(altesKey);
            List<String> neueList = new ArrayList<>();
            for (Integer altesListelement : alteList) {
                Integer altesElement = altesListelement;
                String NeuesElement = altesElement.toString();
                neueList.add(NeuesElement);
            }
            NeueMap.put(neuesKey, neueList);
        }

        return NeueMap;
    }


    /**
     * Wandelt den Datentyp innerhalb der Map um
     * @param AlteMap
     * @return
     */
    public static Map<String, List<String>> MapFormatter2(Map<Integer, Set<Integer>> AlteMap) {
        Map<String, List<String>> NeueMap = new HashMap<>();

        for (Integer altesKey : AlteMap.keySet()) {
            Integer alterKey = altesKey;
            String neuesKey = alterKey.toString();
            Set<Integer> alteList = AlteMap.get(altesKey);
            List<String> neueList = new ArrayList<>();
            for (Integer altesListelement : alteList) {
                Integer altesElement = altesListelement;
                String NeuesElement = altesElement.toString();
                neueList.add(NeuesElement);
            }
            NeueMap.put(neuesKey, neueList);
        }

        return NeueMap;
    }


    /**
     * Wandelt ein String zu einem Date um.
     * @param Datum
     * @return
     */
    public static Date StringToDate(String Datum) {
        SimpleDateFormat DD_MM_JJJJ_Datum = new SimpleDateFormat("dd.MM.yyyy");
        Date Type_Date = null;
        try {
            Type_Date = DD_MM_JJJJ_Datum.parse(Datum);
        } catch (ParseException p) {

        }
        return Type_Date;
    }


    /**
     * Mit dieser Methode wird Abgefragt aus welcher Wahlperiode die Abgeordenten sein sollen, für die HTML Dateien
     * @return
     */
    public static Integer IndexWPAbfrage() {
        Integer meinInput = null;
        Boolean Scanner_switch2 = true;
        while (Scanner_switch2) {
            try {
                System.out.println("Aus welcher Wahlperiode sollen die Abgeordneten erfasst werden? [Zahl] bspw.; [20]");
                System.out.println("Falls alle Abgeordneten erfasst werden sollen dann gebe [0] ein?");

                Scanner scanner = new Scanner(System.in);

                while (Scanner_switch2) {
                    int input = scanner.nextInt();
                    if ((input >= 0) && (input <= 20)) {
                        Scanner_switch2 = false;
                        return input;
                    } else {
                        System.out.println("Bitte gebe eine Zahl zwischen 0 und 20 ein");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (NoSuchElementException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (IllegalStateException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            }
        }
    return 0;
    }


    /**
     * Absturzsichere String Entgegennahme in der Konsole
     * @return
     */
    public static String String_Eingabe(){

        Boolean Scanner_switch1 = true;
        while (Scanner_switch1) {
            try {
                System.out.println("Bitte gebe den Pfad an, welcher benutzt werden soll.\n");
                Scanner scanner2 = new Scanner(System.in);

                String input = scanner2.nextLine();
                File verzeichnis_datei = new File(input);
                if ((input instanceof String) && verzeichnis_datei.exists()) {
                    Scanner_switch1 = false;
                    return input;
                } else {
                    System.out.println("Der eingegebene Pfad führt zur keiner Datei oder Verzeichnis");
                }

            } catch (InputMismatchException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (NoSuchElementException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (IllegalStateException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            }
        }
        return null;
    }

    /**
     * Hier wird Abgefragt welcher Pfad für die Plenarprotokolle verwendet werden soll.
     * @return
     */
    public static String PfadAbfragePlenarprotokollEinlese() {
        Boolean Scanner_switch1 = true;
        while (Scanner_switch1) {
            try {
                System.out.println("\nSoll für das Einlesen der Plenarprotokolle der Standart Pfad verwendet werden? Ja [1] Nein [2]");
                System.out.println("Der Standart Pfad lautet: src/main/resources/20/\n");
                String path = "src/main/resources/20/";
                File datei = new File(path);

                Scanner scanner = new Scanner(System.in);

                int input = scanner.nextInt();
                if (input == 1) {
                    Scanner_switch1 = false;
                    if ((datei.listFiles().length)==0){
                        System.out.println("Die Dateien im Standart Pfad existieren nicht");
                        System.out.println("Das Programm wird beendet, bitte lege die Plenarprotokolle in den Standartpfad falls du diese benutzen möchtest");
                        System.exit(0);
                    } else if (!datei.isDirectory()) {
                        System.out.println("Der Standart Pfad existiert nicht");
                        System.out.println("Das Programm wird beendet, bitte erstelle den Standartpfad falls du diesen benutzen möchtest");
                        System.exit(0);
                    } else{
                        return path;
                    }
                } else if (input == 2) {
                    Scanner_switch1 = false;
                    String Pfad_Eingabe = String_Eingabe();
                    return Pfad_Eingabe;
                } else {
                    System.out.println("Bitte gebe eine 1 oder eine 2 ein.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (NoSuchElementException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (IllegalStateException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (NullPointerException e){
                System.out.println("Nullpointer Exception");
                System.out.println("Du sollst nicht den Pfad zur Datei direkt geben, sondern nur das Verzeichnis");
            }
        }
        return null;
    }


    /**
     * Hier wird abgefragt welcher Pfad für die MDB Stammdaten verwendet werden soll
     * @return
     */
    public static String PfadAbfrageMDBStammdaten() {


        Boolean Scanner_switch1 = true;
        while (Scanner_switch1) {
            try {
                System.out.println("\nSoll für das Einlesen der MDB Stammdaten der Standart Pfad verwendet werden? Ja [1] Nein [2]");
                System.out.println("Der Standart Pfad lautet: src/main/resources/MdB-Stammdaten/MDB_STAMMDATEN.XML\n");
                String path = "src/main/resources/MdB-Stammdaten/MDB_STAMMDATEN.XML";
                File datei = new File(path);

                Scanner scanner = new Scanner(System.in);

                while (Scanner_switch1) {
                    int input = scanner.nextInt();
                    if (input == 1) {
                        Scanner_switch1 = false;
                        File datei1 = new File(path);
                        if (!datei.exists()){
                            System.out.println("Die Datei im Standart Pfad existiert nicht");
                            System.out.println("Das Programm wird beendet, bitte lege die MDB_Stammdaten.XML in den Standartpfad falls du diese benutzen möchtest");
                            System.exit(0);
                        } else{
                            return path;
                        }
                    } else if (input == 2) {
                        Scanner_switch1 = false;
                        String Pfad_Eingabe = String_Eingabe();
                        return Pfad_Eingabe;
                    } else {
                        System.out.println("Bitte gebe eine 1 oder eine 2 ein.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (NoSuchElementException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (IllegalStateException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            }
        }
        return null;
    }

    /**
     * Converts a JCas to a Base64 String
     * @param aCas
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public static String JCasToBase64ToFileAndUpdateDB(JCas aCas, String FileName, MongoDBHandler MyMongoDBHandler, String Collection, String FindVariableKey, int FindVariableValue) throws IOException, SAXException {
        //XMI Base64
        ByteArrayOutputStream Byte_Output_Stream = new ByteArrayOutputStream();
        XmlCasSerializer.serialize(aCas.getCas(), Byte_Output_Stream);
        byte[] bytes = Byte_Output_Stream.toByteArray();
        String XMI_Base64 = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        byte[] XMI_Base64_byte = XMI_Base64.getBytes();

        //Txt erstellen mit dem Base64
        //autoclose
        String Directory = "src/main/resources/JCasXMIBase64/";
        String Path = Directory + FileName + ".txt";
        try (FileOutputStream File_Output_Stream = new FileOutputStream(Path)) {
            File_Output_Stream.write(XMI_Base64_byte);
            //Pfad in die DB hochladen
            MyMongoDBHandler.getCollection(Collection).findOneAndUpdate(new Document(FindVariableKey, FindVariableValue), new Document("$set", new Document("JCasXMILocalPath", Path)));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Byte_Output_Stream.close();

        return XMI_Base64;
    }


    /**
     * Converts a XMI File to a JCas
     * @param JCasXMILocalPath
     * @return
     */
    public static JCas XMIBase64FileToJCas(String JCasXMILocalPath) {

        try {
            File XMI_File = new File(JCasXMILocalPath);
            FileInputStream finp = new FileInputStream(XMI_File);

            StringBuilder XMI_Base64 = new StringBuilder();
            String JCas_XMI_Base64 = XMI_Base64.toString();

            byte[] XMI_File_Binary = org.apache.commons.codec.binary.Base64.decodeBase64(JCas_XMI_Base64);
            try (ByteArrayInputStream XMI_Input_Stream = new ByteArrayInputStream(XMI_File_Binary)) {
                JCas aCas = JCasFactory.createJCas();
                XmlCasDeserializer.deserialize(XMI_Input_Stream, aCas.getCas());

                return aCas;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ResourceInitializationException e) {
            throw new RuntimeException(e);
        } catch (CASException e) {
            throw new RuntimeException(e);
        }
        return null;
    }




    /**
     * Iteriert duch den Bilder Ordner und fügt Abgeordneten deren Bild als Base64 String Attribut hinzu
     * @param AlleAbgeordneten
     */
    public static void AbgeordneteBildBase64(Set<Abgeordneter_Impl> AlleAbgeordneten){

        File[] BilderDateien = new File("src/main/HTML/Bilder/").listFiles();
        for (File file : BilderDateien) {
            if (file.isFile()){
                //System.out.println(file.getName()); // bsp. 11004097.jpg
                //System.out.println(file); // bsp. src/main/HTML/Bilder/11004097.jpg
                String filepfad = file.getName();
                int index_of_punkt = filepfad.indexOf(".");
                int ID = Integer.valueOf(filepfad.substring(0,index_of_punkt));
                //System.out.println(ID); bsp. 11004097

                try {
                    String BildBase64_Binary = org.apache.commons.codec.binary.Base64.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(file));
                    for (Abgeordneter_Impl abgeordneter : AlleAbgeordneten) {
                        if (abgeordneter.getID()==ID){
                            abgeordneter.setBild_Base64(BildBase64_Binary);
                        }
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Entnimmt  das String BildBase64 und eine Abgeordneten ID und speichert das Bild in den MongoDB Ordner.
     * Als Name wird die Abgeordneten ID verwendet
     * @param BildBase64
     * @param AbgeordneterID
     */
    public static void BildBase64ToFileSave(String BildBase64,int AbgeordneterID){
        String PfadBild = String.valueOf(AbgeordneterID) + ".jpg";
        String Pfad = "src/main/HTML/BilderMongoDB/" + PfadBild;
        byte[] BildBase64_Binary = org.apache.commons.codec.binary.Base64.decodeBase64(BildBase64);
        File BildDatei = new File(Pfad);

        try(FileOutputStream Bild_OutputS = new FileOutputStream(BildDatei)){
            Bild_OutputS.write(BildBase64_Binary);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    /**
     * Entnimmt  das Plenarprotokoll als Document aus der DB und speichert diese und zudem gibt diese die Datei zurück.
     * @param Plenarprotokoll
     * @return
     */
    public static File PlenarprotokollBase64ToFileSave(Document Plenarprotokoll){
        String filename = Plenarprotokoll.getString("PlenarprotokollNummer");
        String PlenarprotokollBase64 = Plenarprotokoll.getString("Base64XML");

        String PfadProtokoll = filename + ".xml";
        String Pfad = "src/main/resources/20/" + PfadProtokoll;
        byte[] PlenarprotokollBase64_Binary = org.apache.commons.codec.binary.Base64.decodeBase64(PlenarprotokollBase64);
        File PlenarprotokollDatei = new File(Pfad);

        try(FileOutputStream Plenarprotokoll_OutputS = new FileOutputStream(PlenarprotokollDatei)){
            Plenarprotokoll_OutputS.write(PlenarprotokollBase64_Binary);
            return PlenarprotokollDatei;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a Pfad containing a file to a byte array
     * @param Path
     * @return
     */
    public static byte[] FilePathToByteArray(String Path){

        File datei = new File(Path);

        try{
            byte[] byteArray = FileUtils.readFileToByteArray(datei);
            return byteArray;
        } catch (IOException e) {

        }
        return null;
    }


    /**
     * Diese Methoden nimmt eine Factory an und lädt alle daraus enstehenden Klassen und Objekte in die MongoDB.
     * @param MyMongoDBHandler
     * @param MyFactory
     */
    public static void UploadNewData(MongoDBHandler MyMongoDBHandler, Factory MyFactory){
        Set<Abgeordneter_Impl> AlleAbgeordneten = MyFactory.getAlleAbgeordneten();
        Set<Ausschuss_Impl> AlleAusschusse = MyFactory.getAlleAusschüsse();
        Set<Fraktion_Impl> AlleFraktionen = MyFactory.getAlleFraktionen();
        Set<Institution_Impl> AlleInstitutionen = MyFactory.getAlleInstitutionen();
        Set<Partei_Impl> AlleParteien = MyFactory.getAlleParteien();
        Set<Rede_Impl> AlleReden = MyFactory.getAlleReden();
        List<RedeAuszug_Impl> AlleRedeAuszuge = MyFactory.getAlleRedeAuszuge();
        Set<Sitzung_Impl> AlleSitzungen = MyFactory.getAlleSitzungen();
        Set<Tagesordnungspunkt_Impl> AlleTagesordnungspunkte = MyFactory.getAlleTagesordnungspunkte();
        Set<Wahlkreis_Impl> AlleWahlkreise = MyFactory.getAlleWahlkreise();
        Set<Wahlperiode_Impl> AlleWahlperiode = MyFactory.getAlleWahlperiode();
        Set<Wahlperioden_Impl> AlleWahlperioden = MyFactory.getAlleWahlperioden();


        System.out.println("Das Hochladen beginnt, dies könnte etwas dauern..\n");
        MyMongoDBHandler.Abgeordneter_Uploader("Abgeordneter", AlleAbgeordneten);
        MyMongoDBHandler.Ausschuss_Uploader("Ausschuss", AlleAusschusse);
        MyMongoDBHandler.Fraktion_Uploader("Fraktion", AlleFraktionen);
        MyMongoDBHandler.Institution_Uploader("Institution", AlleInstitutionen);
        MyMongoDBHandler.Partei_Uploader("Partei", AlleParteien);
        MyMongoDBHandler.Rede_Uploader("Rede", AlleReden);
        MyMongoDBHandler.RedeAuszug_Uploader("RedeAuszug", AlleRedeAuszuge);
        MyMongoDBHandler.Sitzung_Uploader("Sitzung", AlleSitzungen);
        MyMongoDBHandler.Tagesordnungspunkt_Uploader("Tagesordnungspunkt", AlleTagesordnungspunkte);
        MyMongoDBHandler.Wahlkreis_Uploader("Wahlkreis", AlleWahlkreise);
        MyMongoDBHandler.Wahlperiode_Uploader("Wahlperiode", AlleWahlperiode);
        MyMongoDBHandler.Wahlperioden_Uploader("Wahlperioden", AlleWahlperioden);
    }

    /**
     * Diese Methode fügt die Bilder Strings aus der mpPictures JSON zu den enstprechenden Abgeordneten
     */
    public static void mpPicturesAttributHinzufugen(Set<Abgeordneter_Impl> AlleAbgeordneten){
        String Pfad = new String("src/main/resources/mpPictures/mpPictures.json");
        File file = new File(Pfad);
        try {
            FileReader reader = new FileReader(file);
            BufferedReader breader = new BufferedReader(reader);
            StringBuilder daten = new StringBuilder();
            String line;
            while ((line=breader.readLine()) != null){
                daten.append(line);
            }
            String daten_string = daten.toString();
            BsonArray bsonarray = BsonArray.parse(daten_string);
            int counter = 0;
            for (BsonValue bsonValue : bsonarray) {
                BsonDocument docu = bsonValue.asDocument();
                Set<Map.Entry<String, BsonValue>> entryset = docu.entrySet();
                for (String s : docu.keySet()) {
                    //System.out.println(s);
                    Integer ID = Integer.valueOf(s);
                    for (BsonValue value : docu.values()) {
                        //System.out.println(value);
                        BsonArray value_array = value.asArray();
                        for (BsonValue iter_value : value_array) {
                            //System.out.println(iter_value);
                            BsonDocument docu_array = iter_value.asDocument();

                            /*
                            BsonString name = docu_array.getString("name");
                            String name_string = name.getValue();
                            System.out.println(name_string);

                             */

                            BsonString hq_picture = docu_array.getString("hq_picture");
                            String hq_picture_string = hq_picture.getValue();
                            //System.out.println(hq_picture_string);

                            BsonString description = docu_array.getString("description");
                            String description_string = description.getValue();
                            //System.out.println(description_string);


                            for (Abgeordneter_Impl abgeordneter : AlleAbgeordneten) {
                                if (abgeordneter.getID() == ID){
                                    abgeordneter.setHq_picture(hq_picture_string);
                                    abgeordneter.setPicture_description(description_string);
                                    //counter++;
                                }
                            }

                        }
                    }
                }
            }
            //System.out.println("So viele abgeordnete geupdated: "+counter);


        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode fragt den Nutzer nach ob alle Abgeordneten RedePortfolios in den Cache geladen werden soll.
     * @return
     */
    public static Boolean Cache_load_Abfrage(){

        Boolean Scanner_switch1 = true;
        while (Scanner_switch1) {
            try {
                System.out.println("Sollen alle Abgeordneten RedePortfolios in den Cache geladen werden? Ja [1] oder Nein [2].");
                System.out.println("Beachte: Dies wurde den Abruf der Redeportfolios beschleunigen, jedoch würde die Initialisierung länger dauern.\n");
                Scanner scanner = new Scanner(System.in);
                int input = scanner.nextInt();
                if (input==1) {
                    Scanner_switch1 = false;
                    return true;
                } else if (input==2){
                    Scanner_switch1 = false;
                    return false;
                }
                System.out.println("Bitte gebe eine gültige Zahl ein");
            } catch (InputMismatchException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (NoSuchElementException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (IllegalStateException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            }
        }
        return null;
    }

    /**
     * Diese Methode fragt den Nutzer nach ob alle Wahlperioden in den Cache geladen werden soll.
     * @return
     */
    public static Boolean WPCache_load_Abfrage(){
        Boolean Scanner_switch1 = true;
        while (Scanner_switch1) {
            try {
                System.out.println("Sollen alle Wahlperioden in den Cache geladen werden? Ja [1] oder Nein [2].");
                System.out.println("Beachte: Dies wurde den Abruf der verschiedener Wahlperioden beschleunigen, jedoch würde die Initialisierung länger dauern.\n");
                Scanner scanner = new Scanner(System.in);
                int input = scanner.nextInt();
                if (input==1) {
                    Scanner_switch1 = false;
                    return true;
                } else if (input==2){
                    Scanner_switch1 = false;
                    return false;
                }
                System.out.println("Bitte gebe eine gültige Zahl ein");
            } catch (InputMismatchException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (NoSuchElementException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            } catch (IllegalStateException e) {
                System.out.println("Bitte gebe ein gültiges Zeichen ein");
            }
        }
        return null;
    }

    /**
     * Nimmt eine Liste von MongoID Referenzpunkten und erstellt dabei die Mongo_Impl und speichert diese im eingebenen Objekt.
     * In dieser Methode handelt es sich um die RedeAuszug Objekte für einem Rede_Mongo_Impl Objekt.
     * @param MyMongoDBHandler
     * @param Rede
     */
    public static void ListIntegerToListRedeAuszugMongoDBObjekt(MongoDBHandler MyMongoDBHandler , Rede_MongoDB_Impl Rede){

        List<Integer> RedeAuszuMongoIDs = Rede.getRedeAuszugMongoIDs();

        for (Integer redeAuszugMongoID : RedeAuszuMongoIDs) {
            RedeAuszug_MongoDB_Impl RedeAuszugObjekt = new RedeAuszug_MongoDB_Impl(MyMongoDBHandler, redeAuszugMongoID);
            Rede.AddRedeAuszug_Objekte(RedeAuszugObjekt);
        }
    }

    /**
     * Nimmt eine Liste von MongoID Referenzpunkten und erstellt dabei die Mongo_Impl und speichert diese im eingebenen Objekt.
     * In dieser Methode handelt es sich um die Historie Objekte für einem RedeAuszug_Mongo_Impl Objekt.
     * @param MyMongoDBHandler
     * @param RedeAuszug
     */
    public static void ListHistorieMongoIDsToListMongoObjekt(MongoDBHandler MyMongoDBHandler, RedeAuszug_MongoDB_Impl RedeAuszug){
        List<Integer> HistorieMongoIDs = RedeAuszug.getHistorieMongoIDs();
        if (HistorieMongoIDs==null){
            return;
        }

        for (Integer historieMongoID : HistorieMongoIDs) {
            Historie_MongoDB_Impl HistorieMongoObjekt = new Historie_MongoDB_Impl(MyMongoDBHandler, historieMongoID);
            RedeAuszug.AddHistorie_Impl(HistorieMongoObjekt);
        }
    }

    /**
     * Nimmt eine Liste von MongoID Referenzpunkten und erstellt dabei die Mongo_Impl und speichert diese im eingebenen Objekt.
     * In dieser Methode handelt es sich um die Historie Objekte für einem Abgeordneter_Mongo_Impl Objekt.
     * @param MyMongoDBHandler
     * @param Abgeordneter
     */
    public static void ListBildHistorieMongoIDsToListMongoObjekt(MongoDBHandler MyMongoDBHandler, Abgeordneter_MongoDB_Impl Abgeordneter){
        List<Integer> HistorieMongoIDs = Abgeordneter.getBildhistorieMongoIDs();

        if (HistorieMongoIDs==null){
            return;
        }

        for (Integer historieMongoID : HistorieMongoIDs) {
            Historie_MongoDB_Impl HistorieMongoObjekt = new Historie_MongoDB_Impl(MyMongoDBHandler, historieMongoID);
            Abgeordneter.AddBildHistorie_Impl(HistorieMongoObjekt);
        }
    }


    /**
     * Nimmt einen Sentiment Wert zwischen -1 und 1 an und wandelt diesen in ein RBG Colour Code, sodass ein kontinuierliches Spektrum von Rot - Weiß - Grün entsteht.
     * @param Sentiment
     * @return
     */
    public static String SentimentToColour(double Sentiment){

        Double Sentiment_s = Double.valueOf(Sentiment);
        Double Sentiment_s_absolutValue = Math.abs(Sentiment_s);
        int B = (int) (255 * (1-Sentiment_s_absolutValue));

        //Bei 1 dann R 0 G 255 B 0 Richtung 0 soll R und B gleichzeitig steigen bis 255
        if (Sentiment>0){
            int R = B;
            int G = 255;
            return "rgb("+R+","+G+","+B+")";

        } else if (Sentiment<0) {
            //Bei -1 dann R 255 G 0 B 0 Richtung 0 soll G und B gleichzeitig steigen bis 255
            int G = B;
            int R = 255;
            return "rgb("+R+","+G+","+B+")";

        } else {
            //Bei 0 dann R 255 G 255 B 255
            int R = 255;
            int G = 255;
            return "rgb("+R+","+G+","+B+")";
        }
    }
}