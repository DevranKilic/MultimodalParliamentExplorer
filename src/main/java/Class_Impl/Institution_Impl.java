package Class_Impl;

import Class_Interfaces.Institution;

import java.util.Date;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Institution_Impl implements Institution {

    private int MongoID;
    private String MongoLabel;
    private String Insart_Lang;
    private String Ins_Lang;
    private Date MDBins_Von;
    private Date MDBins_Bis;
    private String Fkt_lang;
    private Date Fktins_Von;
    private Date Fktins_Bis;
    private Wahlperioden_Impl Wahlperiode;

    public Institution_Impl(){
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

    public String getInsart_Lang() {
        return Insart_Lang;
    }

    public void setInsart_Lang(String insart_Lang) {
        this.Insart_Lang = insart_Lang;
    }

    public String getIns_Lang() {
        return Ins_Lang;
    }

    public void setIns_Lang(String ins_Lang) {
        this.Ins_Lang = ins_Lang;
    }

    public Date getMDBins_Von() {
        return MDBins_Von;
    }

    public void setMDBins_Von(Date MDBins_Von) {
        this.MDBins_Von = MDBins_Von;
    }


    public Date getMDBins_Bis() {
        return MDBins_Bis;
    }

    public void setMDBins_Bis(Date MDBins_Bis) {
        this.MDBins_Bis = MDBins_Bis;
    }

    public String getFkt_lang() {
        return Fkt_lang;
    }

    public void setFkt_lang(String fkt_lang) {
        this.Fkt_lang = fkt_lang;
    }

    public Date getFktins_Von() {
        return Fktins_Von;
    }

    public void setFktins_Von(Date fktins_Von) {
        this.Fktins_Von = fktins_Von;
    }

    public Date getFktins_Bis() {
        return Fktins_Bis;
    }

    public void setFktins_Bis(Date fktins_Bis) {
        this.Fktins_Bis = fktins_Bis;
    }

    public Wahlperioden_Impl getWahlperiode() {
        return Wahlperiode;
    }

    public void setWahlperiode(Wahlperioden_Impl wahlperiode) {
        Wahlperiode = wahlperiode;
    }
}