package fi.vrk.xroad.catalog.collector.mock;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by sjk on 17.2.2016.
 */
@Slf4j
public class MockRestTemplate extends TestRestTemplate {

    protected HttpServer server;
    protected final int PORT = 8932;

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        String[] parts = url.split("/");
        String fileName = parts[3]+".xml";

        String localUrl = "http://localhost:"+PORT+"/"+fileName;
        log.info("Reading response from file {} and local url {}", fileName, localUrl);
        try {

            startServer(fileName);
            T result = super.getForObject(localUrl,responseType);
            stopServer();
            return result;
        } catch (Exception e) {
            log.error("Not able to find local resource from {}", localUrl);
            throw new RuntimeException(e);
        }
    }

    public void startServer(String fileName) throws Exception {

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/" + fileName, new ResourceToHttpHandler(fileName));
        server.setExecutor(null); // creates a default executor
        log.info("Starting local http server {} in port {}", server, PORT);
        server.start();
        TimeUnit.SECONDS.sleep(1);
    }

    public void stopServer() {
        log.info("Stopping local http server {} in port {}", server, PORT);
        server.stop(0);
    }

    static class ResourceToHttpHandler implements HttpHandler {

        public final String fileName;

        public ResourceToHttpHandler(String fileName) {
            this.fileName = fileName;
        }



        @Override
        public void handle(HttpExchange exchange) throws IOException {
            ClassPathResource resource = new ClassPathResource(fileName);
            InputStream is = resource.getInputStream();
            exchange.getResponseHeaders().add("Content-Type", "application/xml");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.close();
        }
    }


}
