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
package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.collector.wsimport.*;
import lombok.extern.slf4j.Slf4j;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * WS client
 */
@Slf4j
public class XRoadClient {

    private XRoadClient() {
        // Private empty constructor
    }

    /**
     * Calls the service using JAX-WS endpoints that have been generated from wsdl
     */
    public static List<XRoadServiceIdentifierType> getMethods(String securityServerHost, XRoadClientIdentifierType
                                                              securityServerIdentity,
                                                              ClientType client)
            throws MalformedURLException {

        securityServerIdentity.setObjectType(XRoadObjectType.MEMBER);

        XRoadServiceIdentifierType serviceIdentifierType = new XRoadServiceIdentifierType();
        copyIdentifierType(serviceIdentifierType, client.getId());

        serviceIdentifierType.setServiceCode("listMethods");
        serviceIdentifierType.setServiceVersion("v1");
        serviceIdentifierType.setObjectType(XRoadObjectType.SERVICE);

        URL url = new URL(securityServerHost);
        log.info("SOAP call at url {} for member {} and service {}", url, ClientTypeUtil.toString
                (securityServerIdentity), ClientTypeUtil
                .toString(securityServerIdentity));

        MetaServicesPort port = getMetaServicesPort(url);

        ListMethodsResponse response = port.listMethods(
                new ListMethods(),
                new Holder<>(securityServerIdentity),
                new Holder<>(serviceIdentifierType),
                new Holder<>("xroad-catalog-collector-"+ UUID.randomUUID()),
                new Holder<>("xroad-catalog-collector"),
                new Holder<>("4.x"));

        return response.getService();
    }

    /**
     * MetaServicesPort for url
     */
    public static MetaServicesPort getMetaServicesPort(URL url) {
        SocketFactory sf = SSLSocketFactory.getDefault();

        URL wsdl = XRoadClient.class.getClassLoader()
                .getResource("schema/list-methods.wsdl");
        ProducerPortService service = new ProducerPortService(wsdl,
                new QName("http://metadata.x-road.eu/", "producerPortService"));
        MetaServicesPort port = service.getMetaServicesPortSoap11();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url.toString());
        bindingProvider.getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory",
                sf);
        return port;
    }

    protected static void copyIdentifierType
    (XRoadIdentifierType target, XRoadIdentifierType source) {

        target.setGroupCode(source.getGroupCode());
        target.setObjectType(XRoadObjectType.fromValue(source.getObjectType().value()));
        target.setMemberCode(source.getMemberCode());
        target.setServiceVersion(source.getServiceVersion());
        target.setMemberClass(source.getMemberClass());
        target.setServiceCode(source.getServiceCode());
        target.setSecurityCategoryCode(source.getSecurityCategoryCode());
        target.setServerCode(source.getServerCode());
        target.setXRoadInstance(source.getXRoadInstance());
        target.setSubsystemCode(source.getSubsystemCode());
    }

}
