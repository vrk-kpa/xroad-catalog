/**
 * The MIT License
 * Copyright (c) 2016, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.mock;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import fi.vrk.xroad.catalog.collector.util.CatalogCollectorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Used for tests
 * Created by sjk on 22.2.2016.
 */
public interface MockHttpServer {

    int PORT = 8932;

    Logger log = LoggerFactory.getLogger("MockHttpServer");

    HttpServer getServer();
    void setServer(HttpServer server);


    /**
     * Start http server that offers the given file through http
     * @param fileName
     */
    default void startServer(String fileName) {
        try {
            setServer(HttpServer.create(new InetSocketAddress(PORT), 0));

            getServer().createContext(getContext(fileName), getHandler(fileName));
            getServer().setExecutor(null); // creates a default executor
            log.info("Starting local http server {} in port {}", getServer(), PORT);
            getServer().start();
            // TODO: what about removing this delay?
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            throw new CatalogCollectorRuntimeException("Cannot start httpserver", e);
        }
    }

    /**
     * Stop http server
     */
    default void stopServer() {
        log.info("Stopping local http server {} in port {}", getServer(), PORT);
        getServer().stop(0);
    }


    /**
     * Start http server using file based on the url in the parameter
     * @param url
     * @return
     */
    default String startServerForUrl(String url) {
        String[] parts = url.split("/");

        String fileName = "/data/" + parts[3]+".xml";
        String localUrl = "http://localhost:"+PORT+fileName;

        log.info("Reading response from file {} and local url {}", fileName, localUrl);

        try {
            startServer(fileName);
            return localUrl;
        } catch (Exception e) {
            log.error("Error getting resource from through http{}", localUrl);
            throw new CatalogCollectorRuntimeException("Error reading resource from httpserver", e);
        }
    }

    default String getContext(String fileName) {
        return fileName;
    }

    default HttpHandler getHandler(String fileName) {
        return new ResourceToHttpHandler(fileName);
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
