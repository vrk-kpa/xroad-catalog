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

import com.google.common.base.Splitter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Map;

/**
 * TestRestTemplate which fires up HttpServer to server each request,
 * parses GET request for WSDL, and returns a sample WSDL
 * with populated serviceCode and version.
 */
@Slf4j
public class WsdlMockRestTemplate extends TestRestTemplate {

    @Getter
    @Setter
    private HttpServer server;

    private final int PORT = 8933;

    public void startServer() throws Exception {
        setServer(HttpServer.create(new InetSocketAddress(PORT), 0));
        getServer().createContext("/", new DynamicHttpHandler());
        getServer().setExecutor(null); // creates a default executor
        log.info("Starting local http server {} in port {}", getServer(), PORT);
        getServer().start();
    }

    public void stopServer() throws Exception {
        log.info("Stopping local http server {} in port {}", getServer(), PORT);
        getServer().stop(0);
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) throws RestClientException {
        try {
            log.info("starting server for url: " + url);
            startServer();
            log.info("getting response: " + url);
            T result = super.getForObject(url, responseType);
            log.info("received response: " + result);
            log.info("calling service stop ");
            stopServer();
            return result;
        } catch (Exception e) {
            log.error("Error getting resource from through http {}", url, e);
            throw new RuntimeException("Error reading resource from httpserver", e);
        }
    }


