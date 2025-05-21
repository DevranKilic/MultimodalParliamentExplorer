package Einlese;

import Class_Impl.Abgeordneter_Impl;
import Class_Impl.Institution_Impl;
import Class_Impl.Wahlkreis_Impl;
import Class_Impl.Wahlperioden_Impl;
import Helper.Hilfsmethoden;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;


/**
 * Diese Klasse übernimmt die Aufgabe die MDB Stammdaten einzulesen, Objekte zu erstellen und den Objekten die entsprechenden Attribute hinzuzufügen.
 */
public class MDB_Stammdaten_Einlese {

    /**
     * Hier werden Referenzen gespeichert.
     * MongoID Attribute ermöglichen eine eindeutige ID vergabe
     */
    private Set<Abgeordneter_Impl> AllAbgeordnetenClass;
    private Set<Wahlperioden_Impl> allWahlperiodenImpls;
    private Set<Wahlkreis_Impl> allWahlkreisImpls;
    private Set<Institution_Impl> AllInsitutuíonenClass;
    private int MongoID_counter_Abgeordneter = 0;
    private int MongoID_counter_Wahlperioden = 0;
    private int MongoID_counter_Wahlkreis = 0;
    private int MongoID_counter_Institution = 0;

    public MDB_Stammdaten_Einlese() {
        this.AllAbgeordnetenClass = new HashSet<>();
        this.allWahlperiodenImpls = new HashSet<>();
        this.allWahlkreisImpls = new HashSet<>();
        this.AllInsitutuíonenClass = new HashSet<>();
    }


