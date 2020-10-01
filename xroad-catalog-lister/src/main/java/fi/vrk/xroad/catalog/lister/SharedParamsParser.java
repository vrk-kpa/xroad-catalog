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
