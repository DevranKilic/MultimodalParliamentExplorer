package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.NamedEntity_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu die linguistischen Merkmale von NamedEntity eines JCas auszulesen.
 */
public class NamedEntity_Extraction_Impl implements NamedEntity_Extraction_Interface {

    private JCas jCas;

    public NamedEntity_Extraction_Impl(JCas aCas){
        this.jCas = aCas;
    }

    public List<NamedEntity> getAllNamedEntity() throws CASException {
        List<NamedEntity> allNamedEntity = new ArrayList<>();

        System.out.println("NamedEntity Dokumente werden geladen.");
        for (NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class)) {
            /*
            System.out.println("Value: "+namedEntity.getValue());
            System.out.println("Begin: "+namedEntity.getBegin());
            System.out.println("End: "+namedEntity.getEnd());

             */
            allNamedEntity.add(namedEntity);
        }
        return allNamedEntity;
    }


    public String getNamedEntity_Value(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class)) {
            //System.out.println("Value: "+namedEntity.getValue());
            if (counter == number){
                found = namedEntity.getValue();
                return found;
            }
            counter++;
        }
        return found;
    }

    public int getNamedEntity_Begin(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class)) {
            //System.out.println("Begin: "+namedEntity.getBegin());
            if (counter == number){
                found = namedEntity.getBegin();
                return found;
            }
            counter++;
        }
        return found;
    }

    public int getNamedEntity_End(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class)) {
            //System.out.println("End: "+namedEntity.getEnd());
            if (counter == number){
                found = namedEntity.getEnd();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getNamedEntity_CoveredText(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class)) {
            //System.out.println("CoveredText: "+getCoveredText.getEnd());
            if (counter == number){
                found = namedEntity.getCoveredText();
                return found;
            }
            counter++;
        }
        return found;
    }
}
