package NLP;

import org.apache.uima.jcas.JCas;

public interface DocumentToJCas_Interface {

    public JCas getJCas_Converted();

    public void tokenConverter();

    public void sentenceConverter();

    public void posConverter();

    public void namedEntityConverter();

    public void audioTokenConverter();

    public void sentimentConverter();

    public void topicConverter();
}
