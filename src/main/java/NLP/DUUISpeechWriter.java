package NLP;

import com.mongodb.MongoCommandException;
import database.MongoDBHandler;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.bson.BsonMaximumSizeExceededException;
import org.bson.Document;

public class DUUISpeechWriter extends JCasAnnotator_ImplBase {

    private MongoDBHandler myMongoDBHandler;

    /**
     *This method updates documents in the MongoDB wiht a JCas.
     * @param jCas A JCas that holds the results of the analysis.
     * @throws AnalysisEngineProcessException
     */
    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        this.myMongoDBHandler = new MongoDBHandler();

        try{
            DocumentMetaData metaData = DocumentMetaData.get(jCas);
            JCasToDocument_Impl redeTextConverter = new JCasToDocument_Impl(jCas);
            myMongoDBHandler.getCollection("Rede").findOneAndUpdate(new Document("MongoID", Integer.parseInt(metaData.getDocumentId())), new Document("$set", new Document("JCas_JSON_RedeText", redeTextConverter.jCasToDocument("RedeText"))));
        }catch (BsonMaximumSizeExceededException e){
            System.out.println("BsonMaximumSizeExceededException");
            System.out.println("BsonMaximumSizeExceededException");
            System.out.println("BsonMaximumSizeExceededException");
        }catch (MongoCommandException e){
            System.out.println("MongoCommandException");
            System.out.println("MongoCommandException");
            System.out.println("MongoCommandException");
        } catch (CASException e) {
            throw new RuntimeException(e);
        }
    }
}
