package Class_Interfaces;

import Class_Impl.Wahlperioden_Impl;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Wahlkreis extends MongoIdent {
    public int getNumber();

    public String getWKR_Name();

    public String getWKR_Land();

    public String getWKR_Liste();

    public Wahlperioden_Impl getWahlperiode();
}
