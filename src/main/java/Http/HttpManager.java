package Http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Dieser Manager beinhaltet alle Funktionen die sich mit Http beschäftigen
 */
public class HttpManager {


    private HttpClient client;

    public HttpManager(){
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Sendet eine Get Anfrage und gibt die Antwort als String zurück.
     * @param Link
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public String SendGetRequest(String Link) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(Link);
        HttpRequest getRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
