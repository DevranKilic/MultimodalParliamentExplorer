package JCas_Extraction_Interfaces;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import org.apache.uima.cas.CASException;

import java.util.List;

public interface POS_Extraction_Interface {

    public List<POS> getAllPOS() throws CASException;

    public String getPOS_CoarseValue(int Number) throws CASException;

    public String getPOS_PosValue(int Number) throws CASException;

    public int getPOS_Begin(int Number) throws CASException;

    public int getPOS_End(int Number) throws CASException;

    public String getPOS_CoveredText(int Number) throws CASException;
}
