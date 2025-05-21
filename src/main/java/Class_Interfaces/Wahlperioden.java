package Class_Interfaces;

import Class_Impl.Institution_Impl;
import Class_Impl.Wahlkreis_Impl;

import java.util.Date;
import java.util.Set;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Wahlperioden extends MongoIdent {

    public int getNumber();

    public Date getStartDate();

    public Date getEndDate();

    public Wahlkreis_Impl getWahlkreis();

    public void AddInstitution(Institution_Impl Institution);

    public Set<Institution_Impl> getInstitutionen();

    public String getListe();

    public String getMandatsart();
}
