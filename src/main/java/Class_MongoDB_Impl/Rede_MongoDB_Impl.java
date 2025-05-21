package Class_MongoDB_Impl;

import Class_Impl.Rede_Impl;
import Class_Interfaces.Rede;
import Helper.Hilfsmethoden;
import JCas_Extraction_Implementations.JCAS_Extractor_Impl;
import JCas_Extraction_Implementations.NamedEntity_Extraction_Impl;
import JCas_Extraction_Implementations.POS_Extraction_Impl;
import JCas_Extraction_Implementations.Topic_Extraction_Impl;
import NLP.DocumentToJCas_Impl;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.XmlCasDeserializer;
import org.bson.Document;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Diese Klasse besitzt eine Verbindung zu einer MongoDB und von dieser Datenbank werden alle Attribute und Funktionen/Objekte abgefragt.
 * Benötigt wird eine interne MongoID, durch welche eine eindeutige Zuordnung zum Dokument stattfinden kann.
 * Da die Klasse von der lokalen Hauptimplementation erbt, besitzt diese Klasse auch alle verfügbaren Attribute.
 */
public class Rede_MongoDB_Impl extends Rede_Impl implements Rede {
    private database.MongoDBHandler MongoDBHandler;
    private int MongoID_match;
    private String sCollection;
    private Document docu;

    public Rede_MongoDB_Impl(database.MongoDBHandler MongoDBHandler, int MongoID_match){
        this.MongoDBHandler = MongoDBHandler;
        this.MongoID_match = MongoID_match;
        this.sCollection = "Rede";
        this.docu = MongoDBHandler.getDocumentByMongoID(this.sCollection, this.MongoID_match);
    }


    public String getText() {
        if (this.docu.getString("Rede") == null) {
            return "";
        } else {
            return this.docu.getString("Rede");
        }
    }


    public int getRedner_Mongo_Impl() {
        if (this.docu.getInteger("Abgeordneter") == null) {
            return 0;
        } else {
            return this.docu.getInteger("Abgeordneter");
        }
    }

    public String getDate_Mongo_Impl() {
        if (this.docu.getString("Datum") == null) {
            return "";
        } else {
            return this.docu.getString("Datum");
        }
    }


    public String getRede_ID() {
        if (this.docu.getString("Rede_ID") == null) {
            return "";
        } else {
            return this.docu.getString("Rede_ID");
        }
    }


    public int getRedner_ID() {
        if (this.docu.getString("Redner_ID") == null) {
            return 0;
        } else {
            return this.docu.getInteger("Redner_ID");
        }
    }

    public String getVorname() {
        if (this.docu.getString("Vorname") == null) {
            return "";
        } else {
            return this.docu.getString("Vorname");
        }
    }

    public String getNachname() {
        if (this.docu.getString("Nachname") == null) {
            return "";
        } else {
            return this.docu.getString("Nachname");
        }
    }

    public String getFraktion() {
        if (this.docu.getString("Fraktion") == null) {
            return "";
        } else {
            return this.docu.getString("Fraktion");
        }
    }


    public int getWahlperiodeNummer() {
        if (this.docu.getInteger("WahlperiodeNummer") == null) {
            return 0;
        } else {
            return this.docu.getInteger("WahlperiodeNummer");
        }
    }

    public int getSitzungsnummer() {
        if (this.docu.getInteger("Sitzungsnummer") == null) {
            return 0;
        } else {
            return this.docu.getInteger("Sitzungsnummer");
        }
    }

    public String getOrt() {
        if (this.docu.getString("Ort") == null) {
            return "";
        } else {
            return this.docu.getString("Ort");
        }
    }


    public String getRolla_lang() {
        if (this.docu.getString("Rolle_lang") == null) {
            return "";
        } else {
            return this.docu.getString("Rolle_lang");
        }
    }

    public String getRolle_kurz() {
        if (this.docu.getString("Rolle_kurz") == null) {
            return "";
        } else {
            return this.docu.getString("Rolle_kurz");
        }
    }

