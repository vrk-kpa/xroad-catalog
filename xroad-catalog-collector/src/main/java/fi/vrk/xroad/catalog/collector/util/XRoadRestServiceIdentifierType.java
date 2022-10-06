
package fi.vrk.xroad.catalog.collector.util;

import fi.vrk.xroad.catalog.collector.wsimport.XRoadIdentifierType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
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
