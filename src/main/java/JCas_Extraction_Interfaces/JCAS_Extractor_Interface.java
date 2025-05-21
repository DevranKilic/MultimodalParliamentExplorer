package JCas_Extraction_Interfaces;

import JCas_Extraction_Implementations.*;

public interface JCAS_Extractor_Interface {

    public Token_Extraction_Impl tokenExtraction();

    public Sentence_Extraction_Impl sentenceExtraction();

    public POS_Extraction_Impl posExtraction();

    public Dependency_Extraction_Impl dependencyExtraction();

    public NamedEntity_Extraction_Impl namedEntityExtraction();

    public Sentiment_Extraction_Impl sentimentExtraction();

    public AudioToken_Extraction_Impl audioTokenExtraction();

    public Topic_Extraction_Impl topicExtraction();
}
