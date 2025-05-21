package Class_Interfaces;

import Class_Impl.Abgeordneter_Impl;

import java.util.Set;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Partei extends MongoIdent {

    public String getName();

    public Set<Abgeordneter_Impl> getMitglieder();
}
