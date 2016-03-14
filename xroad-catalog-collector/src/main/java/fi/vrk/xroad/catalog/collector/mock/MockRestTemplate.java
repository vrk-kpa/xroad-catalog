package fi.vrk.xroad.catalog.collector.mock;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * Creates a http server and serves the file requested from classpath.
 * Used only in development or tests.
 *
 * Created by sjk on 17.2.2016.
 *
 */
@Slf4j
public class MockRestTemplate extends TestRestTemplate implements MockHttpServer {

    protected HttpServer server;

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        String localUrl = null;
        try {

            localUrl = startServerForUrl(url);
            T result = super.getForObject(localUrl,responseType);
            stopServer();
            return result;
        } catch (Exception e) {
            log.error("Error getting resource from through http{}", localUrl);
            throw new RuntimeException("Error reading resource from httpserver", e);
        }
    }


    @Override
    public HttpServer getServer() {
        return server;
    }

    @Override
    public void setServer(HttpServer server) {
        this.server = server;
    }
}
