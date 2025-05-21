package Class_Impl;

import Class_Interfaces.Wahlperiode;

import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 * Hiermit werden Wahlperioden erstellt, die auch alle einzigartig sind
 */
public class Wahlperiode_Impl implements Wahlperiode {
    //WahlperiodeNummer und Set von AbgeordnetenIDs
    private int MongoID;
    private String MongoLabel;
    private int WP_Nummer;
    private Set<Integer> Abgeordneten_IDs;

    public Wahlperiode_Impl(){
        this.MongoID = hashCode();
        this.Abgeordneten_IDs = new HashSet<>();
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

    public int getWP_Nummer() {
        return WP_Nummer;
    }

    public void setWP_Nummer(int WP_Nummer) {
        this.WP_Nummer = WP_Nummer;
    }

    public Set<Integer> getAbgeordneten_IDs() {
        return Abgeordneten_IDs;
    }

    public void AddAbgeordneten_ID(int abgeordneten_ID) {
        this.Abgeordneten_IDs.add(abgeordneten_ID);
    }
}
