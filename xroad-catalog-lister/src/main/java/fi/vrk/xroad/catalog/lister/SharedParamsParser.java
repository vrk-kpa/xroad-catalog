/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import com.google.common.collect.Lists;
import fi.vrk.xroad.catalog.persistence.dto.DescriptorInfo;
import fi.vrk.xroad.catalog.persistence.dto.Email;
import fi.vrk.xroad.catalog.persistence.dto.MemberInfo;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerData;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerDataList;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import fi.vrk.xroad.catalog.persistence.dto.SubsystemName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class SharedParamsParser {

    private static final String MEMBER = "member";
    private static final String MEMBER_CLASS = "memberClass";
    private static final String MEMBER_CODE = "memberCode";
    private static final String XML_VERSION = "1.0";
    private static final String OWNER = "owner";
    private static final String SERVER_CODE = "serverCode";
    private static final String ADDRESS = "address";
    private static final String CLIENT = "client";
    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String SUBSYSTEM = "subsystem";
    private static final String SUBSYSTEM_CODE = "subsystemCode";
    private static final String SECURITY_SERVER = "securityServer";
    private static final String DEFAULT_CONTACT_NAME = "Firstname Lastname";
    private static final String DEFAULT_CONTACT_EMAIL = "yourname@yourdomain";
    private static final String DEFAULT_SUBSYSTEM_NAME_EN = "Subsystem Name EN";
    private static final String DEFAULT_SUBSYSTEM_NAME_ET = "Subsystem Name ET";

    @Autowired
    private Environment environment;

    /**
     * Parses security server information from X-Road global configuration shared-params.xml.
     * Matches member elements with securityServer elements to gather the information.
     *
     * @return list of {@link SecurityServerInfo} objects
     * @throws ParserConfigurationException when there are issues with parsing the file
     * @throws IOException when unable to read input file
     * @throws SAXException when there are issues with parsing of XML
     */
    public Set<SecurityServerInfo> parseInfo(String sharedParamsFile) throws ParserConfigurationException, IOException, SAXException {
        Document document = parseInputAndConvertToXmlDocument(new File(sharedParamsFile));
        Element root = document.getDocumentElement();
        String xRoadInstance = root.getChildNodes().item(1).getFirstChild().getNodeValue();
        NodeList members = root.getElementsByTagName(MEMBER);
        NodeList securityServers = root.getElementsByTagName(SECURITY_SERVER);
        Set<SecurityServerInfo> securityServerInfos = new HashSet<>();

        for (int i = 0; i < securityServers.getLength(); i++) {
            Node securityServer = securityServers.item(i);
            if (securityServer.getNodeType() == Node.ELEMENT_NODE) {
                Element securityServerElement = (Element) securityServer;
                String owner = securityServerElement.getElementsByTagName(OWNER).item(0).getTextContent();
                String serverCode = securityServerElement.getElementsByTagName(SERVER_CODE).item(0).getTextContent();
                String address = securityServerElement.getElementsByTagName(ADDRESS).item(0).getTextContent();
                for (int j = 0; j < members.getLength(); j++) {
                    Node member = members.item(j);
                    if (member.getNodeType() == Node.ELEMENT_NODE) {
                        Element memberElement = (Element) member;
                        if (memberElement.getAttribute(ID).equals(owner)) {
                            Element memberClassElement =
                                    (Element) memberElement.getElementsByTagName(MEMBER_CLASS).item(0);
                            String memberClass =
                                    memberClassElement.getElementsByTagName(CODE).item(0).getTextContent();
                            String memberCode =
                                    memberElement.getElementsByTagName(MEMBER_CODE).item(0).getTextContent();
                            SecurityServerInfo info =
                                    new SecurityServerInfo(xRoadInstance, serverCode, address, memberClass, memberCode);
                            securityServerInfos.add(info);
                            break;
                        }
                    }
                }
            }
        }
        log.debug("Result set: {}", securityServerInfos.toString());
        return securityServerInfos;
    }

    /**
     * Parses security server information from X-Road global configuration shared-params.xml.
     * Matches member elements with securityServer elements to gather the information.
     *
     * @return list of {@link SecurityServerInfo} objects
     * @throws ParserConfigurationException when there are issues with parsing the file
     * @throws IOException when unable to read input file
     * @throws SAXException when there are issues with parsing of XML
     */
    public SecurityServerDataList parseDetails(String sharedParamsFile) throws ParserConfigurationException, IOException, SAXException {
        Document document = parseInputAndConvertToXmlDocument(new File(sharedParamsFile));
        Element root = document.getDocumentElement();
        List<SecurityServerData> securityServerList = getSecurityServerDataList(root.getElementsByTagName(SECURITY_SERVER), root.getElementsByTagName(MEMBER));
        return SecurityServerDataList.builder().securityServerDataList(securityServerList).build();
    }

    /**
     * Parses security server information from X-Road global configuration shared-params.xml.
     * Matches member elements with securityServer elements to gather the information.
     *
     * @return list of {@link SecurityServerInfo} objects
     * @throws ParserConfigurationException when there are issues with parsing the file
     * @throws IOException when unable to read input file
     * @throws SAXException when there are issues with parsing of XML
     */
    public List<DescriptorInfo> parseDescriptorInfo(String sharedParamsFile) throws ParserConfigurationException, IOException, SAXException {
        Document document = parseInputAndConvertToXmlDocument(new File(sharedParamsFile));
        Element root = document.getDocumentElement();
        String xRoadInstance = root.getChildNodes().item(1).getFirstChild().getNodeValue();
        NodeList members = root.getElementsByTagName(MEMBER);
        List<DescriptorInfo> descriptorInfos = new ArrayList<>();

        for (int j = 0; j < members.getLength(); j++) {
            Node member = members.item(j);
            if (member.getNodeType() == Node.ELEMENT_NODE) {
                Element memberElement = (Element) member;
                Element memberClassElement = (Element) memberElement.getElementsByTagName(MEMBER_CLASS).item(0);
                String memberClass = memberClassElement.getElementsByTagName(CODE).item(0).getTextContent();
                String memberCode = memberElement.getElementsByTagName(MEMBER_CODE).item(0).getTextContent();
                String name = memberElement.getElementsByTagName(NAME).item(0).getTextContent();
                NodeList subsystems = memberElement.getElementsByTagName(SUBSYSTEM);
                for (int k = 0; k < subsystems.getLength(); k++) {
                    Node subsystem = subsystems.item(k);
                    if (member.getNodeType() == Node.ELEMENT_NODE) {
                        Element subsystemElement = (Element) subsystem;
                        String subsystemCode =
                                subsystemElement.getElementsByTagName(SUBSYSTEM_CODE).item(0).getTextContent();

                        descriptorInfos.add(DescriptorInfo.builder()
                                .xRoadInstance(xRoadInstance)
                                .memberCode(memberCode)
                                .memberClass(memberClass)
                                .memberName(name)
                                .subsystemCode(subsystemCode)
                                .subsystemName(SubsystemName.builder().en(DEFAULT_SUBSYSTEM_NAME_EN).et(DEFAULT_SUBSYSTEM_NAME_ET).build())
                                .email(Lists.newArrayList(
                                        Email.builder()
                                                .name(DEFAULT_CONTACT_NAME)
                                                .email(DEFAULT_CONTACT_EMAIL)
                                                .build())).build());
                    }
                }

            }
        }

        return descriptorInfos;
    }

    private Document parseInputAndConvertToXmlDocument(File inputFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputFile);
        document.setXmlVersion(XML_VERSION);
        document.getDocumentElement().normalize();
        return document;
    }

    private List<SecurityServerData> getSecurityServerDataList(NodeList securityServers, NodeList members) {
        List<SecurityServerData> securityServerList = new ArrayList<>();
        for (int i = 0; i < securityServers.getLength(); i++) {
            Node securityServer = securityServers.item(i);
            if (securityServer.getNodeType() == Node.ELEMENT_NODE) {
                securityServerList.add(getSecurityServerData(securityServer, members));
            }
        }
        return securityServerList;
    }

    private SecurityServerData getSecurityServerData(Node securityServer, NodeList members) {
        Element securityServerElement = (Element) securityServer;
        String owner = securityServerElement.getElementsByTagName(OWNER).item(0).getTextContent();
        String serverCode = securityServerElement.getElementsByTagName(SERVER_CODE).item(0).getTextContent();
        String address = securityServerElement.getElementsByTagName(ADDRESS).item(0).getTextContent();
        MemberInfo ownerData = MemberInfo.builder().build();
        NodeList clientNames = securityServerElement.getElementsByTagName(CLIENT);
        List<MemberInfo> clients = new ArrayList<>();
        List<String> clientNamesList = new ArrayList<>();
        for (int j = 0; j < clientNames.getLength(); j++) {
            Node member = clientNames.item(j);
            clientNamesList.add(member.getFirstChild().getNodeValue());
        }
        for (int j = 0; j < members.getLength(); j++) {
            Node member = members.item(j);
            if (member.getNodeType() == Node.ELEMENT_NODE) {
                Element memberElement = (Element) member;
                Element memberClassElement = (Element) memberElement.getElementsByTagName(MEMBER_CLASS).item(0);
                String memberClass = memberClassElement.getElementsByTagName(CODE).item(0).getTextContent();
                String memberCode = memberElement.getElementsByTagName(MEMBER_CODE).item(0).getTextContent();
                String name = memberElement.getElementsByTagName(NAME).item(0).getTextContent();
                clients = getClients(memberElement, member, clientNamesList, memberClass, memberCode, name);
                ownerData = getOwnerData(memberElement, owner, memberClass, memberCode, name);
            }
        }
        return SecurityServerData.builder()
                .owner(ownerData)
                .serverCode(serverCode)
                .address(address)
                .clients(clients).build();
    }

    private MemberInfo getOwnerData(Element memberElement,
                                    String owner,
                                    String memberClass,
                                    String memberCode,
                                    String name) {
        MemberInfo ownerData = MemberInfo.builder().build();
        if (memberElement.getAttribute(ID).equals(owner)) {
            ownerData = MemberInfo.builder()
                    .memberClass(memberClass)
                    .memberCode(memberCode)
                    .subsystemCode(null)
                    .name(name).build();
        }
        return ownerData;
    }

    private List<MemberInfo> getClients(Element memberElement,
                                        Node member,
                                        List<String> clientNamesList,
                                        String memberClass,
                                        String memberCode,
                                        String name) {
        List<MemberInfo> clients = new ArrayList<>();
        NodeList subsystems = memberElement.getElementsByTagName(SUBSYSTEM);
        for (int k = 0; k < subsystems.getLength(); k++) {
            Node subsystem = subsystems.item(k);
            if (member.getNodeType() == Node.ELEMENT_NODE) {
                Element subsystemElement = (Element) subsystem;
                if (clientNamesList.contains(subsystemElement.getAttribute(ID))) {
                    String subsystemCode =
                            subsystemElement.getElementsByTagName(SUBSYSTEM_CODE).item(0).getTextContent();
                    clients.add(MemberInfo.builder()
                            .memberClass(memberClass)
                            .memberCode(memberCode)
                            .subsystemCode(subsystemCode)
                            .name(name).build());
                }
            }
        }
        if (clientNamesList.contains(memberElement.getAttribute(ID))) {
            clients.add(MemberInfo.builder()
                    .memberClass(memberClass)
                    .memberCode(memberCode)
                    .subsystemCode(null)
                    .name(name).build());
        }
        return clients;
    }
}
