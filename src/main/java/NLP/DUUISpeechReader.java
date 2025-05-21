package NLP;

import Class_MongoDB_Impl.Rede_MongoDB_Impl;
import database.MongoDBHandler;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.commons.io.FileUtils;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUICollectionReader;
import org.texttechnologylab.DockerUnifiedUIMAInterface.monitoring.AdvancedProgressMeter;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class DUUISpeechReader implements DUUICollectionReader {

    private MongoDBHandler myMongoDBHandler;

    private ConcurrentLinkedQueue<Document> documents;
    private ConcurrentLinkedQueue<Document> documentBackups;

    private int initialSize;
    private AtomicInteger docNumber;

    private AdvancedProgressMeter progress = null;

    private int debugCount = 25;

    /**
     * Constructor creates a queue of documents.
     * @param speeches The speeches in form of a document.
     * @param myMongoDBHandler
     */
    public DUUISpeechReader(List<Document> speeches, MongoDBHandler myMongoDBHandler) {
        this.myMongoDBHandler = myMongoDBHandler;
        documents = new ConcurrentLinkedQueue<>();
        documentBackups = new ConcurrentLinkedQueue<>();

        for (Document document : speeches) {
            documents.add(document);
        }

        documentBackups.addAll(documents);

        this.debugCount = debugCount;

        System.out.printf("Found %d files matching the pattern!", documents.size());
        initialSize = documents.size();
        docNumber = new AtomicInteger(0);

        progress = new AdvancedProgressMeter(initialSize);
    }

    /**
     * Gets the progress.
     * @return the progress.
     */
    @Override
    public AdvancedProgressMeter getProgress() {
        return this.progress;
    }

    /**
     * Gets the next element in the queue and adds it to a JCas.
     * @param empty A JCas that holds the results of the analysis.
     */
    @Override
    public void getNextCas(JCas empty) {

        int val = docNumber.addAndGet(1);

        progress.setDone(val);
        progress.setLeft(initialSize - val);

        Document document = documents.poll();
        Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(myMongoDBHandler, document.getInteger("MongoID"));
        System.out.println(document.getString("Rede_ID"));
        try {
            rede.toJCas(empty);
        } catch (ResourceInitializationException e) {
            throw new RuntimeException(e);
        } catch (CASException e) {
            throw new RuntimeException(e);
        }

        DocumentMetaData dmd = DocumentMetaData.create(empty);
        dmd.setDocumentId(Integer.toString(rede.getMongoID()));
        dmd.addToIndexes();

    }

    /**
     * Returns if the queue has a next element.
     * @return true of false.
     */
    @Override
    public boolean hasNext() {
        return documents.size() > 0;
    }

    /**
     * Gets the size of the queue.
     * @return The size of the queue.
     */
    @Override
    public long getSize() {
        return documents.size();
    }

    /**
     * Gets the current value of the docNumber.
     * @return current value of the docNumber.
     */
    @Override
    public long getDone() {
        return docNumber.get();
    }
}