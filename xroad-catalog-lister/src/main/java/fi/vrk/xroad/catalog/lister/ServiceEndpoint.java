package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.ListServices;
import fi.vrk.xroad.xroad_catalog_lister.ListServicesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalTime;

@Endpoint
public class ServiceEndpoint {
	private static final String NAMESPACE_URI = "http://xroad.vrk.fi/xroad-catalog-lister";

	@Autowired
	private ServiceRepository serviceRepository;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "listServices")
	@ResponsePayload
	public ListServicesResponse listServices(@RequestPayload ListServices request) {
		ListServicesResponse response = new ListServicesResponse();
		response.getServices().addAll(serviceRepository.listServices(LocalTime.now()));

		return response;
	}
}
