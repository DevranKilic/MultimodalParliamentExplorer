package NLP;

import org.apache.uima.cas.CASException;
import org.bson.Document;

import java.util.List;

public interface JCasToDocument_Interface {

    public Document jCasToDocument(String View) throws CASException;

    public List<String> getAllTokensAsJSON(String View) throws CASException;

    public List<String> getAllSentencesAsJSON(String View) throws CASException;

    public List<String> getAllPOSAsJSON(String View) throws CASException;

    public List<String> getAllDependenciesAsJSON(String View) throws CASException;

    public List<String> getAllNamedEntitiesAsJSON(String View) throws CASException;

    public List<String> getAllAudioTokenJSON(String View) throws CASException;

    public List<String> getAllSentimentAsJSON(String View) throws CASException;

    public List<String> getAllTopicsAsJSON(String View) throws CASException;
}
