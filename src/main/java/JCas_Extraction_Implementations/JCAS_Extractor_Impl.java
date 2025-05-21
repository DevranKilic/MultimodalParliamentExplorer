package JCas_Extraction_Implementations;

import JCas_Extraction_Interfaces.JCAS_Extractor_Interface;
import org.apache.uima.jcas.JCas;

/**
 * Diese Klasse dient dazu die verschienden linguistischen Merkmale von verschiedenen classes eines JCas mithilfe nur einer Klasse auszulesen.
 */
public class JCAS_Extractor_Impl implements JCAS_Extractor_Interface {

    private JCas jCasExtract;

    private Token_Extraction_Impl tokenExtraction;
    private Sentence_Extraction_Impl sentenceExtraction;
    private POS_Extraction_Impl posExtraction;
    private Dependency_Extraction_Impl dependencyExtraction;
    private NamedEntity_Extraction_Impl namedEntityExtraction;
    private Sentiment_Extraction_Impl sentimentExtraction;
    private AudioToken_Extraction_Impl audioTokenExtraction;
    private Topic_Extraction_Impl topicExtraction;

    public JCAS_Extractor_Impl(JCas jCas){
        this.jCasExtract = jCas;

        this.tokenExtraction = new Token_Extraction_Impl(this.jCasExtract);
        this.sentenceExtraction = new Sentence_Extraction_Impl(this.jCasExtract);
        this.posExtraction = new POS_Extraction_Impl(this.jCasExtract);
        this.dependencyExtraction = new Dependency_Extraction_Impl(this.jCasExtract);
        this.namedEntityExtraction = new NamedEntity_Extraction_Impl(this.jCasExtract);
        this.audioTokenExtraction = new AudioToken_Extraction_Impl(this.jCasExtract);
        this.sentimentExtraction = new Sentiment_Extraction_Impl(this.jCasExtract);
        this.topicExtraction = new Topic_Extraction_Impl(this.jCasExtract);

    }

    public Token_Extraction_Impl tokenExtraction() {return tokenExtraction;}

    public Sentence_Extraction_Impl sentenceExtraction() {
        return sentenceExtraction;
    }

    public POS_Extraction_Impl posExtraction() {
        return posExtraction;
    }

    public Dependency_Extraction_Impl dependencyExtraction() {
        return dependencyExtraction;
    }

    public NamedEntity_Extraction_Impl namedEntityExtraction() {
        return namedEntityExtraction;
    }

    public Sentiment_Extraction_Impl sentimentExtraction() {
        return sentimentExtraction;
    }

    public AudioToken_Extraction_Impl audioTokenExtraction() {return audioTokenExtraction;}

    public Topic_Extraction_Impl topicExtraction(){return topicExtraction;};
}
