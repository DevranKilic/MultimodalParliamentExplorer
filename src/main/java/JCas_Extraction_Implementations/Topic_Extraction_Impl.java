package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.Topic_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.hucompute.textimager.uima.type.GerVaderSentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.uima.type.Topic;

import java.util.ArrayList;
import java.util.List;


/**
 * Diese Klasse dient dazu die linguistischen Merkmale von Topic eines JCas auszulesen.
 */
public class Topic_Extraction_Impl implements Topic_Extraction_Interface {
    private JCas jCas;

    public Topic_Extraction_Impl(JCas aCas) {
        this.jCas = aCas;
    }

    public List<CategoryCoveredTagged> getAllTopics() throws CASException {
        List<CategoryCoveredTagged> allTopics = new ArrayList<>();

        System.out.println("Sentiment und Sentence werden geladen.");

        for (CategoryCoveredTagged topic : JCasUtil.select(jCas, CategoryCoveredTagged.class)) {
                System.out.println(topic);
                System.out.println(topic.getCoveredText());
            allTopics.add(topic);
        }
        return allTopics;
    }

    public int getTopicBegin() throws CASException {
        int found = -1;
            for (CategoryCoveredTagged topic : JCasUtil.select(jCas, CategoryCoveredTagged.class)) {

                System.out.println("Begin: "+topic.getBegin());
                found = topic.getBegin();
                return found;
        }
        return found;
    }

    public int getTopicEnd() throws CASException {
        int found = -1;
            for (CategoryCoveredTagged topic : JCasUtil.select(jCas, CategoryCoveredTagged.class)) {
                    System.out.println("Begin: "+topic.getEnd());
                    found = topic.getEnd();
                    return found;
            }
        return found;
    }

    public String getTopic_Topic(int number) {
        String found = "";
        int counter = 0;
        for (CategoryCoveredTagged topic : JCasUtil.select(jCas, CategoryCoveredTagged.class)) {
            if (counter == number){
                found = topic.getValue();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getTopic_Score(int number) {
        String found = "";
        int counter = 0;
        for (CategoryCoveredTagged topic : JCasUtil.select(jCas, CategoryCoveredTagged.class)) {
            if (counter == number){
                found = String.valueOf(topic.getScore());
                return found;
            }
            counter++;
        }
        return found;
    }
}

