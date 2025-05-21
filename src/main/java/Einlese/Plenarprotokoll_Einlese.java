package Einlese;

import Class_Impl.*;
import Helper.Hilfsmethoden;
import com.mongodb.client.FindIterable;
import database.MongoDBHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
/**
 * Diese Klasse übernimmt die Aufgabe die Plenarprotokolle einzulesen, Objekte zu erstellen und den Objekten die entsprechenden Attribute hinzuzufügen.
 */
public class Plenarprotokoll_Einlese {
    /**
     * Hier werden Referenzen gespeichert.
     * MongoID Attribute ermöglichen eine eindeutige ID vergabe
     */
    private Set<Rede_Impl> allRedeImpls;
    private Map<String, String> Entschuldigte_Abgeordnete;
    private Set<Sitzung_Impl> AllSitzungen;
    private Set<Tagesordnungspunkt_Impl> AllTagesordnungspunkte;
    private List<RedeAuszug_Impl> AllRedeAuszuge;
    private int MongoID_counter_Sitzung = 0;
    private int MongoID_counter_Rede = 0;
    private int MongoID_counter_RedeAuszug = 0;
    private int MongoID_counter_Tagesordnungspunkt = 0;

    private MongoDBHandler MyMongoDBHandler;

    public Plenarprotokoll_Einlese(MongoDBHandler MyMongoDBHandler) {
        this.MyMongoDBHandler = MyMongoDBHandler;
        this.AllRedeAuszuge = new ArrayList<>();
        this.allRedeImpls = new HashSet<>();
        this.Entschuldigte_Abgeordnete = new HashMap<>();
        this.AllSitzungen = new HashSet<>();
        this.AllTagesordnungspunkte = new HashSet<>();

    }

