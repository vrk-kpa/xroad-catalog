package fi.vrk.xroad.catalog.lister;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapElement;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;
import org.springframework.xml.transform.TransformerHelper;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.*;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * Created by sjk on 4.2.2016.
 */

public class ServiceEndpointInterceptor implements SoapEndpointInterceptor {

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws TransformerException, SOAPException {
		/* Create headers and add to msg */
        WebServiceMessage requestMsg = messageContext.getRequest();
        WebServiceMessage responseMsg = messageContext.getResponse();

        transformHeaders(requestMsg, messageContext.getResponse());

        return true;
    }



    protected SoapHeader getSoapHeader(WebServiceMessage message) {
        if (message instanceof SoapMessage) {
            SoapMessage soapMessage = (SoapMessage) message;
            return soapMessage.getEnvelope().getHeader();
        }
        else {
            return null;
        }
    }


    protected void transformHeaders(WebServiceMessage reqMessage, WebServiceMessage respMessage)
            throws TransformerException, SOAPException {

        SoapHeader reqHeader = getSoapHeader(reqMessage);
        SoapHeader respHeader = getSoapHeader(respMessage);

        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        Iterator<SoapHeaderElement> iter = reqHeader.examineAllHeaderElements();
        while (iter.hasNext()) {
            SoapHeaderElement elem = iter.next();
            transformer.transform(elem.getSource(), respHeader.getResult());
        }
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {

    }



    @Override
    public boolean understands(SoapHeaderElement header) {
        return true;
    }
}
