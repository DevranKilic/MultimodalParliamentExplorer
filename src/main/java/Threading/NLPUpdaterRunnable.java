package Threading;
import NLP.NLP_Impl;
import database.MongoDBHandler;
import java.io.IOException;

public class NLPUpdaterRunnable implements Runnable {

    private MongoDBHandler MyMongoDBHandler;
    private NLP_Impl NLP;

    public NLPUpdaterRunnable(MongoDBHandler MyMongoDBHandler) throws Exception {
        this.MyMongoDBHandler = MyMongoDBHandler;
        this.NLP = new NLP_Impl(MyMongoDBHandler);
    }

    @Override
    public void run(){
        while(true){
            try {
                System.out.println("NLP Analysen werden aktualisiert");
                NLP.analyseAllSpeechesInDB();
                System.out.println("Aktualisierung von NLP abgeschlossen");

                //Jeden Tag
                // 1 Sekunde = 1000 Millisekunden
                // 24 Stunden x 60 Minuten x 60 Sekunden x 1000 Millisekunden
                Thread.sleep(24*60*60*1000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
