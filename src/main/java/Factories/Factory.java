package Factories;
import Class_Impl.*;
import Einlese.MDB_Stammdaten_Einlese;
import Einlese.Plenarprotokoll_Einlese;
import Helper.Hilfsmethoden;
import database.MongoDBHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Factory {
    private Set<Abgeordneter_Impl> AlleAbgeordneten;
    private Set<Wahlperioden_Impl> AlleWahlperioden;
    private Set<Wahlkreis_Impl> AlleWahlkreise;
    private Set<Institution_Impl> AlleInstitutionen;
    private Set<Partei_Impl> AlleParteien;
    private Set<Fraktion_Impl> AlleFraktionen;
    private Set<Wahlperiode_Impl> AlleWahlperiode;
    private Set<Ausschuss_Impl> AlleAusschüsse;
    private List<RedeAuszug_Impl> AlleRedeAuszuge;

    private Set<Rede_Impl> AlleReden;
    private Map<String, String> Fehlende_Abgeordnete;
    private Set<Sitzung_Impl> AlleSitzungen;
    private Set<Tagesordnungspunkt_Impl> AlleTagesordnungspunkte;
    private MongoDBHandler MyMongoDBHandler;


    /**
     * Diese Factory ermöglicht den zentralen Zugriff auf alle Referenzen bezüglich der MDB Stammdaten und Plenarprotokollen.
     * Alle benötigten Referenzen sind hier deklariert und initialisiert worden
     * Es ist möglich hier mit den Referenzen zu arbeiten, jedoch wird empfohlen ein neues Objekt der Factory zu erstellen und von dort (z.B. Main)
     * aus wieder einzelnd auf die Referenzen zuzugreifen
     */
    public Factory(MongoDBHandler MyMongoDBHandler){
        this.MyMongoDBHandler = MyMongoDBHandler;

        /**
         * Hier werden die Refernzspeicher, welche aus dem MDB Stammdaten erstellt werden konnten entnommen.
         */
        MDB_Stammdaten_Einlese MDB_Einleseobjekt = new MDB_Stammdaten_Einlese();
        MDB_Einleseobjekt.MDB_Einlesen();
        this.AlleAbgeordneten = MDB_Einleseobjekt.getAllAbgeordnetenClass();
        this.AlleWahlperioden = MDB_Einleseobjekt.getAllWahlperiodeClass();
        this.AlleWahlkreise = MDB_Einleseobjekt.getAllWahlkreisClass();
        this.AlleInstitutionen = MDB_Einleseobjekt.getAllInsitutuíonenClass();
        Hilfsmethoden.mpPicturesAttributHinzufugen(this.AlleAbgeordneten);
        /**
         * Mithilfe Einlese Operationen können aus den bestehenden Objekten weitere Objekte aus anderen Klassen erstellt werden
         * und zudem werden beziehungen von Objekten zueinander hinzugefügt
         */
        this.AlleParteien = Hilfsmethoden.MDB_Stammdaten_Einlese_Operation1(AlleAbgeordneten);
        this.AlleFraktionen = Hilfsmethoden.MDB_Stammdaten_Einlese_Operation2(AlleAbgeordneten);
        this.AlleWahlperiode = Hilfsmethoden.MDB_Stammdaten_Einlese_Operation3(AlleAbgeordneten);
        this.AlleAusschüsse = Hilfsmethoden.MDB_Stammdaten_Einlese_Operation4(AlleAbgeordneten);
        Hilfsmethoden.AbgeordneteBildBase64(AlleAbgeordneten);
        System.out.println("MDB Stammdaten Eingelesen");


        /**
         * Hier werden die Refernzspeicher, welche aus dem Plenarprotokollen erstellt werden konnten entnommen.
         */
        Plenarprotokoll_Einlese Plenarprotokoll_Einlese = new Plenarprotokoll_Einlese(MyMongoDBHandler);
        Plenarprotokoll_Einlese.Plenar_Einlesen();
        this.AlleReden = Plenarprotokoll_Einlese.getAllRedeClass();
        this.Fehlende_Abgeordnete = Plenarprotokoll_Einlese.getEntschuldigte_Abgeordnete();
        this.AlleSitzungen = Plenarprotokoll_Einlese.getAllSitzungen();
        this.AlleTagesordnungspunkte = Plenarprotokoll_Einlese.getAllTagesordnungspunkte();
        this.AlleRedeAuszuge = Plenarprotokoll_Einlese.getAllRedeAuszuge();

        /**
         * Mithilfe Einlese Operationen können aus den bestehenden Objekten weitere Objekte aus anderen Klassen erstellt werden
         * und zudem werden beziehungen von Objekten zueinander hinzugefügt
         */
        Hilfsmethoden.Pleanarprotokoll_Einlese_Operation1(AlleReden, AlleAbgeordneten);
        Hilfsmethoden.Pleanarprotokoll_Einlese_Operation2(AlleAbgeordneten, Fehlende_Abgeordnete);
        Hilfsmethoden.Pleanarprotokoll_Einlese_Operation3(AlleTagesordnungspunkte,AlleReden);
        System.out.println("Plenarprotokolle Eingelesen");
    }

    public Set<Abgeordneter_Impl> getAlleAbgeordneten() {
        return AlleAbgeordneten;
    }

    public void setAlleAbgeordneten(Set<Abgeordneter_Impl> alleAbgeordneten) {
        AlleAbgeordneten = alleAbgeordneten;
    }

    public Set<Wahlperioden_Impl> getAlleWahlperioden() {
        return AlleWahlperioden;
    }

    public void setAlleWahlperioden(Set<Wahlperioden_Impl> alleWahlperioden) {
        AlleWahlperioden = alleWahlperioden;
    }

    public Set<Wahlkreis_Impl> getAlleWahlkreise() {
        return AlleWahlkreise;
    }

    public void setAlleWahlkreise(Set<Wahlkreis_Impl> alleWahlkreise) {
        AlleWahlkreise = alleWahlkreise;
    }


    public Set<Rede_Impl> getAlleReden() {
        return AlleReden;
    }

    public void setReden(Set<Rede_Impl> alleReden) {
        AlleReden = alleReden;
    }


    public Map<String,String> getFehlende_Abgeordnete() {
        return Fehlende_Abgeordnete;
    }

    public void setFehlende_Abgeordnete(Map<String,String> fehlende_Abgeordnete) {
        Fehlende_Abgeordnete = fehlende_Abgeordnete;
    }


    public Set<Institution_Impl> getAlleInstitutionen() {
        return AlleInstitutionen;
    }

    public void setAlleInstitutionen(Set<Institution_Impl> alleInstitutionen) {
        AlleInstitutionen = alleInstitutionen;
    }

    public Set<Partei_Impl> getAlleParteien() {
        return AlleParteien;
    }

    public void setAlleParteien(Set<Partei_Impl> alleParteien) {
        AlleParteien = alleParteien;
    }

    public Set<Fraktion_Impl> getAlleFraktionen() {
        return AlleFraktionen;
    }

    public void setAlleFraktionen(Set<Fraktion_Impl> alleFraktionen) {
        AlleFraktionen = alleFraktionen;
    }

    public Set<Wahlperiode_Impl> getAlleWahlperiode() {
        return AlleWahlperiode;
    }

    public void setAlleWahlperiode(Set<Wahlperiode_Impl> alleWahlperiode) {
        AlleWahlperiode = alleWahlperiode;
    }

    public Set<Ausschuss_Impl> getAlleAusschüsse() {
        return AlleAusschüsse;
    }

    public void setAlleAusschüsse(Set<Ausschuss_Impl> alleAusschüsse) {
        AlleAusschüsse = alleAusschüsse;
    }

    public Set<Sitzung_Impl> getAlleSitzungen() {
        return AlleSitzungen;
    }

    public void setAlleSitzungen(Set<Sitzung_Impl> alleSitzungen) {
        AlleSitzungen = alleSitzungen;
    }

    public Set<Tagesordnungspunkt_Impl> getAlleTagesordnungspunkte() {
        return AlleTagesordnungspunkte;
    }

    public void setAlleTagesordnungspunkte(Set<Tagesordnungspunkt_Impl> alleTagesordnungspunkte) {
        AlleTagesordnungspunkte = alleTagesordnungspunkte;
    }

    public List<RedeAuszug_Impl> getAlleRedeAuszuge() {
        return AlleRedeAuszuge;
    }

    public void setAlleRedeAuszuge(List<RedeAuszug_Impl> alleRedeAuszuge) {
        AlleRedeAuszuge = alleRedeAuszuge;
    }
}
