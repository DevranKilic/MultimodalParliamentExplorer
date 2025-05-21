package Class_Impl;

import Class_Interfaces.Sitzung;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Sitzung_Impl implements Sitzung {
    private int MongoID;
    private String MongoLabel;
    private int WP_Nummer;
    private int Sitzungsnr;
    private String Sitzungs_Ort;
    private LocalTime Sitzungsbeginn;
    private LocalTime Sitzungsende;
    private Date Sitzungsdatum;
    private Set<Tagesordnungspunkt_Impl> Tagesordnungspunkte;


    public void setMongoID(int MongoID){
        this.MongoID = MongoID;
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

    public Sitzung_Impl(){
        this.MongoID = hashCode();
        this.Tagesordnungspunkte = new HashSet<>();
    }

    public int getWP_Nummer() {
        return WP_Nummer;
    }

    public void setWP_Nummer(int WP_Nummer) {
        this.WP_Nummer = WP_Nummer;
    }

    public int getSitzungsnr() {
        return Sitzungsnr;
    }

    public void setSitzungsnr(int sitzungsnr) {
        Sitzungsnr = sitzungsnr;
    }

    public String getSitzungs_Ort() {
        return Sitzungs_Ort;
    }

    public void setSitzungs_Ort(String sitzungs_Ort) {
        Sitzungs_Ort = sitzungs_Ort;
    }

    public Date getSitzungsdatum() {
        return Sitzungsdatum;
    }

    public void setSitzungsdatum(Date sitzungsdatum) {
        Sitzungsdatum = sitzungsdatum;
    }

    public Set<Tagesordnungspunkt_Impl> getTagesordnungspunkte() {
        return Tagesordnungspunkte;
    }

    public void AddTagesordnungspunkt(Tagesordnungspunkt_Impl Tagesordnungspunkt) {
        this.Tagesordnungspunkte.add(Tagesordnungspunkt);
    }

    public LocalTime getSitzungsbeginn() {
        return Sitzungsbeginn;
    }

    public void setSitzungsbeginn(LocalTime sitzungsbeginn) {
        Sitzungsbeginn = sitzungsbeginn;
    }

    public LocalTime getSitzungsende() {
        return Sitzungsende;
    }

    public void setSitzungsende(LocalTime sitzungsende) {
        Sitzungsende = sitzungsende;
    }
}
