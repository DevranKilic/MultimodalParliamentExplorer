package NLP;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.hucompute.textimager.uima.type.GerVaderSentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.json.JSONObject;
import org.texttechnologylab.annotation.type.AudioToken;
import org.texttechnologylab.uima.type.Topic;

import java.util.List;



/**
 * Diese Klasse übernimmt die Aufgabe ein Document in ein JCas umzuwandeln.
 * Dabei werden im Document auf die einzelnen Annotationen zugegriffen und deren
 * linguistischen Merkmale einem JCas hinzugefügt.
 */

public class DocumentToJCas_Impl implements DocumentToJCas_Interface{

    private Document jCasDocument;
    private JCas jCas;

    public DocumentToJCas_Impl(Document jCasDocument) throws ResourceInitializationException, CASException {
        this.jCasDocument = jCasDocument;
        this.jCas = JCasFactory.createJCas();
    }

    public JCas getJCas_Converted() {
        tokenConverter();
        sentenceConverter();
        posConverter();
        namedEntityConverter();
        audioTokenConverter();
        sentimentConverter();
        topicConverter();
        return this.jCas;
    }

    public void tokenConverter() {
        List<String> listTokenString = (List<String>) jCasDocument.get("Token");

        for (String tokenString : listTokenString) {
            JSONObject tokenJSON = new JSONObject(tokenString);
            Token tokenNew = new Token(jCas);

            tokenNew.setBegin(tokenJSON.getInt("Begin"));
            tokenNew.setEnd(tokenJSON.getInt("End"));
            tokenNew.setText(tokenJSON.getString("Text"));

             /*
            System.out.println("Begin: " + tokenNew.getBegin());
            System.out.println("End: " + tokenNew.getEnd());
            System.out.println("Text: " + tokenNew.getText());
             */


            tokenNew.addToIndexes();
        }
    }

    public void sentenceConverter() {
        List<String> listSentenceString = (List<String>) jCasDocument.get("Sentence");

        for (String sentenceString : listSentenceString) {
            JSONObject sentenceJSON = new JSONObject(sentenceString);
            Sentence sentenceNew = new Sentence(jCas);

            sentenceNew.setBegin(sentenceJSON.getInt("Begin"));
            sentenceNew.setEnd(sentenceJSON.getInt("End"));
            //sentenceNew.setCoveredText(sentenceJSON.getString("CoveredText"));

             /*
            System.out.println("Begin: " + sentenceNew.getBegin());
            System.out.println("End: " + sentenceNew.getEnd());
            System.out.println("CoveredText: " + sentenceNew.getCoveredText());
             */

            //Token sentence_token = new Token(JCas);

            sentenceNew.addToIndexes();
        }
    }


    public void posConverter() {
        List<String> listPOSString = (List<String>) jCasDocument.get("POS");

        for (String posString : listPOSString) {
            JSONObject posJSON = new JSONObject(posString);
            POS posNew = new POS(jCas);

            posNew.setCoarseValue(posJSON.getString("CoarseValue"));
            posNew.setPosValue(posJSON.getString("PosValue"));
            posNew.setBegin(posJSON.getInt("Begin"));
            posNew.setEnd(posJSON.getInt("End"));
            //posNew.setCoveredText(posJSON.getString("CoveredText"));

              /*
            System.out.println("CoarseValue: " + posNew.getCoarseValue());
            System.out.println("PosValue: " + posNew.getPosValue());
            System.out.println("Begin: " + posNew.getBegin());
            System.out.println("End: " + posNew.getEnd());
            System.out.println("CoveredText: "+posNew.getCoveredText());
             */

            posNew.addToIndexes();
        }
    }

