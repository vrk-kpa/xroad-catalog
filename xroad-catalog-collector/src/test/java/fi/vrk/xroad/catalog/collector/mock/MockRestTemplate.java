/**
 * The MIT License
 * Copyright (c) 2021, Population Register Centre (VRK)
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

import fi.vrk.xroad.catalog.collector.util.CatalogCollectorRuntimeException;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Creates a http server and serves the file requested from classpath.
 * Used only in development or tests.
 *
 */
@Slf4j
public class MockRestTemplate extends RestTemplate implements MockHttpServer {

    protected HttpServer server;

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        String localUrl = null;
        try {
            log.info("starting server for url: " + url);
            localUrl = startServerForUrl(url);
            T result = super.getForObject(localUrl,responseType);
            log.info("starting server for url: " + url);
            stopServer();
            return result;
        } catch (Exception e) {
            log.error("Error getting resource from through http{}", localUrl);
            throw new CatalogCollectorRuntimeException("Error reading resource from httpserver", e);
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
