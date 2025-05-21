package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.Dependency_Extraction_Interface;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu die linguistischen Merkmale von Dependency eines JCas auszulesen.
 */
public class Dependency_Extraction_Impl implements Dependency_Extraction_Interface {


    private JCas JCas;

    public Dependency_Extraction_Impl(JCas aCas){
        this.JCas = aCas;
    }

    public List<Dependency> getAllDependencies() throws CASException {
        List<Dependency> allDependency = new ArrayList<>();

        System.out.println("Dependency Dokumente werden geladen.");
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
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
            allDependency.add(dependency);
        }
        return allDependency;
    }

    public int getDependency_TypeIndexID(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("SentenceID: "+dependency.getTypeIndexID());
            if (counter == number){
                found = dependency.getTypeIndexID();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getDependency_DependencyType(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("DependencyType: "+dependency.getDependencyType());
            if (counter == number){
                found = dependency.getDependencyType();
                return found;
            }
            counter++;
        }
        return found;
    }

    public Token getDependency_Dependent(int number) throws CASException {
        Token found = null;
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("Dependent: "+dependency.getDependent());
            if (counter == number){
                found = dependency.getDependent();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getDependency_Flavor(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("Flavor: "+dependency.getFlavor());
            if (counter == number){
                found = dependency.getFlavor();
                return found;
            }
            counter++;
        }
        return found;
    }

    public Token getDependency_Governor(int number) throws CASException {
        Token found = null;
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("Governor: "+dependency.getGovernor());
            if (counter == number){
                found = dependency.getGovernor();
                return found;
            }
            counter++;
        }
        return found;
    }

    public int getDependency_Begin(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("Begin: "+dependency.getBegin());
            if (counter == number){
                found = dependency.getBegin();
                return found;
            }
            counter++;
        }
        return found;
    }


    public int getDependency_End(int number) throws CASException {
        int found = -1;
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("End: "+dependency.getEnd());
            if (counter == number){
                found = dependency.getEnd();
                return found;
            }
            counter++;
        }
        return found;
    }

    public String getDependency_CoveredText(int number) throws CASException {
        String found = "";
        int counter = 0;
        for (Dependency dependency : JCasUtil.select(JCas, Dependency.class)) {
            //System.out.println("CoveredText: "+dependency.getCoveredText());
            if (counter == number){
                found = dependency.getCoveredText();
                return found;
            }
            counter++;
        }
        return found;
    }
}
