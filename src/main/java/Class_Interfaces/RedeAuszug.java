package Class_Interfaces;

import Class_MongoDB_Impl.Abgeordneter_MongoDB_Impl;
import Class_MongoDB_Impl.Fraktion_MongoDB_Impl;


/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface RedeAuszug extends MongoIdent {

    public String getRedeAuszug();

    public Boolean getIsKommentar();

    public int getAbgeordneterMongoID();

    public int getFraktionMongoID();

    public Abgeordneter_MongoDB_Impl getAbgeordneter();


    public Fraktion_MongoDB_Impl getFraktion();

}