    /**
     * Methode liest die iteriert über die Rede Dateien und iteriert jeweils zu den entsprechenden Stellen um Objekte deren Attribute zu füllen.
     */
    public void Plenar_Einlesen() {
        try {
            FindIterable<org.bson.Document> Plenarprotokolle_Docu = MyMongoDBHandler.getCollection("Plenarprotokoll").find();
            for (org.bson.Document Plenarprotokoll_document : Plenarprotokolle_Docu) {

                File Plenarprotokoll = Hilfsmethoden.PlenarprotokollBase64ToFileSave(Plenarprotokoll_document);
                if ((Plenarprotokoll == null) || Plenarprotokoll.getName().equals("dbtplenarprotokoll.dtd")){
                    continue;
                }

                if (Plenarprotokoll_document.getBoolean("Verarbeitet")){
                    System.out.println("Plenarprotokoll: "+Plenarprotokoll_document.getString("PlenarprotokollNummer")+ " wurde schon verarbeitet");
                    Plenarprotokoll.delete();
                    continue;
                }

                System.out.println("Verarbeite folgendes Plenarprotokoll: PlenarprotokollNummer "+Plenarprotokoll_document.getString("PlenarprotokollNummer"));
                DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = Factory.newDocumentBuilder();
                Document document = builder.parse(Plenarprotokoll.getAbsolutePath());
                Integer WahlperiodeNummer = null;
                Integer SitzungsNummer = null;
                String Ort = null;
                Date Datum = null;
                Sitzung_Impl Sitzung = new Sitzung_Impl();
                //Sitzung.setMongoID(MongoID_counter_Sitzung);
                Sitzung.setMongoLabel("SITZUNG");
                MongoID_counter_Sitzung++;
                Sitzung = TagesordnungspunktObject_Einlese(document, Sitzung, AllTagesordnungspunkte);

                NodeList dbtplenarprotokoll_Node = document.getElementsByTagName("dbtplenarprotokoll");
                String SitzungsStart_String = null;
                String SitzungsEnde_String = null;
                for (int i = 0; i < dbtplenarprotokoll_Node.getLength(); i++) {
                    Node iter = dbtplenarprotokoll_Node.item(i);
                    //System.out.println(iter.getAttributes().getNamedItem("sitzung-start-uhrzeit").getTextContent());
                    //System.out.println(iter.getAttributes().getNamedItem("sitzung-ende-uhrzeit").getTextContent());
                    SitzungsStart_String = iter.getAttributes().getNamedItem("sitzung-start-uhrzeit").getTextContent().trim();
                    SitzungsEnde_String = iter.getAttributes().getNamedItem("sitzung-ende-uhrzeit").getTextContent().trim();
                }
                //System.out.println(SitzungsStart_String);
                //System.out.println(SitzungsStart_String.length());
                //System.out.println(SitzungsEnde_String);
                //System.out.println(SitzungsEnde_String.length());
                if (SitzungsStart_String.length() < 5) {
                    SitzungsStart_String = "0" + SitzungsStart_String;
                }
                if (SitzungsEnde_String.length() < 5) {
                    SitzungsEnde_String = "0" + SitzungsEnde_String;
                }
                if (SitzungsStart_String != null) {
                    LocalTime SitzungsStart = LocalTime.parse(SitzungsStart_String);
                    Sitzung.setSitzungsbeginn(SitzungsStart);
                }
                if (SitzungsEnde_String != null) {
                    LocalTime SitzungsEnde = LocalTime.parse(SitzungsEnde_String);
                    Sitzung.setSitzungsende(SitzungsEnde);
                }
                NodeList kopfdaten = document.getElementsByTagName("kopfdaten");
                for (int i = 0; i < kopfdaten.getLength(); i++) {
                    Node iternode = kopfdaten.item(i);
                    if (iternode.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList kopfdaten_drinne = iternode.getChildNodes();
                        for (int j = 0; j < kopfdaten_drinne.getLength(); j++) {
                            Node jiternode = kopfdaten_drinne.item(j);
                            if (jiternode.getNodeType() == Node.ELEMENT_NODE && jiternode.getNodeName().equals("plenarprotokoll-nummer")) {
                                if (jiternode.getNodeName().equals("plenarprotokoll-nummer")) {
                                    NodeList plenarprotokoll_nummer = jiternode.getChildNodes();
                                    for (int k = 0; k < plenarprotokoll_nummer.getLength(); k++) {
                                        Node kiternode = plenarprotokoll_nummer.item(k);
                                        if (kiternode.getNodeType() == Node.ELEMENT_NODE) {
                                            if (kiternode.getNodeName().equals("wahlperiode")) {
                                                //System.out.println("Die Wahlperiode lautet: "+kiternode.getTextContent().trim());
                                                WahlperiodeNummer = Integer.valueOf(kiternode.getTextContent().trim());
                                                Sitzung.setWP_Nummer(WahlperiodeNummer);
                                            }
                                            if (kiternode.getNodeName().equals("sitzungsnr")) {
                                                //System.out.println("Die SitzungsNummer lauter:"+ kiternode.getTextContent().trim());
                                                String SitzungsNummerString = kiternode.getTextContent().trim();
                                                if (SitzungsNummerString.contains("(neu)")) {
                                                    String cleanSitzungsNummerString = SitzungsNummerString.replace(" (neu)", "");
                                                    //System.out.println(cleanSitzungsNummerString);
                                                    //System.out.println(kiternode.getTextContent().trim());
                                                    SitzungsNummer = Integer.valueOf(cleanSitzungsNummerString);
                                                } else {
                                                    SitzungsNummer = Integer.valueOf(kiternode.getTextContent().trim());
                                                }
                                                Sitzung.setSitzungsnr(SitzungsNummer);
                                            }
                                        }
                                    }
                                }
                            }
                            if (jiternode.getNodeName().equals("veranstaltungsdaten")) {
                                NodeList veranstaltungsdaten = jiternode.getChildNodes();
                                for (int k = 0; k < veranstaltungsdaten.getLength(); k++) {
                                    Node kiternode = veranstaltungsdaten.item(k);
                                    if (kiternode.getNodeType() == Node.ELEMENT_NODE) {
                                        if (kiternode.getNodeName().equals("ort")) {
                                            //System.out.println("Der Ort lautet:" +kiternode.getTextContent().trim());
                                            Ort = kiternode.getTextContent().trim();
                                            Sitzung.setSitzungs_Ort(Ort);
                                        }
                                        if (kiternode.getNodeName().equals("datum")) {
                                            //System.out.println("Das datum lautet: " +kiternode.getAttributes().getNamedItem("date").getTextContent().trim());
                                            SimpleDateFormat DD_MM_JJJJ_Datum = new SimpleDateFormat("dd.MM.yyyy");
                                            Datum = DD_MM_JJJJ_Datum.parse(kiternode.getAttributes().getNamedItem("date").getTextContent().trim());
                                            Sitzung.setSitzungsdatum(Datum);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                NodeList RedenNodes = document.getElementsByTagName("rede");
                for (int i = 0; i < RedenNodes.getLength(); i++) {
                    Node iternode = RedenNodes.item(i);
                    if (iternode.getNodeType() == Node.ELEMENT_NODE) {
                        Rede_Impl Rede = new Rede_Impl();
                        //Rede.setMongoID(MongoID_counter_Rede);
                        Rede.setMongoLabel("REDE");
                        MongoID_counter_Rede++;
                        String RedeText = "";
                        String RedeText_mit_Kommentaren = "";
                        //System.out.println("Die Rede_ID lautet: "+iternode.getAttributes().getNamedItem("id").getTextContent().trim());
                        Rede.setRede_ID(iternode.getAttributes().getNamedItem("id").getTextContent().trim());
                        NodeList iter_childs = iternode.getChildNodes();
                        for (int j = 0; j < iter_childs.getLength(); j++) {
                            Node jiternode = iter_childs.item(j);
                            MongoID_counter_RedeAuszug++;
                            //System.out.println("Vor dem if: "+jiternode);
                            if (jiternode.getNodeType() == Node.ELEMENT_NODE && jiternode.getNodeName().equals("p")) {
                                //System.out.println(jiternode);
                                if (jiternode.getAttributes().getNamedItem("klasse") != null) {
                                    if (jiternode.getNodeName().equals("p") && jiternode.getAttributes().getNamedItem("klasse").getTextContent().trim().equals("redner")) {
                                        NodeList Redner = jiternode.getChildNodes();
                                        for (int k = 0; k < Redner.getLength(); k++) {
                                            Node kiternode = Redner.item(k);
                                            if (kiternode.getNodeType() == Node.ELEMENT_NODE) {
                                                //System.out.println("Die Redner_ID lautet: " +kiternode.getAttributes().getNamedItem("id").getTextContent());
                                                //System.out.println(kiternode.getAttributes().getNamedItem("id"));
                                                //System.out.println(RedePath);
                                                if (kiternode.getAttributes().getNamedItem("id") != null) {
                                                    if (!kiternode.getAttributes().getNamedItem("id").getTextContent().contains("r")) {
                                                        Rede.setRedner_ID(Integer.valueOf(kiternode.getAttributes().getNamedItem("id").getTextContent().trim()));
                                                    }
                                                }
                                                NodeList Name = kiternode.getChildNodes();
                                                for (int l = 0; l < Name.getLength(); l++) {
                                                    Node liternode = Name.item(l);
                                                    if (liternode.getNodeType() == Node.ELEMENT_NODE) {
                                                        NodeList Vor_Nach_Fra = liternode.getChildNodes();
                                                        for (int m = 0; m < Vor_Nach_Fra.getLength(); m++) {
                                                            Node miternode = Vor_Nach_Fra.item(m);
                                                            if (miternode.getNodeType() == Node.ELEMENT_NODE) {
                                                                if (miternode.getNodeName().equals("vorname")) {
                                                                    //System.out.println("Der Vorname lautet: "+miternode.getTextContent().trim());
                                                                    Rede.setVorname(miternode.getTextContent().trim());
                                                                }
                                                                if (miternode.getNodeName().equals("nachname")) {
                                                                    //System.out.println("Der Nachname lautet: "+miternode.getTextContent().trim());
                                                                    Rede.setNachname(miternode.getTextContent().trim());
                                                                }
                                                                if (miternode.getNodeName().equals("fraktion")) {
                                                                    //System.out.println("Die Fraktion lautet: "+miternode.getTextContent().trim());
                                                                    Rede.setFraktion(miternode.getTextContent().trim());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (jiternode.getNodeType() == Node.ELEMENT_NODE) {
                                            //Wichtig
                                            //System.out.println(jiternode.getTextContent().trim());
                                            String Text_aus_Node = jiternode.getTextContent().trim();
                                            RedeText = RedeText + Text_aus_Node;
                                            String Text_aus_Node2 = jiternode.getTextContent().trim();
                                            RedeText_mit_Kommentaren = RedeText_mit_Kommentaren + Text_aus_Node2;

                                            RedeAuszug_Impl RedeAuszug = new RedeAuszug_Impl();
                                            int MongoID_RedeAuszug_Rede = RedeAuszug.getMongoID();
                                            RedeAuszug.setMongoID(MongoID_RedeAuszug_Rede);
                                            //RedeAuszug.setMongoID(MongoID_counter_RedeAuszug);
                                            RedeAuszug.setIsKommentar(false);
                                            RedeAuszug.setRedeAuszug(Text_aus_Node);
                                            Rede.AddRedeAuszugMongoIDs(MongoID_RedeAuszug_Rede);
                                            //Rede.AddRedeAuszugMongoIDs(MongoID_counter_RedeAuszug);
                                            AllRedeAuszuge.add(RedeAuszug);

                                        }
                                    }
                                } else {
                                    //Wichtig
                                    //System.out.println(jiternode.getTextContent().trim());
                                    String Text_aus_Node = jiternode.getTextContent().trim();
                                    RedeText = RedeText + Text_aus_Node;

                                    String Text_aus_Node2 = jiternode.getTextContent().trim();
                                    RedeText_mit_Kommentaren = RedeText_mit_Kommentaren + Text_aus_Node2;


                                    RedeAuszug_Impl RedeAuszug = new RedeAuszug_Impl();
                                    int MongoID_RedeAuszug_Rede = RedeAuszug.getMongoID();
                                    RedeAuszug.setMongoID(MongoID_RedeAuszug_Rede);
                                    //RedeAuszug.setMongoID(MongoID_counter_RedeAuszug);
                                    RedeAuszug.setIsKommentar(false);
                                    RedeAuszug.setRedeAuszug(Text_aus_Node);
                                    Rede.AddRedeAuszugMongoIDs(MongoID_RedeAuszug_Rede);
                                    //Rede.AddRedeAuszugMongoIDs(MongoID_counter_RedeAuszug);
                                    AllRedeAuszuge.add(RedeAuszug);


                                }
                            } else if (jiternode.getNodeType() == Node.ELEMENT_NODE && jiternode.getNodeName().equals("kommentar")) {
                                //Wichtig - Hier sind Kommentare
                                //System.out.println(jiternode.getTextContent().trim());
                                String Kommentar = jiternode.getTextContent().trim();
                                //System.out.println(Kommentar)
                                RedeText_mit_Kommentaren = RedeText_mit_Kommentaren + Kommentar;


                                RedeAuszug_Impl RedeAuszug = new RedeAuszug_Impl();
                                int MongoID_RedeAuszug_Rede = RedeAuszug.getMongoID();
                                RedeAuszug.setMongoID(MongoID_RedeAuszug_Rede);
                                //RedeAuszug.setMongoID(MongoID_counter_RedeAuszug);
                                RedeAuszug.setIsKommentar(true);
                                RedeAuszug.setRedeAuszug(Kommentar);
                                Rede.AddRedeAuszugMongoIDs(MongoID_RedeAuszug_Rede);
                                //Rede.AddRedeAuszugMongoIDs(MongoID_counter_RedeAuszug);
                                AllRedeAuszuge.add(RedeAuszug);

                            }
                        }
                        //System.out.println("Der Redetext lautet: "+RedeText);
                        Rede.setRede(RedeText);
                        Rede.setRede_mit_Kommentaren(RedeText_mit_Kommentaren);
                        Rede.setWahlperiodeNummer(WahlperiodeNummer);
                        Rede.setSitzungsnummer(SitzungsNummer);
                        Rede.setOrt(Ort);
                        Rede.setDatum(Datum);

                        allRedeImpls.add(Rede);
                        AllSitzungen.add(Sitzung);

                    }
                }
                String firstTD = null;
                String secondTD = null;
                Boolean Schalter = true;
                NodeList Anlagen = document.getElementsByTagName("anlagen-text");
                for (int i = 0; i < Anlagen.getLength(); i++) {
                    Node iternode = Anlagen.item(i);
                    if (iternode.getNodeType() == Node.ELEMENT_NODE) {
                        if (iternode.getAttributes().getNamedItem("anlagen-typ").getTextContent().trim().equals("Entschuldigte Abgeordnete")) {
                            NodeList table = iternode.getChildNodes();
                            for (int j = 0; j < table.getLength(); j++) {
                                Node jiternode = table.item(j);
                                if (jiternode.getNodeType() == Node.ELEMENT_NODE && jiternode.getNodeName().equals("table")) {
                                    NodeList tbody = jiternode.getChildNodes();
                                    for (int k = 0; k < tbody.getLength(); k++) {
                                        Node kiternode = tbody.item(k);
                                        if (kiternode.getNodeType() == Node.ELEMENT_NODE && kiternode.getNodeName().equals("tbody")) {
                                            NodeList tbody_inside = kiternode.getChildNodes();
                                            for (int l = 0; l < tbody_inside.getLength(); l++) {
                                                Node liternode = tbody_inside.item(l);
                                                if (liternode.getNodeType() == Node.ELEMENT_NODE && liternode.getNodeName().equals("tr")) {
                                                    NodeList tr_inside = liternode.getChildNodes();
                                                    for (int m = 0; m < tr_inside.getLength(); m++) {
                                                        Node miternode = tr_inside.item(m);
                                                        if (miternode.getNodeType() == Node.ELEMENT_NODE) {
                                                            if (Schalter) {
                                                                firstTD = miternode.getTextContent().trim();
                                                                Schalter = !Schalter;
                                                            } else {
                                                                secondTD = miternode.getTextContent().trim();
                                                                Schalter = !Schalter;
                                                            }
                                                            Entschuldigte_Abgeordnete.put(firstTD, secondTD);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Plenarprotokoll.delete();
                //MyMongoDBHandler.getCollection("Plenarprotokoll").findOneAndUpdate(new org.bson.Document("PlenarprotokollNummer", Integer.parseInt(Plenarprotokoll_document.getString("PlenarprotokollNummer"))), new org.bson.Document("$set", new org.bson.Document("Verarbeitet", true)));
            }
        } catch (Exception e) {
            System.out.println("Fehler unterlaufen" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Erstellt Tagespunktobjekte und liest Attribute ein, danach wird der Sitzung alle Tagesordnungen hinzugefügt und dem TagespunktObjekt auch die Sitzung
     * @param document
     * @param Sitzung
     * @param AlleTagesordnungspunkte
     * @return
     */
    public Sitzung_Impl TagesordnungspunktObject_Einlese(Document document, Sitzung_Impl Sitzung, Set<Tagesordnungspunkt_Impl> AlleTagesordnungspunkte){
        NodeList Tagesordnungspunkte_Nodes = document.getElementsByTagName("tagesordnungspunkt");
        for (int i = 0; i < Tagesordnungspunkte_Nodes.getLength(); i++) {
            Node iternode = Tagesordnungspunkte_Nodes.item(i);
            if (iternode.getNodeType()==Node.ELEMENT_NODE){
                Tagesordnungspunkt_Impl Tagesordnungspunkt = new Tagesordnungspunkt_Impl();
                //Tagesordnungspunkt.setMongoID(MongoID_counter_Tagesordnungspunkt);
                Tagesordnungspunkt.setMongoLabel("TAGESORDNUNGSPUNKT");
                MongoID_counter_Tagesordnungspunkt++;
                Tagesordnungspunkt.setSitzung(Sitzung);
                //System.out.println(iternode.getAttributes().getNamedItem("top-id").getTextContent().trim());
                String Tagesordnungspunkt_Name = iternode.getAttributes().getNamedItem("top-id").getTextContent().trim();
                Tagesordnungspunkt.setTagesordnungsname(Tagesordnungspunkt_Name);
                StringBuilder T_Nas = new StringBuilder();
                NodeList innerhalb_Tagesordnungspunkt_Node = iternode.getChildNodes();
                for (int j = 0; j < innerhalb_Tagesordnungspunkt_Node.getLength(); j++) {
                    Node jiternode = innerhalb_Tagesordnungspunkt_Node.item(j);
                    if (jiternode.getNodeType()==Node.ELEMENT_NODE && jiternode.getNodeName().equals("rede")){
                        //System.out.println(jiternode.getNodeName());
                        //System.out.println(jiternode.getAttributes().getNamedItem("id").getTextContent().trim());
                        String Rede_ID = jiternode.getAttributes().getNamedItem("id").getTextContent().trim();
                        Tagesordnungspunkt.AddRede_IDs(Rede_ID);
                    } else if (jiternode.getNodeType()==Node.ELEMENT_NODE && jiternode.getNodeName().equals("p")) {
                        if (jiternode.getAttributes().getNamedItem("klasse") !=null){
                            if (jiternode.getAttributes().getNamedItem("klasse").getTextContent().trim().equals("T_NaS")){
                                //System.out.println(jiternode.getTextContent().trim());
                                T_Nas.append(jiternode.getTextContent().trim()+"\n");
                                //Tagesordnungspunkt.setTOP_Beschreibung(jiternode.getTextContent().trim());
                            } else if (jiternode.getAttributes().getNamedItem("klasse").getTextContent().trim().equals("T_Drs")) {
                                //System.out.println(jiternode.getTextContent().trim());
                            }
                        }
                    }
                    Tagesordnungspunkt.setTOP_Beschreibung(T_Nas.toString());
                }
                Sitzung.AddTagesordnungspunkt(Tagesordnungspunkt);
                this.AllTagesordnungspunkte.add(Tagesordnungspunkt);
            }
        }
        return Sitzung;
    }

    public Set<Rede_Impl> getAllRedeClass() {
        return this.allRedeImpls;
    }

    public void AddAllRedeClass(Rede_Impl Rede) {
        this.allRedeImpls.add(Rede);
    }
    public Map<String,String> getEntschuldigte_Abgeordnete(){
        return this.Entschuldigte_Abgeordnete;
    }
    public void AddEntschuldigte_Abgeordnete(String firstTD, String secondTD){
        this.Entschuldigte_Abgeordnete.put(firstTD,secondTD);
    }

    public Set<Sitzung_Impl> getAllSitzungen() {
        return this.AllSitzungen;
    }

    public void AddSitzungen(Sitzung_Impl Sitzung) {
        this.AllSitzungen.add(Sitzung);
    }

    public Set<Tagesordnungspunkt_Impl> getAllTagesordnungspunkte() {
        return this.AllTagesordnungspunkte;
    }

    public void AddTagesordnungspunkte(Tagesordnungspunkt_Impl Tagesordnungspunkt) {
        this.AllTagesordnungspunkte.add(Tagesordnungspunkt);
    }

    public List<RedeAuszug_Impl> getAllRedeAuszuge(){
        return this.AllRedeAuszuge;
    }

}
