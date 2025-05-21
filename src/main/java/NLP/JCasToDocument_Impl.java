package NLP;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.hucompute.textimager.uima.type.GerVaderSentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.annotation.type.AudioToken;
import org.texttechnologylab.uima.type.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse übernimmt die Aufgabe ein JCas in ein Document umzuwandeln.
 * Dabei werden die linguistischen Merkmale von verschiedenen Annotationen jeweils in einem Document
 * hinzugefügt und die Annotationen in einem gesammten Document zusammengefasst.
 */
public class JCasToDocument_Impl implements JCasToDocument_Interface {

    private JCas jCas;


    public JCasToDocument_Impl(JCas jCas) throws CASException {
        this.jCas = jCas;
    }


    public Document jCasToDocument(String View) throws CASException {
        Document JCasDocument = new Document();

        JCasDocument.put("Token",getAllTokensAsJSON(View));
        JCasDocument.put("Sentence", getAllSentencesAsJSON(View));
        JCasDocument.put("POS", getAllPOSAsJSON(View));
        JCasDocument.put("Dependency", getAllDependenciesAsJSON(View));
        JCasDocument.put("NamedEntity", getAllNamedEntitiesAsJSON(View));
        JCasDocument.put("AudioToken", getAllAudioTokenJSON(View));
        JCasDocument.put("Sentiment", getAllSentimentAsJSON(View));
        JCasDocument.put("Topic", getAllTopicsAsJSON(View));

        return JCasDocument;
    }


    public List<String> getAllTokensAsJSON(String View) throws CASException {
        List<String> allTokensDoc = new ArrayList<>();

        System.out.println("Token Dokumente werden geladen.");
        for (Token token : JCasUtil.select(jCas.getView(View), Token.class)) {
            /*
            //System.out.println("TokenID: "+token.getId());
            System.out.println("Begin: "+token.getBegin());
            System.out.println("End: "+token.getEnd());
            //System.out.println("CoveredText: "+token.getCoveredText());
            System.out.println("Text: "+token.getText());

             */

            Document iterTokenDoc = new Document();
            //iterTokenDoc.put("TokenID", token.getId());
            iterTokenDoc.put("Begin", token.getBegin());
            iterTokenDoc.put("End", token.getEnd());
            //iterTokenDoc.put("CoveredText", token.getCoveredText());
            iterTokenDoc.put("Text", token.getText());
            allTokensDoc.add(iterTokenDoc.toJson());
        }
        return allTokensDoc;
    }

    public List<String> getAllSentencesAsJSON(String View) throws CASException {
        List<String> allSentenceDocuments = new ArrayList<>();
        System.out.println("Sentence Dokumente werden geladen.");
        for (Sentence sentence : JCasUtil.select(jCas.getView(View), Sentence.class)) {
            /*
            //System.out.println("SentenceID: "+sentence.getId());
            System.out.println("Begin: "+sentence.getBegin());
            System.out.println("End: "+sentence.getEnd());
            System.out.println("CoveredText: "+sentence.getCoveredText());

             */

            Document iterSentenceDoc = new Document();
            //iterSentenceDoc.put("SentenceID", sentence.getId());
            iterSentenceDoc.put("Begin", sentence.getBegin());
            iterSentenceDoc.put("End", sentence.getEnd());
            iterSentenceDoc.put("CoveredText", sentence.getCoveredText());
            allSentenceDocuments.add(iterSentenceDoc.toJson());
        }
        return allSentenceDocuments;
    }

