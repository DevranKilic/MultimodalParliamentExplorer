package JCas_Extraction_Interfaces;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.cas.CASException;

import java.util.List;

public interface Dependency_Extraction_Interface {

    public List<Dependency> getAllDependencies() throws CASException;

    public int getDependency_TypeIndexID(int Number) throws CASException;

    public String getDependency_DependencyType(int Number) throws CASException;

    public Token getDependency_Dependent(int Number) throws CASException;

    public String getDependency_Flavor(int Number) throws CASException;

    public Token getDependency_Governor(int Number) throws CASException;

    public int getDependency_Begin(int Number) throws CASException;

    public int getDependency_End(int Number) throws CASException;

    public String getDependency_CoveredText(int Number) throws CASException;
}
