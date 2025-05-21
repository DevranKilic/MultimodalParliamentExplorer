package JCas_Extraction_Interfaces;

import org.apache.uima.cas.CASException;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.texttechnologylab.uima.type.Topic;

import java.util.List;

public interface Topic_Extraction_Interface {
    public List<CategoryCoveredTagged> getAllTopics() throws CASException;

    public int getTopicBegin() throws CASException;

    public int getTopicEnd() throws CASException;
}
