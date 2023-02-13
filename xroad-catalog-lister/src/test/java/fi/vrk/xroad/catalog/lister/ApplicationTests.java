/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.util.JaxbServiceUtil;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.entity.OpenApi;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import fi.vrk.xroad.xroad_catalog_lister.Company;
import fi.vrk.xroad.xroad_catalog_lister.Email;
import fi.vrk.xroad.xroad_catalog_lister.EmailList;
import fi.vrk.xroad.xroad_catalog_lister.ErrorLog;
import fi.vrk.xroad.xroad_catalog_lister.GetCompanies;
import fi.vrk.xroad.xroad_catalog_lister.GetCompaniesResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetErrors;
import fi.vrk.xroad.xroad_catalog_lister.GetErrorsResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetOpenAPI;
import fi.vrk.xroad.xroad_catalog_lister.GetOpenAPIResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetOrganizations;
import fi.vrk.xroad.xroad_catalog_lister.GetOrganizationsResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetServiceType;
import fi.vrk.xroad.xroad_catalog_lister.GetServiceTypeResponse;
import fi.vrk.xroad.xroad_catalog_lister.GetWsdl;
import fi.vrk.xroad.xroad_catalog_lister.GetWsdlResponse;
import fi.vrk.xroad.xroad_catalog_lister.HasCompanyChanged;
import fi.vrk.xroad.xroad_catalog_lister.HasCompanyChangedResponse;
import fi.vrk.xroad.xroad_catalog_lister.HasOrganizationChanged;
import fi.vrk.xroad.xroad_catalog_lister.HasOrganizationChangedResponse;
import fi.vrk.xroad.xroad_catalog_lister.IsProvider;
import fi.vrk.xroad.xroad_catalog_lister.IsProviderResponse;
import fi.vrk.xroad.xroad_catalog_lister.ListMembers;
import fi.vrk.xroad.xroad_catalog_lister.ListMembersResponse;
import fi.vrk.xroad.xroad_catalog_lister.Member;
import fi.vrk.xroad.xroad_catalog_lister.Organization;
import fi.vrk.xroad.xroad_catalog_lister.WebPage;
import fi.vrk.xroad.xroad_catalog_lister.WebPageList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ClassUtils;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = ListerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"default","fi"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApplicationTests {

	private final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

	@LocalServerPort
	private int port;

	@MockBean
	CatalogService catalogService;

	@MockBean
	JaxbCatalogService jaxbCatalogService;

	@MockBean
	JaxbCompanyService jaxbCompanyService;

	@MockBean
	JaxbOrganizationService jaxbOrganizationService;

	@BeforeAll
	public void init() throws Exception {
		marshaller.setPackagesToScan(ClassUtils.getPackageName(ListMembers.class));
		marshaller.afterPropertiesSet();
	}

	@Test
	public void testListServices() {
		mockMembersForListServices();
		ListMembers request = new ListMembers();
		XMLGregorianCalendar startDateTime = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2020, 1, 1, 1, 1));
		XMLGregorianCalendar endDateTime = JaxbServiceUtil.toXmlGregorianCalendar(LocalDateTime.of(2030, 1, 31, 1, 1));
		request.setStartDateTime(startDateTime);
		request.setEndDateTime(endDateTime);
		ListMembersResponse result = (ListMembersResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws", request);
		assertNotNull(result);
		assertEquals(3, result.getMemberList().getMember().size());
	}

	@Test
	public void testGetServiceType() {
		GetServiceType request = new GetServiceType();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		request.setServiceCode("testService");
		request.setSubsystemCode("TestSubSystem");
		request.setServiceVersion("v1");

		mockServicesForGetServiceType(
				request.getXRoadInstance(),
				request.getMemberClass(),
				request.getMemberCode(),
				request.getSubsystemCode(),
				request.getServiceCode(),
				request.getServiceVersion(),
				"SOAP");

		GetServiceTypeResponse result = (GetServiceTypeResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetServiceType/", request);
		assertNotNull(result);
		assertEquals("SOAP", result.getType());

		request.setServiceCode("getAnotherRandom");
		mockServicesForGetServiceType(
				request.getXRoadInstance(),
				request.getMemberClass(),
				request.getMemberCode(),
				request.getSubsystemCode(),
				request.getServiceCode(),
				request.getServiceVersion(),
				"REST");
		result = (GetServiceTypeResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetServiceType/", request);
		assertNotNull(result);
		assertEquals("REST", result.getType());

		request.setServiceCode("getRandom");
		mockServicesForGetServiceType(
				request.getXRoadInstance(),
				request.getMemberClass(),
				request.getMemberCode(),
				request.getSubsystemCode(),
				request.getServiceCode(),
				request.getServiceVersion(),
				"OPENAPI");
		result = (GetServiceTypeResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetServiceType/", request);
		assertNotNull(result);
		assertEquals("OPENAPI", result.getType());

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
				"memberClass \"PUB\", memberCode \"14151328\", subsystemCode \"TestSubSystem\", serviceCode \"testService123\" " +
				"and serviceVersion \"v1\" not found");
	}

	@Test
	public void testGetWsdl() {
		GetWsdl request = new GetWsdl();
		request.setExternalId("1000");
		given(catalogService.getWsdl(request.getExternalId())).willReturn(new Wsdl(new Service(), "This is WSDL", request.getExternalId()));
		GetWsdlResponse result = (GetWsdlResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetWsdl/", request);
		assertNotNull(result);
		assertEquals("This is WSDL", result.getWsdl());
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
		assertEquals("wsdl with external id 1001 not found", exceptionMessage);
	}

	@Test
	public void testGetOpenApi() {
		GetOpenAPI request = new GetOpenAPI();
		request.setExternalId("3003");
		given(catalogService.getOpenApi(request.getExternalId())).willReturn(new OpenApi(new Service(), "This is OpenAPI", request.getExternalId()));
		GetOpenAPIResponse result = (GetOpenAPIResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetOpenAPI/", request);
		assertNotNull(result);
		assertEquals("This is OpenAPI", result.getOpenapi());
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
		assertEquals("OpenApi with external id 3001 not found", exceptionMessage);
	}

	@Test
	public void testIsProvider() {
		IsProvider request = new IsProvider();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("14151328");
		mockProvider(request.getXRoadInstance(), request.getMemberClass(), request.getMemberCode());
		IsProviderResponse result = (IsProviderResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsProvider/", request);
		assertNotNull(result);
		assertTrue(result.isProvider());
	}

	@Test
	public void testIsProviderFalse() {
		IsProvider request = new IsProvider();
		request.setXRoadInstance("dev-cs");
		request.setMemberClass("PUB");
		request.setMemberCode("88855888");
		mockNoProvider(request.getXRoadInstance(), request.getMemberClass(), request.getMemberCode());
		IsProviderResponse result = (IsProviderResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/IsProvider/", request);
		assertNotNull(result);
		assertFalse(result.isProvider());
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
		assertEquals("Member with xRoadInstance \"dev-cs\", memberClass \"PUB\" and memberCode \"123\" not found", exceptionMessage);
	}

	@Test
	public void testGetOrganizations() {
		String businessCode = "0123456-9";
		String emailAddress = "vaasa@vaasa.fi";
		String url = "https://www.vaasa.fi/";
		GetOrganizations request = new GetOrganizations();
		request.setBusinessCode(businessCode);
		mockOrganizations(businessCode, emailAddress, url);
		GetOrganizationsResponse result = (GetOrganizationsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetOrganizations/", request);
		assertNotNull(result);
		assertEquals(1, result.getOrganizationList().getOrganization().size());
		assertEquals(businessCode, result.getOrganizationList().getOrganization().get(0).getBusinessCode());
		assertEquals(emailAddress, result.getOrganizationList().getOrganization().get(0).getEmails().getEmail().get(0).getValue());
		assertEquals(url, result.getOrganizationList().getOrganization().get(0).getWebPages().getWebPage().get(0).getUrl());
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
		assertEquals("Organizations with businessCode 0123456-1 not found", exceptionMessage);
	}

	@Test
	public void testHasOrganizationChangedValueList() {
		HasOrganizationChanged request = new HasOrganizationChanged();
		request.setGuid("abcdef123456");
		LocalDateTime changedAfter = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
		LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar startDateTime = null;
		XMLGregorianCalendar endDateTime = null;
		try {
			startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
			endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setStartDateTime(startDateTime);
		request.setEndDateTime(endDateTime);
		mockChangedOrganizationMultipleValues(request.getGuid(), request.getStartDateTime(), request.getEndDateTime());
		HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		assertNotNull(result);
		assertTrue(result.isChanged());
		assertEquals(7, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasOrganizationChangedSingleValue() {
		String guid = "abcdef123456";
		HasOrganizationChanged request = new HasOrganizationChanged();
		request.setGuid(guid);
		LocalDateTime changedAfter = LocalDateTime.of(2019, Month.JULY, 29, 19, 30, 40);
		LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar startDateTime = null;
		XMLGregorianCalendar endDateTime = null;
		try {
			startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
			endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setStartDateTime(startDateTime);
		request.setEndDateTime(endDateTime);
		mockChangedOrganization(request.getGuid(), request.getStartDateTime(), request.getEndDateTime());
		HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		assertNotNull(result);
		assertTrue(result.isChanged());
		assertEquals(1, result.getChangedValueList().getChangedValue().size());
		assertEquals("Email", result.getChangedValueList().getChangedValue().get(0).getName());
	}

	@Test
	public void testHasOrganizationChangedFalse() {
		HasOrganizationChanged request = new HasOrganizationChanged();
		request.setGuid("abcdef123456");
		LocalDateTime changedAfter = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
		LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar startDateTime = null;
		XMLGregorianCalendar endDateTime = null;
		try {
			startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
			endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setStartDateTime(startDateTime);
		request.setEndDateTime(endDateTime);
		mockUnchangedOrganization(request.getGuid(), request.getStartDateTime(), request.getEndDateTime());
		HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		assertNotNull(result);
		assertEquals(false, result.isChanged(), "Organization changed");
		assertEquals(0, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasOrganizationChangedException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			HasOrganizationChanged request = new HasOrganizationChanged();
			request.setGuid("a123456");
			LocalDateTime changedAfter = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
			LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
			GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar startDateTime = null;
			XMLGregorianCalendar endDateTime = null;
			try {
				startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
				endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setStartDateTime(startDateTime);
			request.setEndDateTime(endDateTime);
			mockChangedOrganizationNotFoundException(request.getGuid(), request.getStartDateTime(), request.getEndDateTime());
			HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals("Organization with guid a123456 not found", exceptionMessage);

	}

	@Test
	public void testHasOrganizationChangedGuidRequiredException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			HasOrganizationChanged request = new HasOrganizationChanged();
			request.setGuid(null);
			LocalDateTime changedAfter = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
			LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
			GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar startDateTime = null;
			XMLGregorianCalendar endDateTime = null;
			try {
				startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
				endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setStartDateTime(startDateTime);
			request.setEndDateTime(endDateTime);
			HasOrganizationChangedResponse result = (HasOrganizationChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/HasOrganizationChanged/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals("Guid is a required parameter", exceptionMessage);

	}

	@Test
	public void testGetCompanies() {
		String businessId = "1710128-9";
		String detailsUri = "detailsUri";
		String companyForm = "OYJ";
		String name = "Gofore Oyj";
		GetCompanies request = new GetCompanies();
		request.setBusinessId(businessId);
		mockCompanies(businessId, detailsUri, companyForm, name);
		GetCompaniesResponse result = (GetCompaniesResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetCompanies/", request);
		assertNotNull(result);
		assertEquals(1, result.getCompanyList().getCompany().size());
		assertEquals(businessId, result.getCompanyList().getCompany().get(0).getBusinessId());
		assertEquals(detailsUri, result.getCompanyList().getCompany().get(0).getDetailsUri());
		assertEquals(companyForm, result.getCompanyList().getCompany().get(0).getCompanyForm());
		assertEquals(name, result.getCompanyList().getCompany().get(0).getName());
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
		assertEquals("Companies with businessId 1710128-1 not found", exceptionMessage);
	}

	@Test
	public void testGetCompaniesBusinessIdRequiredException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			GetCompanies request = new GetCompanies();
			request.setBusinessId(null);
			GetCompaniesResponse result = (GetCompaniesResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetCompanies/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals("Companies with businessId null not found", exceptionMessage);
	}

	@Test
	public void testHasCompanyChangedValueList() {
		HasCompanyChanged request = new HasCompanyChanged();
		request.setBusinessId("1710128-9");
		LocalDateTime changedAfter = LocalDateTime.of(2020, Month.MAY, 4, 0, 0, 0);
		LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar startDateTime = null;
		XMLGregorianCalendar endDateTime = null;
		try {
			startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
			endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setStartDateTime(startDateTime);
		request.setEndDateTime(endDateTime);
		mockChangedCompany(request.getBusinessId(), request.getStartDateTime(), request.getEndDateTime());
		HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		assertNotNull(result);
		assertEquals(true, result.isChanged(), "Company changed");
		assertEquals(12, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasCompanyChangedFalse() {
		String businessId = "1710128-9";
		LocalDateTime changedAfter = LocalDateTime.of(2021, Month.MAY, 6, 12, 0, 0);
		LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
		HasCompanyChanged request = new HasCompanyChanged();
		request.setBusinessId(businessId);
		XMLGregorianCalendar startDateTime = null;
		XMLGregorianCalendar endDateTime = null;
		try {
			startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
			endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setStartDateTime(startDateTime);
		request.setEndDateTime(endDateTime);
		mockUnchangedCompany(request.getBusinessId(), request.getStartDateTime(), request.getEndDateTime());
		HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		assertNotNull(result);
		assertFalse(result.isChanged());
		assertEquals( 0, result.getChangedValueList().getChangedValue().size());
	}

	@Test
	public void testHasCompanyChangedException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			HasCompanyChanged request = new HasCompanyChanged();
			request.setBusinessId("1710128-1");
			LocalDateTime changedAfter = LocalDateTime.of(2020, Month.MAY, 6, 12, 0, 0);
			LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
			GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar startDateTime = null;
			XMLGregorianCalendar endDateTime = null;
			try {
				startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
				endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setStartDateTime(startDateTime);
			request.setEndDateTime(endDateTime);
			mockChangedCompanyNotFoundException(request.getBusinessId(), request.getStartDateTime(), request.getEndDateTime());
			HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals("company with businessId 1710128-1 not found", exceptionMessage);

	}

	@Test
	public void testHasCompanyChangedBusinessIdRequiredException() {
		boolean thrown = false;
		String exceptionMessage = null;
		try {
			HasCompanyChanged request = new HasCompanyChanged();
			request.setBusinessId(null);
			LocalDateTime changedAfter = LocalDateTime.of(2020, Month.MAY, 6, 12, 0, 0);
			LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
			GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar startDateTime = null;
			XMLGregorianCalendar endDateTime = null;
			try {
				startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
				endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setStartDateTime(startDateTime);
			request.setEndDateTime(endDateTime);
			HasCompanyChangedResponse result = (HasCompanyChangedResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/HasCompanyChanged/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals("BusinessId is a required parameter", exceptionMessage);

	}

	@Test
	public void testGetErrors() {
		GetErrors request = new GetErrors();
		LocalDateTime changedAfter = LocalDateTime.of(2001, Month.MAY, 6, 12, 0, 0);
		LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
		GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
		GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
		XMLGregorianCalendar startDateTime = null;
		XMLGregorianCalendar endDateTime = null;
		try {
			startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
			endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		request.setStartDateTime(startDateTime);
		request.setEndDateTime(endDateTime);
		mockErrors(request.getStartDateTime(), request.getEndDateTime());
		GetErrorsResponse result = (GetErrorsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
				"http://localhost:" + port + "/ws/GetErrors/", request);
		assertNotNull(result);
		assertEquals(6, result.getErrorLogList().getErrorLog().size());
		assertEquals("Service not found", result.getErrorLogList().getErrorLog().get(0).getMessage());
	}

	@Test
	public void testGetErrorsException() {
		boolean thrown = false;
		String exceptionMessage = null;
		GetErrors request = new GetErrors();
		try {
			LocalDateTime changedAfter = LocalDateTime.of(2021, Month.JANUARY, 6, 12, 0, 0);
			LocalDateTime changedUntil = LocalDateTime.of(2022, Month.JULY, 29, 19, 30, 40);
			GregorianCalendar calStart = GregorianCalendar.from(changedAfter.atZone(ZoneId.systemDefault()));
			GregorianCalendar calEnd = GregorianCalendar.from(changedUntil.atZone(ZoneId.systemDefault()));
			XMLGregorianCalendar startDateTime = null;
			XMLGregorianCalendar endDateTime = null;
			try {
				startDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calStart);
				endDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calEnd);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			request.setStartDateTime(startDateTime);
			request.setEndDateTime(endDateTime);
			GetErrorsResponse result = (GetErrorsResponse)new WebServiceTemplate(marshaller).marshalSendAndReceive(
					"http://localhost:" + port + "/ws/GetErrors/", request);
		} catch (SoapFaultClientException e) {
			thrown = true;
			exceptionMessage = e.getMessage();
		}
		assertTrue(thrown);
		assertEquals(exceptionMessage, "ErrorLog entries since " + request.getStartDateTime().toString() +
				" until " + request.getEndDateTime().toString() + " not found");
	}

	private void mockMembersForListServices() {
		Member member = new Member();
		Member member2 = new Member();
		Member member3 = new Member();
		member.setXRoadInstance("DEV");
		member.setMemberClass("GOV");
		member.setMemberCode("1234");
		member2.setXRoadInstance("DEV");
		member2.setMemberClass("GOV");
		member2.setMemberCode("5678");
		member3.setXRoadInstance("DEV");
		member3.setMemberClass("COM");
		member3.setMemberCode("1234");
		given(jaxbCatalogService.getAllMembers(any(), any())).willReturn(Arrays.asList(member, member2, member3));
	}

	private void mockErrors(XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		List<ErrorLog> errors = new ArrayList<>();
		List<String> errorMessages = Arrays.asList(
				"Service not found",
				"Error with certificate",
				"Unknown error",
				"Access restricted",
				"Connection refused",
				"Multiple values returned");
		for (String errorMessage : errorMessages) {
			ErrorLog errorLog = new ErrorLog();
			errorLog.setMessage(errorMessage);
			errorLog.setXRoadInstance("DEV");
			errorLog.setMemberClass("GOV");
			errorLog.setMemberCode("1234");
			errorLog.setSubsystemCode("TestSubsystem");
			errorLog.setServiceCode("TestService");
			errors.add(errorLog);
		}

		given(jaxbCatalogService.getErrorLog(calStart, calEnd)).willReturn(errors);
	}

	private void mockServicesForGetServiceType(String xRoadInstance,
											   String memberClass,
											   String memberCode,
											   String subsystemCode,
											   String serviceCode,
											   String serviceVersion,
											   String serviceType) {
		Service service = new Service();
		service.setServiceCode(serviceCode);
		service.setServiceVersion(serviceVersion);
		if (serviceType.equalsIgnoreCase("soap")) {
			service.setWsdl(new Wsdl());
		} else if (serviceType.equalsIgnoreCase("openapi")) {
			service.setOpenApi(new OpenApi());
		}
		given(catalogService.getService(xRoadInstance, memberClass, memberCode, serviceCode, subsystemCode, serviceVersion)).willReturn(service);
	}

	private void mockUnchangedCompany(String businessId, XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		given(jaxbCompanyService.getChangedCompanyValues(businessId, calStart, calEnd)).willReturn(new ArrayList<>());
	}

	private void mockChangedCompany(String businessId, XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		List<ChangedValue> changedValues = new ArrayList<>();
		List<String> changeValueStrings = Arrays.asList(
				"BusinessAddress",
				"BusinessAuxiliaryName",
				"BusinessIdChange",
				"BusinessLine",
				"BusinessName",
				"Company",
				"CompanyForm",
				"ContactDetail",
				"Language",
				"Liquidation",
				"RegisteredEntry",
				"RegisteredOffice");
		for (String changeValueString : changeValueStrings) {
			ChangedValue changedValue = new ChangedValue();
			changedValue.setName(changeValueString);
			changedValues.add(changedValue);
		}
		given(jaxbCompanyService.getChangedCompanyValues(businessId, calStart, calEnd)).willReturn(changedValues);
	}

	private void mockChangedCompanyNotFoundException(String businessId, XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		String exceptionMessage = "company with businessId " + businessId + " not found";
		given(jaxbCompanyService.getChangedCompanyValues(businessId, calStart, calEnd)).willThrow(new CatalogListerRuntimeException(exceptionMessage));
	}

	private void mockChangedOrganization(String guid, XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		ChangedValue changedValue = new ChangedValue();
		changedValue.setName("Email");
		given(jaxbOrganizationService.getChangedOrganizationValues(guid, calStart, calEnd)).willReturn(Arrays.asList(changedValue));
	}

	private void mockChangedOrganizationMultipleValues(String guid, XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		List<ChangedValue> changedValues = new ArrayList<>();
		List<String> changeValueStrings = Arrays.asList(
				"Address",
				"Email",
				"Organization",
				"OrganizationName",
				"OrganizationDescription",
				"PhoneNumber",
				"WebPage");
		for (String changeValueString : changeValueStrings) {
			ChangedValue changedValue = new ChangedValue();
			changedValue.setName(changeValueString);
			changedValues.add(changedValue);
		}
		given(jaxbOrganizationService.getChangedOrganizationValues(guid, calStart, calEnd)).willReturn(changedValues);
	}

	private void mockChangedOrganizationNotFoundException(String guid, XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		String exceptionMessage = "Organization with guid " + guid + " not found";
		given(jaxbOrganizationService.getChangedOrganizationValues(guid, calStart, calEnd)).willThrow(new CatalogListerRuntimeException(exceptionMessage));
	}

	private void mockUnchangedOrganization(String guid, XMLGregorianCalendar calStart, XMLGregorianCalendar calEnd) {
		given(jaxbOrganizationService.getChangedOrganizationValues(guid, calStart, calEnd)).willReturn(new ArrayList<>());
	}

	private void mockProvider(String xRoadInstance, String memberClass, String memberCode) {
		fi.vrk.xroad.catalog.persistence.entity.Member member = new fi.vrk.xroad.catalog.persistence.entity.Member();
		member.setXRoadInstance(xRoadInstance);
		member.setMemberClass(memberClass);
		member.setMemberCode(memberCode);
		Subsystem subsystem = new Subsystem();
		subsystem.setSubsystemCode("TestSubsystem");
		Service service = new Service();
		service.setServiceCode("TestService");
		service.setWsdl(new Wsdl(service, "This is WSDL", "3242efdf34r"));
		subsystem.setServices(Set.of(service));
		member.setSubsystems(Set.of(subsystem));
		given(catalogService.getMember(xRoadInstance, memberClass, memberCode)).willReturn(member);
	}

	private void mockNoProvider(String xRoadInstance, String memberClass, String memberCode) {
		fi.vrk.xroad.catalog.persistence.entity.Member member = new fi.vrk.xroad.catalog.persistence.entity.Member();
		member.setXRoadInstance(xRoadInstance);
		member.setMemberClass(memberClass);
		member.setMemberCode(memberCode);
		Subsystem subsystem = new Subsystem();
		subsystem.setSubsystemCode("TestSubsystem");
		Service service = new Service();
		service.setServiceCode("TestService");
		subsystem.setServices(Set.of(service));
		member.setSubsystems(Set.of(subsystem));
		given(catalogService.getMember(xRoadInstance, memberClass, memberCode)).willReturn(member);
	}

	private void mockOrganizations(String businessCode, String emailAddress, String url) {
		List<Organization> organizations = new ArrayList<>();
		Organization organization = new Organization();
		organization.setBusinessCode(businessCode);
		Email email = new Email();
		email.setValue(emailAddress);
		EmailList emailList = new EmailList();
		emailList.getEmail().add(email);
		organization.setEmails(emailList);
		WebPage webPage = new WebPage();
		webPage.setUrl(url);
		WebPageList webPageList = new WebPageList();
		webPageList.getWebPage().add(webPage);
		organization.setWebPages(webPageList);
		organizations.add(organization);
		given(jaxbOrganizationService.getOrganizations(businessCode)).willReturn(organizations);
	}

	private void mockCompanies(String businessId, String detailsUri, String companyForm, String name) {
		List<Company> companies = new ArrayList<>();
		Company company = new Company();
		company.setBusinessId(businessId);
		company.setDetailsUri(detailsUri);
		company.setCompanyForm(companyForm);
		company.setName(name);
		companies.add(company);
		given(jaxbCompanyService.getCompanies(businessId)).willReturn(companies);
	}
}
