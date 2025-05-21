package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.POS_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu die linguistischen Merkmale von POS eines JCas auszulesen.
 */
public class POS_Extraction_Impl implements POS_Extraction_Interface {

    private JCas jCas;

    public POS_Extraction_Impl(JCas aCas){
        this.jCas = aCas;
    }

    public List<POS> getAllPOS() throws CASException {
        List<POS> allPOS = new ArrayList<>();

        System.out.println("POS werden geladen.");
        for (POS pos : JCasUtil.select(jCas, POS.class)) {
            /*
            System.out.println("CoarseValue: "+pos.getCoarseValue());
            System.out.println("PosValue: "+pos.getPosValue());
            System.out.println("Begin: "+pos.getBegin());
            System.out.println("End: "+pos.getEnd());

             */

            allPOS.add(pos);
        }
        return allPOS;
    }


    public String getPOS_CoarseValue(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (POS pos : JCasUtil.select(jCas, POS.class)) {
            //System.out.println("CoarseValue: "+pos.getCoarseValue());
            if (counter == number){
                found = pos.getCoarseValue();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getPOS_PosValue(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (POS pos : JCasUtil.select(jCas, POS.class)) {
            //System.out.println("PosValue: "+pos.getPosValue());
            if (counter == number){
                found = pos.getPosValue();
                return found;
            }
            counter++;
        }
        return found;
    }

    public int getPOS_Begin(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (POS pos : JCasUtil.select(jCas, POS.class)) {
            //System.out.println("Begin: "+pos.getBegin());
            if (counter == number){
                found = pos.getBegin();
                return found;
            }
            counter++;
        }
        return found;
    }


    public int getPOS_End(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (POS pos : JCasUtil.select(jCas, POS.class)) {
            //System.out.println("End: "+pos.getEnd());
            if (counter == number){
                found = pos.getEnd();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getPOS_CoveredText(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (POS pos : JCasUtil.select(jCas, POS.class)) {
            //System.out.println("CoveredText: "+pos.getCoveredText());
            if (counter == number){
                found = pos.getCoveredText();
                return found;
            }
            counter++;
        }
        return found;
    }
}
