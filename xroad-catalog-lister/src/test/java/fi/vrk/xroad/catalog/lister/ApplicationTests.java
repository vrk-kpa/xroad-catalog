/**
 * The MIT License
 * Copyright (c) 2021, Population Register Centre (VRK)
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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.GregorianCalendar;

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
	public void testGetServiceTypeSOAP() {
		GetServiceType request = new GetServiceType();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("testService");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");
		GetServiceTypeResponse result = (GetServiceTypeResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetServiceType/", request);
		assertNotNull(result);
		assertEquals("Is given service a SOAP service", "SOAP", result.getType());
	}


	@Test
	public void testGetServiceTypeREST() {
		GetServiceType request = new GetServiceType();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("getAnotherRandom");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");
		GetServiceTypeResponse result = (GetServiceTypeResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetServiceType/", request);
		assertNotNull(result);
		assertEquals("Is given service a SOAP service", "REST", result.getType());
	}

	@Test
	public void testGetServiceTypeOPENAPI() {
		GetServiceType request = new GetServiceType();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("getRandom");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");
		GetServiceTypeResponse result = (GetServiceTypeResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetServiceType/", request);
		assertNotNull(result);
		assertEquals("Is given service a SOAP service", "OPENAPI", result.getType());
	}

	@Test
	public void testGetServiceTypeException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			GetServiceType request = new GetServiceType();
			request.setXRoadInstance("dev-cs");
			request.setMemberClass("PUB");
			request.setMemberCode("14151328");
			request.setServiceCode("testService123");
			request.setSubsystemCode("TestSubSystem");
			request.setServiceVersion("v1");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetServiceType/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Service with xRoadInstance \"dev-cs\", " +
				"memberClass \"PUB\", memberCode \"14151328\", serviceCode \"testService123\", subsystemCode \"TestSubSystem\" " +
				"and serviceVersion \"v1\" not found");
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

	@Test
	public void testGetErrors() {
		GetErrors request = new GetErrors();
		LocalDateTime changedAfter = LocalDateTime.of(2001, Month.MAY, 6, 12, 0, 0);
		GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xc = null;
		try {
			xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setSince(xc);
		GetErrorsResponse result = (GetErrorsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetErrors/", request);
		assertNotNull(result);
		assertEquals("ErrorLogList size", 6, result.getErrorLogList().getErrorLog().size());
		assertEquals("ErrorLog message", "Service not found", result.getErrorLogList().getErrorLog().get(0).getMessage());
	}

	@Test
	public void testGetErrorsException() {
		boolean thrown = false;
		String exceptionMessage = null;
		GetErrors request = new GetErrors();
		try {
			LocalDateTime changedAfter = LocalDateTime.of(2021, Month.JANUARY, 6, 12, 0, 0);
			GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar xc = null;
			try {
				xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setSince(xc);

			GetErrorsResponse result = (GetErrorsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetErrors/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "ErrorLog entries since " + request.getSince().toString() + " not found");
	}
}
