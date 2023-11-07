/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.collector.actors;

import fi.vrk.xroad.catalog.collector.configuration.DevelopmentConfiguration;
import fi.vrk.xroad.catalog.collector.wsimport.ClientList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Slf4j
@SpringBootTest(classes = DevelopmentConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListClientsRestTest {

    private static final String VALID_LISTCLIENTS_RESPONSE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns2:clientList xmlns:ns1=\"http://x-road.eu/xsd/identifiers\" xmlns:ns2=\"http://x-road.eu/xsd/xroad.xsd\">\n"
                    +
                    "    <ns2:member>\n" +
                    "        <ns2:id ns1:objectType=\"MEMBER\">\n" +
                    "            <ns1:xRoadInstance>FI</ns1:xRoadInstance>\n" +
                    "            <ns1:memberClass>GOV</ns1:memberClass>\n" +
                    "            <ns1:memberCode>1710128-9</ns1:memberCode>\n" +
                    "        </ns2:id>\n" +
                    "        <ns2:name>Gofore</ns2:name>\n" +
                    "    </ns2:member>\n" +
                    "    <ns2:member>\n" +
                    "        <ns2:id ns1:objectType=\"SUBSYSTEM\">\n" +
                    "            <ns1:xRoadInstance>FI</ns1:xRoadInstance>\n" +
                    "            <ns1:memberClass>GOV</ns1:memberClass>\n" +
                    "            <ns1:memberCode>1710128-9</ns1:memberCode>\n" +
                    "            <ns1:subsystemCode>Management</ns1:subsystemCode>\n" +
                    "        </ns2:id>\n" +
                    "        <ns2:name>Gofore</ns2:name>\n" +
                    "    </ns2:member>\n" +
                    "    <ns2:member>\n" +
                    "        <ns2:id ns1:objectType=\"SUBSYSTEM\">\n" +
                    "            <ns1:xRoadInstance>FI</ns1:xRoadInstance>\n" +
                    "            <ns1:memberClass>GOV</ns1:memberClass>\n" +
                    "            <ns1:memberCode>1710128-9</ns1:memberCode>\n" +
                    "            <ns1:subsystemCode>SS1</ns1:subsystemCode>\n" +
                    "        </ns2:id>\n" +
                    "        <ns2:name>Gofore</ns2:name>\n" +
                    "    </ns2:member>\n" +
                    "</ns2:clientList>\n";

    private static final String OUTDATED_GLOBAL_CONF =
            "<?xml version=\"1.0\" encoding=\"utf-8\" ?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><SOAP-ENV:Fault><faultcode>Server.ClientProxy.OutdatedGlobalConf</faultcode><faultstring>Global configuration is expired</faultstring><faultactor/><detail><xroad:faultDetail xmlns:xroad=\"http://x-road.eu/xsd/xroad.xsd\">OutdatedGlobalConf: \n"
                    +
                    "\tat ee.ria.xroad.common.conf.globalconf.GlobalConf.verifyValidity(GlobalConf.java:160)\n" +
                    "\tat ee.ria.xroad.proxy.util.MessageProcessorBase.cacheConfigurationForCurrentThread(MessageProcessorBase.java:64)\n"
                    +
                    "\tat ee.ria.xroad.proxy.util.MessageProcessorBase.&lt;init&gt;(MessageProcessorBase.java:55)\n" +
                    "\tat ee.ria.xroad.proxy.clientproxy.AsicContainerClientRequestProcessor.&lt;init&gt;(AsicContainerClientRequestProcessor.java:108)\n"
                    +
                    "\tat ee.ria.xroad.proxy.clientproxy.AsicContainerHandler.createRequestProcessor(AsicContainerHandler.java:63)\n"
                    +
                    "\tat ee.ria.xroad.proxy.clientproxy.AbstractClientProxyHandler.handle(AbstractClientProxyHandler.java:80)\n"
                    +
                    "\tat ee.ria.xroad.proxy.clientproxy.AsicContainerHandler.handle(AsicContainerHandler.java:39)\n" +
                    "\tat org.eclipse.jetty.server.handler.HandlerCollection.handle(HandlerCollection.java:154)\n" +
                    "\tat org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:116)\n" +
                    "\tat org.eclipse.jetty.server.Server.handle(Server.java:370)\n" +
                    "\tat org.eclipse.jetty.server.AbstractHttpConnection.handleRequest(AbstractHttpConnection.java:494)\n"
                    +
                    "\tat org.eclipse.jetty.server.AbstractHttpConnection.headerComplete(AbstractHttpConnection.java:971)\n"
                    +
                    "\tat org.eclipse.jetty.server.AbstractHttpConnection$RequestHandler.headerComplete(AbstractHttpConnection.java:1033)\n"
                    +
                    "\tat org.eclipse.jetty.http.HttpParser.parseNext(HttpParser.java:640)\n" +
                    "\tat org.eclipse.jetty.http.HttpParser.parseAvailable(HttpParser.java:231)\n" +
                    "\tat org.eclipse.jetty.server.AsyncHttpConnection.handle(AsyncHttpConnection.java:82)\n" +
                    "\tat org.eclipse.jetty.io.nio.SelectChannelEndPoint.handle(SelectChannelEndPoint.java:696)\n" +
                    "\tat org.eclipse.jetty.io.nio.SelectChannelEndPoint$1.run(SelectChannelEndPoint.java:53)\n" +
                    "\tat org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:608)\n" +
                    "\tat org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)\n" +
                    "\tat java.lang.Thread.run(Thread.java:745)\n" +
                    "</xroad:faultDetail></detail></SOAP-ENV:Fault></SOAP-ENV:Body></SOAP-ENV:Envelope>";

    private static final String TEST_REQUEST_PATH = "/dummy-request-path";

    @Test
    public void testBadMessage() throws Exception {
        RestTemplate t = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(t);

        mockServer.expect(requestTo(TEST_REQUEST_PATH)).andRespond(
                withSuccess(OUTDATED_GLOBAL_CONF, MediaType.APPLICATION_XML));

        try {
            ClientList clientList = t.getForObject(TEST_REQUEST_PATH, ClientList.class);
            fail("should fail since restOperation is not returning valid ClientList");
        } catch (Exception expected) {
            log.error("expected exception: ", expected);
        }
    }

    @Test
    public void testOkMessage() throws Exception {
        RestTemplate t = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(t);

        mockServer.expect(requestTo(TEST_REQUEST_PATH)).andRespond(
                withSuccess(VALID_LISTCLIENTS_RESPONSE, MediaType.APPLICATION_XML));

        try {
            ClientList clientList = t.getForObject(TEST_REQUEST_PATH, ClientList.class);
            assertNotNull(clientList);
            assertEquals(3, clientList.getMember().size());
        } catch (Exception expected) {
            log.error("expected exception: ", expected);
        }

    }

    @Test
    public void testErrorResponseCode() throws Exception {
        RestTemplate t = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(t);

        mockServer.expect(requestTo(TEST_REQUEST_PATH)).andRespond(
                withServerError());
        try {
            ClientList clientList = t.getForObject(TEST_REQUEST_PATH, ClientList.class);
            fail("should fail since not http 200 OK");
        } catch (Exception expected) {
            log.error("expected exception: ", expected);
        }
    }
}
