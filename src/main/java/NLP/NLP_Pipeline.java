package NLP;

import database.MongoDBHandler;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.bson.Document;
import org.texttechnologylab.DockerUnifiedUIMAInterface.DUUIComposer;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIRemoteDriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIUIMADriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUIAsynchronousProcessor;
import org.texttechnologylab.DockerUnifiedUIMAInterface.io.DUUICollectionReader;
import org.texttechnologylab.DockerUnifiedUIMAInterface.lua.DUUILuaContext;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * Diese Klasse beinhaltet alle möglichen Pipelines für eine NLP Analyse.
 * RemoteDriver sind hier zu finden.
 */
public class NLP_Pipeline {

    private DUUILuaContext ctx;
    private DUUIComposer composer;

    /**
     * This method is the constructor fo the class. It sets up a DUUI-based processing enviroment.
     * @throws IOException
     * @throws URISyntaxException
     */
    public NLP_Pipeline() throws IOException, URISyntaxException {

        ctx = new DUUILuaContext().withJsonLibrary();

        // Composer erstellen
        composer = new DUUIComposer()
                .withSkipVerification(true)
                .withLuaContext(ctx)
                .withWorkers(1);

        DUUIRemoteDriver remoteDriver = new DUUIRemoteDriver();
        DUUIUIMADriver uimaDriver = new DUUIUIMADriver();

        //Es wird nur Remote verwendet.
        composer.addDriver(remoteDriver, uimaDriver);
    }

    /**
     * This method adds spacy to the pipeline.
     * @param sourceView the input view .
     * @param targetView the output view.
     * @return the updated composer
     * @throws URISyntaxException
     * @throws IOException
     * @throws CompressorException
     * @throws InvalidXMLException
     * @throws SAXException
     */
    public DUUIComposer addSpacyToPipeline(String sourceView, String targetView) throws URISyntaxException, IOException, CompressorException, InvalidXMLException, SAXException {
        composer.add(new DUUIRemoteDriver.Component("http://spacy.lehre.texttechnologylab.org")
                .withScale(1)
                .withSourceView(sourceView)
                .withTargetView(targetView)
                .build());
        return composer;
    }

    /**
     * This method adds gervader to the pipeline.
     * @param sourceView the input view .
     * @param targetView the output view.
     * @return the updated composer
     * @throws URISyntaxException
     * @throws IOException
     * @throws CompressorException
     * @throws InvalidXMLException
     * @throws SAXException
     */
    public DUUIComposer addGervaderToPipeline(String sourceView, String targetView) throws URISyntaxException, IOException, CompressorException, InvalidXMLException, SAXException {
        composer.add(new DUUIRemoteDriver.Component("http://gervader.lehre.texttechnologylab.org")
                .withScale(1)
                .withParameter("selection", "text")
                .withSourceView(sourceView)
                .withTargetView(targetView)
                .build());
        return composer;
    }

    /**
     * This method adds parlBERT to the pipeline.
     * @param sourceView the input view .
     * @param targetView the output view.
     * @return the updated composer
     * @throws URISyntaxException
     * @throws IOException
     * @throws CompressorException
     * @throws InvalidXMLException
     * @throws SAXException
     */
    public DUUIComposer addParlBERTToPipeline(String sourceView, String targetView) throws URISyntaxException, IOException, CompressorException, InvalidXMLException, SAXException {
        composer.add(new DUUIRemoteDriver.Component("http://parlbert.lehre.texttechnologylab.org")
                .withScale(1)
                .withSourceView(sourceView)
                .withTargetView(targetView)
                .build());
        return composer;
    }

    /**
     * This method adds whisperX to the pipeline.
     * @param sourceView the input view .
     * @param targetView the output view.
     * @return the updated composer
     * @throws URISyntaxException
     * @throws IOException
     * @throws CompressorException
     * @throws InvalidXMLException
     * @throws SAXException
     */
    public DUUIComposer addWhisperxToPipeline(String sourceView, String targetView) throws URISyntaxException, IOException, CompressorException, InvalidXMLException, SAXException {
        composer.add(new DUUIRemoteDriver.Component("http://whisperx.lehre.texttechnologylab.org")
                .withScale(1)
                .withSourceView(sourceView)
                .withTargetView(targetView)
                .build());
        return composer;
    }

    /**
     *Srtats the pipeline for the speeches.
     * @param documents The speeches that are to be analysed.
     * @throws Exception
     */
    public void startSpeechPipeline(List<Document> documents, MongoDBHandler myMongo) throws Exception {
        DUUISpeechReader reader = new DUUISpeechReader(documents, myMongo);
        Set<DUUICollectionReader> readers = new HashSet<>();
        readers.add(reader);
        DUUIAsynchronousProcessor processor = new DUUIAsynchronousProcessor(readers);
        composer.run(processor, "speeches");
    }

    /**
     * Adds the DUUISpeechWriter to the pipline.
     * @return the updated composer.
     * @throws URISyntaxException
     * @throws IOException
     * @throws CompressorException
     * @throws InvalidXMLException
     * @throws ResourceInitializationException
     * @throws SAXException
     */
    public DUUIComposer addDUUISpeechWriterToPipeline() throws URISyntaxException, IOException, CompressorException, InvalidXMLException, ResourceInitializationException, SAXException {
        composer.add(new DUUIUIMADriver.Component(createEngineDescription(DUUISpeechWriter.class)).build());
        return composer;
    }

    /**
     * Starts the pipeline.
     * @param cas A JCas that holds the results of the analysis.
     * @throws Exception
     */
    public void startPipeline(JCas cas) throws Exception {
        composer.run(cas);
    }


    /**
     * resets the pipeline.
     * @throws Exception
     */
    public void resetPipeline() throws Exception {
        composer.resetPipeline();
    }
}
