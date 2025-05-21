package Class_Impl;

import Class_Interfaces.Fraktion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Fraktion_Impl implements Fraktion {
    private int MongoID;
    private String MongoLabel;
    private String Name;
    //Enthält die AbgeordnetenID und die Wahlperiodenen in denen er Teil dieser Fraktion war
    private Map<Integer, Set<Integer>> Abgeordneter_ID_WP_Nr;

    public Fraktion_Impl(){
        this.MongoID = hashCode();
        this.Abgeordneter_ID_WP_Nr = new HashMap<>();
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Map<Integer, Set<Integer>> getAbgeordneter_ID_WP_Nr() {
        return Abgeordneter_ID_WP_Nr;
    }

    public void AddNeuenAbgeordneten(int Abgeordneter_ID, int Wahlperiode_Nummer) {
        Set<Integer> Wahlperiode_Nummern = new HashSet<>();
        Wahlperiode_Nummern.add(Wahlperiode_Nummer);
        this.Abgeordneter_ID_WP_Nr.put(Abgeordneter_ID, Wahlperiode_Nummern);
    }

    public void AddWahlperiode(int Abgeordneter_ID, int Wahlperiode_Nummer){
        for (Integer iter_IDs : Abgeordneter_ID_WP_Nr.keySet()) {
            if (iter_IDs == Abgeordneter_ID){
                this.Abgeordneter_ID_WP_Nr.get(iter_IDs).add(Wahlperiode_Nummer);
                return;
            }
        }
    }
}