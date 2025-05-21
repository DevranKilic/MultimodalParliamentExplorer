package Class_Interfaces;

/**
 * Jedes Objekt einer Klasse soll eine eindeutige interne MongoID zugewiesen bekommen, damit dieses Objekt in der Mongo Datenbank leicht aufzufinden
 * und identifizieren ist.
 * Zusätzlich erlaubt ein Label dieses Objekt zu Beschreiben.
 */
public interface MongoIdent {

    public int getMongoID();

    public String getMongoLabel();
}
