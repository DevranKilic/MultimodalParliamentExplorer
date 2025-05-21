package Class_Interfaces;

import Class_Impl.Sitzung_Impl;

import java.util.Set;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Tagesordnungspunkt extends MongoIdent {
    public String getTagesordnungsname();

    public Set<String> getRede_IDs();

    public Sitzung_Impl getSitzung();

    public String getTOP_Beschreibung();
}
