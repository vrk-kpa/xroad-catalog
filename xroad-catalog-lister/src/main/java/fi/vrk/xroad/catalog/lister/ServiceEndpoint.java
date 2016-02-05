package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.ListMembers;
import fi.vrk.xroad.xroad_catalog_lister.ListMembersResponse;
import fi.vrk.xroad.xroad_catalog_lister.MemberList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.TransformerHelper;

import javax.xml.namespace.QName;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.Iterator;

@Endpoint
public class ServiceEndpoint {
	private static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

	@Autowired
	private ServiceRepository serviceRepository;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "ListMembers")
	@ResponsePayload
	public ListMembersResponse listMembers(@RequestPayload ListMembers request, MessageContext messageContext) throws
			Exception {
		ListMembersResponse response = new ListMembersResponse();
		response.setMemberList(new MemberList());
		response.getMemberList().getMembers().addAll(serviceRepository.listServices(LocalTime.now
				()));


		SoapHeader reqHeader = getSoapHeader(messageContext.getRequest());

		SoapHeader respHeader = getSoapHeader(messageContext.getResponse());
		transformHeaders(reqHeader, respHeader);
		return response;
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

	protected void transformHeaders(SoapHeader reqHeader, SoapHeader respHeader) throws TransformerException {
		Source source = reqHeader.getSource();
		Transformer transformer = createNonIndentingTransformer();
		StringWriter writer = new StringWriter();
		transformer.transform(source, respHeader.getResult());
	}


	private Transformer createNonIndentingTransformer() throws TransformerConfigurationException {
		Transformer transformer = createTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		return transformer;
	}

	protected final Transformer createTransformer() throws TransformerConfigurationException {
		return new TransformerHelper().createTransformer();
	}
}
