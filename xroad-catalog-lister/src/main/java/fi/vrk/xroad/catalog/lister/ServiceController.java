/**
 * The MIT License
 * Copyright (c) 2022, Population Register Centre (VRK)
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

import fi.vrk.xroad.catalog.lister.util.JaxbUtil;
import fi.vrk.xroad.catalog.lister.util.ServiceUtil;
import fi.vrk.xroad.catalog.persistence.CompanyService;
import fi.vrk.xroad.catalog.persistence.OrganizationService;
import fi.vrk.xroad.catalog.persistence.dto.CompanyData;
import fi.vrk.xroad.catalog.persistence.dto.DescriptorInfo;
import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatisticsResponse;
import fi.vrk.xroad.catalog.persistence.dto.EndpointData;
import fi.vrk.xroad.catalog.persistence.dto.ErrorLogResponse;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationChanged;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationDTO;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationData;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerDataList;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.dto.ListOfServicesResponse;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import fi.vrk.xroad.catalog.persistence.dto.ServiceEndpointsResponse;
import fi.vrk.xroad.catalog.persistence.dto.ServiceResponse;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatisticsResponse;
import fi.vrk.xroad.catalog.persistence.dto.XRoadData;
import fi.vrk.xroad.catalog.persistence.entity.Company;
import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import fi.vrk.xroad.catalog.persistence.entity.Organization;
import fi.vrk.xroad.catalog.persistence.entity.Rest;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.*;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:lister.properties")
public class ServiceController {

    @Value("${xroad-catalog.shared-params-file}")
    private String sharedParamsFile;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private JaxbCompanyService jaxbCompanyService;

    @Autowired
    private JaxbOrganizationService jaxbOrganizationService;


    @Autowired
    private SharedParamsParser sharedParamsParser;

    @GetMapping(path = {"/getOrganization/{businessCode}"}, produces = "application/json")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable String businessCode) {
        OrganizationDTO organizationDTO = null;
        Iterable<Company> companies = companyService.getCompanies(businessCode);
        if (companies.iterator().hasNext()) {
            organizationDTO = OrganizationDTO.builder().companyData(CompanyData.builder()
                    .businessCode(businessCode)
                    .changed(companies.iterator().next().getStatusInfo().getChanged())
                    .created(companies.iterator().next().getStatusInfo().getCreated())
                    .fetched(companies.iterator().next().getStatusInfo().getFetched())
                    .removed(companies.iterator().next().getStatusInfo().getRemoved())
                    .registrationDate(companies.iterator().next().getRegistrationDate())
                    .companyForm(companies.iterator().next().getCompanyForm())
                    .detailsUri(companies.iterator().next().getDetailsUri())
                    .name(companies.iterator().next().getName())
                    .businessAddresses(ServiceUtil.getBusinessAddressData(companies))
                    .businessAuxiliaryNames(ServiceUtil.getBusinessAuxiliaryNameData(companies))
                    .businessIdChanges(ServiceUtil.getBusinessIdChangeData(companies))
                    .businessLines(ServiceUtil.getBusinessLineData(companies))
                    .businessNames(ServiceUtil.getBusinessNameData(companies))
                    .companyForms(ServiceUtil.getCompanyFormData(companies))
                    .contactDetails(ServiceUtil.getContactDetailData(companies))
                    .languages(ServiceUtil.getLanguageData(companies))
                    .liquidations(ServiceUtil.getLiquidationData(companies))
                    .registeredEntries(ServiceUtil.getRegisteredEntryData(companies))
                    .registeredOffices(ServiceUtil.getRegisteredOfficeData(companies))
                    .build())
                    .organizationData(null).build();
        } else {
            Iterable<Organization> organizations = organizationService.getOrganizations(businessCode);
            if (organizations.iterator().hasNext()) {
                organizationDTO = OrganizationDTO.builder().organizationData(OrganizationData.builder()
                        .businessCode(businessCode)
                        .changed(organizations.iterator().next().getStatusInfo().getChanged())
                        .created(organizations.iterator().next().getStatusInfo().getCreated())
                        .fetched(organizations.iterator().next().getStatusInfo().getFetched())
                        .removed(organizations.iterator().next().getStatusInfo().getRemoved())
                        .guid(organizations.iterator().next().getGuid())
                        .organizationType(organizations.iterator().next().getOrganizationType())
                        .publishingStatus(organizations.iterator().next().getPublishingStatus())
                        .organizationNames(ServiceUtil.getOrganizationNameData(organizations))
                        .organizationDescriptions(ServiceUtil.getOrganizationDescriptionData(organizations))
                        .addresses(ServiceUtil.getAddressData(organizations))
                        .emails(ServiceUtil.getEmailData(organizations))
                        .webPages(ServiceUtil.getWebpageData(organizations))
                        .phoneNumbers(ServiceUtil.getPhoneNumberData(organizations))
                        .build()).companyData(null).build();
            }
        }

        return organizationDTO != null ? ResponseEntity.ok(organizationDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping(path = {"/getOrganizationChanges/{businessCode}"}, produces = "application/json")
    public ResponseEntity<OrganizationChanged> getOrganizationChanges(@PathVariable String businessCode,
                                                    @RequestParam(required = false) String startDate,
                                                    @RequestParam(required = false) String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }

        OrganizationChanged organizationChanged = null;
        Iterable<ChangedValue> changedValues = null;
        Iterable<Company> companies = companyService.getCompanies(businessCode);
        if (companies.iterator().hasNext()) {
            changedValues = jaxbCompanyService.getChangedCompanyValues(companies.iterator().next().getBusinessId(),
                    JaxbUtil.toXmlGregorianCalendar(startDateTime),
                    JaxbUtil.toXmlGregorianCalendar(endDateTime));
        } else {
            Iterable<Organization> organizations = organizationService.getOrganizations(businessCode);
            if (organizations.iterator().hasNext()) {
                changedValues = jaxbOrganizationService.getChangedOrganizationValues(organizations.iterator().next().getGuid(),
                        JaxbUtil.toXmlGregorianCalendar(startDateTime),
                        JaxbUtil.toXmlGregorianCalendar(endDateTime));
            }
        }
        if (changedValues == null) {
            return ResponseEntity.noContent().build();
        }
        if (changedValues.iterator().hasNext()) {
            List<fi.vrk.xroad.catalog.persistence.dto.ChangedValue> changedValueList = new ArrayList<>();
            changedValues.forEach(changedValue -> changedValueList.add(fi.vrk.xroad.catalog.persistence.dto.ChangedValue.builder()
                    .name(changedValue.getName())
                    .build()));
            organizationChanged = OrganizationChanged.builder().changed(true).changedValueList(changedValueList).build();
        }

        return organizationChanged != null ? ResponseEntity.ok(organizationChanged) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = {"/listErrors/{xRoadInstance}/{memberClass}/{memberCode}/{subsystemCode}",
                        "/listErrors/{xRoadInstance}/{memberClass}/{memberCode}",
                        "/listErrors/{xRoadInstance}/{memberClass}",
                        "/listErrors/{xRoadInstance}",
                        "/listErrors"},
                        produces = "application/json")
    public ResponseEntity<ErrorLogResponse> listErrors(@PathVariable(required = false) String xRoadInstance,
                                                       @PathVariable(required = false) String memberClass,
                                                       @PathVariable(required = false) String memberCode,
                                                       @PathVariable(required = false) String subsystemCode,
                                                       @RequestParam(required = false) String startDate,
                                                       @RequestParam(required = false) String endDate,
                                                       @RequestParam(required = false) Integer page,
                                                       @RequestParam(required = false) Integer limit) {
        page = (page == null) ? 0 : page;
        limit = (limit == null) ? 100 : limit;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        XRoadData xRoadData = XRoadData.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(subsystemCode).build();
        Page<ErrorLog> errors = catalogService.getErrors(xRoadData,
                                                         Integer.valueOf(page),
                                                         Integer.valueOf(limit),
                                                         startDateTime,
                                                         endDateTime);
        if (errors == null) {
            return ResponseEntity.noContent().build();
        } else if (!errors.hasContent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(ErrorLogResponse.builder()
                    .pageNumber(page)
                    .pageSize(limit)
                    .numberOfPages(errors.getTotalPages())
                    .errorLogList(errors.getContent()).build());
        }
    }

    @GetMapping(path = "/getDistinctServiceStatistics", produces = "application/json")
    public ResponseEntity<DistinctServiceStatisticsResponse> getDistinctServiceStatistics(@RequestParam(required = false) String startDate,
                                                          @RequestParam(required = false) String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        List<DistinctServiceStatistics> serviceStatisticsList = catalogService.getDistinctServiceStatistics(startDateTime, endDateTime);
        if (serviceStatisticsList == null || serviceStatisticsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(DistinctServiceStatisticsResponse.builder().distinctServiceStatisticsList(serviceStatisticsList).build());
        }
    }

    @GetMapping(path = "/getServiceStatistics", produces = "application/json")
    public ResponseEntity<ServiceStatisticsResponse> getServiceStatistics(@RequestParam(required = false) String startDate,
                                                  @RequestParam(required = false) String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        List<ServiceStatistics> serviceStatisticsList = catalogService.getServiceStatistics(startDateTime, endDateTime);
        if (serviceStatisticsList == null || serviceStatisticsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(ServiceStatisticsResponse.builder().serviceStatisticsList(serviceStatisticsList).build());
        }
    }

    @GetMapping(path = "/getServiceStatisticsCSV", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> getServiceStatisticsCSV(@RequestParam(required = false) String startDate,
                                                                     @RequestParam(required = false) String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        List<ServiceStatistics> serviceStatisticsList = catalogService.getServiceStatistics(startDateTime, endDateTime);
        if (serviceStatisticsList != null) {
            try {
                StringWriter sw = new StringWriter();
                CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                        .withHeader("Date", "Number of REST services", "Number of SOAP services", "Number of OpenApi services"));
                serviceStatisticsList.forEach(serviceStatistics -> ServiceUtil.printCSVRecord(csvPrinter,
                        Arrays.asList(serviceStatistics.getCreated().toString(),
                                serviceStatistics.getNumberOfRestServices().toString(),
                                serviceStatistics.getNumberOfSoapServices().toString(),
                                serviceStatistics.getNumberOfOpenApiServices().toString())));
                String reportName = "service_statistics_" + LocalDateTime.now().toString();
                sw.close();
                csvPrinter.close();
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + reportName + ".csv")
                        .contentType(org.springframework.http.MediaType.parseMediaType("text/csv"))
                        .body(new ByteArrayResource(sw.toString().getBytes()));
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/getListOfServices", produces = "application/json")
    public ResponseEntity<ListOfServicesResponse> getListOfServices(@RequestParam(required = false) String startDate,
                                               @RequestParam(required = false) String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        List<SecurityServerInfo> securityServerList = ServiceUtil.getSecurityServerInfoList(sharedParamsParser, sharedParamsFile);
        List<MemberDataList> memberDataList = catalogService.getMemberData(startDateTime, endDateTime);
        if (memberDataList != null) {
            return ResponseEntity.ok(ListOfServicesResponse.builder()
                    .memberData(memberDataList)
                    .securityServerData(securityServerList)
                    .build());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/getListOfServicesCSV", produces = "text/csv")
    public ResponseEntity<ByteArrayResource> getListOfServicesCSV(@RequestParam(required = false) String startDate,
                                                                  @RequestParam(required = false) String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        List<SecurityServerInfo> securityServerList = ServiceUtil.getSecurityServerInfoList(sharedParamsParser, sharedParamsFile);
        List<MemberDataList> memberDataList = catalogService.getMemberData(startDateTime, endDateTime);
        if (memberDataList != null) {
            try {
                StringWriter sw = new StringWriter();
                CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                        .withHeader("Date", "XRoad instance", "Member class", "Member code",
                                "Member name", "Member created", "Subsystem code", "Subsystem created", "Subsystem active",
                                "Service code", "Service version", "Service created", "Service active"));
                ServiceUtil.printListOfServicesCSV(csvPrinter, memberDataList, securityServerList);
                String reportName = "list_of_services_" + LocalDateTime.now().toString();
                sw.close();
                csvPrinter.close();
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + reportName + ".csv")
                        .contentType(org.springframework.http.MediaType.valueOf(MediaType.TEXT_PLAIN))
                        .body(new ByteArrayResource(sw.toString().getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/listSecurityServers", produces = "application/json")
    public ResponseEntity<SecurityServerDataList> listSecurityServers() {
        SecurityServerDataList securityServerDataList = ServiceUtil.getSecurityServerDataList(sharedParamsParser, sharedParamsFile);
        if (securityServerDataList != null) {
            return ResponseEntity.ok(securityServerDataList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/listDescriptors", produces = "application/json")
    public ResponseEntity<List<DescriptorInfo>> listDescriptors() {
        List<DescriptorInfo> descriptorInfo = ServiceUtil.getDescriptorInfoList(sharedParamsParser, sharedParamsFile);
        if (descriptorInfo != null) {
            return ResponseEntity.ok(descriptorInfo);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/getEndpoints/{xRoadInstance}/{memberClass}/{memberCode}/{subsystemCode}/{serviceCode}", produces = "application/json")
    public ResponseEntity<ServiceResponse> getEndpoints(@PathVariable String xRoadInstance,
                                                        @PathVariable String memberClass,
                                                        @PathVariable String memberCode,
                                                        @PathVariable String subsystemCode,
                                                        @PathVariable String serviceCode) {
        List<ServiceEndpointsResponse> listOfServices = new ArrayList<>();
        List<Service> services = catalogService.getServices(xRoadInstance, memberClass, memberCode, subsystemCode, serviceCode);
        services.forEach(service -> listOfServices.add(getServiceEndpointsResponse(service, xRoadInstance, memberClass, memberCode, subsystemCode, serviceCode)));
        ServiceResponse response = ServiceResponse.builder().listOfServices(listOfServices).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/getRest/{xRoadInstance}/{memberClass}/{memberCode}/{subsystemCode}/{serviceCode}", produces = "application/json")
    public ResponseEntity<ServiceResponse> getRest(@PathVariable String xRoadInstance,
                                          @PathVariable String memberClass,
                                          @PathVariable String memberCode,
                                          @PathVariable String subsystemCode,
                                          @PathVariable String serviceCode) {
        List<ServiceEndpointsResponse> listOfServices = new ArrayList<>();
        List<Service> services = catalogService.getServices(xRoadInstance, memberClass, memberCode, subsystemCode, serviceCode);
        services.forEach(service -> {
            Rest rest = catalogService.getRest(service);
            if (rest != null) {
                listOfServices.add(getServiceEndpointsResponse(service, xRoadInstance, memberClass, memberCode, subsystemCode, serviceCode));
            }
        });
        return ResponseEntity.ok(ServiceResponse.builder().listOfServices(listOfServices).build());
    }

    private ServiceEndpointsResponse getServiceEndpointsResponse(Service service,
                                                                 String xRoadInstance,
                                                                 String memberClass,
                                                                 String memberCode,
                                                                 String subsystemCode,
                                                                 String serviceCode) {
        List<EndpointData> endpointDataList = new ArrayList<>();
        service.getAllEndpoints().forEach(endpoint -> endpointDataList.add(EndpointData.builder()
                .method(endpoint.getMethod())
                .path(endpoint.getPath()).build()));
        return ServiceEndpointsResponse.builder()
                .xRoadInstance(xRoadInstance)
                .memberClass(memberClass)
                .memberCode(memberCode)
                .subsystemCode(subsystemCode)
                .serviceCode(serviceCode)
                .serviceVersion(service.getServiceVersion())
                .endpointList(endpointDataList).build();
    }
}
