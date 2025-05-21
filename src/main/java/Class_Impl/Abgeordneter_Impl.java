package Class_Impl;

import Class_Interfaces.Abgeordneter;

import java.util.*;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Abgeordneter_Impl implements Abgeordneter {


    private int MongoID;
    private String MongoLabel;
    private int ID;
    private String Nachname;
    private String Vorname;
    private String Ortszusatz;
    private String Praefix;
    private String Adel;
    private String Anrede_Titel;
    private String Akad_Titel;
    private Date Geburtsdatum;
    private String Geburtsort;
    private Date Sterbedatum;
    private String Geschlecht; //enum
    private String Religion;
    private String Beruf;
    private String Vita_kurz;
    private Date Historie_von;
    private Date Historie_bis;
    private String Geburtsland;
    private String Familienstand;
    private String Partei_kurz;
    private Partei_Impl Partei;
    private String Veröffentlichungspflichtiges;
    private int Fehltage_Sitzung;
    private Set<Wahlperioden_Impl> Wahlperioden;
    private Set<Institution_Impl> Institutionen;
    //private Set<AbstimmungClass> Abstimmungen;
    private Set<Rede_Impl> Reden;
    private String Bild_Base64;

    private String hq_picture;
    private String picture_description;
    private List<Historie_Impl> BildHistorie_Impl = new ArrayList<>();
    private List<Integer> BildHistorie_MongoIDs = new ArrayList<>();

    public Abgeordneter_Impl() {
        this.MongoID = hashCode();
        this.Wahlperioden = new HashSet<>();
        this.Institutionen = new HashSet<>();
        //this.Abstimmungen = new HashSet<>();
        this.Reden = new HashSet<>();
    }


    public void setMongoID(int MongoID) {
        this.MongoID = MongoID;
    }

    public int getMongoID() {
        return MongoID;
    }

    public void setMongoLabel(String MongoLabel) {
        this.MongoLabel = MongoLabel;
    }

    public String getMongoLabel() {
        return MongoLabel;
    }

    public void setName(String Nachname) {
        this.Nachname = Nachname;
    }

    public String getName() {
        return Nachname;
    }

    public void setVorname(String Vorname) {
        this.Vorname = Vorname;
    }

    public String getVorname() {
        return Vorname;
    }

    public void setOrtszusatz(String Ortzusatz) {
        this.Ortszusatz = Ortzusatz;
    }

    public String getOrtzusatz() {
        return Ortszusatz;
    }

    public void setAdel(String Adel) {
        this.Adel = Adel;
    }

    public String getAdel() {
        return Adel;
    }

    public void setAnrede_Titel(String Anrede_Titel) {
        this.Anrede_Titel = Anrede_Titel;
    }

    public String getAnrede() {
        return Anrede_Titel;
    }

    public void setAkad_Titel(String Akad_Titel) {
        this.Akad_Titel = Akad_Titel;
    }

    public String getAkadTitel() {
        return Akad_Titel;
    }

    public void setGeburtsdatum(Date Geburtsdatum) {
        this.Geburtsdatum = Geburtsdatum;
    }
    
    public Date getGeburtsdatum() {
        return Geburtsdatum;
    }

    public void setGeburtsort(String Geburtsort) {
        this.Geburtsort = Geburtsort;
    }

    public String getGeburtsort() {
        return Geburtsort;
    }

    public void setSterbedatum(Date Sterbedatum) {
        this.Sterbedatum = Sterbedatum;
    }

    public Date getSterbedatum() {
        return Sterbedatum;
    }

    public void setGeschlecht(String Geschlecht) {
        this.Geschlecht = Geschlecht;
    }

    public String getGeschlecht() {
        return Geschlecht;
    }

    public void setReligion(String Religion) {
        this.Religion = Religion;
    }

    public String getReligion() {
        return Religion;
    }

    public void setBeruf(String Beruf) {
        this.Beruf = Beruf;
    }

    public String getBeruf() {
        return Beruf;
    }

    public void setVita_kurz(String Vita_kurz) {
        this.Vita_kurz = Vita_kurz;
    }

    public String getVita() {
        return Vita_kurz;
    }

    public void setWahlperioden(Wahlperioden_Impl Wahlperiode) {
        this.Wahlperioden.add(Wahlperiode);
    }

    public Set<Wahlperioden_Impl> getWahlperioden() {
        return Wahlperioden;
    }

    public void setInstitutionen(Institution_Impl Institution) {
        this.Institutionen.add(Institution);
    }

    public Set<Institution_Impl> getInstitutionen() {
        return Institutionen;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPraefix() {
        return this.Praefix;
    }

    public void setPraefix(String praefix) {
        this.Praefix = praefix;
    }

    public Date getHistorie_von() {
        return Historie_von;
    }

    public void setHistorie_von(Date historie_von) {
        Historie_von = historie_von;
    }

    public Date getHistorie_bis() {
        return this.Historie_bis;
    }

    public void setHistorie_bis(Date historie_bis) {
        this.Historie_bis = historie_bis;
    }

    public String getGeburtsland() {
        return this.Geburtsland;
    }

    public void setGeburtsland(String geburtsland) {
        this.Geburtsland = geburtsland;
    }

    public String getFamilienstand() {
        return Familienstand;
    }

    public void setFamilienstand(String familienstand) {
        Familienstand = familienstand;
    }

    public String getPartei_kurz() {
        return Partei_kurz;
    }

    public void setPartei_kurz(String partei_kurz) {
        Partei_kurz = partei_kurz;
    }

    public String getVeröffentlichungspflichtiges() {
        return this.Veröffentlichungspflichtiges;
    }

    public void setVeröffentlichungspflichtiges(String veröffentlichungspflichtiges) {
        this.Veröffentlichungspflichtiges = veröffentlichungspflichtiges;
    }
    /*
    public Set<AbstimmungClass> getAbstimmungen() {
        return this.Abstimmungen;
    }

    public void AddAbstimmungen(AbstimmungClass Abstimmung) {
        this.Abstimmungen.add(Abstimmung);
    }

     */

    public Set<Rede_Impl> getReden() {
        return Reden;
    }

    public void AddRede(Rede_Impl Rede) {
        this.Reden.add(Rede);
    }

    public int getFehltage_Sitzung() {
        return Fehltage_Sitzung;
    }

    public void AddFehltag_Sitzung() {
        this.Fehltage_Sitzung++;
    }


    public Partei_Impl getPartei() {
        return Partei;
    }

    public void setPartei(Partei_Impl partei) {
        Partei = partei;
    }

    public String getBild_Base64() {
        return Bild_Base64;
    }

    public void setBild_Base64(String bild_Base64) {
        Bild_Base64 = bild_Base64;
    }

    public String getHq_picture() {
        return hq_picture;
    }

    public void setHq_picture(String hq_picture) {
        this.hq_picture = hq_picture;
    }

    public String getPicture_description() {
        return picture_description;
    }

    public void setPicture_description(String picture_description) {
        this.picture_description = picture_description;
    }

    public List<Historie_Impl> getBildHistorie_Impl() {
        return BildHistorie_Impl;
    }

    public void AddBildHistorie_Impl(Historie_Impl bildHistorie_Impl) {
        this.BildHistorie_Impl.add(bildHistorie_Impl);
    }

    public void setBildHistorie_Impl(List<Historie_Impl> BildHistorien){
        this.BildHistorie_Impl = BildHistorien;
    }

    public List<Integer> getBildHistorie_MongoIDs() {
        return BildHistorie_MongoIDs;
    }

    public void setBildHistorie_MongoIDs(List<Integer> bildHistorie_MongoIDs) {
        BildHistorie_MongoIDs = bildHistorie_MongoIDs;
    }
}
