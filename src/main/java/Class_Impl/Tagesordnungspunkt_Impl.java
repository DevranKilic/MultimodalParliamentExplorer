package Class_Impl;

import Class_Interfaces.Tagesordnungspunkt;

import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Tagesordnungspunkt_Impl implements Tagesordnungspunkt {
    private int MongoID;
    private String MongoLabel;
    private Sitzung_Impl Sitzung;
    private String Tagesordnungsname;
    private String TOP_Beschreibung;
    private Set<String> Rede_IDs;



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

    public Tagesordnungspunkt_Impl(){
        this.MongoID = hashCode();
        this.Rede_IDs = new HashSet<>();
    }

    public String getTagesordnungsname() {
        return Tagesordnungsname;
    }

    public void setTagesordnungsname(String tagesordnungsname) {
        Tagesordnungsname = tagesordnungsname;
    }

    public Set<String> getRede_IDs() {
        return Rede_IDs;
    }

    public void AddRede_IDs(String rede_ID) {
        this.Rede_IDs.add(rede_ID);
    }

    public Sitzung_Impl getSitzung() {
        return Sitzung;
    }

    public void setSitzung(Sitzung_Impl sitzung) {
        Sitzung = sitzung;
    }

    public String getTOP_Beschreibung() {
        return TOP_Beschreibung;
    }

    public void setTOP_Beschreibung(String TOP_Beschreibung) {
        this.TOP_Beschreibung = TOP_Beschreibung;
    }
}