package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.Sentiment_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.hucompute.textimager.uima.type.GerVaderSentiment;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu die linguistischen Merkmale von Sentiment eines JCas auszulesen.
 */
public class Sentiment_Extraction_Impl implements Sentiment_Extraction_Interface {

    private JCas jCas;

    public Sentiment_Extraction_Impl(JCas aCas) {
        this.jCas = aCas;
    }

    public List<GerVaderSentiment> getAllSentiment() throws CASException {
        List<GerVaderSentiment> allGerVaderSentiment = new ArrayList<>();

        System.out.println("Sentiment und Sentence werden geladen.");

        for (GerVaderSentiment gerVaderSentiment : JCasUtil.select(jCas, GerVaderSentiment.class)) {
            /*
            System.out.println("Sentiment: " + gerVaderSentiment.getSentiment());
            System.out.println("Begin: " + gerVaderSentiment.getBegin());
            System.out.println("End: " + gerVaderSentiment.getEnd());

             */
            allGerVaderSentiment.add(gerVaderSentiment);
        }
        return allGerVaderSentiment;
    }


    public double getSentiment_Sentiment(int number) throws CASException {
        double found = -1;
        int counter = 0;
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            //System.out.println(sentence.getCoveredText());
            for (GerVaderSentiment gerVaderSentiment : JCasUtil.selectCovered(GerVaderSentiment.class, sentence)) {
                if (counter == number) {
                    //System.out.println("Sentiment: "+Sentiment.getSentiment());
                    found = gerVaderSentiment.getSentiment();
                    return found;
                }
                counter++;
            }
        }
        return found;
    }


    public int getSentiment_Begin(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            //System.out.println(sentence.getCoveredText());
            for (GerVaderSentiment gerVaderSentiment : JCasUtil.selectCovered(GerVaderSentiment.class, sentence)) {
                if (counter == number) {
                    //System.out.println("Begin: "+Sentiment.getBegin());
                    found = gerVaderSentiment.getBegin();
                    return found;
                }
                counter++;
            }
        }
        return found;
    }

    public int getSentiment_End(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            //System.out.println(sentence.getCoveredText());
            for (GerVaderSentiment gerVaderSentiment : JCasUtil.selectCovered(GerVaderSentiment.class, sentence)) {
                if (counter == number) {
                    //System.out.println("End: "+Sentiment.getEnd());
                    found = gerVaderSentiment.getEnd();
                    return found;
                }
                counter++;
            }
        }
        return found;
    }

    public String getSentiment_CoveredText(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            //System.out.println(sentence.getCoveredText());
            for (GerVaderSentiment gerVaderSentiment : JCasUtil.selectCovered(GerVaderSentiment.class, sentence)) {
                if (counter == number) {
                    //System.out.println("CoveredText: "+Sentiment.getCoveredText());
                    found = gerVaderSentiment.getCoveredText();
                    return found;
                }
                counter++;
            }
        }
        return found;
    }
}
