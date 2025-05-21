package NLP;

import Class_MongoDB_Impl.Rede_MongoDB_Impl;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import database.MongoDBHandler;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.BsonMaximumSizeExceededException;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public class NLP_Impl {

    private MongoDBHandler myMongoDBHandler;

    /**
     * the constructor of the class. It starts the analysis of the speeches and videos.
     * @param myMongoDBHandler the MongoDBHandler needed.
     * @throws Exception
     */
    public NLP_Impl(MongoDBHandler myMongoDBHandler) throws Exception {
        this.myMongoDBHandler = myMongoDBHandler;
        //analyseAllSpeechesInDB();
        //analyseAllVideosInFolder("src/main/resources/BundestagRedeVideos");
    }

    /**
     * Gets all unanalysed documents and starts the analysis and uploading of the speeches.
     * @throws Exception
     */
    public void analyseAllSpeechesInDB() throws Exception {
        List<Document> results = myMongoDBHandler.getUnanalysedDocs("Rede", "JCas_JSON_RedeText");
        analyseAndUplaodSpeeches(results);
    }

    /**
     * Analysis and uploading of a given speech.
     * @param speeches A list of speeches that are to be analysed and uploaded.
     * @throws Exception
     */
    public void analyseAndUplaodSpeeches(List<Document> speeches) throws Exception {
        NLP_Pipeline pipeline = new NLP_Pipeline();
        pipeline.addSpacyToPipeline("RedeText", "RedeText");
        pipeline.addGervaderToPipeline("RedeText", "RedeText");
        pipeline.addParlBERTToPipeline("RedeText", "RedeText");
        pipeline.addDUUISpeechWriterToPipeline();
        pipeline.startSpeechPipeline(speeches, myMongoDBHandler);
    }

    /**
     * Starts the process to analyse and upload all videos in the folder.
     * @param folderPath The path to the folder where the videos are.
     * @throws Exception
     */
    public void analyseAllVideosInFolder(String folderPath) throws Exception {

        File OrdnerFile = new File(folderPath);
        File[] VideoDateien = OrdnerFile.listFiles();
        for (File videofile : VideoDateien) {
            String videoName = videofile.getName();
            String redeID = getRedeIDFromVideo(videoName);
            System.out.println(redeID);

            if (!wasAnalysed(redeID)) {
                analyseAndUploadVideo(videofile, redeID);
            }
        }
    }

    /**
     * Gets the speech from the video with the ID
     * @param filename Filename of the video.
     * @return The ID of the speech.
     */
    private String getRedeIDFromVideo(String filename){
        int Unterstrich_Index = filename.indexOf(".");
        String Video_RedeID = filename.substring(0,Unterstrich_Index);
        Document redeDoc = myMongoDBHandler.getCollection("Rede").find(new Document("Rede_ID", Video_RedeID)).first();

        if (redeDoc==null){
            return null;
        }

        return redeDoc.getString("Rede_ID");
    }

    /**
     * Starts the process to analyse and upload one video.
     * @param videofile The videofile.
     * @param redeID The ID of the speech.
     * @throws Exception
     */
    public void analyseAndUploadVideo(File videofile, String redeID) throws Exception {
        if (!wasAnalysed(redeID)) {
            JCas videoCas = analyseVideo(videofile);
            uploadNlpVideo(videoCas, redeID, videofile);
        }
    }

    /**
     * Analyses the vidoe.
     * @param videofile The videofile.
     * @return A JCas that holds the results of the analysis.
     * @throws Exception
     */
    private JCas analyseVideo(File videofile) throws Exception {

        NLP_Pipeline pipeline = new NLP_Pipeline();
        JCas jCasVideo = JCasFactory.createJCas();
        jCasVideo.setSofaDataString("","text/plain");
        jCasVideo.createView("Video");
        AddVideoToJCas(jCasVideo, videofile,"Video");

        pipeline.resetPipeline();
        pipeline.addWhisperxToPipeline("Video", "VideoAnalyse");
        pipeline.addSpacyToPipeline("VideoAnalyse", "VideoAnalyse");
        pipeline.addGervaderToPipeline("VideoAnalyse", "VideoAnalyse");
        pipeline.addParlBERTToPipeline("VideoAnalyse", "VideoAnalyse");
        pipeline.startPipeline(jCasVideo);
        JCas jCasVideoAnalyse = jCasVideo.getView("VideoAnalyse");

        return jCasVideoAnalyse;
    }

    /**
     * Uploads one analysis of a video.
     * @param videoCas A JCas that holds the results of the analysis.
     * @param redeID The ID of the speech
     * @param file The videofile.
     * @throws Exception
     */
    private void uploadNlpVideo(JCas videoCas, String redeID, File file) throws Exception {

        try{
            System.out.println("Hallo");
            JCasToDocument_Impl videoAnalyseConverter = new JCasToDocument_Impl(videoCas);
            //MongoCollection<Document> myCollection = myMongoDBHandler.getCollection("speeches");
            //Document uploadDoc = toDocument(redeID, videoAnalyseConverter);
            //myCollection.insertOne(uploadDoc);

            myMongoDBHandler.getCollection("speeches").findOneAndUpdate(
                    new Document("_id", redeID),
                    new Document("$set", new Document("JCas_JSON_VideoAnalyse", videoAnalyseConverter.jCasToDocument("VideoAnalyse"))),
                    new FindOneAndUpdateOptions().upsert(true));

            myMongoDBHandler.getCollection("Rede").findOneAndUpdate(
                    new Document("_id", redeID),
                    new Document("$set", new Document("Videoanalyse", true)));

            myMongoDBHandler.getCollection("Rede").findOneAndUpdate(
                    new Document("Rede_ID", redeID),
                    new Document("$set", new Document("VideoAbsolutPath", file.getAbsolutePath())));

        }catch (BsonMaximumSizeExceededException e){
            System.out.println("BsonMaximumSizeExceededException");
        }catch (MongoCommandException e){
            System.out.println("MongoCommandException");
        }
    }

    /**
     * Looks if the video was already analysed.
     * @param redeID The ID of the speech.
     * @return True if it was already analysied false if not.
     * @throws ResourceInitializationException
     * @throws CASException
     */
    private boolean wasAnalysed(String redeID) throws ResourceInitializationException, CASException {

        Document redeDoc = myMongoDBHandler.getCollection("Rede").find(new Document("Rede_ID", redeID)).first();
        return redeDoc.get("Videoanalyse") != null;
    }

    /**
     * gets the speech form the video.
     * @param redeID the speech ID from the video.
     * @return
     */
    private Rede_MongoDB_Impl getRedeFromVideo(String redeID) {
        Document redeDoc = myMongoDBHandler.getCollection("Rede").find(new Document("Rede_ID", redeID)).first();

        if (redeDoc == null) {
            return null;
        }
        Rede_MongoDB_Impl rede = new Rede_MongoDB_Impl(myMongoDBHandler, redeDoc.getInteger("MongoID"));
        return rede;
    }

    /**
     * Fügt einem JCas das Video und MimeType einer bestimmten view hinzu.
     * @param aCas A JCas that holds the results of the analysis.
     * @param videoFile The videofile.
     * @param view The view.
     * @throws IOException
     * @throws CASException
     */
    private void AddVideoToJCas(JCas aCas, File videoFile, String view) throws IOException, CASException {
        // Video der CAS hinzufügen
        if (videoFile.exists()) {
            System.out.println(videoFile.getTotalSpace());
            String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(videoFile));
            String mimeType = Files.probeContentType(videoFile.toPath());
            aCas.getView(view).setSofaDataString(encoded, mimeType);
        } else{
            System.out.println(videoFile.getAbsolutePath() + " not found");
        }
    }
}