    public List<String> getAllPOSAsJSON(String View) throws CASException {
        List<String> allPOSDocuments = new ArrayList<>();
        System.out.println("POS Dokumente werden geladen.");
        for (POS pos : JCasUtil.select(jCas.getView(View), POS.class)) {
            /*
            System.out.println("TypeIndexID: "+pos.getTypeIndexID());
            System.out.println("CoarseValue: "+pos.getCoarseValue());
            System.out.println("PosValue: "+pos.getPosValue());
            System.out.println("Begin: "+pos.getBegin());
            System.out.println("End: "+pos.getEnd());
            System.out.println("CoveredText: "+pos.getCoveredText());

             */

            Document iterPOSDoc = new Document();
            iterPOSDoc.put("TypeIndexID", pos.getTypeIndexID());
            iterPOSDoc.put("CoarseValue", pos.getCoarseValue());
            iterPOSDoc.put("PosValue", pos.getPosValue());
            iterPOSDoc.put("Begin", pos.getBegin());
            iterPOSDoc.put("End", pos.getEnd());
            iterPOSDoc.put("CoveredText", pos.getCoveredText());
            allPOSDocuments.add(iterPOSDoc.toJson());
        }
        return allPOSDocuments;
    }

    public List<String> getAllDependenciesAsJSON(String View) throws CASException {
        List<String> allDependencyDocuments = new ArrayList<>();

        System.out.println("Dependency Dokumente werden geladen.");
        for (Dependency dependency : JCasUtil.select(jCas.getView(View), Dependency.class)) {
            /*
            System.out.println("TypeIndexID: "+dependency.getTypeIndexID());
            System.out.println("DependencyType: "+dependency.getDependencyType());
            System.out.println("Dependent: "+dependency.getDependent());
            System.out.println("Flavor: "+dependency.getFlavor());
            System.out.println("Governor: "+dependency.getGovernor());
            System.out.println("Begin: "+dependency.getBegin());
            System.out.println("End: "+dependency.getEnd());
            System.out.println("CoveredText: "+dependency.getCoveredText());

             */

            Document iterDependencyDoc = new Document();
            //iterDependencyDoc.put("TypeIndexID", dependency.getTypeIndexID());
            iterDependencyDoc.put("DependencyType", dependency.getDependencyType());
            iterDependencyDoc.put("Dependent", dependency.getDependent().toString());
            iterDependencyDoc.put("Flavor", dependency.getFlavor().toString());
            iterDependencyDoc.put("Governor", dependency.getGovernor().toString());
            iterDependencyDoc.put("Begin", dependency.getBegin());
            iterDependencyDoc.put("End", dependency.getEnd());
            iterDependencyDoc.put("CoveredText", dependency.getCoveredText());
            allDependencyDocuments.add(iterDependencyDoc.toJson());
        }
        return allDependencyDocuments;
    }

    public List<String> getAllNamedEntitiesAsJSON(String View) throws CASException {
        List<String> allNamedEntitesDocuments = new ArrayList<>();

        System.out.println("NamedEntity Dokumente werden geladen.");
        for (NamedEntity namedEntity : JCasUtil.select(jCas.getView(View), NamedEntity.class)) {
            /*
            System.out.println("TypeIndexID: "+namedEntity.getTypeIndexID());
            //System.out.println("Identifier: "+namedEntity.getIdentifier());
            System.out.println("Value: "+namedEntity.getValue());
            System.out.println("Begin: "+namedEntity.getBegin());
            System.out.println("End: "+namedEntity.getEnd());
            System.out.println("CoveredText: "+namedEntity.getCoveredText());

             */

            Document iterNamedEntity = new Document();
            iterNamedEntity.put("TypeIndexID", namedEntity.getTypeIndexID());
            //iterNamedEntity.put("Identifier", namedEntity.getIdentifier());
            iterNamedEntity.put("Value", namedEntity.getValue());
            iterNamedEntity.put("Begin", namedEntity.getBegin());
            iterNamedEntity.put("End", namedEntity.getEnd());
            iterNamedEntity.put("CoveredText", namedEntity.getCoveredText());
            allNamedEntitesDocuments.add(iterNamedEntity.toJson());
        }
        return allNamedEntitesDocuments;
    }


