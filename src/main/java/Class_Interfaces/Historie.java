package Class_Interfaces;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Historie extends MongoIdent{

    public String getOldValue1();

    public String getNewValue1();

    public String getOldValue2();

    public String getNewValue2();

    public String getDate();

    public String getTime();
}
