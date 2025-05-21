package Threading;

import database.MongoDBHandler;

/**
 * Diese Klasse ist dafür da alle Threads zu initialisieren und von hier aus auszuführen.
 */
public class ThreadsManager implements Runnable{

    private Thread PlenarprotokolleUpdaterThread;
    private Thread AbgeordnetenFotosUpdaterThread;
    private Thread RedeVideosUpdaterThread;
    private Thread NLPUpdaterThread;
    private MongoDBHandler MyMongoDBHandler;

    public ThreadsManager(MongoDBHandler MyMongoDBHandler){
        this.MyMongoDBHandler = MyMongoDBHandler;
    }

    @Override
    public void run() {
        PlenarprotokollUpdaterRunnable PleanerprotokolleUpdater_Runnable = new PlenarprotokollUpdaterRunnable(MyMongoDBHandler);
        AbgeordnetenFotosUpdaterRunnable AbgeordneterFotosUpdater_Runnable = new AbgeordnetenFotosUpdaterRunnable(MyMongoDBHandler);
        RedeVideosUpdaterRunnable RedeVideosUpdater_Runnable = null;
        NLPUpdaterRunnable NLPUpdater_Runnable = null;
        try {
            NLPUpdater_Runnable = new NLPUpdaterRunnable(MyMongoDBHandler);
            RedeVideosUpdater_Runnable = new RedeVideosUpdaterRunnable(MyMongoDBHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        this.PlenarprotokolleUpdaterThread = new Thread(PleanerprotokolleUpdater_Runnable);
        this.AbgeordnetenFotosUpdaterThread = new Thread(AbgeordneterFotosUpdater_Runnable);
        this.NLPUpdaterThread = new Thread(NLPUpdater_Runnable);
        this.RedeVideosUpdaterThread = new Thread(RedeVideosUpdater_Runnable);

        init();
    }

    private void init(){
        //Hier werden die Threads ausgeführt
        this.PlenarprotokolleUpdaterThread.start();
        this.AbgeordnetenFotosUpdaterThread.start();
        this.NLPUpdaterThread.start();
        this.RedeVideosUpdaterThread.start();
    }
}