    /**
     * Methode liest die MDB Stammdaten Datei ein und iteriert zu den entsprechenden Stellen um Objekte deren Attribute zu füllen.
     */
    public void MDB_Einlesen() {
        try {
            //String path = Hilfsmethoden.PfadAbfrageMDBStammdaten();
            String path = "src/main/resources/MdB-Stammdaten/MDB_STAMMDATEN.XML";

            File datei = new File(path);
            if (!datei.exists()) {
                System.out.println("Die Datei im angegebenen Pfad existiert nicht");
                System.out.println("Angegebener Pfad: "+path);
            }
            DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = Factory.newDocumentBuilder();
            Document document = builder.parse(path);
            NodeList MDB = document.getElementsByTagName("MDB");
            SimpleDateFormat DD_MM_JJJJ_Datum = new SimpleDateFormat("dd.MM.yyyy");

            for (int i = 0; i < MDB.getLength(); i++) {
                Node iternode = MDB.item(i);
                if (iternode.getNodeType() == Node.ELEMENT_NODE) {
                    Abgeordneter_Impl Abgeordneter = new Abgeordneter_Impl();
                    //Abgeordneter.setMongoID(MongoID_counter_Abgeordneter);
                    Abgeordneter.setMongoLabel("ABGEORDNETER");
                    MongoID_counter_Abgeordneter++;

                    NodeList ID_Namen_BIO_WP = iternode.getChildNodes();

                    for (int j = 0; j < ID_Namen_BIO_WP.getLength(); j++) {
                        Node jiternode = ID_Namen_BIO_WP.item(j);
                        if (jiternode.getNodeType() == Node.ELEMENT_NODE) {

                            if (jiternode.getNodeName().equals("ID")){
                                //System.out.println("ID Lautet: "+jiternode.getTextContent());
                                Abgeordneter.setID(Integer.parseInt(jiternode.getTextContent().trim()));
                            }
                            if (jiternode.getNodeName().equals("NAMEN")){

                                NodeList NAMEN = jiternode.getChildNodes();
                                for (int k = 0; k < NAMEN.getLength(); k++) {
                                    Node kiternode = NAMEN.item(k);
                                    if (kiternode.getNodeType() == Node.ELEMENT_NODE){

                                        NodeList NAME = kiternode.getChildNodes();
                                        for (int l = 0; l < NAME.getLength() ; l++) {
                                            Node liternode = NAME.item(l);
                                            //System.out.println(liternode.getNodeName());
                                            //System.out.println(liternode.getTextContent());

                                            if (liternode.getNodeName().equals("NACHNAME")){
                                                //System.out.println("Der Nachname lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setName(liternode.getTextContent().trim());
                                            }
                                            if (liternode.getNodeName().equals("VORNAME")){
                                                //System.out.println("Der Vorname lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setVorname(liternode.getTextContent().trim());
                                            }
                                            if (liternode.getNodeName().equals("ORTSZUSATZ")){
                                                //System.out.println("Der Ortszusatz lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setOrtszusatz(liternode.getTextContent().trim());
                                            }
                                            if (liternode.getNodeName().equals("ADEL")){
                                                //System.out.println("Der Adel lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setAdel(liternode.getTextContent().trim());
                                            }
                                            if (liternode.getNodeName().equals("PRAEFIX")){
                                                //System.out.println("Der Praefix lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setPraefix(liternode.getTextContent().trim());
                                            }
                                            if (liternode.getNodeName().equals("ANREDE_TITEL")){
                                                //System.out.println("Der Anrede Titel lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setAnrede_Titel(liternode.getTextContent().trim());
                                            }
                                            if (liternode.getNodeName().equals("AKAD_TITEL")){
                                                //System.out.println("Der Akademische Titel lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setAkad_Titel(liternode.getTextContent().trim());
                                            }
                                            if (liternode.getNodeName().equals("HISTORIE_VON") && !liternode.getTextContent().trim().isBlank()){
                                                //System.out.println("Die Historie_von lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setHistorie_von(DD_MM_JJJJ_Datum.parse(liternode.getTextContent().trim()));
                                            }
                                            if (liternode.getNodeName().equals("HISTORIE_BIS") && !liternode.getTextContent().trim().isBlank()){
                                                //System.out.println("Die Historie_bis lautet: "+ liternode.getTextContent().trim());
                                                Abgeordneter.setHistorie_bis(DD_MM_JJJJ_Datum.parse(liternode.getTextContent().trim()));
                                            }
                                        }
                                    }

                                }
                            }

                            if (jiternode.getNodeName().equals("BIOGRAFISCHE_ANGABEN")){

                                NodeList BIOGRAFISCHE_ANGABEN = jiternode.getChildNodes();

                                for (int k = 0; k < BIOGRAFISCHE_ANGABEN.getLength(); k++) {
                                    Node kiternode = BIOGRAFISCHE_ANGABEN.item(k);
                                    if (kiternode.getNodeType() == Node.ELEMENT_NODE){

                                        if (kiternode.getNodeName().equals("GEBURTSDATUM") && !kiternode.getTextContent().trim().isBlank()){
                                            //System.out.println("Geburtsdatum lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setGeburtsdatum(DD_MM_JJJJ_Datum.parse(kiternode.getTextContent().trim()));
                                        }
                                        if (kiternode.getNodeName().equals("GEBURTSORT")){
                                            //System.out.println("Geburtsort lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setGeburtsort(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("GEBURTSLAND")){
                                            //System.out.println("Geburtsland lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setGeburtsland(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("STERBEDATUM") && !kiternode.getTextContent().trim().isBlank()){
                                            //System.out.println("Sterbedatum lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setSterbedatum(DD_MM_JJJJ_Datum.parse(kiternode.getTextContent().trim()));
                                        }
                                        if (kiternode.getNodeName().equals("GESCHLECHT")){
                                            //System.out.println("Geschlecht lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setGeschlecht(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("FAMILIENSTAND")){
                                            //System.out.println("Familienstand lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setFamilienstand(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("RELIGION")){
                                            //System.out.println("Religion lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setReligion(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("BERUF")){
                                            //System.out.println("Beruf lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setBeruf(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("PARTEI_KURZ")){
                                            //System.out.println("Partei kurz lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setPartei_kurz(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("VITA_KURZ")){
                                            //System.out.println("VITA Kurz lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setVita_kurz(kiternode.getTextContent().trim());
                                        }
                                        if (kiternode.getNodeName().equals("VEROEFFENTLICHUNGSPFLICHTIGES")){
                                            //System.out.println("Veröffentlichungspflichtiges lautet: "+ kiternode.getTextContent().trim());
                                            Abgeordneter.setVeröffentlichungspflichtiges(kiternode.getTextContent().trim());
                                        }

                                    }

                                }

                            }

                            if (jiternode.getNodeName().equals("WAHLPERIODEN")){
                                NodeList WAHLPERIODE = jiternode.getChildNodes();
                                int Anzahl_Wahlperioden = 0;
                                for (int k = 0; k < WAHLPERIODE.getLength(); k++) {
                                    Node kiternode = WAHLPERIODE.item(k);
                                    if (kiternode.getNodeType() == Node.ELEMENT_NODE){
                                        Anzahl_Wahlperioden++;
                                        Wahlperioden_Impl Wahlperiode = new Wahlperioden_Impl();
                                        //Wahlperiode.setMongoID(MongoID_counter_Wahlperioden);
                                        Wahlperiode.setMongoLabel("WAHLPERIODEN");
                                        MongoID_counter_Wahlperioden++;
                                        Wahlkreis_Impl Wahlkreis = new Wahlkreis_Impl();
                                        //Wahlkreis.setMongoID(MongoID_counter_Wahlkreis);
                                        Wahlkreis.setMongoLabel("WAHLKREIS");
                                        MongoID_counter_Wahlkreis++;
                                        NodeList WAHLPERIODE_Inhalt = kiternode.getChildNodes();
                                        for (int l = 0; l < WAHLPERIODE_Inhalt.getLength(); l++) {
                                            Node liternode = WAHLPERIODE_Inhalt.item(l);
                                            if (liternode.getNodeType() == Node.ELEMENT_NODE){
                                                if (liternode.getNodeName().equals("WP")){
                                                    //System.out.println("WP lautet:"+ liternode.getTextContent().trim());
                                                    Wahlperiode.setNumber(Integer.valueOf(liternode.getTextContent().trim()));
                                                }
                                                if (liternode.getNodeName().equals("MDBWP_VON") && !liternode.getTextContent().trim().isBlank()){
                                                    //System.out.println("MDBWP_VON lautet:"+ liternode.getTextContent().trim());
                                                    Wahlperiode.setStartDate(DD_MM_JJJJ_Datum.parse(liternode.getTextContent().trim()));
                                                }
                                                if (liternode.getNodeName().equals("MDBWP_BIS") && !liternode.getTextContent().trim().isBlank()){
                                                    //System.out.println("MDBWP_BIS lautet:"+ liternode.getTextContent().trim());
                                                    Wahlperiode.setEndDate(DD_MM_JJJJ_Datum.parse(liternode.getTextContent().trim()));
                                                }
                                                if (liternode.getNodeName().equals("WKR_NUMMER") && !liternode.getTextContent().trim().isBlank()){
                                                    //System.out.println("WKR_NUMMER lautet:"+ liternode.getTextContent().trim());
                                                    Wahlkreis.setWKR_Nummer(Integer.valueOf(liternode.getTextContent().trim()));
                                                }
                                                if (liternode.getNodeName().equals("WKR_NAME")){
                                                    //System.out.println("WKR_NAME lautet:"+ liternode.getTextContent().trim());
                                                    Wahlkreis.setWKR_Name(liternode.getTextContent().trim());
                                                }
                                                if (liternode.getNodeName().equals("WKR_LAND")){
                                                    //System.out.println("WKR_LAND lautet:"+ liternode.getTextContent().trim());
                                                    Wahlkreis.setWKR_Land(liternode.getTextContent().trim());
                                                }
                                                if (liternode.getNodeName().equals("LISTE")){
                                                    //System.out.println("LISTE lautet:"+ liternode.getTextContent().trim());
                                                    Wahlperiode.setListe(liternode.getTextContent().trim());
                                                }
                                                if (liternode.getNodeName().equals("MANDATSART")){
                                                    //System.out.println("MANDATSART lautet:"+ liternode.getTextContent().trim());
                                                    Wahlperiode.setMandatsart(liternode.getTextContent().trim());
                                                }
                                                if (liternode.getNodeName().equals("INSTITUTIONEN")){
                                                    //System.out.println("Es gibt INSTITUTIONEN");
                                                    NodeList INSTITUTION = liternode.getChildNodes();
                                                    for (int m = 0; m < INSTITUTION.getLength() ; m++) {
                                                        Node miternode = INSTITUTION.item(m);
                                                        if (miternode.getNodeType() == Node.ELEMENT_NODE){
                                                            Institution_Impl Insitution = new Institution_Impl();
                                                            //Insitution.setMongoID(MongoID_counter_Institution);
                                                            Insitution.setMongoLabel("INSTITUTION");
                                                            MongoID_counter_Institution++;
                                                            NodeList INSTITUTION_Inhalt = miternode.getChildNodes();
                                                            for (int n = 0; n < INSTITUTION_Inhalt.getLength() ; n++) {
                                                                Node niternode = INSTITUTION_Inhalt.item(n);
                                                                //System.out.println(niternode);
                                                                if (niternode.getNodeName().equals("INSART_LANG")){
                                                                    //System.out.println("INSART_LANG lautet: "+ niternode.getTextContent().trim());
                                                                    Insitution.setInsart_Lang(niternode.getTextContent().trim());
                                                                }
                                                                if (niternode.getNodeName().equals("INS_LANG")){
                                                                    //System.out.println("INS_LANG lautet: "+ niternode.getTextContent().trim());
                                                                    Insitution.setIns_Lang(niternode.getTextContent().trim());
                                                                }
                                                                if (niternode.getNodeName().equals("MDBINS_VON") && !niternode.getTextContent().trim().isBlank()){
                                                                    //System.out.println("MDBINS_VON lautet: "+ niternode.getTextContent().trim());
                                                                    Insitution.setMDBins_Von(DD_MM_JJJJ_Datum.parse(niternode.getTextContent().trim()));
                                                                }
                                                                if (niternode.getNodeName().equals("MDBINS_BIS") && !niternode.getTextContent().trim().isBlank()){
                                                                    //System.out.println("MDBINS_BIS lautet: "+ niternode.getTextContent().trim());
                                                                    Insitution.setMDBins_Bis(DD_MM_JJJJ_Datum.parse(niternode.getTextContent().trim()));
                                                                }
                                                                if (niternode.getNodeName().equals("FKT_LANG")){
                                                                    //System.out.println("FKT_LANG lautet: "+ niternode.getTextContent().trim());
                                                                    Insitution.setFkt_lang(niternode.getTextContent().trim());
                                                                }
                                                                if (niternode.getNodeName().equals("FKTINS_VON") && !niternode.getTextContent().trim().isBlank()){
                                                                    //System.out.println("FKTINS_VON lautet: "+ niternode.getTextContent().trim());
                                                                    Insitution.setFktins_Von(DD_MM_JJJJ_Datum.parse(niternode.getTextContent().trim()));
                                                                }
                                                                if (niternode.getNodeName().equals("FKTINS_BIS") && !niternode.getTextContent().trim().isBlank()){
                                                                    //System.out.println("FKTINS_BIS lautet: "+ niternode.getTextContent().trim());
                                                                    Insitution.setFktins_Bis(DD_MM_JJJJ_Datum.parse(niternode.getTextContent().trim()));
                                                                }
                                                            }
                                                            //System.out.println("Neue Institution");
                                                            AllInsitutuíonenClass.add(Insitution);
                                                            Abgeordneter.setInstitutionen(Insitution);
                                                            Wahlperiode.AddInstitution(Insitution);


                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //System.out.println("Neue Wahlperiode");
                                        allWahlperiodenImpls.add(Wahlperiode);
                                        allWahlkreisImpls.add(Wahlkreis);
                                        Abgeordneter.setWahlperioden(Wahlperiode);
                                        Wahlperiode.setWahlkreis(Wahlkreis);
                                        Wahlkreis.setWahlperiode(Wahlperiode);

                                    }
                                }

                            }
                        }
                    }
                    //System.out.println("Neuer Abgeordneter");
                    AllAbgeordnetenClass.add(Abgeordneter);
                }
            }
        } catch (Exception e) {
            System.out.println("Fehler unterlaufen" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Set<Abgeordneter_Impl> getAllAbgeordnetenClass(){
        return this.AllAbgeordnetenClass;
    }

    public Set<Wahlperioden_Impl> getAllWahlperiodeClass(){
        return this.allWahlperiodenImpls;
    }

    public Set<Wahlkreis_Impl> getAllWahlkreisClass(){
        return this.allWahlkreisImpls;
    }

    public Set<Institution_Impl> getAllInsitutuíonenClass(){
        return this.AllInsitutuíonenClass;
    }

}