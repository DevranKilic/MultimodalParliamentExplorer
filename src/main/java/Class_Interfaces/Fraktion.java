package Class_Interfaces;

import java.util.Map;
import java.util.Set;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Fraktion extends MongoIdent {

    public String getName();

    public Map<Integer, Set<Integer>> getAbgeordneter_ID_WP_Nr();


}
