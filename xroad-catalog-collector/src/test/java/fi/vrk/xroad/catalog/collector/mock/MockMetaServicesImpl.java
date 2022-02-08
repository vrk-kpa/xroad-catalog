/**
 * The MIT License
 * Copyright (c) 2022, Population Register Centre (VRK)
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

import fi.vrk.xroad.catalog.collector.util.ClientTypeUtil;
import fi.vrk.xroad.catalog.collector.wsimport.*;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * Mock metaservices -service which answers something valid and semi-reasonable
 * dummy data when queried for listMethods
 */
@WebService(serviceName = "producerPortService", targetNamespace = "http://metadata.x-road.eu/",
        wsdlLocation = "schema/metaservices.wsdl")
@Slf4j
public class MockMetaServicesImpl implements MetaServicesPort {

    @Resource
    private WebServiceContext ctx;

    @Override
    public AllowedMethodsResponse allowedMethods(AllowedMethods allowedMethods,
            Holder<XRoadClientIdentifierType> client, Holder<XRoadServiceIdentifierType> service, Holder<String> userId,
            Holder<String> id, Holder<String> protocolVersion) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public ListMethodsResponse listMethods(ListMethods listMethods,
                                           Holder<XRoadClientIdentifierType> client,
                                           Holder<XRoadServiceIdentifierType> service,
                                           Holder<String> userId,
                                           Holder<String> id,
                                           Holder<String> protocolVersion) {
        log.info("mock listMethods");
        log.info("client= {}", ClientTypeUtil.toString(client.value));

        ListMethodsResponse response = new ListMethodsResponse();
        response.getService().add(generateService("testServiceFoo", "v1", service.value));
        response.getService().add(generateService("testServiceBar", "v1", service.value));
        response.getService().add(generateService("testServiceBaz", "v1", service.value));
        return response;
    }

    @Override
    public void getWsdl(GetWsdl getWsdl, Holder<XRoadClientIdentifierType> client,
            Holder<XRoadServiceIdentifierType> service, Holder<String> userId, Holder<String> id,
            Holder<String> protocolVersion, Holder<GetWsdlResponse> getWsdlResponse, Holder<byte[]> wsdl) {

        final GetWsdlResponse response = new GetWsdlResponse();
        response.setServiceCode(getWsdl.getServiceCode());
        response.setServiceVersion(getWsdl.getServiceVersion());
        getWsdlResponse.value = response;
        final String tmp = MessageFormat.format(WSDL_TEMPLATE, getWsdl.getServiceCode(), getWsdl.getServiceVersion());
        wsdl.value = tmp.getBytes(StandardCharsets.UTF_8);
        log.info("Returning WSDL");
    }

    private XRoadServiceIdentifierType generateService(String serviceCode,
                                                       String serviceVersion,
                                                       XRoadServiceIdentifierType serviceHeader) {
        XRoadServiceIdentifierType service = new XRoadServiceIdentifierType();
        service.setXRoadInstance(serviceHeader.getXRoadInstance());
        service.setMemberClass(serviceHeader.getMemberClass());
        service.setMemberCode(serviceHeader.getMemberCode());
        service.setSubsystemCode(serviceHeader.getSubsystemCode());
        service.setServiceCode(serviceCode);
        service.setServiceVersion(serviceVersion);
        service.setObjectType(XRoadObjectType.SERVICE);
        return service;
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
