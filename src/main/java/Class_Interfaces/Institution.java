package Class_Interfaces;

import Class_Impl.Wahlperioden_Impl;

import java.util.Date;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Institution extends MongoIdent{

    public String getInsart_Lang();

    public String getIns_Lang();

    public Date getMDBins_Von();

    public Date getMDBins_Bis();

    public String getFkt_lang();

    public Date getFktins_Von();

    public Date getFktins_Bis();

    public Wahlperioden_Impl getWahlperiode();
}
