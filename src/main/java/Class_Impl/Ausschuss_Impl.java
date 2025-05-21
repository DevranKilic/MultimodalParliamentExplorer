package Class_Impl;

import Class_Interfaces.Ausschuss;

import java.util.*;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Ausschuss_Impl implements Ausschuss {
    private int MongoID;
    private String MongoLabel;
    private String Ausschuss_Art;
    private String Beschreibung;
    private Map<Integer, List<Integer>> WP_Nr_Abgeorndeten_IDs;

    public Ausschuss_Impl(){
        this.MongoID = hashCode();
        this.WP_Nr_Abgeorndeten_IDs = new HashMap<>();
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

    public String getAusschuss_Art() {
        return Ausschuss_Art;
    }

    public void setAusschuss_Art(String ausschuss_Art) {
        Ausschuss_Art = ausschuss_Art;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        Beschreibung = beschreibung;
    }

    public Map<Integer, List<Integer>> getWP_Nr_Abgeorndeten_IDs() {
        return WP_Nr_Abgeorndeten_IDs;
    }


    public void AddWP_mit_Abgeordneten(int Wahlperiode_Nummer, int Abgeordneten_ID){
        Boolean found = false;
        for (Integer WP_Iter : WP_Nr_Abgeorndeten_IDs.keySet()) {
            if (WP_Iter == Wahlperiode_Nummer){
                this.WP_Nr_Abgeorndeten_IDs.get(WP_Iter).add(Abgeordneten_ID);
                found = true;
            }
        }
        if (!found){
            List<Integer> Abgeordneten_IDs = new ArrayList<>();
            Abgeordneten_IDs.add(Abgeordneten_ID);
            this.WP_Nr_Abgeorndeten_IDs.put(Wahlperiode_Nummer,Abgeordneten_IDs);
        }
    }
}
