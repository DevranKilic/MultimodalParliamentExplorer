package JCas_Extraction_Interfaces;

import org.apache.uima.cas.CASException;
import org.hucompute.textimager.uima.type.GerVaderSentiment;
import org.texttechnologylab.uima.type.Sentiment;

import java.util.List;

public interface Sentiment_Extraction_Interface {

    public List<GerVaderSentiment> getAllSentiment() throws CASException;

    public double getSentiment_Sentiment(int Number) throws CASException;

    public int getSentiment_Begin(int Number) throws CASException;

    public int getSentiment_End(int Number) throws CASException;

    public String getSentiment_CoveredText(int Number) throws CASException;
}
