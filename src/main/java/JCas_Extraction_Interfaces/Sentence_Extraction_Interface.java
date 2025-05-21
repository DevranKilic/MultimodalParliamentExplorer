package JCas_Extraction_Interfaces;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.cas.CASException;

import java.util.List;

public interface Sentence_Extraction_Interface {

    public List<Sentence> getAllSentence() throws CASException;

    public int getSentence_Begin(int Number) throws CASException;

    public int getSentence_End(int Number) throws CASException;

    public String getSentence_CoveredText(int Number) throws CASException;

}
