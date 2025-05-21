package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.AudioToken_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.texttechnologylab.annotation.type.AudioToken;
import org.texttechnologylab.uima.type.Sentiment;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu die linguistischen Merkmale von AudioToken eines JCas auszulesen.
 */
public class AudioToken_Extraction_Impl implements AudioToken_Extraction_Interface {
    private org.apache.uima.jcas.JCas JCas;

    public AudioToken_Extraction_Impl(JCas aCas){
        this.JCas = aCas;
    }


    public List<AudioToken> getAllAudioToken() throws CASException {
        List<AudioToken> audioTokenList = new ArrayList<>();

        System.out.println("AudioToken werden geladen.");
        for (AudioToken audioToken : JCasUtil.select(JCas, AudioToken.class)) {
            /*
            System.out.println("Value: "+audioToken.getValue());
            System.out.println("Begin: "+audioToken.getBegin());
            System.out.println("End: "+audioToken.getEnd());
            System.out.println("TimeStart: "+audioToken.getTimeStart());
            System.out.println("TimeEnd: "+audioToken.getTimeEnd());

             */

            audioTokenList.add(audioToken);
        }
        return audioTokenList;
    }

    public String getAudioToken_Value(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (AudioToken audioToken : JCasUtil.select(JCas, AudioToken.class)) {
            //System.out.println("Value: "+audioToken.getValue());
            if (counter == number){
                found = audioToken.getValue();
                return found;
            }
            counter++;
        }
        return found;
    }


    public int getAudioToken_Begin(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (AudioToken audioToken : JCasUtil.select(JCas, AudioToken.class)) {
            //System.out.println("Begin: "+audioToken.getBegin());
            if (counter == number){
                found = audioToken.getBegin();
                return found;
            }
            counter++;
        }
        return found;
    }

    public int getAudioToken_End(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (AudioToken audioToken : JCasUtil.select(JCas, AudioToken.class)) {
            //System.out.println("End: "+audioToken.getEnd());
            if (counter == number){
                found = audioToken.getEnd();
                return found;
            }
            counter++;
        }
        return found;
    }

    public float getAudioToken_TimeStart(int number) throws CASException {
        float found = -1;
        int counter = 0;
        for (AudioToken audioToken : JCasUtil.select(JCas, AudioToken.class)) {
            //System.out.println("TimeStart: "+audioToken.getTimeStart());
            if (counter == number){
                found = audioToken.getTimeStart();
                return found;
            }
            counter++;
        }
        return found;
    }

    public float getAudioToken_TimeEnd(int number) throws CASException {
        float found = -1;
        int counter = 0;
        for (AudioToken audioToken : JCasUtil.select(JCas, AudioToken.class)) {
            //System.out.println("TimeEnd: "+audioToken.getTimeEnd());
            if (counter == number){
                found = audioToken.getTimeEnd();
                return found;
            }
            counter++;
        }
        return found;
    }


}
