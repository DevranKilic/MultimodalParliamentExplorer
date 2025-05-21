import Rest.RESTHandler;
import Threading.ThreadsManager;
import database.MongoDBHandler;

/**
 * Multimodal Parliament Explorer
 */
public class Main {
    public static void main(String[] args) throws Exception {
        MongoDBHandler MyMongoDBHandler = new MongoDBHandler();

        ThreadsManager ThreadsManager_Runnable = new ThreadsManager(MyMongoDBHandler);
        Thread ThreadsManager_Thread = new Thread(ThreadsManager_Runnable);
        //ThreadsManager_Thread.start();

        RESTHandler MyRESTHandler = new RESTHandler(MyMongoDBHandler);
    }
}