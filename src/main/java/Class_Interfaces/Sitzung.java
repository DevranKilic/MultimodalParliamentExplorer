package Class_Interfaces;

import Class_Impl.Tagesordnungspunkt_Impl;

import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Sitzung extends MongoIdent {

    public int getWP_Nummer();

    public int getSitzungsnr();

    public String getSitzungs_Ort();

    public Date getSitzungsdatum();

    public Set<Tagesordnungspunkt_Impl> getTagesordnungspunkte();

    public LocalTime getSitzungsbeginn();

    public LocalTime getSitzungsende();

}
