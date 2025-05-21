package Class_Interfaces;

import Class_Impl.Abgeordneter_Impl;

import java.util.Date;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Rede extends MongoIdent {
    public String getText();
    public Abgeordneter_Impl getRedner();
    public Date getDate();

    public String getRede_ID();

    public int getRedner_ID();

    public String getVorname();

    public String getNachname();

    public String getFraktion();

    public int getWahlperiodeNummer();

    public int getSitzungsnummer();

    public String getOrt();

    public String getRolla_lang();

    public String getRolle_kurz();

    public String getTOP_Beschreibung();

    public String getRede_mit_Kommentaren();

    public String getTagesordnungspunkt();
}
