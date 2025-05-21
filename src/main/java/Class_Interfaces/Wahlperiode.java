package Class_Interfaces;

import java.util.Set;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Wahlperiode extends MongoIdent {

    public int getWP_Nummer();

    //public Date getStartDate();

    //public Date getEndDate();

    public Set<Integer> getAbgeordneten_IDs();



}
