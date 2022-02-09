-- some test data to be used freely...actual unit test data is in resources/data.sql
--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.0
-- Dumped by pg_dump version 9.5.0

-- Started on 2016-01-29 14:34:01

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2145 (class 0 OID 0)
-- Dependencies: 181
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('client_id_seq', 1, false);


--
-- TOC entry 2133 (class 0 OID 16396)
-- Dependencies: 180
-- Data for Name: member; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched, removed) VALUES (1, 'dev-cs', 'PUB', '14151328', 'Nahka-Albert', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO member (id, x_road_instance, member_class, member_code, name, created, changed, fetched, removed) VALUES (2, 'dev-cs', 'PUB', '88855888', 'Suutari Simo', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);


--
-- TOC entry 2137 (class 0 OID 16412)
-- Dependencies: 184
-- Data for Name: subsystem; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched, removed) VALUES (1, 1, 'subsystem_a1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched, removed) VALUES (2, 1, 'subsystem_a2', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO subsystem (id, member_id, subsystem_code, created, changed, fetched, removed) VALUES (3, 2, 'subsystem_b1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);


--
-- TOC entry 2135 (class 0 OID 16404)
-- Dependencies: 182
-- Data for Name: service; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched, removed) VALUES (1, 2, 'testService', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO service (id, subsystem_id, service_code, service_version, created, changed, fetched, removed) VALUES (2, 1, 'getRandom', 'v1', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);


--
-- TOC entry 2146 (class 0 OID 0)
-- Dependencies: 183
-- Name: service_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('service_id_seq', 1, false);


--
-- TOC entry 2147 (class 0 OID 0)
-- Dependencies: 185
-- Name: subsystem_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('subsystem_id_seq', 1, false);


--
-- TOC entry 2139 (class 0 OID 16420)
-- Dependencies: 186
-- Data for Name: wsdl; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched, removed) VALUES (1, 1, '<?xml version="1.0" standalone="no"?>
<wsdl:definitions xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
                  xmlns:tns="http://test.x-road.fi/producer"
                  xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                  xmlns:xrd="http://x-road.eu/xsd/xroad.xsd"
                  xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                  xmlns:id="http://x-road.eu/xsd/identifiers"
                  name="testService" targetNamespace="http://test.x-road.fi/producer">
    <wsdl:types>
        <!-- Schema for identifiers (reduced) -->
        <xsd:schema elementFormDefault="qualified"
                    targetNamespace="http://x-road.eu/xsd/identifiers"
                    xmlns="http://x-road.eu/xsd/identifiers">
            <xsd:simpleType name="XRoadObjectType">
                <xsd:annotation>
                    <xsd:documentation>Enumeration for X-Road identifier
                        types that can be used in requests.
                    </xsd:documentation>
                </xsd:annotation>
                <xsd:restriction base="xsd:string">
                    <xsd:enumeration value="MEMBER" />
                    <xsd:enumeration value="SUBSYSTEM" />
                    <xsd:enumeration value="SERVICE" />
                </xsd:restriction>
            </xsd:simpleType>
            <xsd:element name="xRoadInstance" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Identifies the X-Road instance.
                        This field is applicable to all identifier
                        types.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberClass" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Type of the member (company,
                        government institution, private person, etc.)
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        member of given member type.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="subsystemCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        subsystem of given X-Road member.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        service offered by given X-Road member or
                        subsystem.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceVersion" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Version of the service.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:attribute name="objectType" type="XRoadObjectType" />
            <xsd:complexType name="XRoadClientIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
            <xsd:complexType name="XRoadServiceIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                    <xsd:element ref="serviceCode" />
                    <xsd:element minOccurs="0" ref="serviceVersion" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
        </xsd:schema>

        <!-- Schema for request headers -->
        <xsd:schema xmlns="http://www.w3.org/2001/XMLSchema"
                    targetNamespace="http://x-road.eu/xsd/xroad.xsd"
                    elementFormDefault="qualified">

            <xsd:element name="client" type="id:XRoadClientIdentifierType" />
            <xsd:element name="service" type="id:XRoadServiceIdentifierType" />
            <xsd:element name="userId" type="xsd:string" />
            <xsd:element name="id" type="xsd:string" />
            <xsd:element name="protocolVersion" type="xsd:string" />
        </xsd:schema>

        <!-- Schema for requests (reduced) -->
        <xsd:schema targetNamespace="http://test.x-road.fi/producer">
            <xsd:element name="testService">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="responseBodySize" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Response body character count.
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                    <xsd:element name="responseAttachmentSize" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Response attachment character count.
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            <xsd:element name="testServiceResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="responseBodySize" nillable="true" type="xsd:string"/>
                                    <xsd:element name="responseAttachmentSize" nillable="true" type="xsd:string"/>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                        <xsd:element name="response">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="data" type="xsd:string" />
                                    <xsd:element name="processingTime" type="xsd:string" />
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
        </xsd:schema>
    </wsdl:types>

    <wsdl:message name="requestheader">
        <wsdl:part name="client" element="xrd:client" />
        <wsdl:part name="service" element="xrd:service" />
        <wsdl:part name="userId" element="xrd:userId" />
        <wsdl:part name="id" element="xrd:id" />
        <wsdl:part name="issue" element="xrd:issue"/>
        <wsdl:part name="protocolVersion" element="xrd:protocolVersion" />
    </wsdl:message>

    <wsdl:message name="testService">
        <wsdl:part name="body" element="tns:testService"/>
    </wsdl:message>
    <wsdl:message name="testServiceResponse">
        <wsdl:part name="body" element="tns:testServiceResponse"/>
    </wsdl:message>

    <wsdl:portType name="testServicePortType">
        <wsdl:operation name="testService">
            <wsdl:input message="tns:testService"/>
            <wsdl:output message="tns:testServiceResponse"/>
        </wsdl:operation>
    </wsdl:portType>

    <wsdl:binding name="testServiceBinding" type="tns:testServicePortType">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http" />

        <wsdl:operation name="testService">
            <soap:operation soapAction="" style="document" />
            <id:version>v1</id:version>
            <wsdl:input>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>
    <wsdl:service name="testService">
        <wsdl:port binding="tns:testServiceBinding" name="testServicePort">
            <soap:address location="http://localhost:8080/test-service-0.0.2-SNAPSHOT/Endpoint"/>
        </wsdl:port>
    </wsdl:service>
