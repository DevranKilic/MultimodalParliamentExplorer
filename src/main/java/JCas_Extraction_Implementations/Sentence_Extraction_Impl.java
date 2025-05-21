package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.Sentence_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu die linguistischen Merkmale von Sentence eines JCas auszulesen.
 */
public class Sentence_Extraction_Impl implements Sentence_Extraction_Interface {


    private JCas jCas;

    public Sentence_Extraction_Impl(JCas aCas){
        this.jCas = aCas;
    }

    public List<Sentence> getAllSentence() throws CASException {
        List<Sentence> allSentence = new ArrayList<>();

        System.out.println("Sentence werden geladen.");
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            /*
            System.out.println("Begin: "+sentence.getBegin());
            System.out.println("End: "+sentence.getEnd());

             */

            allSentence.add(sentence);
        }
        return allSentence;
    }


    public int getSentence_Begin(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            //System.out.println("Begin: "+sentence.getBegin());
            if (counter == number){
                found = sentence.getBegin();
                return found;
            }
            counter++;
        }
        return found;
    }


    public int getSentence_End(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            //System.out.println("End: "+sentence.getEnd());
            if (counter == number){
                found = sentence.getEnd();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getSentence_CoveredText(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (Sentence sentence : JCasUtil.select(jCas, Sentence.class)) {
            //System.out.println("CoveredText: "+sentence.getCoveredText());
            if (counter == number){
                found = sentence.getCoveredText();
                return found;
            }
            counter++;
        }
        return found;
    }

}
