package JCas_Extraction_Interfaces;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.cas.CASException;
import org.texttechnologylab.annotation.type.AudioToken;

import java.util.List;

public interface AudioToken_Extraction_Interface {

    public List<AudioToken> getAllAudioToken() throws CASException;

    public String getAudioToken_Value(int Number) throws CASException;

    public int getAudioToken_Begin(int Number) throws CASException;

    public int getAudioToken_End(int Number) throws CASException;

    public float getAudioToken_TimeStart(int Number) throws CASException;

    public float getAudioToken_TimeEnd(int Number) throws CASException;

}
