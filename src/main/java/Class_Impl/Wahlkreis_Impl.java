package Class_Impl;

import Class_Interfaces.Wahlkreis;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Wahlkreis_Impl implements Wahlkreis {

    private int MongoID;
    private String MongoLabel;
    private int WKR_Nummer;
    private String WKR_Name;
    private String WKR_Land;
    private String WKR_Liste;
    private Wahlperioden_Impl Wahlperiode;


    public Wahlkreis_Impl(){
        this.MongoID = hashCode();
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

    public void setWKR_Nummer(int WKR_Nummer) {
        this.WKR_Nummer = WKR_Nummer;
    }

    public int getNumber() {
        return WKR_Nummer;
    }

    public String getWKR_Name() {
        return WKR_Name;
    }

    public void setWKR_Name(String WKR_Name) {
        this.WKR_Name = WKR_Name;
    }

    public String getWKR_Land() {
        return WKR_Land;
    }

    public void setWKR_Land(String WKR_Land) {
        this.WKR_Land = WKR_Land;
    }

    public String getWKR_Liste() {
        return WKR_Liste;
    }

    public void setWKR_Liste(String WKR_Liste) {
        this.WKR_Liste = WKR_Liste;
    }

    public Wahlperioden_Impl getWahlperiode() {
        return Wahlperiode;
    }

    public void setWahlperiode(Wahlperioden_Impl Wahlperiode) {
        this.Wahlperiode = Wahlperiode;
    }
}