    public String getRede_mit_Kommentaren() {
        if (this.docu.getString("Rede_mit_Kommentaren") == null) {
            return "";
        } else {
            return this.docu.getString("Rede_mit_Kommentaren");
        }
    }

    public int getMongoID() {
        if (this.docu.getInteger("MongoID") == null) {
            return 0;
        } else {
            return this.docu.getInteger("MongoID");
        }
    }

    public String getMongoID_AsString() {
        if (this.docu.getInteger("MongoID") == null) {
            return "";
        } else {
            return String.valueOf(this.docu.getInteger("MongoID"));
        }
    }

    public String getMongoLabel() {
        if (this.docu.getString("MongoLabel") == null) {
            return "";
        } else {
            return this.docu.getString("MongoLabel");
        }
    }


    public String getTagesordnungspunkt() {
        if (this.docu.getString("Tagesordnungspunkt") == null) {
            return "";
        } else {
            return this.docu.getString("Tagesordnungspunkt");
        }
    }


    public String getTOP_Beschreibung() {
        if (this.docu.getString("TOPBeschreibung") == null) {
            return "";
        } else {
            return this.docu.getString("TOPBeschreibung");
        }
    }

    @Override
    public List<Integer> getRedeAuszugMongoIDs() {
        List<Integer> empty = new ArrayList<>();
        if (this.docu.getList("RedeAuszugMongoIDs", Integer.class).isEmpty()) {
            return empty;
        } else {
            return this.docu.getList("RedeAuszugMongoIDs", Integer.class);
        }
    }

    public Document getJCasDocument(){
        Document empty = null;
        if (this.docu.get("JCasDocument", Document.class).isEmpty()){
            return empty;
        } else{
            return this.docu.get("JCasDocument", Document.class);
        }
    }

    public String getXMI_Base64() {
        if (this.docu.getString("JCas_XMI_Base64") == null) {
            return "";
        } else {
            return this.docu.getString("JCas_XMI_Base64");
        }
        //JCasXMILocalPath
    }


    public String getVideoAbsolutPath() {
        if (this.docu.getString("VideoAbsolutPath") == null) {
            return null;
        } else {
            return this.docu.getString("VideoAbsolutPath");
        }
    }

    public String getJCasXMILocalPath() {
        if (this.docu.getString("JCasXMILocalPath") == null) {
            return "";
        } else {
            return this.docu.getString("JCasXMILocalPath");
        }
    }

    public String getBundestagVideoID(){
        if (this.docu.getString("BundestagVideoID") == null) {
            return "";
        } else {
            return this.docu.getString("BundestagVideoID");
        }
    }

    public String getBundestagVideoDownloadLink(){
        if (this.docu.getString("BundestagVideoDownloadLink") == null) {
            return "";
        } else {
            return this.docu.getString("BundestagVideoDownloadLink");
        }
    }

    public String getBundestagVideoLocalPath(){
        if (this.docu.getString("BundestagVideoLocalPath") == null) {
            return "";
        } else {
            return this.docu.getString("BundestagVideoLocalPath");
        }
    }

    public Document getJCas_JSON_RedeText_Document() {
        if (this.docu.get("JCas_JSON_RedeText", Document.class) == null) {
            return null;
        } else {
            return this.docu.get("JCas_JSON_RedeText", Document.class);
        }
    }

    public boolean getCheckJCasRedeTextExists() {
        return this.docu.containsKey("JCas_JSON_RedeText");
    }


    public JCas toJCas() throws ResourceInitializationException, CASException {
        String JCas_XMI_Base64 = getXMI_Base64();
        if (JCas_XMI_Base64.isEmpty()) {
            //JCas erstellen und _intitalview ein Sofa hinzufügen
            JCas aCas = JCasFactory.createJCas();
            aCas.setSofaDataString("", "text/plain");
            aCas.setDocumentLanguage("de");

            //Aus Rede_Mongo_Impl toJcas verwenden
            //Füge Text View hinzu.
            aCas.createView("RedeText");
            aCas.getView("RedeText").setSofaDataString(this.getRede_mit_Kommentaren(), "text/plain");
            return aCas;
        } else {
            return null;
        }
    }