    static class DynamicHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String query = uri.getQuery();
            log.info("parsing query [{}]", query);
            Map<String, String> params = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query);
            String serviceCode = params.get("serviceCode");
            String version = params.get("version");
            assert serviceCode != null;
            assert version != null;
            log.info("params: {}", params);

            // write example wsdl with populated serviceCode + version
            exchange.getResponseHeaders().add("Content-Type", "application/xml");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            String wsdl = getWsdl(serviceCode, version);
            os.write(wsdl.getBytes("UTF-8"));
            log.info("wrote response, closing");
            os.close();
        }
    }

    private static String getWsdl(String serviceCode, String serviceVersion) {
        return MessageFormat.format(WSDL_TEMPLATE, serviceCode, serviceVersion);
    }

    private static String WSDL_TEMPLATE = "<wsdl:definitions xmlns:soap=\"http://schemas.xmlsoap.org/wsdl/soap/\"\n" +
            "                  xmlns:tns=\"http://vrk-test.x-road.fi/producer\"\n" +
            "                  xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"\n" +
            "                  xmlns:xrd=\"http://x-road.eu/xsd/xroad.xsd\"\n" +
            "                  xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "                  xmlns:id=\"http://x-road.eu/xsd/identifiers\"\n" +
            "                  name=\"testService\" targetNamespace=\"http://vrk-test.x-road.fi/producer\">\n" +
            "    <wsdl:types>\n" +
            "        <!-- Schema for identifiers (reduced) -->\n" +
            "        <xsd:schema elementFormDefault=\"qualified\"\n" +
            "                    targetNamespace=\"http://x-road.eu/xsd/identifiers\"\n" +
            "                    xmlns=\"http://x-road.eu/xsd/identifiers\">\n" +
            "            <xsd:simpleType name=\"XRoadObjectType\">\n" +
            "                <xsd:annotation>\n" +
            "                    <xsd:documentation>Enumeration for X-Road identifier\n" +
            "                        types that can be used in requests.\n" +
            "                    </xsd:documentation>\n" +
            "                </xsd:annotation>\n" +
            "                <xsd:restriction base=\"xsd:string\">\n" +
            "                    <xsd:enumeration value=\"MEMBER\" />\n" +
            "                    <xsd:enumeration value=\"SUBSYSTEM\" />\n" +
            "                    <xsd:enumeration value=\"SERVICE\" />\n" +
            "                </xsd:restriction>\n" +
            "            </xsd:simpleType>\n" +
            "            <xsd:element name=\"xRoadInstance\" type=\"xsd:string\">\n" +
            "                <xsd:annotation>\n" +
            "                    <xsd:documentation>Identifies the X-Road instance.\n" +
            "                        This field is applicable to all identifier\n" +
            "                        types.\n" +
            "                    </xsd:documentation>\n" +
            "                </xsd:annotation>\n" +
            "            </xsd:element>\n" +
            "            <xsd:element name=\"memberClass\" type=\"xsd:string\">\n" +
            "                <xsd:annotation>\n" +
            "                    <xsd:documentation>Type of the member (company,\n" +
            "                        government institution, private person, etc.)\n" +
            "                    </xsd:documentation>\n" +
            "                </xsd:annotation>\n" +
            "            </xsd:element>\n" +
            "            <xsd:element name=\"memberCode\" type=\"xsd:string\">\n" +
            "                <xsd:annotation>\n" +
            "                    <xsd:documentation>Code that uniquely identifies a\n" +
            "                        member of given member type.\n" +
            "                    </xsd:documentation>\n" +
            "                </xsd:annotation>\n" +
            "            </xsd:element>\n" +
            "            <xsd:element name=\"subsystemCode\" type=\"xsd:string\">\n" +
            "                <xsd:annotation>\n" +
            "                    <xsd:documentation>Code that uniquely identifies a\n" +
            "                        subsystem of given SDSB member.\n" +
            "                    </xsd:documentation>\n" +
            "                </xsd:annotation>\n" +
            "            </xsd:element>\n" +
            "            <xsd:element name=\"serviceCode\" type=\"xsd:string\">\n" +
            "                <xsd:annotation>\n" +
            "                    <xsd:documentation>Code that uniquely identifies a\n" +
            "                        service offered by given SDSB member or\n" +
            "                        subsystem.\n" +
            "                    </xsd:documentation>\n" +
            "                </xsd:annotation>\n" +
            "            </xsd:element>\n" +
            "            <xsd:element name=\"serviceVersion\" type=\"xsd:string\">\n" +
            "                <xsd:annotation>\n" +
            "                    <xsd:documentation>Version of the service.\n" +
            "                    </xsd:documentation>\n" +
            "                </xsd:annotation>\n" +
            "            </xsd:element>\n" +
            "            <xsd:attribute name=\"objectType\" type=\"XRoadObjectType\" />\n" +
            "            <xsd:complexType name=\"XRoadClientIdentifierType\">\n" +
            "                <xsd:sequence>\n" +
            "                    <xsd:element ref=\"xRoadInstance\" />\n" +
            "                    <xsd:element ref=\"memberClass\" />\n" +
            "                    <xsd:element ref=\"memberCode\" />\n" +
            "                    <xsd:element minOccurs=\"0\" ref=\"subsystemCode\" />\n" +
            "                </xsd:sequence>\n" +
            "                <xsd:attribute ref=\"objectType\" use=\"required\" />\n" +
            "            </xsd:complexType>\n" +
            "            <xsd:complexType name=\"XRoadServiceIdentifierType\">\n" +
            "                <xsd:sequence>\n" +
            "                    <xsd:element ref=\"xRoadInstance\" />\n" +
            "                    <xsd:element ref=\"memberClass\" />\n" +
            "                    <xsd:element ref=\"memberCode\" />\n" +
            "                    <xsd:element minOccurs=\"0\" ref=\"subsystemCode\" />\n" +
            "                    <xsd:element ref=\"serviceCode\" />\n" +
            "                    <xsd:element minOccurs=\"0\" ref=\"serviceVersion\" />\n" +
            "                </xsd:sequence>\n" +
            "                <xsd:attribute ref=\"objectType\" use=\"required\" />\n" +
            "            </xsd:complexType>\n" +
            "        </xsd:schema>\n" +
            " \n" +
            "        <!-- Schema for request headers -->\n" +
            "        <xsd:schema xmlns=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "                    targetNamespace=\"http://x-road.eu/xsd/xroad.xsd\"\n" +
            "                    elementFormDefault=\"qualified\">\n" +
            "            \n" +
            "            <xsd:element name=\"client\" type=\"id:XRoadClientIdentifierType\" />\n" +
            "            <xsd:element name=\"service\" type=\"id:XRoadServiceIdentifierType\" />\n" +
            "            <xsd:element name=\"userId\" type=\"xsd:string\" />\n" +
            "            <xsd:element name=\"id\" type=\"xsd:string\" />\n" +
            "            <xsd:element name=\"protocolVersion\" type=\"xsd:string\" />\n" +
            "        </xsd:schema>\n" +
            "                  \n" +
            "        <!-- Schema for requests (reduced) -->\n" +
            "        <xsd:schema targetNamespace=\"http://vrk-test.x-road.fi/producer\">           \n" +
            "            <xsd:element name=\"{0}\" nillable=\"true\" />\n" +
            "            <xsd:element name=\"{0}Response\">\n" +
            "                <xsd:complexType>\n" +
            "                    <xsd:sequence>\n" +
            "                        <xsd:element name=\"response\">\n" +
            "                            <xsd:complexType>\n" +
            "                                <xsd:sequence>\n" +
            "                                    <xsd:element name=\"data\" type=\"xsd:string\">\n" +
            "                                        <xsd:annotation>\n" +
            "                                            <xsd:documentation>\n" +
            "                                                Service response\n" +
            "                                            </xsd:documentation>\n" +
            "                                        </xsd:annotation>\n" +
            "                                    </xsd:element>\n" +
            "                                </xsd:sequence>\n" +
            "                            </xsd:complexType>\n" +
            "                        </xsd:element>\n" +
            "                    </xsd:sequence>\n" +
            "                </xsd:complexType>\n" +
            "            </xsd:element>   \n" +
            "        </xsd:schema>\n" +
            "    </wsdl:types>\n" +
            " \n" +
            "    <wsdl:message name=\"requestheader\">\n" +
            "        <wsdl:part name=\"client\" element=\"xrd:client\" />\n" +
            "        <wsdl:part name=\"service\" element=\"xrd:service\" />\n" +
            "        <wsdl:part name=\"userId\" element=\"xrd:userId\" />\n" +
            "        <wsdl:part name=\"id\" element=\"xrd:id\" />\n" +
            "        <wsdl:part name=\"protocolVersion\" element=\"xrd:protocolVersion\" />\n" +
            "    </wsdl:message>\n" +
            "     \n" +
            "    <wsdl:message name=\"{0}\">\n" +
            "        <wsdl:part name=\"body\" element=\"tns:{0}\"/>\n" +
            "    </wsdl:message>\n" +
            "    <wsdl:message name=\"{0}Response\">\n" +
            "        <wsdl:part name=\"body\" element=\"tns:{0}Response\"/>\n" +
            "    </wsdl:message>\n" +
            "   \n" +
            "    <wsdl:portType name=\"testServicePortType\">\n" +
            "        <wsdl:operation name=\"{0}\">\n" +
            "            <wsdl:input message=\"tns:{0}\"/>\n" +
            "            <wsdl:output message=\"tns:{0}Response\"/>\n" +
            "        </wsdl:operation>\n" +
            "    </wsdl:portType>\n" +
            "   \n" +
            "    <wsdl:binding name=\"testServiceBinding\" type=\"tns:testServicePortType\">\n" +
            "        <soap:binding style=\"document\" transport=\"http://schemas.xmlsoap.org/soap/http\" />\n" +
            "        <wsdl:operation name=\"{0}\">\n" +
            "            <soap:operation soapAction=\"\" style=\"document\" />\n" +
            "            <id:version>{1}</id:version>\n" +
            "            <wsdl:input>\n" +
            "                <soap:body parts=\"body\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"client\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"service\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"userId\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"id\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"protocolVersion\" use=\"literal\"/>\n" +
            "            </wsdl:input>\n" +
            "            <wsdl:output>\n" +
            "                <soap:body parts=\"body\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"client\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"service\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"userId\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"id\" use=\"literal\"/>\n" +
            "                <soap:header message=\"tns:requestheader\" part=\"protocolVersion\" use=\"literal\"/>\n" +
            "            </wsdl:output>\n" +
            "        </wsdl:operation>\n" +
            "    </wsdl:binding>\n" +
            "    <wsdl:service name=\"testService\">\n" +
            "        <wsdl:port binding=\"tns:testServiceBinding\" name=\"testServicePort\">\n" +
            "            <soap:address location=\"SOME-SERVICE_ENDPOINT\"/>\n" +
            "        </wsdl:port>\n" +
            "    </wsdl:service>\n" +
            "</wsdl:definitions>";

}
