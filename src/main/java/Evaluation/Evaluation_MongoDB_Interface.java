package Evaluation;

import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public interface Evaluation_MongoDB_Interface {

    public JCas toJCasFromDatabaseDocumentJSON(String MongoDBjCasFileName) throws ResourceInitializationException, CASException;

    public boolean getCheckJCasVideoAnalyseExists();

    public String getVideoName();

    public String getMongoID();

    public String getTranscript();
}
