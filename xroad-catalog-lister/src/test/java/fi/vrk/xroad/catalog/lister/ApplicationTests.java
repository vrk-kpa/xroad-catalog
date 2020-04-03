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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.xroad_catalog_lister.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Http tests for lister interface
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ListerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	private Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

	@LocalServerPort
	private int port;

	@Before
	public void init() throws Exception {
		marshaller.setPackagesToScan(ClassUtils.getPackageName(ListMembers.class));
		marshaller.afterPropertiesSet();
	}


	@Test
	public void testListServices() {
		ListMembers request = new ListMembers();
		ListMembersResponse result = (ListMembersResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws", request);
		assertNotNull(result);
		assertEquals("MemberList size", 3, result.getMemberList().getMember().size());
	}

	@Test
	public void testIsSoapService() {
		IsSoapService request = new IsSoapService();
		request.setServiceCode("testService");
		request.setSubsystemCode("TestSubSystem");
		IsSoapServiceResponse result = (IsSoapServiceResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsSoapService/", request);
		assertNotNull(result);
		assertEquals("Is given service a SOAP service", true, result.isSoap());
	}

	@Test
	public void testIsSoapServiceFalse() {
		IsSoapService request = new IsSoapService();
		request.setServiceCode("getRandom");
		request.setSubsystemCode("TestSubSystem");
		IsSoapServiceResponse result = (IsSoapServiceResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsSoapService/", request);
		assertNotNull(result);
		assertEquals("Is given service a SOAP service", false, result.isSoap());
	}

	@Test
	public void testIsSoapServiceException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			IsSoapService request = new IsSoapService();
			request.setServiceCode("testService123");
			request.setSubsystemCode("TestSubSystem");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/IsSoapService/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Service with serviceCode \"testService123\" and subsystemCode \"TestSubSystem\" not found");
	}

	@Test
	public void testIsRestService() {
		IsRestService request = new IsRestService();
		request.setServiceCode("getRandom");
		request.setSubsystemCode("TestSubSystem");
		IsRestServiceResponse result = (IsRestServiceResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsRestService/", request);
		assertNotNull(result);
		assertEquals("Is given service a REST service", true, result.isRest());
	}

	@Test
	public void testIsRestServiceFalse() {
		IsRestService request = new IsRestService();
		request.setServiceCode("testService");
		request.setSubsystemCode("TestSubSystem");
		IsRestServiceResponse result = (IsRestServiceResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsRestService/", request);
		assertNotNull(result);
		assertEquals("Is given service a REST service", false, result.isRest());
	}

	@Test
	public void testIsRestServiceException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			IsRestService request = new IsRestService();
			request.setServiceCode("getRandom123");
			request.setSubsystemCode("TestSubSystem");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/IsSoapService/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Service with serviceCode \"getRandom123\" and subsystemCode \"TestSubSystem\" not found");
	}

	@Test
	public void testGetWsdl() {
		GetWsdl request = new GetWsdl();
		request.setExternalId("1000");
		GetWsdlResponse result = (GetWsdlResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetWsdl/", request);
		assertNotNull(result);
		assertEquals("getWsdl",
				"<?xml version=\"1.0\" standalone=\"no\"?><wsdl-6-1-1-1-changed/>",
				result.getWsdl());
	}

	@Test
	public void testGetWsdlException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			GetWsdl request = new GetWsdl();
			request.setExternalId("1001");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetWsdl/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "wsdl with external id 1001 not found");
	}

	@Test
	public void testGetOpenApi() {
		GetOpenAPI request = new GetOpenAPI();
		request.setExternalId("3003");
		GetOpenAPIResponse result = (GetOpenAPIResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetOpenAPI/", request);
		assertNotNull(result);
		assertEquals("getOpenAPI", "<openapi>", result.getOpenapi());
	}

	@Test
	public void testGetOpenApiException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			GetOpenAPI request = new GetOpenAPI();
			request.setExternalId("3001");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetOpenAPI/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "OpenApi with external id 3001 not found");
	}

	@Test
	public void testIsProvider() {
		IsProvider request = new IsProvider();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		IsProviderResponse result = (IsProviderResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsProvider/", request);
		assertNotNull(result);
		assertEquals("Is given member a service provider", true, result.isProvider());
	}

	@Test
	public void testIsProviderFalse() {
		IsProvider request = new IsProvider();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("88855888");
		IsProviderResponse result = (IsProviderResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsProvider/", request);
		assertNotNull(result);
		assertEquals("Is given member a service provider", false, result.isProvider());
	}

	@Test
	public void testIsProviderException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			IsProvider request = new IsProvider();
			request.setXRoadInstance("dev-cs");
			request.setMemberClass("PUB");
			request.setMemberCode("123");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/IsProvider/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Member with xRoadInstance \"dev-cs\", memberClass \"PUB\" and memberCode \"123\" not found");
	}

}
