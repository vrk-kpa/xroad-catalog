package fi.vrk.xroad.catalog.collector.mock;

import com.sun.net.httpserver.HttpServer;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Created by sjk on 22.2.2016.
 */
public class MockWebServiceTemplate extends WebServiceTemplate implements MockHttpServer {

    public MockWebServiceTemplate(Marshaller marshaller) {
        super(marshaller);
    }

    @Override
    public Object marshalSendAndReceive(String uri, Object requestPayload) {
        String localUrl = null;
        try {

            localUrl = startServerForUrl(uri);
            Object result = super.marshalSendAndReceive(localUrl, requestPayload);
            stopServer();
            return result;
        } catch (Exception e) {
            log.error("Error getting resource from through http{}", localUrl);
            throw new RuntimeException("Error reading resource from httpserver", e);
        }
    }

    protected HttpServer server;

    @Override
    public HttpServer getServer() {
        return server;
    }

    @Override
    public void setServer(HttpServer server) {
        this.server = server;
    }
}

