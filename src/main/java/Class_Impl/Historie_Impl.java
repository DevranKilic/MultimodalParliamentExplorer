package Class_Impl;


import Helper.Hilfsmethoden;
import Class_Interfaces.Historie;

import java.util.Date;


/**
 * Diese Klasse beschreibt die Attribute und Methoden die für alle anderen Arten von Implementationen zur verfügung stehen sollten.
 * Mit dieser Klasse kann man lokal am effektivsten Arbeiten.
 */
public class Historie_Impl implements Historie {
    private String OldValue1;
    private String NewValue1;
    private String OldValue2;
    private String NewValue2;
    private String Date;
    private String Time;

    private String MongoLabel = "HISTORIE";
    private int MongoID;


    public Historie_Impl(){

        this.MongoID = hashCode();

        java.util.Date DateObjekt = new Date();
        this.Date = Hilfsmethoden.Date_zu_String(DateObjekt);
        this.Time = Hilfsmethoden.Uhrzeit_zu_String(DateObjekt);
    }



    public String getOldValue1() {
        return OldValue1;
    }

    public void setOldValue1(String oldValue1) {
        OldValue1 = oldValue1;
    }

    public String getNewValue1() {
        return NewValue1;
    }

    public void setNewValue1(String newValue1) {
        NewValue1 = newValue1;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMongoLabel() {
        return MongoLabel;
    }

    public int getMongoID() {
        return MongoID;
    }

    public void setMongoID(int mongoID) {
        MongoID = mongoID;
    }

    public String getOldValue2() {
        return OldValue2;
    }

    public void setOldValue2(String oldValue2) {
        OldValue2 = oldValue2;
    }

    public String getNewValue2() {
        return NewValue2;
    }

    public void setNewValue2(String newValue2) {
        NewValue2 = newValue2;
    }
}
