package fi.vrk.xroad.catalog.collector.mock;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by sjk on 22.2.2016.
 */
public interface MockHttpServer {

    HttpServer getServer();
    void setServer(HttpServer server);
    final int PORT = 8932;

    final Logger log = LoggerFactory.getLogger("MockHttpServer");


    default void startServer(String fileName) throws Exception {
        setServer(HttpServer.create(new InetSocketAddress(PORT), 0));
        getServer().createContext(fileName, new ResourceToHttpHandler(fileName));
        getServer().setExecutor(null); // creates a default executor
        log.info("Starting local http server {} in port {}", getServer(), PORT);
        getServer().start();
        TimeUnit.SECONDS.sleep(1);
    }

    default void stopServer() {
        log.info("Stopping local http server {} in port {}", getServer(), PORT);
        getServer().stop(0);

    }


    default String startServerForUrl(String url) throws Exception {
        String[] parts = url.split("/");

        String fileName = "/data/" + parts[3]+".xml";
        String localUrl = "http://localhost:"+PORT+fileName;

        log.info("Reading response from file {} and local url {}", fileName, localUrl);

        try {

            startServer(fileName);
            return localUrl;
        } catch (Exception e) {
            log.error("Error getting resource from through http{}", localUrl);
            throw new RuntimeException("Error reading resource from httpserver", e);
        }
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
