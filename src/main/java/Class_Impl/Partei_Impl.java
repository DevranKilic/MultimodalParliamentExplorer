package Class_Impl;

import Class_Interfaces.Partei;

import java.util.HashSet;
import java.util.Set;

/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Partei_Impl implements Partei {
    private int MongoID;
    private String MongoLabel;
    private String Name;
    private Set<Abgeordneter_Impl> Mitglieder;


    public Partei_Impl(){
        this.MongoID = hashCode();
        this.Mitglieder = new HashSet<>();
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

    public Set<Abgeordneter_Impl> getMitglieder() {
        return Mitglieder;
    }

    //Für Fraktionsklasse sinnvoll aber hier in Partei?
    //Partei_Kurz immer gleich, egal nach WP
    public Set<Abgeordneter_Impl> getMitglieder(int WP_Nummer){
        Set<Abgeordneter_Impl> Mitglieder_in_WP = new HashSet<>();
        for (Abgeordneter_Impl Abgeordneter : Mitglieder) {
            Set<Wahlperioden_Impl> Wahlperioden_von_iter_Abgeordneter = Abgeordneter.getWahlperioden();
            for (Wahlperioden_Impl Wahlperiode : Wahlperioden_von_iter_Abgeordneter) {
                if (Wahlperiode.getNumber()==WP_Nummer){
                    Mitglieder_in_WP.add(Abgeordneter);
                }
            }
        }
        return Mitglieder_in_WP;
    }

    public void AddMitglied(Abgeordneter_Impl Mitglied) {
        this.Mitglieder.add(Mitglied);
    }



}