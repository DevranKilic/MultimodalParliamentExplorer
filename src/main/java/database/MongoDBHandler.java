package database;

import Class_Impl.*;
import Class_MongoDB_Impl.Abgeordneter_MongoDB_Impl;
import Helper.HTML_Creator;
import Helper.Hilfsmethoden;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Diese Klasse stellt die Verbindung zur MongoDB auf und enthält verschiedene Funktionen zum Lesen, Löschen, Bearbeiten und Suchen der Daten.
 * Diese Klasse enthält zudem Methoden welche Objekte entgegen nimmt und diese in die MongoDB hochlädt.
 * Die Verbindungsdaten werden aus der MongoDBConfig.properties entnommen.
 */
public class MongoDBHandler {

    private MongoDatabase Database;
    private MongoDBConfig Config;


    /**
     * Initialisert eine Verbindung mit der MongoDB
     */
    public MongoDBHandler() {
        this.Config = new MongoDBConfig();

        ServerAddress srvAddr1 = new ServerAddress(Config.getRemote_host(), Config.getRemote_port());
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Arrays.asList(srvAddr1)))
                .applyToSocketSettings(builder -> builder.connectTimeout(30, TimeUnit.SECONDS))
                .credential(MongoCredential.createCredential(Config.getRemote_user(), Config.getRemote_database_credential(), Config.getRemote_password().toCharArray())).build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase pDatabase = mongoClient.getDatabase(Config.getRemote_database());

        this.Database = pDatabase;
    }


    /**
     * Zeigt alle vorhandenen Collections in der DB an.
     */
    public void ShowCollections() {
        MongoDatabase Database = getDatabase();
        ListCollectionNamesIterable Collections = (ListCollectionNamesIterable) Database.listCollectionNames();
        System.out.println("Folgende Collections existieren:");

        for (String Collection : Collections) {
            System.out.println(Collection);
        }

    }

    /**
     * Diese Methode löscht alle vorhandenen Collections in der MongoDB
     */
    public void DeleteAllCollections() {
        MongoDatabase Database = getDatabase();
        ListCollectionNamesIterable Collections = (ListCollectionNamesIterable) Database.listCollectionNames();

        for (String Collection : Collections) {
            MongoCollection<Document> pCollection = Database.getCollection(Collection);
            pCollection.drop();
        }
        System.out.println("Alle Collections in "+Database.getName()+" Datenbank wurden gelöscht");
    }

    /**
     * Fügt ein Dokument mit einem Key Value Paar in einer bestimmten Collection hinzu.
     * @param Collection
     * @param Key
     * @param Value
     * @param <V>
     */
    public <V> void insert(String Collection, String Key,V Value) {
        MongoCollection<Document> pCollection = this.getDatabase().getCollection(Collection);

        Document MyInsertObject = new Document();
        MyInsertObject.append(Key,Value);

        pCollection.insertOne(MyInsertObject);
    }

    /**
     * Sucht in einer bestimmten Collection nach einem Dokument mithilfe eines Key und Value Suchabfrage und
     * ermöglicht eine Modifikation des Dokuments, dabei ist der modparam frei auswählbar.
     * Ein neues Key Value Paar wird entgegengenommen.
     * @param Collection
     * @param Key_query
     * @param Value_query
     * @param modparam
     * @param Key
     * @param Value
     * @param <V>
     */
    public <V> void update(String Collection, String Key_query, V Value_query, String modparam, String Key, V Value) {

        // Adjustable Property
        Document updateObject = new Document();
        updateObject.put(Key, Value);
        // modparam`s $set $unset $inc $push $each
        // $slice $sort $ne $addToSet $pop $pull
        Document updateCMD = new Document();
        updateCMD.put(modparam, updateObject);
        // Query
        Document query = new Document();
        query.put(Key_query, Value_query);
        // Update
        this.getDatabase().getCollection(Collection).updateOne(query, updateCMD);

        //Example
        //this.getDatabase().getCollection("Speeches").updateOne(new Document("Name", "John"), new Document("$set", new Document("Age", 21)));
    }


    /**
     * Such in einer bestimmten Collection mithilfe eines Key und Value Suchabfrage
     * @param Collection
     * @param Key_query
     * @param Value_query
     * @return
     * @param <V>
     */
    public <V> Document query_output(String Collection, String Key_query, V Value_query) {

        Document searchQuery = new Document();
        searchQuery.put(Key_query, Value_query);
        FindIterable OutputIterable = this.getDatabase().getCollection(Collection).find(searchQuery);

        return (Document) OutputIterable.first();
    }

    /**
     * Sucht nach einem bestimmten Dokument in einer Collection mithilfe der MongoID
     * @param sCollection
     * @param MongoID
     * @return
     */
    public Document getDocumentByMongoID(String sCollection, int MongoID) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("MongoID", MongoID);

        Document MongoID_match = pCollection.find(searchQuery).first();

        return MongoID_match;
    }

    /**
     * Sucht ein einzelnes Dokument mit einem bestimmten Key und Value.
     * @param Collection
     * @param Key_query
     * @param Value_query
     * @return
     * @param <V>
     */
    public <V> Document getDocument(String Collection, String Key_query, V Value_query) {

        Document searchQuery = new Document();
        searchQuery.put(Key_query, Value_query);
        return this.getDatabase().getCollection(Collection).find(searchQuery).first();
    }


    /**
     * Löscht ein Dokument in einer Collection mit einem bestimmten Key und Value
     * @param Collection
     * @param Key
     * @param Value
     * @return
     * @param <V>
     */
    public <V> boolean delete(String Collection, String Key, V Value) {
        MongoCollection<Document> pCollection = this.getDatabase().getCollection(Collection);
        Document searchQuery = new Document();
        searchQuery.put(Key, Value);

        return pCollection.deleteOne(searchQuery).wasAcknowledged();
    }


    /**
     * Diese Methode zählt die Anzahl der Dokumente in einer bestimmten Collection und mit einem bestimmten Wert.
     * @param sCollection
     * @param Key
     * @param Value
     * @return
     * @param <V>
     */
    public <V> Long count(String sCollection, String Key, V Value) {

        Document searchQuery = new Document();
        searchQuery.put(Key, Value);
        return this.getDatabase().getCollection(sCollection).countDocuments(searchQuery);
    }


    /**
     * Diese Methode zählt die Anzahl der Dokumente in einer bestimmten Collection
     * @param sCollection
     * @return
     */
    public Long countDocuments(String sCollection) {
        return this.getDatabase().getCollection(sCollection).countDocuments();
    }

    /**
     * Diese Methode erlaubt es in der MongoDB nach einem bestimmten Textausschnitt zu suchen
     * @param sCollection
     * @param SearchingString
     * @return
     */
    public List<Document> FullTextSearch(String sCollection, String SearchingString) {
        MongoCollection<Document> pCollection = this.getDatabase().getCollection(sCollection);
        Document searchQuery = Document.parse("$text: { $search: \"" + SearchingString + "\"}}");

        FindIterable results = pCollection.find(searchQuery);
        List<Document> ResultList = new ArrayList<>();
        for (Object result : results) {
            ResultList.add((Document) result);
        }

        return ResultList;
    }


    /**
     * Diese Methode prüft anhand der MongoID ob ein Document in der MongoDB bereits existiert
     * @param sCollection
     * @param MongoID
     * @return
     */
    public Boolean Check_if_double(String sCollection, int MongoID){
        Boolean isDouble = null;

        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        BasicDBObject query = new BasicDBObject();
        query.put("MongoID", MongoID);
        List empy_or_not = new ArrayList<>();
        FindIterable iter = pCollection.find(query);
        for (Object o : iter) {
            empy_or_not.add(o);
        }
        if (empy_or_not.isEmpty()){
            isDouble = false;
        } else {
            System.out.println("MongoID "+MongoID+" befindet sich bereits in der Collection "+sCollection);
            isDouble = true;
        }

        return isDouble;
    }


    /**
     * @param MyMongoDBHandler
     * @param AlleAbgeordnete
     */
    public void BilderUploader(MongoDBHandler MyMongoDBHandler,Set<Abgeordneter_Impl> AlleAbgeordnete){
        MongoCollection pCollection = getCollection("Abgeordneter");
        for (Abgeordneter_Impl abgeordneter : AlleAbgeordnete) {
            if (!(abgeordneter.getBild_Base64()== null) && !(abgeordneter.getBild_Base64().isEmpty())){
                Document Abgeordneter = getDocumentByMongoID("Abgeordneter",abgeordneter.getMongoID());
                //Anzupassende Eingenschaft
                Document update_doc = new Document();
                update_doc.put("BildBase64", abgeordneter.getBild_Base64());
                //Suchabfrage
                Abgeordneter.put("MongoID",abgeordneter.getMongoID());
                //Modifikationsparameter
                Document UpdateCMD = new Document();
                UpdateCMD.put("$set", update_doc);
                //Update
                try{
                    pCollection.updateOne(Abgeordneter, UpdateCMD);
                    System.out.println("Bild für folgenden Abgeordneten erkannt: "+Abgeordneter.getString("Vorname")+ " " + Abgeordneter.getString("Nachname"));
                    HTML_Creator.Create_Abgeordneter_RedePortfolio(MyMongoDBHandler,Abgeordneter.getInteger("MongoID"));
                } catch (org.bson.BsonMaximumSizeExceededException e){
                    System.out.println("Die Bilddatei von "+Abgeordneter.getString("Vorname")+ " " + Abgeordneter.getString("Nachname")+" ist zu Groß");
                    e.printStackTrace();
                }

            }
        }
    }

    public MongoCollection<Document> getCollection(String sCollection){
        return this.getDatabase().getCollection(sCollection);
    }

    public <V> boolean CheckExistanceByCriterion(String Collection, String CriterionKey, V CriterionValue){
        return this.getDatabase().getCollection(Collection).find(new Document(CriterionKey, CriterionValue)).first() != null;
    }

    /**
     * @param sCollection
     * @param AlleAbgeordnete
     */
    public void Abgeordneter_Uploader(String sCollection, Set<Abgeordneter_Impl> AlleAbgeordnete) {
        SimpleDateFormat DD_MM_JJJJ = new SimpleDateFormat("dd.MM.yyyy");


        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Abgeordnete_Dokumente = new ArrayList<>();

        for (Abgeordneter_Impl abgeordneterImpl : AlleAbgeordnete) {
            Document Abgeordneter = new Document();

            Abgeordneter.put("MongoID", abgeordneterImpl.getMongoID());


            Bson Filter = Filters.and(
                    Filters.eq("ID", abgeordneterImpl.getID()),
                    Filters.eq("Nachname", abgeordneterImpl.getName()),
                    Filters.eq("Vorname", abgeordneterImpl.getVorname()),
                    Filters.eq("Geburtsdatum", abgeordneterImpl.getGeburtsdatum())
            );

            Document DBcheckExists = Database.getCollection("Abgeordneter").find(Filter).first();

            if (DBcheckExists!=null){
                Abgeordneter_MongoDB_Impl Abgeordneter_DB = new Abgeordneter_MongoDB_Impl(DBcheckExists);
                //Bsp.
                Abgeordneter.put("Wichtige Daten", Abgeordneter_DB.getBildhistorieMongoIDs());

                //Database.getCollection("Abgeordneter").deleteOne(Filter);
            }
            Abgeordneter.put("MongoLabel", abgeordneterImpl.getMongoLabel());
            Abgeordneter.put("ID", abgeordneterImpl.getID());
            Abgeordneter.put("Nachname", abgeordneterImpl.getName());
            Abgeordneter.put("Vorname", abgeordneterImpl.getVorname());
            Abgeordneter.put("Ortszusatz", abgeordneterImpl.getOrtzusatz());
            Abgeordneter.put("Praefix", abgeordneterImpl.getPraefix());
            Abgeordneter.put("Adel", abgeordneterImpl.getAdel());
            Abgeordneter.put("Anrede_Titel", abgeordneterImpl.getAnrede());
            Abgeordneter.put("Akad_Titel", abgeordneterImpl.getAkadTitel());
            if (abgeordneterImpl.getGeburtsdatum()!=null){
                Abgeordneter.put("Geburtsdatum", DD_MM_JJJJ.format(abgeordneterImpl.getGeburtsdatum()));
            }
            Abgeordneter.put("Geburtsort", abgeordneterImpl.getGeburtsort());
            if (abgeordneterImpl.getSterbedatum()!=null){
                Abgeordneter.put("Sterbedatum", DD_MM_JJJJ.format(abgeordneterImpl.getSterbedatum()));
            }
            Abgeordneter.put("Geschlecht", abgeordneterImpl.getGeschlecht());
            Abgeordneter.put("Religion", abgeordneterImpl.getReligion());
            Abgeordneter.put("Beruf", abgeordneterImpl.getBeruf());
            Abgeordneter.put("Vita_kurz", abgeordneterImpl.getVita());
            if (abgeordneterImpl.getHistorie_von()!=null){
                Abgeordneter.put("Historie_von", DD_MM_JJJJ.format(abgeordneterImpl.getHistorie_von()));
            }
            if (abgeordneterImpl.getHistorie_bis()!=null){
                Abgeordneter.put("Historie_bis", DD_MM_JJJJ.format(abgeordneterImpl.getHistorie_bis()));
            }
            Abgeordneter.put("Geburtsland", abgeordneterImpl.getGeburtsland());
            Abgeordneter.put("Familienstand", abgeordneterImpl.getFamilienstand());
            Abgeordneter.put("Partei_kurz", abgeordneterImpl.getPartei_kurz());
            Abgeordneter.put("Veröffentlichungspflichtiges", abgeordneterImpl.getVeröffentlichungspflichtiges());
            Abgeordneter.put("BildBase64",abgeordneterImpl.getBild_Base64());
            Abgeordneter.put("hq_picture",abgeordneterImpl.getHq_picture());
            Abgeordneter.put("picture_description",abgeordneterImpl.getPicture_description());

            if (abgeordneterImpl.getPartei()!=null){
                Abgeordneter.put("Partei", abgeordneterImpl.getPartei().getMongoID());
            }

            List<Integer> Wahlperioden = new ArrayList<>();
            Set<Wahlperioden_Impl> Wahlperioden_des_Abg = abgeordneterImpl.getWahlperioden();
            for (Wahlperioden_Impl wahlperiodenImpl : Wahlperioden_des_Abg) {
                if (wahlperiodenImpl !=null){
                    Wahlperioden.add(wahlperiodenImpl.getMongoID());
                }
            }
            Abgeordneter.put("Wahlperioden",Wahlperioden);

            List<Integer> Institutionen = new ArrayList<>();
            Set<Institution_Impl> Institutionen_des_Abg = abgeordneterImpl.getInstitutionen();
            for (Institution_Impl institutionImpl : Institutionen_des_Abg) {
                if (institutionImpl !=null){
                    Institutionen.add(institutionImpl.getMongoID());
                }
            }
            Abgeordneter.put("Institutionen",Institutionen);

            List<Integer> Reden = new ArrayList<>();
            Set<Rede_Impl> Reden_des_Abg = abgeordneterImpl.getReden();
            for (Rede_Impl redeImpl : Reden_des_Abg) {
                if (redeImpl !=null){
                    Reden.add(redeImpl.getMongoID());
                }
            }

            Abgeordneter.put("Reden",Reden);



            Abgeordnete_Dokumente.add(Abgeordneter);
        }
        if (!Abgeordnete_Dokumente.isEmpty()){
            pCollection.insertMany(Abgeordnete_Dokumente);
        }

        System.out.println("Es wurde "+Abgeordnete_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }

    /**
     * @param sCollection
     * @param AlleAusschusse
     */
    public void Ausschuss_Uploader(String sCollection, Set<Ausschuss_Impl> AlleAusschusse) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Ausschuss_Dokumente = new ArrayList<>();

        for (Ausschuss_Impl ausschussImpl : AlleAusschusse) {
            Document Ausschuss = new Document();




            Ausschuss.put("MongoID", ausschussImpl.getMongoID());
            Ausschuss.put("MongoLabel", ausschussImpl.getMongoLabel());
            Ausschuss.put("Ausschuss_Art", ausschussImpl.getAusschuss_Art());
            Ausschuss.put("Beschreibung", ausschussImpl.getBeschreibung());

            Map<Integer,List<Integer>> WP_Nr_Abgeorndeten_IDs = ausschussImpl.getWP_Nr_Abgeorndeten_IDs();
            if (WP_Nr_Abgeorndeten_IDs!=null){
                Map<String, List<String>> WP_Nr_Abgeorndeten_IDs_asString = Hilfsmethoden.MapFormatter1(WP_Nr_Abgeorndeten_IDs);
                Ausschuss.put("WP_Nr_Abgeorndeten_IDs",WP_Nr_Abgeorndeten_IDs_asString);
            }

            Ausschuss_Dokumente.add(Ausschuss);
        }
        if (!Ausschuss_Dokumente.isEmpty()){
            pCollection.insertMany(Ausschuss_Dokumente);
        }

        System.out.println("Es wurde "+Ausschuss_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }

    /**
     * @param sCollection
     * @param AlleFraktionen
     */
    public void Fraktion_Uploader(String sCollection, Set<Fraktion_Impl> AlleFraktionen) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Fraktion_Dokumente = new ArrayList<>();

        for (Fraktion_Impl fraktionImpl : AlleFraktionen) {
            Document Fraktion = new Document();
            if (Check_if_double(sCollection, fraktionImpl.getMongoID())){
                continue;
            }
            Fraktion.put("MongoID", fraktionImpl.getMongoID());
            Fraktion.put("MongoLabel", fraktionImpl.getMongoLabel());
            Fraktion.put("Name", fraktionImpl.getName());

            Map<Integer,Set<Integer>> Abgeordneter_ID_WP_Nr = fraktionImpl.getAbgeordneter_ID_WP_Nr();
            if (Abgeordneter_ID_WP_Nr!=null){
                Map<String,List<String>> Abgeordneter_ID_WP_Nr_asString = Hilfsmethoden.MapFormatter2(Abgeordneter_ID_WP_Nr);
                Fraktion.put("Abgeordneter_ID_WP_Nr",Abgeordneter_ID_WP_Nr_asString);
            }

            Fraktion_Dokumente.add(Fraktion);
        }
        if (!Fraktion_Dokumente.isEmpty()){
            pCollection.insertMany(Fraktion_Dokumente);
        }

        System.out.println("Es wurde "+Fraktion_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }

    /**
     * @param sCollection
     * @param AlleInstitutionen
     */
    public void Institution_Uploader(String sCollection, Set<Institution_Impl> AlleInstitutionen) {
        SimpleDateFormat DD_MM_JJJJ = new SimpleDateFormat("dd.MM.yyyy");

        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Institution_Dokumente = new ArrayList<>();

        for (Institution_Impl institutionImpl : AlleInstitutionen) {
            Document Institution = new Document();
            if (Check_if_double(sCollection, institutionImpl.getMongoID())){
                continue;
            }
            Institution.put("MongoID", institutionImpl.getMongoID());
            Institution.put("MongoLabel", institutionImpl.getMongoLabel());
            Institution.put("Insart_Lang", institutionImpl.getInsart_Lang());
            Institution.put("Ins_Lang", institutionImpl.getIns_Lang());
            if (institutionImpl.getMDBins_Von()!=null){
                Institution.put("MDBins_Von", DD_MM_JJJJ.format(institutionImpl.getMDBins_Von()));
            }
            if (institutionImpl.getMDBins_Bis()!=null){
                Institution.put("MDBins_Bis", DD_MM_JJJJ.format(institutionImpl.getMDBins_Bis()));
            }
            Institution.put("Fkt_lang", institutionImpl.getFkt_lang());
            if (institutionImpl.getFktins_Von()!=null){
                Institution.put("Fktins_Von", DD_MM_JJJJ.format(institutionImpl.getFktins_Von()));
            }
            if (institutionImpl.getFktins_Bis()!=null){
                Institution.put("Fktins_Bis", DD_MM_JJJJ.format(institutionImpl.getFktins_Bis()));
            }
            if (institutionImpl.getWahlperiode()!=null){
                Institution.put("Wahlperioden", institutionImpl.getWahlperiode().getMongoID());
            }

            Institution_Dokumente.add(Institution);
        }
        if (!Institution_Dokumente.isEmpty()){
            pCollection.insertMany(Institution_Dokumente);
        }

        System.out.println("Es wurde "+Institution_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }

    /**
     * @param sCollection
     * @param AlleParteien
     */
    public void Partei_Uploader(String sCollection, Set<Partei_Impl> AlleParteien) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Partei_Dokumente = new ArrayList<>();

        for (Partei_Impl parteiImpl : AlleParteien) {
            Document Partei = new Document();
            if (Check_if_double(sCollection, parteiImpl.getMongoID())){
                continue;
            }
            Partei.put("MongoID", parteiImpl.getMongoID());
            Partei.put("MongoLabel", parteiImpl.getMongoLabel());
            Partei.put("Name", parteiImpl.getName());

            List<Integer> Mitglieder = new ArrayList<>();
            Set<Abgeordneter_Impl> Mitglieder_der_Partei = parteiImpl.getMitglieder();
            for (Abgeordneter_Impl abgeordneterImpl : Mitglieder_der_Partei) {
                if (abgeordneterImpl != null){
                    Mitglieder.add(abgeordneterImpl.getMongoID());
                }
            }
            Partei.put("Mitglieder",Mitglieder);

            Partei_Dokumente.add(Partei);
        }
        if (!Partei_Dokumente.isEmpty()){
            pCollection.insertMany(Partei_Dokumente);
        }

        System.out.println("Es wurde "+Partei_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }


    /**
     * @param sCollection
     * @param AlleReden
     */
    public void Rede_Uploader(String sCollection, Set<Rede_Impl> AlleReden) {
        SimpleDateFormat DD_MM_JJJJ = new SimpleDateFormat("dd.MM.yyyy");

        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Rede_Dokumente = new ArrayList<>();

        for (Rede_Impl redeImpl : AlleReden) {

            Document Rede = new Document();
            if (Check_if_double(sCollection, redeImpl.getMongoID())){
                continue;
            }

            Rede.put("MongoID", redeImpl.getMongoID());
            Rede.put("MongoLabel", redeImpl.getMongoLabel());
            if (redeImpl.getRedner()!=null){
                Rede.put("Abgeordneter", redeImpl.getRedner().getMongoID());
            }
            Rede.put("WahlperiodeNummer", redeImpl.getWahlperiodeNummer());
            Rede.put("Sitzungsnummer", redeImpl.getSitzungsnummer());
            Rede.put("Ort", redeImpl.getOrt());
            if (redeImpl.getDate()!=null){
                Rede.put("Datum", DD_MM_JJJJ.format(redeImpl.getDate()));
            }
            Rede.put("Rede_ID", redeImpl.getRede_ID());
            Rede.put("Redner_ID", redeImpl.getRedner_ID());
            Rede.put("Vorname", redeImpl.getVorname());
            Rede.put("Nachname", redeImpl.getNachname());
            Rede.put("Fraktion", redeImpl.getFraktion());
            Rede.put("Rolle_lang", redeImpl.getRolla_lang());
            Rede.put("Rolle_kurz", redeImpl.getRolle_kurz());
            Rede.put("Tagesordnungspunkt",redeImpl.getTagesordnungspunkt());
            Rede.put("Rede", redeImpl.getText());
            Rede.put("Rede_mit_Kommentaren", redeImpl.getRede_mit_Kommentaren());
            Rede.put("TOPBeschreibung",redeImpl.getTOP_Beschreibung());
            Rede.put("RedeAuszugMongoIDs",redeImpl.getRedeAuszugMongoIDs());

            Rede_Dokumente.add(Rede);

            System.out.println("Aktuelle Rede: "+Rede.get("Rede_ID"));
        }
        if (!Rede_Dokumente.isEmpty()){
            pCollection.insertMany(Rede_Dokumente);
        }

        System.out.println("Es wurde "+Rede_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }

    /**
     * @param sCollection
     * @param AlleSitzungen
     */
    public void Sitzung_Uploader(String sCollection, Set<Sitzung_Impl> AlleSitzungen) {
        SimpleDateFormat DD_MM_JJJJ = new SimpleDateFormat("dd.MM.yyyy");
        DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Sitzung_Dokumente = new ArrayList<>();

        for (Sitzung_Impl sitzungImpl : AlleSitzungen) {

            Document Sitzung = new Document();
            if (Check_if_double(sCollection, sitzungImpl.getMongoID())){
                continue;
            }
            Sitzung.put("MongoID", sitzungImpl.getMongoID());
            Sitzung.put("MongoLabel", sitzungImpl.getMongoLabel());
            Sitzung.put("WP_Nummer", sitzungImpl.getWP_Nummer());
            Sitzung.put("Sitzungsnr", sitzungImpl.getSitzungsnr());
            Sitzung.put("Sitzungs_Ort", sitzungImpl.getSitzungs_Ort());
            if (sitzungImpl.getSitzungsbeginn()!=null){
                Sitzung.put("Sitzungsbeginn", sitzungImpl.getSitzungsbeginn().format(HH_MM));
            }
            if (sitzungImpl.getSitzungsende()!=null){
                Sitzung.put("Sitzungsende", sitzungImpl.getSitzungsende().format(HH_MM));
            }
            if (sitzungImpl.getSitzungsdatum()!=null){
                Sitzung.put("Sitzungsdatum", DD_MM_JJJJ.format(sitzungImpl.getSitzungsdatum()));
            }
            List<Integer> Tagesordnungspunkte = new ArrayList<>();
            Set<Tagesordnungspunkt_Impl> TOP_von_Sitzung = sitzungImpl.getTagesordnungspunkte();
            for (Tagesordnungspunkt_Impl tagesordnungspunktImpl : TOP_von_Sitzung) {
                if(tagesordnungspunktImpl !=null){
                    Tagesordnungspunkte.add(tagesordnungspunktImpl.getMongoID());
                }
            }
            Sitzung.put("Tagesordnungspunkt",Tagesordnungspunkte);

            Sitzung_Dokumente.add(Sitzung);
        }
        if (!Sitzung_Dokumente.isEmpty()){
            pCollection.insertMany(Sitzung_Dokumente);
        }

        System.out.println("Es wurde "+Sitzung_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }

    /**
     * @param sCollection
     * @param AlleTagesordnungspunkte
     */
    public void Tagesordnungspunkt_Uploader(String sCollection, Set<Tagesordnungspunkt_Impl> AlleTagesordnungspunkte) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Tagesordnungspunkt_Dokumente = new ArrayList<>();

        for (Tagesordnungspunkt_Impl tagesordnungspunktImpl : AlleTagesordnungspunkte) {
            Document Tagesordnungspunkt = new Document();
            if (Check_if_double(sCollection, tagesordnungspunktImpl.getMongoID())){
                continue;
            }

            Tagesordnungspunkt.put("MongoID", tagesordnungspunktImpl.getMongoID());
            Tagesordnungspunkt.put("MongoLabel", tagesordnungspunktImpl.getMongoLabel());
            if(tagesordnungspunktImpl.getSitzung()!=null){
                Tagesordnungspunkt.put("Sitzung", tagesordnungspunktImpl.getSitzung().getMongoID());
            }
            Tagesordnungspunkt.put("Tagesordnungsname", tagesordnungspunktImpl.getTagesordnungsname());

            List<String> Rede_IDs = new ArrayList<>();
            Set<String> Rede_IDs_in_TOP = tagesordnungspunktImpl.getRede_IDs();
            for (String RedeID_String : Rede_IDs_in_TOP) {
                if (RedeID_String!=null){
                    Rede_IDs.add(RedeID_String);
                }
            }
            Tagesordnungspunkt.put("Rede_IDs",Rede_IDs);



            Tagesordnungspunkt_Dokumente.add(Tagesordnungspunkt);
        }
        if (!Tagesordnungspunkt_Dokumente.isEmpty()){
            pCollection.insertMany(Tagesordnungspunkt_Dokumente);
        }

        System.out.println("Es wurde "+Tagesordnungspunkt_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }


    /**
     * @param sCollection
     * @param AlleWahlkreise
     */
    public void Wahlkreis_Uploader(String sCollection, Set<Wahlkreis_Impl> AlleWahlkreise) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Wahlkreis_Dokumente = new ArrayList<>();

        for (Wahlkreis_Impl wahlkreisImpl : AlleWahlkreise) {

            Document Wahlkreis = new Document();

            if (Check_if_double(sCollection, wahlkreisImpl.getMongoID())){
                continue;
            }
            Wahlkreis.put("MongoID", wahlkreisImpl.getMongoID());
            Wahlkreis.put("MongoLabel", wahlkreisImpl.getMongoLabel());
            Wahlkreis.put("WKR_Nummer", wahlkreisImpl.getNumber());
            Wahlkreis.put("WKR_Name", wahlkreisImpl.getWKR_Name());
            Wahlkreis.put("WKR_Land", wahlkreisImpl.getWKR_Land());
            Wahlkreis.put("WKR_Liste", wahlkreisImpl.getWKR_Liste());

            if (wahlkreisImpl.getWahlperiode()!=null){
                Wahlkreis.put("Wahlperiode", wahlkreisImpl.getMongoID());
            }


            Wahlkreis_Dokumente.add(Wahlkreis);
        }
        if (!Wahlkreis_Dokumente.isEmpty()){
            pCollection.insertMany(Wahlkreis_Dokumente);
        }

        System.out.println("Es wurde "+Wahlkreis_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }

    /**
     * @param sCollection
     * @param AlleWahlperiode
     */
    public void Wahlperiode_Uploader(String sCollection, Set<Wahlperiode_Impl> AlleWahlperiode) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Wahlperiode_Dokumente = new ArrayList<>();

        for (Wahlperiode_Impl wahlperiodeImpl : AlleWahlperiode) {

            Document Wahlperiode = new Document();

            if (Check_if_double(sCollection, wahlperiodeImpl.getMongoID())){
                continue;
            }
            Wahlperiode.put("MongoID", wahlperiodeImpl.getMongoID());
            Wahlperiode.put("MongoLabel", wahlperiodeImpl.getMongoLabel());
            Wahlperiode.put("WP_Nummer", wahlperiodeImpl.getWP_Nummer());

            List<Integer> Abgeordneten_IDs = new ArrayList<>();
            Set<Integer> Abgeordneten_IDs_von_Wahlperiode = wahlperiodeImpl.getAbgeordneten_IDs();
            for (Integer ID : Abgeordneten_IDs_von_Wahlperiode) {
                if (ID!=null){
                    Abgeordneten_IDs.add(ID);
                }
            }
            Wahlperiode.put("Abgeordneten_IDs",Abgeordneten_IDs);

            Wahlperiode_Dokumente.add(Wahlperiode);
        }
        if (!Wahlperiode_Dokumente.isEmpty()){
            pCollection.insertMany(Wahlperiode_Dokumente);
        }

        System.out.println("Es wurde "+Wahlperiode_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }


    /**
     * @param sCollection
     * @param AlleWahlperioden
     */
    public void Wahlperioden_Uploader(String sCollection, Set<Wahlperioden_Impl> AlleWahlperioden) {
        SimpleDateFormat DD_MM_JJJJ = new SimpleDateFormat("dd.MM.yyyy");

        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> Wahlperioden_Dokumente = new ArrayList<>();

        for (Wahlperioden_Impl wahlperiodenImpl : AlleWahlperioden) {

            Document Wahlperiode = new Document();
            if (Check_if_double(sCollection, wahlperiodenImpl.getMongoID())){
                continue;
            }
            Wahlperiode.put("MongoID", wahlperiodenImpl.getMongoID());
            Wahlperiode.put("MongoLabel", wahlperiodenImpl.getMongoLabel());
            Wahlperiode.put("WP", wahlperiodenImpl.getNumber());
            if (wahlperiodenImpl.getStartDate()!=null){
                Wahlperiode.put("WP_Von", DD_MM_JJJJ.format(wahlperiodenImpl.getStartDate()));
            }

            if (wahlperiodenImpl.getEndDate()!=null){
                Wahlperiode.put("WP_Bis", DD_MM_JJJJ.format(wahlperiodenImpl.getEndDate()));
            }
            Wahlperiode.put("Liste", wahlperiodenImpl.getListe());
            Wahlperiode.put("Mandatsart", wahlperiodenImpl.getMandatsart());
            if (wahlperiodenImpl.getWahlkreis()!=null){
                Wahlperiode.put("Wahlkreis", wahlperiodenImpl.getWahlkreis().getMongoID());
            }


            List<Integer> Institutionen = new ArrayList<>();
            Set<Institution_Impl> Institutionen_in_Wahlperioden = wahlperiodenImpl.getInstitutionen();
            for (Institution_Impl institutionImpl : Institutionen_in_Wahlperioden) {
                if (institutionImpl !=null){
                    Institutionen.add(institutionImpl.getMongoID());
                }
            }
            Wahlperiode.put("Institutionen",Institutionen);

            Wahlperioden_Dokumente.add(Wahlperiode);
        }
        if (!Wahlperioden_Dokumente.isEmpty()){
            pCollection.insertMany(Wahlperioden_Dokumente);
        }

        System.out.println("Es wurde "+Wahlperioden_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }


    /**
     * @param sCollection
     * @param AlleRedeAuszuge
     */
    public void RedeAuszug_Uploader(String sCollection, List<RedeAuszug_Impl> AlleRedeAuszuge) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        List<Document> RedeAuszug_Dokumente = new ArrayList<>();

        for (RedeAuszug_Impl RedeAuszugImpl : AlleRedeAuszuge) {

            Document RedeAuszug = new Document();

            if (Check_if_double(sCollection, RedeAuszugImpl.getMongoID())){
                continue;
            }


            RedeAuszug.put("MongoID", RedeAuszugImpl.getMongoID());
            RedeAuszug.put("MongoLabel", RedeAuszugImpl.getMongoLabel());
            RedeAuszug.put("Auszug", RedeAuszugImpl.getRedeAuszug());
            RedeAuszug.put("isKommentar", RedeAuszugImpl.getIsKommentar());
            RedeAuszug.put("Fraktion", RedeAuszugImpl.getFraktionMongoID());
            RedeAuszug.put("Abgeordneter", RedeAuszugImpl.getAbgeordneterMongoID());

            RedeAuszug_Dokumente.add(RedeAuszug);

            //System.out.println("Aktueller RedeAuszug: "+RedeAuszug.get("Kommentar"));
        }
        if (!RedeAuszug_Dokumente.isEmpty()){
            pCollection.insertMany(RedeAuszug_Dokumente);
        }

        System.out.println("Es wurde "+RedeAuszug_Dokumente.size()+" Objekte in die Collection "+sCollection+ " hochgeladen.");
    }


    /**
     * @param sCollection
     * @param Historie
     */
    public void Historie_single_Uploader(String sCollection, Historie_Impl Historie) {
        MongoDatabase Database = getDatabase();
        MongoCollection<Document> pCollection = Database.getCollection(sCollection);

        Document HistorieDoc = new Document();

        if (Check_if_double(sCollection, Historie.getMongoID())){
            return;
        }
        HistorieDoc.put("MongoID", Historie.getMongoID());
        HistorieDoc.put("MongoLabel", Historie.getMongoLabel());
        HistorieDoc.put("OldValue1", Historie.getOldValue1());
        HistorieDoc.put("NewValue1", Historie.getNewValue1());
        HistorieDoc.put("OldValue2", Historie.getOldValue2());
        HistorieDoc.put("NewValue2", Historie.getNewValue2());
        HistorieDoc.put("Date", Historie.getDate());
        HistorieDoc.put("Time", Historie.getTime());

        pCollection.insertOne(HistorieDoc);

        System.out.println("Es wurde 1 Objekt in die Collection "+sCollection+ " hochgeladen.");
    }

    public MongoDatabase getDatabase() {
        return Database;
    }

    /**
     * Gets all unanalysed speeches from the MongoDB.
     * @param collectionName name of the collection.
     * @param fieldname The fieldname that is searched.
     * @return
     */
    public List<Document> getUnanalysedDocs(String collectionName, String fieldname) {
        MongoDatabase database = getDatabase();
        MongoCollection<Document> collection = Database.getCollection(collectionName);
        return collection.find(new Document(fieldname, new Document("$exists", false))).into(new ArrayList<>());
    }

    /**
     * Gets all unanalysed speeches from the MongoDB.
     * @param collectionName name of the collection.
     * @param fieldname The fieldname that is searched.
     * @return
     */
    public List<Document> getAnalysedDocs(String collectionName, String fieldname) {
        MongoDatabase database = getDatabase();
        MongoCollection<Document> collection = Database.getCollection(collectionName);
        return collection.find(new Document(fieldname, new Document("$exists", true))).into(new ArrayList<>());
    }
}
