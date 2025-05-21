package Class_Impl;

import Class_Interfaces.Wahlperioden;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;



/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 * Diese Klasse bezieht sich auf den Abgeordneten, wie aus der Sicht der MDB_Stammdaten XML
 * Diese Klasse ist keine eigenständige Wahlperiode, hier können mehrere Walhperioden zur gleichen Wahlperiode existieren
 */

public class Wahlperioden_Impl implements Wahlperioden {

    private int MongoID;
    private String MongoLabel;
    private int WP;
    private Date WP_Von;
    private Date WP_Bis;
    private String Liste;
    private String Mandatsart;
    private Wahlkreis_Impl Wahlkreis;
    private Set<Institution_Impl> Institutionen;

    public Wahlperioden_Impl(){
        this.MongoID = hashCode();
        this.Institutionen = new HashSet<>();
    }


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

    public int getNumber() {
        return WP;
    }

    public void setNumber(int WP){
        this.WP = WP;
    }

    public void setStartDate(Date WP_Von){
        this.WP_Von = WP_Von;
    }

    public Date getStartDate() {
        return WP_Von;
    }

    public void setEndDate(Date WP_Bis){
        this.WP_Bis = WP_Bis;
    }

    public Date getEndDate() {
        return WP_Bis;
    }


    public Wahlkreis_Impl getWahlkreis(){
        return this.Wahlkreis;
    }

    public void setWahlkreis(Wahlkreis_Impl Wahlkreis){
        this.Wahlkreis = Wahlkreis;
    }

    public void AddInstitution(Institution_Impl Institution){
        this.Institutionen.add(Institution);
    }

    public Set<Institution_Impl> getInstitutionen(){
        return Institutionen;
    }

    public String getListe() {
        return this.Liste;
    }

    public void setListe(String liste) {
        this.Liste = liste;
    }

    public String getMandatsart() {
        return this.Mandatsart;
    }

    public void setMandatsart(String mandatsart) {
        this.Mandatsart = mandatsart;
    }
}
