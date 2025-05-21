package JCas_Extraction_Interfaces;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import org.apache.uima.cas.CASException;

import java.util.List;

public interface NamedEntity_Extraction_Interface {

    public List<NamedEntity> getAllNamedEntity() throws CASException;

    public String getNamedEntity_Value(int Number) throws CASException;

    public int getNamedEntity_Begin(int Number) throws CASException;

    public int getNamedEntity_End(int Number) throws CASException;

    public String getNamedEntity_CoveredText(int Number) throws CASException;
}
