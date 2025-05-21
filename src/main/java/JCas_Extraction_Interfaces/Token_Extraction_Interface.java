package JCas_Extraction_Interfaces;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.cas.CASException;

import java.util.List;

public interface Token_Extraction_Interface {

    public List<Token> getAllToken() throws CASException;

    public int getToken_Begin(int Number) throws CASException;

    public int getToken_End(int Number) throws CASException;

    public String getToken_Text(int Number) throws CASException;
}