    public void namedEntityConverter() {
        List<String> listNamedEntityString = (List<String>) jCasDocument.get("NamedEntity");

        for (String namedEntityString : listNamedEntityString) {
            JSONObject namedEntityJSON = new JSONObject(namedEntityString);
            NamedEntity namedEntityNew = new NamedEntity(jCas);

            namedEntityNew.setValue(namedEntityJSON.getString("Value"));
            namedEntityNew.setBegin(namedEntityJSON.getInt("Begin"));
            namedEntityNew.setEnd(namedEntityJSON.getInt("End"));
            //namedEntityNew.setCoveredText(namedEntityJSON.getString("CoveredText"));

              /*
            System.out.println("Value: " + namedEntityNew.getValue());
            System.out.println("Begin: " + namedEntityNew.getBegin());
            System.out.println("End: " + namedEntityNew.getEnd());
            System.out.println("CoveredText", + namedEntityNew.getCoveredText());
             */

            namedEntityNew.addToIndexes();
        }
    }

    public void audioTokenConverter() {
        List<String> listAudioTokenString = (List<String>) jCasDocument.get("AudioToken");

        for (String audioTokenString : listAudioTokenString) {
            JSONObject audioTokenJSON = new JSONObject(audioTokenString);
            AudioToken audioTokenNew = new AudioToken(jCas);

            audioTokenNew.setValue(audioTokenJSON.getString("Value"));
            audioTokenNew.setBegin(audioTokenJSON.getInt("Begin"));
            audioTokenNew.setEnd(audioTokenJSON.getInt("End"));
            audioTokenNew.setTimeStart(audioTokenJSON.getFloat("TimeStart"));
            audioTokenNew.setTimeEnd(audioTokenJSON.getFloat("TimeEnd"));

            /*
            System.out.println("Value: " + audioTokenNew.getValue());
            System.out.println("Begin: " + audioTokenNew.getBegin());
            System.out.println("End: " + audioTokenNew.getEnd());
            System.out.println("TimeStart: "+audioTokenNew.getTimeStart());
            System.out.println("TimeEnd: "+audioTokenNew.getTimeEnd());
             */

            audioTokenNew.addToIndexes();
        }
    }


    public void sentimentConverter() {
        List<String> listSentimentString = (List<String>) jCasDocument.get("Sentiment");

        for (String sentimentString : listSentimentString) {
            JSONObject SentimentJSON = new JSONObject(sentimentString);
            GerVaderSentiment gerVaderSentimentNew = new GerVaderSentiment(jCas);

            gerVaderSentimentNew.setSentiment(SentimentJSON.getDouble("Sentiment"));
            gerVaderSentimentNew.setBegin(SentimentJSON.getInt("Begin"));
            gerVaderSentimentNew.setEnd(SentimentJSON.getInt("End"));
            //gerVaderSentimentNew.setCoveredText(SentimentJSON.getFloat("CoveredText"));

            /*
            System.out.println("Sentiment: "+gerVaderSentimentNew.getSentiment());
            System.out.println("Begin: " + gerVaderSentimentNew.getBegin());
            System.out.println("End: " + gerVaderSentimentNew.getEnd());
            System.out.println("CoveredText: " + gerVaderSentimentNew.getCoveredText());
             */

            gerVaderSentimentNew.addToIndexes();
        }

    }

    public void topicConverter(){
        List<String> ListTopicString = (List<String>) jCasDocument.get("Topic");

        for (String topicString : ListTopicString) {
            JSONObject topicJASON = new JSONObject(topicString);
            CategoryCoveredTagged newTopic = new CategoryCoveredTagged(jCas);

            newTopic.setValue(topicJASON.getString("Topic"));
            newTopic.setBegin(topicJASON.getInt("Begin"));
            newTopic.setEnd(topicJASON.getInt("End"));
            newTopic.setScore(topicJASON.getDouble("Score"));

             /*
            System.out.println("Begin: " + newTopic.getBegin());
            System.out.println("End: " + newTopic.getEnd());
            System.out.println("Text: " + newTopic.getText());
             */


            newTopic.addToIndexes();
        }
    }
}



