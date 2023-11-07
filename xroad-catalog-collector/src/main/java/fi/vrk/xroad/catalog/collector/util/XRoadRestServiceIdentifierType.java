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
package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.collector.wsimport.XRoadIdentifierType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.util.List;


/**
 * <p>Java class for XRoadServiceIdentifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XRoadServiceIdentifierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://x-road.eu/xsd/identifiers}XRoadIdentifierType">
 *       &lt;sequence>
 *         &lt;element ref="{http://x-road.eu/xsd/identifiers}xRoadInstance"/>
 *         &lt;element ref="{http://x-road.eu/xsd/identifiers}memberClass"/>
 *         &lt;element ref="{http://x-road.eu/xsd/identifiers}memberCode"/>
 *         &lt;element ref="{http://x-road.eu/xsd/identifiers}subsystemCode" minOccurs="0"/>
 *         &lt;element ref="{http://x-road.eu/xsd/identifiers}serviceCode"/>
 *         &lt;element ref="{http://x-road.eu/xsd/identifiers}serviceVersion" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://x-road.eu/xsd/identifiers}objectType use="required" fixed="SERVICE""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XRoadServiceIdentifierType", namespace = "http://x-road.eu/xsd/identifiers")
public class XRoadRestServiceIdentifierType extends XRoadIdentifierType
{
    protected String serviceType;

    protected List<Endpoint> endpointList;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String value) {
        this.serviceType = value;
    }

    public List<Endpoint> getEndpoints() { return endpointList; }

    public void setEndpoints(List<Endpoint> endpoints) { this.endpointList = endpoints; }
}