    public JCas toJCas(JCas jCas) throws ResourceInitializationException, CASException {
        String JCas_XMI_Base64 = getXMI_Base64();
        if (JCas_XMI_Base64.isEmpty()) {
            //JCas erstellen und _intitalview ein Sofa hinzufügen
            jCas.setSofaDataString("", "text/plain");
            jCas.setDocumentLanguage("de");

            //Aus Rede_Mongo_Impl toJcas verwenden
            //Füge Text View hinzu.
            jCas.createView("RedeText");
            jCas.getView("RedeText").setSofaDataString(this.getRede_mit_Kommentaren(), "text/plain");
            return jCas;
        } else {
            return null;
        }
    }

    public JCas toJCasFromDatabaseXMI() {
        String JCas_XMI_Base64 = getXMI_Base64();

        if (JCas_XMI_Base64.isEmpty()) {
            return null;
        } else {
            byte[] XMI_File_Binary = org.apache.commons.codec.binary.Base64.decodeBase64(JCas_XMI_Base64);
            try (ByteArrayInputStream XMI_Input_Stream = new ByteArrayInputStream(XMI_File_Binary)) {
                JCas aCas = JCasFactory.createJCas();
                XmlCasDeserializer.deserialize(XMI_Input_Stream, aCas.getCas());

                return aCas;

            } catch (IOException io) {
                io.printStackTrace();
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (ResourceInitializationException e) {
                throw new RuntimeException(e);
            } catch (CASException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public JCas toJCasFromDatabaseDocumentJSON(String MongoDBjCasFileName) throws ResourceInitializationException, CASException {

        if (this.docu.get(MongoDBjCasFileName)!= null){
            Document JCas_JSON = (Document) this.docu.get(MongoDBjCasFileName);
            DocumentToJCas_Impl Converter = new DocumentToJCas_Impl(JCas_JSON);

            return Converter.getJCas_Converted();

        }
        return null;
    }

    public JCas toJCasFromFile() throws IOException {
        return Hilfsmethoden.XMIBase64FileToJCas(getJCasXMILocalPath());
    }



    public void RedeText_NoEdit_processing() throws ResourceInitializationException, CASException {

        String RedeTextMitKommentaren = getRede_mit_Kommentaren();
        StringBuilder RedeText_processed = new StringBuilder();

        JCas Rede_RedeText = this.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
        JCAS_Extractor_Impl RedeText_Analysis = new JCAS_Extractor_Impl(Rede_RedeText);

        int Senitment_size = RedeText_Analysis.sentimentExtraction().getAllSentiment().size();

        for (int i = 0; i < Senitment_size; i++) {
            int Sentiment_begin = RedeText_Analysis.sentimentExtraction().getSentiment_Begin(i);
            int Sentiment_end = RedeText_Analysis.sentimentExtraction().getSentiment_End(i);
            double Sentiment = RedeText_Analysis.sentimentExtraction().getSentiment_Sentiment(i);

            String front = "<p> <span style=\"background-color:"+Hilfsmethoden.SentimentToColour(Sentiment)+";\">";
            String SentimentExcerpt = RedeTextMitKommentaren.substring(Sentiment_begin, Sentiment_end);
            String back = "</span></p>";
            String p = front + SentimentExcerpt +back;

            RedeText_processed.append(p);
            RedeText_processed.append("\n");
        }
        setRedeText_NoEdit(RedeText_processed.toString());
    }

    public void RedeText_WithEdit_processing() throws ResourceInitializationException, CASException {

        String RedeTextMitKommentaren = getRede_mit_Kommentaren();
        StringBuilder RedeTextNLPProcessed = new StringBuilder();

        JCas Rede_RedeText = this.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
        JCAS_Extractor_Impl RedeText_Analysis = new JCAS_Extractor_Impl(Rede_RedeText);

        int Senitment_size = RedeText_Analysis.sentimentExtraction().getAllSentiment().size();

        for (int i = 0; i < Senitment_size; i++) {
            int Sentiment_begin = RedeText_Analysis.sentimentExtraction().getSentiment_Begin(i);
            int Sentiment_end = RedeText_Analysis.sentimentExtraction().getSentiment_End(i);
            double Sentiment = RedeText_Analysis.sentimentExtraction().getSentiment_Sentiment(i);
            String front = "<p> <span style=\"background-color:"+Hilfsmethoden.SentimentToColour(Sentiment)+";\">";
            String SentimentExcerpt = RedeTextMitKommentaren.substring(Sentiment_begin, Sentiment_end);
            String back = "</span></p>";
            String p = front + SentimentExcerpt +back;

            RedeTextNLPProcessed.append(p);
            RedeTextNLPProcessed.append("\n");
        }
        String RedeTextNLPprocessedString = RedeTextNLPProcessed.toString();
        Hilfsmethoden.ListIntegerToListRedeAuszugMongoDBObjekt(MongoDBHandler,this);
        List<RedeAuszug_MongoDB_Impl> RedeAuszüge = this.getRedeAuszug_Objekte();
        int AbgeordneterMongoID = this.getRedner_Mongo_Impl();
        Abgeordneter_MongoDB_Impl Abgeordneter = new Abgeordneter_MongoDB_Impl(MongoDBHandler, AbgeordneterMongoID);

        for (RedeAuszug_MongoDB_Impl redeAuszugMongoDB : RedeAuszüge) {
            if (redeAuszugMongoDB.getIsKommentarMongoDB()){
                String Auszug = redeAuszugMongoDB.getAuszug();
                if (RedeTextNLPprocessedString.contains(Auszug)){
                    String AbgVorname = Abgeordneter.getVorname();
                    String AbgName = Abgeordneter.getName();
                    String AbgIDString = Abgeordneter.getID_String();
                    String RedeAuszugMongoIDString = redeAuszugMongoDB.getMongoIDMongoDB_String();
                    String Abstand = "\n";
                    String front = "<a href=\"/Kommentar/?Abgeordneter="+AbgVorname+"_"+AbgName+"_"+AbgIDString+"&RedeAuszugMongoID="+RedeAuszugMongoIDString+"\">";
                    String Excerpt = "<b>"+Auszug+"</b>";
                    String back = "</a>";
                    String KommentarHTML = Abstand + front + Abstand + Excerpt + back + Abstand;

                    RedeTextNLPprocessedString = RedeTextNLPprocessedString.replace(Auszug, KommentarHTML);
                }
            }
        }
        setRedeText_WithEdit(RedeTextNLPprocessedString);
    }

    public void setVideoBase64AndVideoMimeType_process() throws IOException {
        String RedeVideosPfad = "src/main/resources/BundestagRedeVideos";
        File OrdnerFile = new File(RedeVideosPfad);
        File[] VideoDateien = OrdnerFile.listFiles();
        for (File videofile : VideoDateien) {
            String VideoName = videofile.getName();
            int Unterstrich_Index = VideoName.indexOf(".");
            String Video_RedeID = VideoName.substring(0,Unterstrich_Index);
            if (Video_RedeID.equalsIgnoreCase(this.getRede_ID())) {
                File videoFile = new File(videofile.getAbsolutePath());
                if (videoFile.exists()) {
                    String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(org.apache.commons.io.FileUtils.readFileToByteArray(videoFile));
                    this.setVideoBase64(encoded);
                    String mimeType = Files.probeContentType(videoFile.toPath());
                    this.setVideoMimeType(mimeType);
                }
            }
        }
    }

    public StringBuilder toHTML(){
        StringBuilder Rede = new StringBuilder();

        Rede.append("<div class=\"Rede\">\n");
        if (!(getText()==null) && !(getText().trim().isEmpty())){
            Rede.append(
                    "<div class=\"Redeinfo\" id=\""+this.getRede_ID()+"\">\n" +
                            "<p>Wahlperiode: "+this.getWahlperiodeNummer()+"</p>\n" +
                            "<p>Sitzungsnummer: "+this.getSitzungsnummer()+"</p>\n" +
                            "<p>Rede ID: "+this.getRede_ID()+"</p>\n" +
                            "<p>Datum der Sitzung: "+this.getDate_Mongo_Impl()+"</p>\n");

            if (!(this.getTagesordnungspunkt()==null) && !(this.getTagesordnungspunkt().trim().isEmpty())){
                Rede.append(
                        "<p>Tagesordnungspunkt: "+getTagesordnungspunkt()+" </p>\n");

            }
            if (!(this.getTOP_Beschreibung()==null) && !(this.getTOP_Beschreibung().trim().isEmpty())){
                Rede.append("<p>Tagesordnungspunkt Beschreibung: \n"+getTOP_Beschreibung()+"</p>\n");
            }

            Rede.append("</div>\n");
            Rede.append("<div class=\"RedeText\">\n");
            Rede.append(this.getRede_mit_Kommentaren().trim()+"\n");
            Rede.append("</div>\n");
        }
        Rede.append("</div>\n");

        return Rede;
    }


    /**
     * Convert the speech text into LaTeX code with custom color highlighting for the sentiment values
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    private String Rede_Kommentare_tex() throws ResourceInitializationException, CASException {

        String RedeTextMitKommentaren = getRede_mit_Kommentaren();
        StringBuilder RedeTextNLPProcessed = new StringBuilder();



        Map<String, String> colorDefinitions = new HashMap<>();

        JCas Rede_RedeText = this.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
        JCAS_Extractor_Impl RedeText_Analysis = new JCAS_Extractor_Impl(Rede_RedeText);

        int Senitment_size = RedeText_Analysis.sentimentExtraction().getAllSentiment().size();


        for (int i = 0; i < Senitment_size; i++) {
            int Sentiment_begin = RedeText_Analysis.sentimentExtraction().getSentiment_Begin(i);
            int Sentiment_end = RedeText_Analysis.sentimentExtraction().getSentiment_End(i);
            double Sentiment = RedeText_Analysis.sentimentExtraction().getSentiment_Sentiment(i);


            if (Sentiment_begin >= 0 && Sentiment_end > Sentiment_begin && Sentiment_end <= RedeTextMitKommentaren.length()) {
                String rgbColor = Hilfsmethoden.SentimentToColour(Sentiment);


                if (!colorDefinitions.containsKey(rgbColor)) {

                    String[] rgbValues = rgbColor.replace("rgb(", "").replace(")", "").split(",");
                    int r = Integer.parseInt(rgbValues[0].trim());
                    int g = Integer.parseInt(rgbValues[1].trim());
                    int b = Integer.parseInt(rgbValues[2].trim());


                    String colorName = "CustomColor" + colorDefinitions.size();
                    RedeTextNLPProcessed.append("\\definecolor{" + colorName + "}{RGB}{" + r + "," + g + "," + b + "}\n");


                    colorDefinitions.put(rgbColor, colorName);
                }


                String colorName = colorDefinitions.get(rgbColor);
                String SentimentExcerpt = RedeTextMitKommentaren.substring(Sentiment_begin, Sentiment_end);
                String latexHighlight = "\\colorbox{" + colorName + "}{\\parbox{\\linewidth}{" + SentimentExcerpt + "}}";

                RedeTextNLPProcessed.append(TeXCleanup(latexHighlight));
            } else {

                String SentimentExcerpt = RedeTextMitKommentaren.substring(Sentiment_begin, Sentiment_end);
                RedeTextNLPProcessed.append("\\strut " + SentimentExcerpt);
            }

            RedeTextNLPProcessed.append("\n\n");
        }

        String RedeTextNLPprocessedString = RedeTextNLPProcessed.toString();
        Hilfsmethoden.ListIntegerToListRedeAuszugMongoDBObjekt(MongoDBHandler, this);
        List<RedeAuszug_MongoDB_Impl> RedeAuszüge = this.getRedeAuszug_Objekte();


        for (RedeAuszug_MongoDB_Impl redeAuszugMongoDB : RedeAuszüge) {
            if (redeAuszugMongoDB.getIsKommentarMongoDB()) {
                String Auszug = redeAuszugMongoDB.getAuszug();
                if (RedeTextNLPprocessedString.contains(Auszug)) {
                    String boldComment = "\\textbf{" + Auszug + "}"; // Bold formatting for comments
                    RedeTextNLPprocessedString = RedeTextNLPprocessedString.replace(Auszug, boldComment);
                }
            }
        }


        return RedeTextNLPprocessedString;
    }

    /**
     * Special case if NLP values are not available then only the speech itself gets converted into LaTeX code
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public StringBuilder Rede_only_comments() throws ResourceInitializationException, CASException {

        String RedeTextMitKommentaren = getRede_mit_Kommentaren();
        StringBuilder RedeTextNLPProcessed = new StringBuilder();


        Hilfsmethoden.ListIntegerToListRedeAuszugMongoDBObjekt(MongoDBHandler, this);
        List<RedeAuszug_MongoDB_Impl> RedeAuszüge = this.getRedeAuszug_Objekte();


        for (RedeAuszug_MongoDB_Impl redeAuszugMongoDB : RedeAuszüge) {
            if (redeAuszugMongoDB.getIsKommentarMongoDB()) {
                String Auszug = redeAuszugMongoDB.getAuszug();
                if (RedeTextMitKommentaren.contains(Auszug)) {

                    String boldComment = "\\textbf{" + Auszug + "}";
                    RedeTextMitKommentaren = RedeTextMitKommentaren.replace(Auszug, boldComment);
                }
            }
        }


        RedeTextNLPProcessed.append(TeXCleanup("\\strut " + RedeTextMitKommentaren));


        return RedeTextNLPProcessed;
    }


    /**
     * Collects all POS Values of a speech and converts it into a LaTeX table
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    private String collectAndListPOSValues() throws ResourceInitializationException, CASException {

        Map<String, Integer> posCounts = new HashMap<>();


        JCas documentJCas = this.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
        POS_Extraction_Impl posExtractor = new POS_Extraction_Impl(documentJCas);


        int posCount = posExtractor.getAllPOS().size();


        for (int i = 0; i < posCount; i++) {
            String posCoarseValue = posExtractor.getPOS_CoarseValue(i);


            posCounts.put(posCoarseValue, posCounts.getOrDefault(posCoarseValue, 0) + 1);
        }


        StringBuilder latexOutput = new StringBuilder();


        latexOutput.append("\\subsubsection*{POS Statistics}\n");



        latexOutput.append("\\begin{tabular}{|l|c|}\n");
        latexOutput.append("\\hline\n");
        latexOutput.append("\\textbf{POS Coarse Value} & \\textbf{Count} \\\\\n");
        latexOutput.append("\\hline\n");


        for (Map.Entry<String, Integer> entry : posCounts.entrySet()) {
            String posCoarseValue = entry.getKey();
            int count = entry.getValue();
            latexOutput.append(posCoarseValue).append(" & ").append(count).append(" \\\\\n");
            latexOutput.append("\\hline\n");
        }

        latexOutput.append("\\end{tabular}\n");


        return latexOutput.toString();
    }

    /**
     * Generates the LaTeX code for a table containing counts of the NamedEntities of the speech.
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    private String collectAndListNamedEntities() throws ResourceInitializationException, CASException {

        Map<String, Integer> namedEntityCounts = new HashMap<>();


        JCas documentJCas = this.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
        NamedEntity_Extraction_Impl namedEntityExtractor = new NamedEntity_Extraction_Impl(documentJCas);
        int namedEntityCount = namedEntityExtractor.getAllNamedEntity().size();
        for (int i = 0; i < namedEntityCount; i++) {
            String namedEntityValue = namedEntityExtractor.getNamedEntity_Value(i);


            namedEntityCounts.put(namedEntityValue, namedEntityCounts.getOrDefault(namedEntityValue, 0) + 1);
        }


        StringBuilder latexOutput = new StringBuilder();


        latexOutput.append("\\subsubsection*{Named Entity Statistics}\n");
        latexOutput.append("\\begin{tabular}{|l|c|}\n");
        latexOutput.append("\\hline\n");
        latexOutput.append("\\textbf{Named Entity Value} & \\textbf{Count} \\\\\n");
        latexOutput.append("\\hline\n");


        for (Map.Entry<String, Integer> entry : namedEntityCounts.entrySet()) {
            String namedEntityValue = entry.getKey();
            int count = entry.getValue();
            latexOutput.append(namedEntityValue).append(" & ").append(count).append(" \\\\\n");
            latexOutput.append("\\hline\n");
        }

        latexOutput.append("\\end{tabular}\n");

        return latexOutput.toString();
    }


    /**
     * Generates the LaTeX code for a table containing the means of scores of the different topic values.
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public String collectAndListTopics() throws ResourceInitializationException, CASException {
        Map<String, List<Double>> topicScores = new HashMap<>();

        JCas documentJCas = this.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");

        Topic_Extraction_Impl topicExtractor = new Topic_Extraction_Impl(documentJCas);

        int topicSize = topicExtractor.getAllTopics().size();

        for (int i = 0; i < topicSize; i++) {
            String topicValue = topicExtractor.getTopic_Topic(i);
            String topicScore = topicExtractor.getTopic_Score(i);
            double tScoreDouble = Double.parseDouble(topicScore);

            topicScores.computeIfAbsent(topicValue, k -> new ArrayList<>()).add(tScoreDouble);
        }

        Map<String, Double> topicMeanScores = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : topicScores.entrySet()) {
            String topicValue = entry.getKey();
            List<Double> scores = entry.getValue();

            double meanScore = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            topicMeanScores.put(topicValue, meanScore);
        }

        StringBuilder latexOutput = new StringBuilder();
        latexOutput.append("\\subsubsection*{Topic Statistics}\n");
        latexOutput.append("\\begin{tabular}{|l|c|}\n");
        latexOutput.append("\\hline\n");
        latexOutput.append("\\textbf{Topic Value} & \\textbf{Mean Score} \\\\\n");
        latexOutput.append("\\hline\n");

        for (Map.Entry<String, Double> entry : topicMeanScores.entrySet()) {
            String topicValue = entry.getKey();
            double meanScore = entry.getValue();
            latexOutput.append(topicValue).append(" & ").append(meanScore).append(" \\\\\n");
            latexOutput.append("\\hline\n");
        }

        latexOutput.append("\\end{tabular}\n");

        return latexOutput.toString();
    }



    /**
     * Converts all contents of the speech into LaTeX code.
     * If the NLP data is available all the available information gets converted in to LaTeX otherwise only the
     * comment-highlighting gets visualized for the speech.
     * @return
     * @throws ResourceInitializationException
     * @throws CASException
     */
    public StringBuilder toLaTeX() throws ResourceInitializationException, CASException {
        StringBuilder redeLaTeX = new StringBuilder();


        if (getText() != null && !getText().trim().isEmpty()) {
            redeLaTeX.append("\\subsection{Redeinformationen " + this.getRede_ID() +"}\n");
            redeLaTeX.append("\\begin{itemize}\n");
            redeLaTeX.append("\\item Redner: " + this.getVorname() +" " + this.getNachname() + "\n");
            redeLaTeX.append("\\item Fraktion: " + this.getFraktion() + "\n");
            redeLaTeX.append("\\item Wahlperiode: " + this.getWahlperiodeNummer() + "\n");
            redeLaTeX.append("\\item Sitzungsnummer: " + this.getSitzungsnummer() + "\n");
            redeLaTeX.append("\\item Rede ID: " + this.getRede_ID() + "\n");
            redeLaTeX.append("\\item Datum der Sitzung: " + this.getDate_Mongo_Impl() + "\n");

            if (this.getTagesordnungspunkt() != null && !this.getTagesordnungspunkt().trim().isEmpty()) {
                redeLaTeX.append("\\item Tagesordnungspunkt: " + this.getTagesordnungspunkt() + "\n");
            }

            if (this.getTOP_Beschreibung() != null && !this.getTOP_Beschreibung().trim().isEmpty()) {
                redeLaTeX.append("\\item Tagesordnungspunkt Beschreibung: " + this.getTOP_Beschreibung() + "\n");
            }

            redeLaTeX.append("\\end{itemize}\n");

            redeLaTeX.append("\\subsubsection{Redetext}\n");
            if (this.getCheckJCasRedeTextExists()) {
                redeLaTeX.append(this.Rede_Kommentare_tex());
                redeLaTeX.append(collectAndListPOSValues());
                redeLaTeX.append(collectAndListNamedEntities());
                redeLaTeX.append(collectAndListTopics());
            }
            else {
                redeLaTeX.append(Rede_only_comments());
            }
        }


        return redeLaTeX;
    }

    /**
     * Helpermethod to exchange LaTeX specific characters
     * @param texcode
     * @return
     */
    private String TeXCleanup(String texcode){
        return texcode.replace("&", "\\&")
                .replace("%", "\\%")
                .replace("$", "\\$")
                .replace("#", "\\#")
                .replace("_", "\\_");
    }

    /**
     * Generates the contents of the speech into the xmi format
     * @return
     */
    public String toXmiSpeech() {
        StringBuilder speechXmi = new StringBuilder();

        speechXmi.append("      <types:Speech");
        speechXmi.append(" xmi:id=\"").append(getRede_ID()).append("\"");
        speechXmi.append(" text=\"").append(escapeXml(getText())).append("\"");
        speechXmi.append(" date=\"").append(getDate_Mongo_Impl()).append("\"");
        speechXmi.append(" wahlperiode=\"").append(getWahlperiodeNummer()).append("\"");
        speechXmi.append(" sitzungsnummer=\"").append(getSitzungsnummer()).append("\"");
        speechXmi.append(">\n");

        if (getTagesordnungspunkt() != null && !getTagesordnungspunkt().isEmpty()) {
            speechXmi.append("        <tagesordnungspunkt>").append(escapeXml(getTagesordnungspunkt())).append("</tagesordnungspunkt>\n");
        }
        if (getTOP_Beschreibung() != null && !getTOP_Beschreibung().isEmpty()) {
            speechXmi.append("        <topBeschreibung>").append(escapeXml(getTOP_Beschreibung())).append("</topBeschreibung>\n");
        }

        try {
            JCas documentJCas = this.toJCasFromDatabaseDocumentJSON("JCas_JSON_RedeText");
            if (documentJCas != null) {
                JCAS_Extractor_Impl jcasExtractor = new JCAS_Extractor_Impl(documentJCas);

                speechXmi.append("        <namedEntities>\n");
                NamedEntity_Extraction_Impl namedEntityExtractor = jcasExtractor.namedEntityExtraction();
                List<NamedEntity> namedEntities = namedEntityExtractor.getAllNamedEntity();
                for (NamedEntity namedEntity : namedEntities) {
                    speechXmi.append("          <namedEntity");
                    speechXmi.append(" value=\"").append(escapeXml(namedEntity.getValue())).append("\"");
                    speechXmi.append(" begin=\"").append(namedEntity.getBegin()).append("\"");
                    speechXmi.append(" end=\"").append(namedEntity.getEnd()).append("\"");
                    speechXmi.append(" coveredText=\"").append(escapeXml(namedEntity.getCoveredText())).append("\"");
                    speechXmi.append("/>\n");
                }
                speechXmi.append("        </namedEntities>\n");

                speechXmi.append("        <posTags>\n");
                POS_Extraction_Impl posExtractor = jcasExtractor.posExtraction();
                List<POS> posTags = posExtractor.getAllPOS();
                for (POS pos : posTags) {
                    speechXmi.append("          <pos");
                    speechXmi.append(" coarseValue=\"").append(escapeXml(pos.getCoarseValue())).append("\"");
                    speechXmi.append(" posValue=\"").append(escapeXml(pos.getPosValue())).append("\"");
                    speechXmi.append(" begin=\"").append(pos.getBegin()).append("\"");
                    speechXmi.append(" end=\"").append(pos.getEnd()).append("\"");
                    speechXmi.append(" coveredText=\"").append(escapeXml(pos.getCoveredText())).append("\"");
                    speechXmi.append("/>\n");
                }
                speechXmi.append("        </posTags>\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        speechXmi.append("      </types:Speech>\n");

        return speechXmi.toString();
    }


    /**
     * Helper method to escape XML special characters
     * @param input
     * @return
     */
    private String escapeXml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