</wsdl:definitions>', '1000', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', '2016-01-01 00:00:00+02', NULL);
INSERT INTO wsdl (id, service_id, data, external_id, created, changed, fetched, removed) VALUES (2, 2, '<?xml version="1.0" standalone="no"?>
<wsdl:definitions xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
                  xmlns:tns="http://test.x-road.fi/producer"
                  xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                  xmlns:xrd="http://x-road.eu/xsd/xroad.xsd"
                  xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                  xmlns:id="http://x-road.eu/xsd/identifiers"
                  name="testService" targetNamespace="http://test.x-road.fi/producer">
    <wsdl:types>
        <!-- Schema for identifiers (reduced) -->
        <xsd:schema elementFormDefault="qualified"
                    targetNamespace="http://x-road.eu/xsd/identifiers"
                    xmlns="http://x-road.eu/xsd/identifiers">
            <xsd:simpleType name="XRoadObjectType">
                <xsd:annotation>
                    <xsd:documentation>Enumeration for X-Road identifier
                        types that can be used in requests.
                    </xsd:documentation>
                </xsd:annotation>
                <xsd:restriction base="xsd:string">
                    <xsd:enumeration value="MEMBER" />
                    <xsd:enumeration value="SUBSYSTEM" />
                    <xsd:enumeration value="SERVICE" />
                </xsd:restriction>
            </xsd:simpleType>
            <xsd:element name="xRoadInstance" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Identifies the X-Road instance.
                        This field is applicable to all identifier
                        types.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberClass" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Type of the member (company,
                        government institution, private person, etc.)
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="memberCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        member of given member type.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="subsystemCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        subsystem of given X-Road member.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceCode" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Code that uniquely identifies a
                        service offered by given X-Road member or
                        subsystem.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:element name="serviceVersion" type="xsd:string">
                <xsd:annotation>
                    <xsd:documentation>Version of the service.
                    </xsd:documentation>
                </xsd:annotation>
            </xsd:element>
            <xsd:attribute name="objectType" type="XRoadObjectType" />
            <xsd:complexType name="XRoadClientIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
            <xsd:complexType name="XRoadServiceIdentifierType">
                <xsd:sequence>
                    <xsd:element ref="xRoadInstance" />
                    <xsd:element ref="memberClass" />
                    <xsd:element ref="memberCode" />
                    <xsd:element minOccurs="0" ref="subsystemCode" />
                    <xsd:element ref="serviceCode" />
                    <xsd:element minOccurs="0" ref="serviceVersion" />
                </xsd:sequence>
                <xsd:attribute ref="objectType" use="required" />
            </xsd:complexType>
        </xsd:schema>

        <!-- Schema for request headers -->
        <xsd:schema xmlns="http://www.w3.org/2001/XMLSchema"
                    targetNamespace="http://x-road.eu/xsd/xroad.xsd"
                    elementFormDefault="qualified">

            <xsd:element name="client" type="id:XRoadClientIdentifierType" />
            <xsd:element name="service" type="id:XRoadServiceIdentifierType" />
            <xsd:element name="userId" type="xsd:string" />
            <xsd:element name="id" type="xsd:string" />
            <xsd:element name="protocolVersion" type="xsd:string" />
        </xsd:schema>

        <!-- Schema for requests (reduced) -->
        <xsd:schema targetNamespace="http://test.x-road.fi/producer">
            <xsd:element name="getRandom" nillable="true" />
            <xsd:element name="getRandomResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="response">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="data" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Service response
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            <xsd:element name="helloService">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="name" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Name
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
            <xsd:element name="helloServiceResponse">
                <xsd:complexType>
                    <xsd:sequence>
                        <xsd:element name="request">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="name" nillable="true" type="xsd:string"/>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                        <xsd:element name="response">
                            <xsd:complexType>
                                <xsd:sequence>
                                    <xsd:element name="message" type="xsd:string">
                                        <xsd:annotation>
                                            <xsd:documentation>
                                                Service response
                                            </xsd:documentation>
                                        </xsd:annotation>
                                    </xsd:element>
                                </xsd:sequence>
                            </xsd:complexType>
                        </xsd:element>
                    </xsd:sequence>
                </xsd:complexType>
            </xsd:element>
        </xsd:schema>
    </wsdl:types>

    <wsdl:message name="requestheader">
        <wsdl:part name="client" element="xrd:client" />
        <wsdl:part name="service" element="xrd:service" />
        <wsdl:part name="userId" element="xrd:userId" />
        <wsdl:part name="id" element="xrd:id" />
        <wsdl:part name="issue" element="xrd:issue"/>
        <wsdl:part name="protocolVersion" element="xrd:protocolVersion" />
    </wsdl:message>

    <wsdl:message name="getRandom">
        <wsdl:part name="body" element="tns:getRandom"/>
    </wsdl:message>
    <wsdl:message name="getRandomResponse">
        <wsdl:part name="body" element="tns:getRandomResponse"/>
    </wsdl:message>
    <wsdl:message name="helloService">
        <wsdl:part name="body" element="tns:helloService"/>
    </wsdl:message>
    <wsdl:message name="helloServiceResponse">
        <wsdl:part name="body" element="tns:helloServiceResponse"/>
    </wsdl:message>

    <wsdl:portType name="testServicePortType">
        <wsdl:operation name="getRandom">
            <wsdl:input message="tns:getRandom"/>
            <wsdl:output message="tns:getRandomResponse"/>
        </wsdl:operation>
        <wsdl:operation name="helloService">
            <wsdl:input message="tns:helloService"/>
            <wsdl:output message="tns:helloServiceResponse"/>
        </wsdl:operation>
    </wsdl:portType>

    <wsdl:binding name="testServiceBinding" type="tns:testServicePortType">
        <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http" />
        <wsdl:operation name="getRandom">
            <soap:operation soapAction="" style="document" />
            <id:version>v1</id:version>
            <wsdl:input>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:output>
        </wsdl:operation>

        <wsdl:operation name="helloService">
            <soap:operation soapAction="" style="document" />
            <id:version>v1</id:version>
            <wsdl:input>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body parts="body" use="literal"/>
                <soap:header message="tns:requestheader" part="client" use="literal"/>
                <soap:header message="tns:requestheader" part="service" use="literal"/>
                <soap:header message="tns:requestheader" part="userId" use="literal"/>
                <soap:header message="tns:requestheader" part="id" use="literal"/>
                <soap:header message="tns:requestheader" part="issue" use="literal"/>
                <soap:header message="tns:requestheader" part="protocolVersion" use="literal"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>
    <wsdl:service name="testService">
        <wsdl:port binding="tns:testServiceBinding" name="testServicePort">
            <soap:address location="http://localhost:8080/example-adapter-0.0.4-SNAPSHOT/Endpoint"/>
        </wsdl:port>
    </wsdl:service>
</wsdl:definitions>', 2050,'2016-01-01 00:00:00+02','2016-01-01 00:00:00+02','2016-01-01 00:00:00+02', NULL);


--
-- TOC entry 2148 (class 0 OID 0)
-- Dependencies: 187
-- Name: wsdl_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('wsdl_id_seq', 1, false);


-- Completed on 2016-01-29 14:34:02

--
-- PostgreSQL database dump complete
--

