/**
 * The MIT License
 * Copyright (c) 2020, Population Register Centre (VRK)
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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for parsing X-Road shared-params.xml
 */
@Slf4j
@Component
public class SharedParamsParser {


    @Autowired
    private Environment environment;

    /**
     * Parses security server information from X-Road global configuration shared-params.xml.
     * Matches member elements with securityServer elements to gather the information.
     *
     * @return list of {@link SecurityServerInfo} objects
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public Set<SecurityServerInfo> parse(String sharedParamsFile) throws ParserConfigurationException, IOException, SAXException {
        File inputFile = new File(sharedParamsFile);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputFile);
        document.setXmlVersion("1.0");
        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();

        NodeList members = root.getElementsByTagName("member");
        NodeList securityServers = root.getElementsByTagName("securityServer");
        Set<SecurityServerInfo> securityServerInfos = new HashSet<>();

        for (int i = 0; i < securityServers.getLength(); i++) {
            Node securityServer = securityServers.item(i);
            if (securityServer.getNodeType() == Node.ELEMENT_NODE) {
                Element securityServerElement = (Element) securityServer;
                String owner = securityServerElement.getElementsByTagName("owner").item(0).getTextContent();
                String serverCode = securityServerElement.getElementsByTagName("serverCode").item(0).getTextContent();
                String address = securityServerElement.getElementsByTagName("address").item(0).getTextContent();
                for (int j = 0; j < members.getLength(); j++) {
                    Node member = members.item(j);
                    if (member.getNodeType() == Node.ELEMENT_NODE) {
                        Element memberElement = (Element) member;
                        if (memberElement.getAttribute("id").equals(owner)) {
                            Element memberClassElement =
                                    (Element) memberElement.getElementsByTagName("memberClass").item(0);
                            String memberClass =
                                    memberClassElement.getElementsByTagName("code").item(0).getTextContent();
                            String memberCode =
                                    memberElement.getElementsByTagName("memberCode").item(0).getTextContent();
                            SecurityServerInfo info =
                                    new SecurityServerInfo(serverCode, address, memberClass, memberCode);
                            log.debug("SecurityServerInfo: {}", info);
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
}
