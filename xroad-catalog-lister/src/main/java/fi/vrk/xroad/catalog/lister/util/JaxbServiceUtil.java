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
package fi.vrk.xroad.catalog.lister.util;

import fi.vrk.xroad.catalog.lister.CatalogListerRuntimeException;
import fi.vrk.xroad.catalog.persistence.entity.OpenApi;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.xroad_catalog_lister.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

public class JaxbServiceUtil {

    private JaxbServiceUtil() {

    }

    public static fi.vrk.xroad.xroad_catalog_lister.Service convertService(Service service, boolean onlyActiveChildren) {
        fi.vrk.xroad.xroad_catalog_lister.Service cs = new fi.vrk.xroad.xroad_catalog_lister.Service();
        cs.setChanged(toXmlGregorianCalendar(service.getStatusInfo().getChanged()));
        cs.setCreated(toXmlGregorianCalendar(service.getStatusInfo().getCreated()));
        cs.setFetched(toXmlGregorianCalendar(service.getStatusInfo().getFetched()));
        cs.setRemoved(toXmlGregorianCalendar(service.getStatusInfo().getRemoved()));
        cs.setServiceCode(service.getServiceCode());
        cs.setServiceVersion(service.getServiceVersion());
        Wsdl wsdl = null;
        if (onlyActiveChildren) {
            if (service.getWsdl() != null && !service.getWsdl().getStatusInfo().isRemoved()) {
                wsdl = service.getWsdl();
            }
        } else {
            wsdl = service.getWsdl();
        }
        if (wsdl != null) {
            cs.setWsdl(convertWsdl(service.getWsdl()));
        }
        OpenApi openApi = null;
        if (onlyActiveChildren) {
            if (service.getOpenApi() != null && !service.getOpenApi().getStatusInfo().isRemoved()) {
                openApi = service.getOpenApi();
            }
        } else {
            openApi = service.getOpenApi();
        }
        if (openApi != null) {
            cs.setOpenapi(convertOpenApi(service.getOpenApi()));
        }
        return cs;
    }

    public static WSDL convertWsdl(Wsdl wsdl) {
        WSDL cw = new WSDL();
        cw.setChanged(toXmlGregorianCalendar(wsdl.getStatusInfo().getChanged()));
        cw.setCreated(toXmlGregorianCalendar(wsdl.getStatusInfo().getCreated()));
        cw.setFetched(toXmlGregorianCalendar(wsdl.getStatusInfo().getFetched()));
        cw.setRemoved(toXmlGregorianCalendar(wsdl.getStatusInfo().getRemoved()));
        cw.setExternalId(wsdl.getExternalId());
        return cw;
    }

    public static OPENAPI convertOpenApi(OpenApi openApi) {
        OPENAPI cw = new OPENAPI();
        cw.setChanged(toXmlGregorianCalendar(openApi.getStatusInfo().getChanged()));
        cw.setCreated(toXmlGregorianCalendar(openApi.getStatusInfo().getCreated()));
        cw.setFetched(toXmlGregorianCalendar(openApi.getStatusInfo().getFetched()));
        cw.setRemoved(toXmlGregorianCalendar(openApi.getStatusInfo().getRemoved()));
        cw.setExternalId(openApi.getExternalId());
        return cw;
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            GregorianCalendar cal = GregorianCalendar.from(localDateTime.atZone(ZoneId.systemDefault()));
            XMLGregorianCalendar xc;
            try {
                xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            } catch (DatatypeConfigurationException e) {
                throw new CatalogListerRuntimeException("Cannot instantiate DatatypeFactory");
            }
            return xc;
        }
    }

}
