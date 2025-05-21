package Evaluation;

import NLP.DocumentToJCas_Impl;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Diese Klasse existiert um einzelne Videos ohne Kontext mithilfe von NLP zu Analysieren und auszugeben.
 */
public class Evaluation_MongoDB_Impl {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;

    private String VideoBase64;
    private String VideoMimeType;

    public Evaluation_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Evaluation";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection, this.MongoID_match);
    }


    public JCas toJCasFromDatabaseDocumentJSON(String MongoDBjCasFileName) throws ResourceInitializationException, CASException {

        if (this.docu.get(MongoDBjCasFileName)!= null){
            Document JCas_JSON = (Document) this.docu.get(MongoDBjCasFileName);
            DocumentToJCas_Impl Converter = new DocumentToJCas_Impl(JCas_JSON);

            return Converter.getJCas_Converted();

        }
        return null;
    }


    public boolean getCheckJCasVideoAnalyseExists() {
        return this.docu.containsKey("JCas_JSON_VideoAnalyse");
    }


    public String getVideoName() {
        if (this.docu.getString("VideoName") == null) {
            return "";
        } else {
            return this.docu.getString("VideoName");
        }
    }


    public String getVideoAbsolutPath() {
        if (this.docu.getString("VideoAbsolutPath") == null) {
            return null;
        } else {
            return this.docu.getString("VideoAbsolutPath");
        }
    }


    public String getMongoID() {
        if (String.valueOf(this.docu.getInteger("MongoID")) == null) {
            return "";
        } else {
            return String.valueOf(this.docu.getInteger("MongoID"));
        }
    }

    public String getTranscript(){
        if (this.docu.getString("Transcript") == null) {
            return "";
        } else {
            return this.docu.getString("Transcript");
        }
    }

    public void setVideoBase64AndVideoMimeType_process() throws IOException {
        String EvaluationsVideosPfad = "src/main/resources/EvaluationVideos";
        File Verzeichnis = new File(EvaluationsVideosPfad);
        File[] EvaluationsVideos = Verzeichnis.listFiles();
        for (File evaluationsVideo : EvaluationsVideos) {
            if (evaluationsVideo.getName().equalsIgnoreCase(this.getVideoName())) {
                File videoFile = new File(evaluationsVideo.getAbsolutePath());
                if (videoFile.exists()) {
                    String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(videoFile));
                    this.setVideoBase64(encoded);
                    String mimeType = Files.probeContentType(videoFile.toPath());
                    this.setVideoMimeType(mimeType);
                }
            }
        }
    }

    public String getVideoBase64() {
        return VideoBase64;
    }

    public void setVideoBase64(String videoBase64) {
        VideoBase64 = videoBase64;
    }

    public String getVideoMimeType() {
        return VideoMimeType;
    }

    public void setVideoMimeType(String videoMimeType) {
        VideoMimeType = videoMimeType;
    }
}
