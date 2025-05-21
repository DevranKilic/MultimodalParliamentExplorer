package Rest;

import database.MongoDBHandler;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import io.javalin.rendering.template.JavalinFreemarker;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Diese Klasse startet einen Javalin Webserver und entimmt dabei die Configs aus der JavalinConfig Klasse.
 * Hier befinden sich auch Endpunkte, welche keine Templates benötigen.
 */
public class RESTHandler {
    private Javalin Handler;

    private TemplateHandler pTemplateHandler;

    private MongoDBHandler MyMongoDBHandler;

    private JavalinConfig pConfig;

    public RESTHandler(MongoDBHandler MyMongoDBHandler){
        this.MyMongoDBHandler = MyMongoDBHandler;
        this.pConfig = new JavalinConfig();
        try{
            this.pTemplateHandler = new TemplateHandler(this, this.pConfig.getTemplateDirectory());
        } catch (IOException e){
            System.out.println("Template konnte nicht geladen werden");
            e.printStackTrace();
        }

        this.Handler = Javalin.create(pConfig->{
            pConfig.fileRenderer(new JavalinFreemarker(this.pTemplateHandler.getfConfiguration()));
            pConfig.staticFiles.add(staticFiles->{
                //staticFiles.directory = this.pConfig.getStaticFilesDirectory();
                //staticFiles.location = this.pConfig.getStaticFilesLocation();
                staticFiles.directory = "./src/main/resources/static/";
                staticFiles.location = Location.EXTERNAL;

                System.out.println(staticFiles.refinedToString$javalin());
            });

            //Open API Documentation
            pConfig.registerPlugin(new OpenApiPlugin(pluginConfig -> {
                pluginConfig.withDefinitionConfiguration((version, definition) -> {
                    definition.withOpenApiInfo(info -> info.setTitle("Javalin"));
                });
            }));
            pConfig.registerPlugin(new ReDocPlugin());
            pConfig.registerPlugin(new SwaggerPlugin());
        });
        //safe shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            this.Handler.stop();
        }));
        //Hauptseiten
        this.init();

        //Templates-Seiten
        try{
            // Wenn die Template Engine null ist, wird eine Exception geworfen.
            if(this.pTemplateHandler==null){
                throw new NullPointerException("TemplateHandler is null!");
            }
            this.pTemplateHandler.init();

        } catch (NullPointerException e){
            e.printStackTrace();
        }
        this.Handler.start(this.pConfig.getPort());
    }

    protected MongoDBHandler getMyMongoDBHandler(){
        return this.MyMongoDBHandler;
    }

    protected Javalin getHandler(){
        return this.Handler;
    }

    private void init(){
        this.Handler.get("/", ctx->{
            ctx.redirect("/hub");
        });
    }
}
