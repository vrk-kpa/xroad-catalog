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
	public void testIsSoapService() {
		IsSoapService request = new IsSoapService();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("testService");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");
		IsSoapServiceResponse result = (IsSoapServiceResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsSoapService/", request);
		assertNotNull(result);
		assertEquals("Is given service a SOAP service", true, result.isSoap());
	}

	@Test
	public void testIsSoapServiceFalse() {
		IsSoapService request = new IsSoapService();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("getRandom");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");
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
			request.setXRoadInstance("dev-cs");
			request.setMemberClass("PUB");
			request.setMemberCode("14151328");
			request.setServiceCode("testService123");
			request.setSubsystemCode("TestSubSystem");
			request.setServiceVersion("v1");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/IsSoapService/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Service with xRoadInstance \"dev-cs\", " +
				"memberClass \"PUB\", memberCode \"14151328\", subsystemCode \"TestSubSystem\" " +
				"and serviceVersion \"v1\" not found");
	}

	@Test
	public void testIsRestService() {
		IsRestService request = new IsRestService();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("getRandom");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");
		IsRestServiceResponse result = (IsRestServiceResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsRestService/", request);
		assertNotNull(result);
		assertEquals("Is given service a REST service", true, result.isRest());
	}

	@Test
	public void testIsRestServiceFalse() {
		IsRestService request = new IsRestService();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("testService");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");
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
			request.setXRoadInstance("dev-cs");
			request.setMemberClass("PUB");
			request.setMemberCode("14151328");
			request.setServiceCode("getRandom123");
			request.setSubsystemCode("TestSubSystem");
			request.setServiceVersion("v1");
			new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/IsSoapService/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Service with xRoadInstance \"dev-cs\", " +
				"memberClass \"PUB\", memberCode \"14151328\", subsystemCode \"TestSubSystem\" " +
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
	public void testGetOrganizations() {
		GetOrganizations request = new GetOrganizations();
		request.setBusinessCode("0123456-9");
		GetOrganizationsResponse result = (GetOrganizationsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetOrganizations/", request);
		assertNotNull(result);
		assertEquals("OrganizationList size", 1, result.getOrganizationList().getOrganization().size());
		assertEquals("Organization businessCode", "0123456-9", result.getOrganizationList().getOrganization().get(0).getBusinessCode());
		assertEquals("Organization guid", "abcdef123456", result.getOrganizationList().getOrganization().get(0).getGuid());
		assertEquals("Organization street address latitude", "6939589.246", result.getOrganizationList()
				.getOrganization().get(0).getAddresses().getAddress().get(0).getStreetAddresses().getStreetAddress().get(0).getLatitude());
		assertEquals("Organization street address longitude", "208229.722", result.getOrganizationList()
				.getOrganization().get(0).getAddresses().getAddress().get(0).getStreetAddresses().getStreetAddress().get(0).getLongitude());
		assertEquals("Organization e-mail address", "vaasa@vaasa.fi", result.getOrganizationList()
				.getOrganization().get(0).getEmails().getEmail().get(0).getValue());
		assertEquals("Organization web page", "https://www.vaasa.fi/", result.getOrganizationList()
				.getOrganization().get(0).getWebPages().getWebPage().get(0).getUrl());
	}

	@Test
	public void testGetOrganizationsException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			GetOrganizations request = new GetOrganizations();
			request.setBusinessCode("0123456-1");
			GetOrganizationsResponse result = (GetOrganizationsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetOrganizations/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Organizations with businessCode 0123456-1 not found");
	}

	@Test
	public void testHasOrganizationChangedValueList() {
		HasOrganizationChanged request = new HasOrganizationChanged();
		request.setGuid("abcdef123456");
		LocalDateTime changedAfter = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xc = null;
		try {
			xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setChangedAfter(xc);
		HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		assertNotNull(result);
		assertEquals("Organization changed", true, result.isChanged());
		assertEquals("Organization changedValueList size", 19, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasOrganizationChangedSingleValue() {
		HasOrganizationChanged request = new HasOrganizationChanged();
		request.setGuid("abcdef123456");
		LocalDateTime changedAfter = LocalDateTime.of(2019, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xc = null;
		try {
			xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setChangedAfter(xc);
		HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		assertNotNull(result);
		assertEquals("Organization changed", true, result.isChanged());
		assertEquals("Organization changedValueList size", 1, result.getChangedValueList().getChangedValue().size());
		assertEquals("Organization changed value", "Email", result.getChangedValueList().getChangedValue().get(0).getName());
	}

	@Test
	public void testHasOrganizationChangedFalse() {
		HasOrganizationChanged request = new HasOrganizationChanged();
		request.setGuid("abcdef123456");
		LocalDateTime changedAfter = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
		GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xc = null;
		try {
			xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setChangedAfter(xc);
		HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		assertNotNull(result);
		assertEquals("Organization changed", false, result.isChanged());
		assertEquals("Organization changedValueList size", 0, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasOrganizationChangedException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			HasOrganizationChanged request = new HasOrganizationChanged();
			request.setGuid("a123456");
			LocalDateTime changedAfter = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
			GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar xc = null;
			try {
				xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setChangedAfter(xc);
			HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Organization with guid a123456 not found");

	}

	@Test
	public void testGetCompanies() {
		GetCompanies request = new GetCompanies();
		request.setBusinessId("1710128-9");
		GetCompaniesResponse result = (GetCompaniesResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetCompanies/", request);
		assertNotNull(result);
		assertEquals("CompanyList size", 1, result.getCompanyList().getCompany().size());
		assertEquals("Company businessId", "1710128-9", result.getCompanyList().getCompany().get(0).getBusinessId());
		assertEquals("Company detailsUri", "", result.getCompanyList().getCompany().get(0).getDetailsUri());
		assertEquals("Company companyForm", "OYJ", result.getCompanyList().getCompany().get(0).getCompanyForm());
		assertEquals("Company name", "Gofore Oyj", result.getCompanyList().getCompany().get(0).getName());
		assertEquals("Company BusinessAddress street", "Kalevantie 2",
				result.getCompanyList().getCompany().get(0).getBusinessAddresses().getBusinessAddress().get(0).getStreet());
		assertEquals("Company BusinessAuxiliaryName name", "Solinor",
				result.getCompanyList().getCompany().get(0).getBusinessAuxiliaryNames().getBusinessAuxiliaryName().get(0).getName());
		assertEquals("Company BusinessIdChange oldBusinessId", "1796717-0",
				result.getCompanyList().getCompany().get(0).getBusinessIdChanges().getBusinessIdChange().get(0).getOldBusinessId());
		assertEquals("Company BusinessLine name", "Dataprogrammering",
				result.getCompanyList().getCompany().get(0).getBusinessLines().getBusinessLine().get(0).getName());
		assertEquals("Company BusinessName language", "FI",
				result.getCompanyList().getCompany().get(0).getBusinessNames().getBusinessName().get(0).getLanguage());
		assertEquals("Company CompanyForm name", "Public limited company",
				result.getCompanyList().getCompany().get(0).getCompanyForms().getCompanyForm().get(0).getName());
		assertEquals("Company ContactDetails language", "EN",
				result.getCompanyList().getCompany().get(0).getContactDetails().getContactDetail().get(0).getLanguage());
		assertEquals("Company Language name", "Finska",
				result.getCompanyList().getCompany().get(0).getLanguages().getLanguage().get(0).getName());
		assertEquals("Company Liquidation language", "FI",
				result.getCompanyList().getCompany().get(0).getLiquidations().getLiquidation().get(0).getLanguage());
		assertEquals("Company RegisteredEntry descritpion", "Unregistered",
				result.getCompanyList().getCompany().get(0).getRegisteredEntries().getRegisteredEntry().get(0).getDescription());
		assertEquals("Company RegisteredOffice language", "FI",
				result.getCompanyList().getCompany().get(0).getRegisteredOffices().getRegisteredOffice().get(0).getLanguage());
	}

	@Test
	public void testGetCompaniesException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			GetCompanies request = new GetCompanies();
			request.setBusinessId("1710128-1");
			GetCompaniesResponse result = (GetCompaniesResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetCompanies/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "Companies with businessId 1710128-1 not found");
	}

	@Test
	public void testHasCompanyChangedValueList() {
		HasCompanyChanged request = new HasCompanyChanged();
		request.setBusinessId("1710128-9");
		LocalDateTime changedAfter = LocalDateTime.of(2020, Month.MAY, 4, 0, 0, 0);
		GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xc = null;
		try {
			xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setChangedAfter(xc);
		HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		assertNotNull(result);
		assertEquals("Company changed", true, result.isChanged());
		assertEquals("Company changedValueList size", 12, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasCompanyChangedTwoValues() {
		HasCompanyChanged request = new HasCompanyChanged();
		request.setBusinessId("1710128-9");
		LocalDateTime changedAfter = LocalDateTime.of(2020, Month.MAY, 6, 0, 0, 0);
		GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xc = null;
		try {
			xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setChangedAfter(xc);
		HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		assertNotNull(result);
		assertEquals("Company changed", true, result.isChanged());
		assertEquals("Company changedValueList size", 2, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasCompanyChangedFalse() {
		HasCompanyChanged request = new HasCompanyChanged();
		request.setBusinessId("1710128-9");
		LocalDateTime changedAfter = LocalDateTime.of(2020, Month.MAY, 6, 12, 0, 0);
		GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar xc = null;
		try {
			xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setChangedAfter(xc);
		HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		assertNotNull(result);
		assertEquals("Company changed", false, result.isChanged());
		assertEquals("Company changedValueList size", 0, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasCompanyChangedException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			HasCompanyChanged request = new HasCompanyChanged();
			request.setBusinessId("1710128-1");
			LocalDateTime changedAfter = LocalDateTime.of(2020, Month.MAY, 6, 12, 0, 0);
			GregorianCalendar cal = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar xc = null;
			try {
				xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setChangedAfter(xc);
			HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "company with businessId 1710128-1 not found");

	}

}
