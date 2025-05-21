package Class_Interfaces;

import Class_Impl.Institution_Impl;
import Class_Impl.Rede_Impl;
import Class_Impl.Wahlperioden_Impl;

import java.util.Date;
import java.util.Set;

/**
 * Interface besitzt alle getter Methoden die alle Arten von Implementation verfügen müssen.
 * Die lokale Haupt-Implementation, die MongoDB-Implementation oder z.B. SQL-Implementation sollten ermöglichen auf die Attribute zugreifen zu können.
 */
public interface Abgeordneter extends MongoIdent{
    public String getName();

    public String getVorname();

    public String getOrtzusatz();

    public String getAdel();

    public String getAnrede();

    public String getAkadTitel();

    public Date getGeburtsdatum();

    public String getGeburtsort();

    public Date getSterbedatum();

    public String getGeschlecht();

    public String getReligion();

    public String getBeruf();

    public String getVita();

   public int getID();

   public Date getHistorie_von();

    public Date getHistorie_bis();

    public String getGeburtsland();

    public String getFamilienstand();

    public String getPartei_kurz();

    public String getVeröffentlichungspflichtiges();

    public Set<Wahlperioden_Impl> getWahlperioden();

    public Set<Institution_Impl> getInstitutionen();

    public Set<Rede_Impl> getReden();

    public String getHq_picture();

    public String getPicture_description();
}
