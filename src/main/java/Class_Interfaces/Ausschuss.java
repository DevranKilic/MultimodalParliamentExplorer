package Class_Interfaces;

import java.util.List;
import java.util.Map;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Ausschuss extends MongoIdent{

    public String getAusschuss_Art();

    public String getBeschreibung();

    public Map<Integer, List<Integer>> getWP_Nr_Abgeorndeten_IDs();
}
