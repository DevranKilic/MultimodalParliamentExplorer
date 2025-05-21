package Class_Impl;

import Class_MongoDB_Impl.RedeAuszug_MongoDB_Impl;
import Class_Interfaces.Rede;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Rede_Impl implements Rede {

    private int MongoID;
    private String MongoLabel;
    private Abgeordneter_Impl Redner;
    private int WahlperiodeNummer;
    private int Sitzungsnummer;
    private String Ort;
    private Date Datum;
    private String Rede_ID;
    private int Redner_ID;
    private String Vorname;
    private String Nachname;
    private String Fraktion;
    private String Rolla_lang;
    private String Rolle_kurz;
    private String Rede;
    private String Tagesordnungspunkt;
    private String TOP_Beschreibung;
    private String Rede_mit_Kommentaren;
    private List<Integer> RedeAuszugMongoIDs = new ArrayList<>();
    private List<RedeAuszug_MongoDB_Impl> RedeAuszug_Objekte = new ArrayList<>();
    private String RedeText_NoEdit;
    private String RedeText_WithEdit;

    private String VideoBase64;
    private String VideoMimeType;


    public void setMongoID(){

        //this.MongoID = MongoID;
        this.MongoID = hashCode();
    }

    public int getMongoID(){
        return MongoID;
    }

    public void setMongoLabel(String MongoLabel){
        this.MongoLabel = MongoLabel;
    }

    public String getMongoLabel(){
        return MongoLabel;
    }

    public void setRede(String Rede){
        this.Rede = Rede;
    }

    public String getText() {
        return this.Rede;
    }

    public void setRedner(Abgeordneter_Impl Redner){
        this.Redner = Redner;
    }

    public Abgeordneter_Impl getRedner() {
        return this.Redner;
    }

    public void setDatum(Date Datum){
        this.Datum = Datum;
    }

    @Override
    public Date getDate() {
        return this.Datum;
    }


    public String getRede_ID() {
        return Rede_ID;
    }

    public void setRede_ID(String rede_ID) {
        Rede_ID = rede_ID;
    }

    public int getRedner_ID() {
        return Redner_ID;
    }

    public void setRedner_ID(int redner_ID) {
        Redner_ID = redner_ID;
    }

    public String getVorname() {
        return this.Vorname;
    }

    public void setVorname(String vorname) {
        this.Vorname = vorname;
    }

    public String getNachname() {
        return this.Nachname;
    }

    public void setNachname(String nachname) {
        this.Nachname = nachname;
    }

    public String getFraktion() {
        return Fraktion;
    }

    public void setFraktion(String fraktion) {
        this.Fraktion = fraktion;
    }

    public int getWahlperiodeNummer() {
        return WahlperiodeNummer;
    }

    public void setWahlperiodeNummer(int wahlperiodeNummer) {
        this.WahlperiodeNummer = wahlperiodeNummer;
    }

    public int getSitzungsnummer() {
        return this.Sitzungsnummer;
    }

    public void setSitzungsnummer(int sitzungsnummer) {
        this.Sitzungsnummer = sitzungsnummer;
    }

    public String getOrt() {
        return this.Ort;
    }

    public void setOrt(String ort) {
        this.Ort = ort;
    }

    public String getRolla_lang() {
        return Rolla_lang;
    }

    public void setRolla_lang(String rolla_lang) {
        Rolla_lang = rolla_lang;
    }

    public String getRolle_kurz() {
        return Rolle_kurz;
    }

    public void setRolle_kurz(String rolle_kurz) {
        Rolle_kurz = rolle_kurz;
    }

    public String getRede_mit_Kommentaren() {
        return Rede_mit_Kommentaren;
    }

    public void setRede_mit_Kommentaren(String rede_mit_Kommentaren) {
        Rede_mit_Kommentaren = rede_mit_Kommentaren;
    }

    public String getTagesordnungspunkt() {
        return Tagesordnungspunkt;
    }

    public void setTagesordnungspunkt(String tagesordnungspunkt) {
        Tagesordnungspunkt = tagesordnungspunkt;
    }

    public String getTOP_Beschreibung() {
        return TOP_Beschreibung;
    }

    public void setTOP_Beschreibung(String TOP_Beschreibung) {
        this.TOP_Beschreibung = TOP_Beschreibung;
    }


    public List<Integer> getRedeAuszugMongoIDs() {
        return RedeAuszugMongoIDs;
    }

    public void AddRedeAuszugMongoIDs(Integer redeAuszugMongoID) {
        this.RedeAuszugMongoIDs.add(redeAuszugMongoID);
    }

    public List<RedeAuszug_MongoDB_Impl> getRedeAuszug_Objekte() {
        return RedeAuszug_Objekte;
    }

    public void AddRedeAuszug_Objekte(RedeAuszug_MongoDB_Impl redeAuszug_Objekt) {
        this.RedeAuszug_Objekte.add(redeAuszug_Objekt);
    }

    public String getRedeText_NoEdit() {
        return RedeText_NoEdit;
    }

    public void setRedeText_NoEdit(String redeText_NoEdit) {
        RedeText_NoEdit = redeText_NoEdit;
    }

    public String getRedeText_WithEdit() {
        return RedeText_WithEdit;
    }

    public void setRedeText_WithEdit(String redeText_WithEdit) {
        RedeText_WithEdit = redeText_WithEdit;
    }

    public String getVideoBase64() {
        return VideoBase64;
    }

    public void setVideoBase64(String videoBase64) {
        VideoBase64 = videoBase64;
    }

    public String getVideoMimeType() {
        return VideoMimeType;
    }

    public void setVideoMimeType(String videoMimeType) {
        VideoMimeType = videoMimeType;
    }
}