    public List<String> getAllAudioTokenJSON(String View) throws CASException {
        List<String> allAudioTokenDocs = new ArrayList<>();

        System.out.println("AudioToken Dokumente werden geladen.");
        for (AudioToken audioToken : JCasUtil.select(jCas.getView(View), AudioToken.class)) {
            /*
            System.out.println("TypeIndexID: "+audioToken.getTypeIndexID());
            System.out.println("Value: "+audioToken.getValue());
            System.out.println("Begin: "+audioToken.getBegin());
            System.out.println("End: "+audioToken.getEnd());
            System.out.println("TimeStart: "+audioToken.getTimeStart());
            System.out.println("TimeEnd: "+audioToken.getTimeEnd());
            //System.out.println("CoveredText: "+audioToken.getCoveredText());

             */

            Document iterAudioToken = new Document();
            //iterAudioToken.put("TypeIndexID", audioToken.getTypeIndexID());
            iterAudioToken.put("Value", audioToken.getValue());
            iterAudioToken.put("Begin", audioToken.getBegin());
            iterAudioToken.put("End", audioToken.getEnd());
            iterAudioToken.put("TimeStart", audioToken.getTimeStart());
            iterAudioToken.put("TimeEnd", audioToken.getTimeEnd());
            iterAudioToken.put("CoveredText", audioToken.getCoveredText());
            allAudioTokenDocs.add(iterAudioToken.toJson());
        }
        return allAudioTokenDocs;
    }


    public List<String> getAllSentimentAsJSON(String View) throws CASException {
        List<String> allSentimentDocuments = new ArrayList<>();

        System.out.println("Sentiment und Sentence Dokumente werden geladen.");

        //Achte auf class
        for (Sentence sentence: JCasUtil.select(jCas.getView(View), Sentence.class)) {
            for (GerVaderSentiment gerVaderSentiment : JCasUtil.selectCovered(GerVaderSentiment.class, sentence)) {
                /*
                System.out.println("TypeIndexID: "+gerVaderSentiment.getTypeIndexID());
                System.out.println("Sentiment: "+gerVaderSentiment.getSentiment());
                //System.out.println("Subjectivity: "+gerVaderSentiment.getSubjectivity());
                System.out.println("Begin: "+gerVaderSentiment.getBegin());
                System.out.println("End: "+gerVaderSentiment.getEnd());
                System.out.println("CoveredText: "+gerVaderSentiment.getCoveredText());

                 */

                Document iterSentimentDoc = new Document();
                iterSentimentDoc.put("TypeIndexID", gerVaderSentiment.getTypeIndexID());
                iterSentimentDoc.put("Sentiment", gerVaderSentiment.getSentiment());
                //iterSentimentDoc.put("Subjectivity", gerVaderSentiment.getSubjectivity());
                iterSentimentDoc.put("Begin", gerVaderSentiment.getBegin());
                iterSentimentDoc.put("End", gerVaderSentiment.getEnd());
                iterSentimentDoc.put("CoveredText", gerVaderSentiment.getCoveredText());

                allSentimentDocuments.add(iterSentimentDoc.toJson());
            }
        }
        return allSentimentDocuments;
    }

    public List<String> getAllTopicsAsJSON(String View) throws CASException {
        List<String> allTopicDocuments = new ArrayList<>();

        System.out.println("Topics Dokumente werden geladen.");
        for (CategoryCoveredTagged topic : JCasUtil.select(jCas.getView(View), CategoryCoveredTagged.class)) {
            /*
            System.out.println("Topic: "+topic.getValue());
            System.out.println("Begin: "+topic.getBegin());
            System.out.println("End: "+topic.getEnd());
            System.out.println("Score: "+topic.topic.getScore());

             */

            Document iterTopic = new Document();
            iterTopic.put("Topic", topic.getValue());
            iterTopic.put("Begin", topic.getBegin());
            iterTopic.put("End", topic.getEnd());
            iterTopic.put("Score", topic.getScore());
            allTopicDocuments.add(iterTopic.toJson());
        }
        return allTopicDocuments;
    }
}