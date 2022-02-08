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

import fi.vrk.xroad.catalog.persistence.dto.AddressData;
import fi.vrk.xroad.catalog.persistence.dto.BusinessAddressData;
import fi.vrk.xroad.catalog.persistence.dto.BusinessAuxiliaryNameData;
import fi.vrk.xroad.catalog.persistence.dto.BusinessIdChangeData;
import fi.vrk.xroad.catalog.persistence.dto.BusinessLineData;
import fi.vrk.xroad.catalog.persistence.dto.BusinessNameData;
import fi.vrk.xroad.catalog.persistence.dto.CompanyData;
import fi.vrk.xroad.catalog.persistence.dto.CompanyFormData;
import fi.vrk.xroad.catalog.persistence.dto.ContactDetailData;
import fi.vrk.xroad.catalog.persistence.dto.DescriptorInfoList;
import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatisticsResponse;
import fi.vrk.xroad.catalog.persistence.dto.EmailData;
import fi.vrk.xroad.catalog.persistence.dto.ErrorLogResponse;
import fi.vrk.xroad.catalog.persistence.dto.LanguageData;
import fi.vrk.xroad.catalog.persistence.dto.LiquidationData;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationChanged;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationDTO;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationData;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationDescriptionData;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationNameData;
import fi.vrk.xroad.catalog.persistence.dto.PhoneNumberData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressAdditionalInformationData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressMunicipalityData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxAddressMunicipalityNameData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeBoxData;
import fi.vrk.xroad.catalog.persistence.dto.PostOfficeData;
import fi.vrk.xroad.catalog.persistence.dto.RegisteredEntryData;
import fi.vrk.xroad.catalog.persistence.dto.RegisteredOfficeData;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerDataList;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.dto.ListOfServicesResponse;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatisticsResponse;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressAdditionalInformationData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressMunicipalityData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressMunicipalityNameData;
import fi.vrk.xroad.catalog.persistence.dto.StreetAddressPostOfficeData;
import fi.vrk.xroad.catalog.persistence.dto.StreetData;
import fi.vrk.xroad.catalog.persistence.dto.WebpageData;
import fi.vrk.xroad.catalog.persistence.entity.Address;
import fi.vrk.xroad.catalog.persistence.entity.BusinessAddress;
import fi.vrk.xroad.catalog.persistence.entity.BusinessAuxiliaryName;
import fi.vrk.xroad.catalog.persistence.entity.BusinessIdChange;
import fi.vrk.xroad.catalog.persistence.entity.BusinessLine;
import fi.vrk.xroad.catalog.persistence.entity.BusinessName;
import fi.vrk.xroad.catalog.persistence.entity.Company;
import fi.vrk.xroad.catalog.persistence.entity.CompanyForm;
import fi.vrk.xroad.catalog.persistence.entity.ContactDetail;
import fi.vrk.xroad.catalog.persistence.entity.Email;
import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import fi.vrk.xroad.catalog.persistence.entity.Language;
import fi.vrk.xroad.catalog.persistence.entity.Liquidation;
import fi.vrk.xroad.catalog.persistence.entity.Organization;
import fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription;
import fi.vrk.xroad.catalog.persistence.entity.OrganizationName;

import fi.vrk.xroad.catalog.persistence.entity.PhoneNumber;
import fi.vrk.xroad.catalog.persistence.entity.PostOffice;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality;
import fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName;
import fi.vrk.xroad.catalog.persistence.entity.RegisteredEntry;
import fi.vrk.xroad.catalog.persistence.entity.RegisteredOffice;
import fi.vrk.xroad.catalog.persistence.entity.Street;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddress;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName;
import fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice;
import fi.vrk.xroad.catalog.persistence.entity.WebPage;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;

import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.*;
import javax.xml.parsers.ParserConfigurationException;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:lister.properties")
public class ServiceController {

    @Value("${xroad-catalog.max-history-length-in-days}")
    private Integer maxHistoryLengthInDays;

    @Value("${xroad-catalog.shared-params-file}")
    private String sharedParamsFile;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private JaxbCatalogService jaxbCatalogService;

    @Autowired
    @Setter
    private JaxbConverter jaxbConverter;

    @Autowired
    private SharedParamsParser sharedParamsParser;

    @GetMapping(path = {"/getOrganization/{businessCode}"}, produces = "application/json")
    public ResponseEntity<?> getOrganization(@PathVariable String businessCode) {
        OrganizationDTO organizationDTO = null;
        Iterable<Organization> organizations = catalogService.getOrganizations(businessCode);
        if (!organizations.iterator().hasNext()) {
            Iterable<Company> companies = catalogService.getCompanies(businessCode);
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
                                .businessAddresses(getBusinessAddressData(companies))
                                .businessAuxiliaryNames(getBusinessAuxiliaryNameData(companies))
                                .businessIdChanges(getBusinessIdChangeData(companies))
                                .businessLines(getBusinessLineData(companies))
                                .businessNames(getBusinessNameData(companies))
                                .companyForms(getCompanyFormData(companies))
                                .contactDetails(getContactDetailData(companies))
                                .languages(getLanguageData(companies))
                                .liquidations(getLiquidationData(companies))
                                .registeredEntries(getRegisteredEntryData(companies))
                                .registeredOffices(getRegisteredOfficeData(companies))
                                .build())
                        .organizationData(null).build();
            }
        } else {
            organizationDTO = OrganizationDTO.builder().organizationData(OrganizationData.builder()
                    .businessCode(businessCode)
                    .changed(organizations.iterator().next().getStatusInfo().getChanged())
                    .created(organizations.iterator().next().getStatusInfo().getCreated())
                    .fetched(organizations.iterator().next().getStatusInfo().getFetched())
                    .removed(organizations.iterator().next().getStatusInfo().getRemoved())
                    .guid(organizations.iterator().next().getGuid())
                    .organizationType(organizations.iterator().next().getOrganizationType())
                    .publishingStatus(organizations.iterator().next().getPublishingStatus())
                    .organizationNames(getOrganizationNameData(organizations))
                    .organizationDescriptions(getOrganizationDescriptionData(organizations))
                    .addresses(getAddressData(organizations))
                    .emails(getEmailData(organizations))
                    .webPages(getWebpageData(organizations))
                    .phoneNumbers(getPhoneNumberData(organizations))
                    .build()).companyData(null).build();
        }

        return organizationDTO != null ? ResponseEntity.ok(organizationDTO) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = {"/getOrganizationChanges/{businessCode}/{since}"}, produces = "application/json")
    public ResponseEntity<?> getOrganizationChanges(@PathVariable String businessCode, @PathVariable String since) {
        if (businessCode == null || since == null) {
            return new ResponseEntity<>("BusinessCode and Since parameters are required to fetch organization changes ", HttpStatus.BAD_REQUEST);
        }

        OrganizationChanged organizationChanged = null;
        try {
            Iterable<ChangedValue> changedValues;
            LocalDateTime sinceDateTime = LocalDate.parse(since, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            Iterable<Organization> organizations = catalogService.getOrganizations(businessCode);
            if (organizations.iterator().hasNext()) {
                changedValues = jaxbCatalogService.getChangedOrganizationValues(organizations.iterator().next().getGuid(),
                                jaxbConverter.toXmlGregorianCalendar(sinceDateTime));
            } else {
                changedValues = jaxbCatalogService.getChangedCompanyValues(businessCode, jaxbConverter.toXmlGregorianCalendar(sinceDateTime));
            }
            if (changedValues.iterator().hasNext()) {
                List<fi.vrk.xroad.catalog.persistence.dto.ChangedValue> changedValueList = new ArrayList<>();
                changedValues.forEach(changedValue -> changedValueList.add(fi.vrk.xroad.catalog.persistence.dto.ChangedValue.builder()
                        .name(changedValue.getName())
                        .build()));
                organizationChanged = OrganizationChanged.builder().changed(true).changedValueList(changedValueList).build();
            }
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Exception parsing since parameter: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return organizationChanged != null ? ResponseEntity.ok(organizationChanged) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = {"/listErrors/{xRoadInstance}/{memberClass}/{memberCode}/{subsystemCode}/{historyAmountInDays}",
                        "/listErrors/{xRoadInstance}/{memberClass}/{memberCode}/{historyAmountInDays}",
                        "/listErrors/{xRoadInstance}/{memberClass}/{historyAmountInDays}",
                        "/listErrors/{xRoadInstance}/{historyAmountInDays}",
                        "/listErrors/{historyAmountInDays}"},
                        produces = "application/json")
    public ResponseEntity<?> listErrors(@PathVariable(required = false) String xRoadInstance,
                                        @PathVariable(required = false) String memberClass,
                                        @PathVariable(required = false) String memberCode,
                                        @PathVariable(required = false) String subsystemCode,
                                        @PathVariable Long historyAmountInDays,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer limit) {
        if (historyAmountInDays < 1 || historyAmountInDays > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 100;
        }
        Page<ErrorLog> errors = catalogService.getErrors(xRoadInstance,
                                                         memberClass,
                                                         memberCode,
                                                         subsystemCode,
                                                         historyAmountInDays,
                                                         Integer.valueOf(page),
                                                         Integer.valueOf(limit));
        if (errors == null | !errors.hasContent()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(ErrorLogResponse.builder()
                    .pageNumber(page)
                    .pageSize(limit)
                    .numberOfPages(errors.getTotalPages())
                    .errorLogList(errors.getContent()).build());
        }
    }

    @GetMapping(path = "/getDistinctServiceStatistics/{historyAmountInDays}", produces = "application/json")
    public ResponseEntity<?> getDistinctServiceStatistics(@PathVariable Long historyAmountInDays) {
        if (historyAmountInDays < 1 || historyAmountInDays > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        List<DistinctServiceStatistics> serviceStatisticsList = catalogService.getDistinctServiceStatistics(historyAmountInDays);
        if (serviceStatisticsList == null || serviceStatisticsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(DistinctServiceStatisticsResponse.builder().distinctServiceStatisticsList(serviceStatisticsList).build());
        }
    }

    @GetMapping(path = "/getServiceStatistics/{historyAmountInDays}", produces = "application/json")
    public ResponseEntity<?> getServiceStatistics(@PathVariable Long historyAmountInDays) {
        if (historyAmountInDays < 1 || historyAmountInDays > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        List<ServiceStatistics> serviceStatisticsList = catalogService.getServiceStatistics(historyAmountInDays);
        if (serviceStatisticsList == null || serviceStatisticsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(ServiceStatisticsResponse.builder().serviceStatisticsList(serviceStatisticsList).build());
        }
    }

    @GetMapping(path = "/getServiceStatisticsCSV/{historyAmountInDays}", produces = "text/csv")
    public ResponseEntity<?> getServiceStatisticsCSV(@PathVariable Long historyAmountInDays) {
        if (historyAmountInDays < 1 || historyAmountInDays > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        List<ServiceStatistics> serviceStatisticsList = catalogService.getServiceStatistics(historyAmountInDays);
        if (serviceStatisticsList != null) {
            try {
                StringWriter sw = new StringWriter();
                CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                        .withHeader("Date", "Number of REST services", "Number of SOAP services", "Number of OpenApi services"));
                serviceStatisticsList.forEach(serviceStatistics -> printCSVRecord(csvPrinter,
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
                e.printStackTrace();
            }
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/getListOfServices/{historyAmountInDays}", produces = "application/json")
    public ResponseEntity<?> getListOfServices(@PathVariable Long historyAmountInDays) {
        if (historyAmountInDays < 1 || historyAmountInDays > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        List<SecurityServerInfo> securityServerList = getSecurityServerInfoList();
        List<MemberDataList> memberDataList = catalogService.getMemberData(historyAmountInDays);
        if (memberDataList != null) {
            return ResponseEntity.ok(ListOfServicesResponse.builder()
                    .memberData(memberDataList)
                    .securityServerData(securityServerList)
                    .build());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/getListOfServicesCSV/{historyAmountInDays}", produces = "text/csv")
    public ResponseEntity<?> getListOfServicesCSV(@PathVariable Long historyAmountInDays) {
        if (historyAmountInDays < 1 || historyAmountInDays > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        List<SecurityServerInfo> securityServerList = getSecurityServerInfoList();
        List<MemberDataList> memberDataList = catalogService.getMemberData(historyAmountInDays);
        if (memberDataList != null) {
            try {
                StringWriter sw = new StringWriter();
                CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                        .withHeader("Date", "XRoad instance", "Member class", "Member code",
                                "Member name", "Member created", "Subsystem code", "Subsystem created", "Subsystem active",
                                "Service code", "Service version", "Service created", "Service active"));
                printListOfServicesCSV(csvPrinter, memberDataList, securityServerList);
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
    public ResponseEntity<?> listSecurityServers() {
        SecurityServerDataList securityServerDataList = getSecurityServerDataList();
        if (securityServerDataList != null) {
            return ResponseEntity.ok(securityServerDataList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/listDescriptors", produces = "application/json")
    public ResponseEntity<?> listDescriptors() {
        DescriptorInfoList descriptorInfoList = getDescriptorInfoList();
        if (descriptorInfoList != null) {
            return ResponseEntity.ok(descriptorInfoList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    private void printCSVRecord(CSVPrinter csvPrinter, List<String> data) {
        try {
            csvPrinter.printRecord(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printListOfServicesCSV(CSVPrinter csvPrinter, List<MemberDataList> memberDataList, List<SecurityServerInfo> securityServerList) {
        memberDataList.forEach(memberList -> {
            printCSVRecord(csvPrinter, Arrays.asList(memberList.getDate().toString(), "", "", "", "", "", "", "", "", "", "", "", ""));
            memberList.getMemberDataList().forEach(memberData -> {
                String memberCreated = memberData.getCreated().toString();
                String xRoadInstance = memberData.getXRoadInstance();
                String memberClass = memberData.getMemberClass();
                String memberCode = memberData.getMemberCode();
                String memberName = memberData.getName();

                if (memberData.getSubsystemList().isEmpty() || memberData.getSubsystemList() == null) {
                    printCSVRecord(csvPrinter, Arrays.asList("", xRoadInstance, memberClass, memberCode, memberName,
                            memberCreated, "", "", "", "", "", "", ""));
                }

                memberData.getSubsystemList().forEach(subsystemData -> {

                    if (subsystemData.getServiceList().isEmpty() || subsystemData.getServiceList() == null) {
                        printCSVRecord(csvPrinter, Arrays.asList("", xRoadInstance, memberClass, memberCode, memberName, memberCreated,
                                subsystemData.getSubsystemCode(), subsystemData.getCreated().toString(), subsystemData.getActive().toString(),
                                "", "", "", ""));
                    }

                    subsystemData.getServiceList().forEach(serviceData -> printCSVRecord(csvPrinter, Arrays.asList(
                            "", xRoadInstance, memberClass, memberCode, memberName, memberCreated, subsystemData.getSubsystemCode(),
                            subsystemData.getCreated().toString(), subsystemData.getActive().toString(), serviceData.getServiceCode(),
                            serviceData.getServiceVersion(), serviceData.getCreated().toString(), serviceData.getActive().toString())));
                });
            });
        });

        if (securityServerList != null && !securityServerList.isEmpty()) {
            printCSVRecord(csvPrinter, Arrays.asList("", "Security server (SS) info:", "", "", "", "", "", "", "", "", "", "", ""));
            printCSVRecord(csvPrinter, Arrays.asList("instance", "member class", "member code", "server code", "address", "", "", "", "", "","", "", ""));

            securityServerList.forEach(securityServerInfo -> printCSVRecord(csvPrinter, Arrays.asList(securityServerInfo.getXRoadInstance(),
                    securityServerInfo.getMemberClass(), securityServerInfo.getMemberCode(),
                    securityServerInfo.getServerCode(), securityServerInfo.getAddress()
                    , "", "", "", "", "", "", "", "")));
        }
    }

    private List<SecurityServerInfo> getSecurityServerInfoList() {
        List<SecurityServerInfo> securityServerList = new ArrayList<>();
        try {
            Set<SecurityServerInfo> securityServerInfos = sharedParamsParser.parseInfo(sharedParamsFile);
            if (securityServerInfos.iterator().hasNext()) {
                securityServerList = new ArrayList<>(securityServerInfos);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return securityServerList;
    }

    private SecurityServerDataList getSecurityServerDataList() {
        SecurityServerDataList securityServerDataList;
        try {
            securityServerDataList = sharedParamsParser.parseDetails(sharedParamsFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return securityServerDataList;
    }

    private DescriptorInfoList getDescriptorInfoList() {
        DescriptorInfoList descriptorInfoList;
        try {
            descriptorInfoList = sharedParamsParser.parseDescriptorInfo(sharedParamsFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return descriptorInfoList;
    }

    private List<OrganizationNameData> getOrganizationNameData(Iterable<Organization> organizations) {
        List<OrganizationNameData> organizationNameDataList = new ArrayList<>();
        List<OrganizationName> organizationNames = new ArrayList<>(organizations.iterator().next().getAllOrganizationNames());
        organizationNames.forEach(organizationName -> organizationNameDataList.add(OrganizationNameData.builder()
                .language(organizationName.getLanguage())
                .type(organizationName.getType())
                .value(organizationName.getValue())
                .changed(organizationName.getStatusInfo().getChanged())
                .created(organizationName.getStatusInfo().getCreated())
                .fetched(organizationName.getStatusInfo().getFetched())
                .removed(organizationName.getStatusInfo().getRemoved())
                .build()));
        return organizationNameDataList;
    }

    private List<OrganizationDescriptionData> getOrganizationDescriptionData(Iterable<Organization> organizations) {
        List<OrganizationDescriptionData> organizationDescriptionDataList = new ArrayList<>();
        List<OrganizationDescription> organizationDescriptions = new ArrayList<>(organizations.iterator().next().getAllOrganizationDescriptions());
        organizationDescriptions.forEach(organizationDescription -> organizationDescriptionDataList.add(OrganizationDescriptionData.builder()
                .language(organizationDescription.getLanguage())
                .type(organizationDescription.getType())
                .value(organizationDescription.getValue())
                .changed(organizationDescription.getStatusInfo().getChanged())
                .created(organizationDescription.getStatusInfo().getCreated())
                .fetched(organizationDescription.getStatusInfo().getFetched())
                .removed(organizationDescription.getStatusInfo().getRemoved())
                .build()));
        return organizationDescriptionDataList;
    }

    private List<AddressData> getAddressData(Iterable<Organization> organizations) {
        List<AddressData> addressDataList = new ArrayList<>();
        List<Address> addresses = new ArrayList<>(organizations.iterator().next().getAllAddresses());
        addresses.forEach(address -> addressDataList.add(AddressData.builder()
                .country(address.getCountry())
                .type(address.getType())
                .subType(address.getSubType())
                .postOfficeBoxAddressData(getPostOfficeBoxAddressData(organizations.iterator().next().getAllAddresses()))
                .streetAddressData(getStreetAddressData(organizations.iterator().next().getAllAddresses()))
                .changed(address.getStatusInfo().getChanged())
                .created(address.getStatusInfo().getCreated())
                .fetched(address.getStatusInfo().getFetched())
                .removed(address.getStatusInfo().getRemoved())
                .build()));
        return addressDataList;
    }

    private List<PostOfficeBoxAddressData> getPostOfficeBoxAddressData(Iterable<Address> addresses) {
        List<PostOfficeBoxAddressData> postOfficeBoxAddressDataList = new ArrayList<>();
        List<PostOfficeBoxAddress> postOfficeBoxAddresses = new ArrayList<>(addresses.iterator().next().getAllPostOfficeBoxAddresses());
        postOfficeBoxAddresses.forEach(postOfficeBoxAddress -> postOfficeBoxAddressDataList.add(PostOfficeBoxAddressData.builder()
                .additionalInformation(getPostOfficeBoxAddressAdditionalInformationData(postOfficeBoxAddress))
                .postalCode(postOfficeBoxAddress.getPostalCode())
                .postOfficeBoxAddressMunicipalities(getPostOfficeBoxAddressMunicipalityData(postOfficeBoxAddress))
                .postOfficeBoxes(getPostOfficeBoxData(postOfficeBoxAddress))
                .postOffices(getPostOfficeData(postOfficeBoxAddress))
                .changed(postOfficeBoxAddress.getStatusInfo().getChanged())
                .created(postOfficeBoxAddress.getStatusInfo().getCreated())
                .fetched(postOfficeBoxAddress.getStatusInfo().getFetched())
                .removed(postOfficeBoxAddress.getStatusInfo().getRemoved())
                .build()));
        return postOfficeBoxAddressDataList;
    }


    private List<StreetAddressData> getStreetAddressData(Iterable<Address> addresses) {
        List<StreetAddressData> streetAddressDataList = new ArrayList<>();
        List<StreetAddress> streetAddresses = new ArrayList<>(addresses.iterator().next().getAllStreetAddresses());
        streetAddresses.forEach(streetAddress -> streetAddressDataList.add(StreetAddressData.builder()
                .additionalInformation(getStreetAddressAdditionalInformationData(streetAddress))
                .coordinateState(streetAddress.getCoordinateState())
                .latitude(streetAddress.getLatitude())
                .longitude(streetAddress.getLongitude())
                .streetNumber(streetAddress.getStreetNumber())
                .streets(getStreetData(streetAddress))
                .municipalities(getStreetAddressMunicipalityData(streetAddress))
                .postOffices(getStreetAddressPostOfficeData(streetAddress))
                .postalCode(streetAddress.getPostalCode())
                .changed(streetAddress.getStatusInfo().getChanged())
                .created(streetAddress.getStatusInfo().getCreated())
                .fetched(streetAddress.getStatusInfo().getFetched())
                .removed(streetAddress.getStatusInfo().getRemoved())
                .build()));
        return streetAddressDataList;
    }

    private List<StreetAddressAdditionalInformationData> getStreetAddressAdditionalInformationData(StreetAddress streetAddress) {
        List<StreetAddressAdditionalInformationData> streetAddressAdditionalInformationDataList = new ArrayList<>();
        List<StreetAddressAdditionalInformation> streetAddressAdditionalInformationList = new ArrayList<>(streetAddress.getAllAdditionalInformation());
        streetAddressAdditionalInformationList.forEach(streetAddressAdditionalInformation -> streetAddressAdditionalInformationDataList.add(
                StreetAddressAdditionalInformationData.builder()
                        .language(streetAddressAdditionalInformation.getLanguage())
                        .value(streetAddressAdditionalInformation.getValue())
                        .changed(streetAddressAdditionalInformation.getStatusInfo().getChanged())
                        .created(streetAddressAdditionalInformation.getStatusInfo().getCreated())
                        .fetched(streetAddressAdditionalInformation.getStatusInfo().getFetched())
                        .removed(streetAddressAdditionalInformation.getStatusInfo().getRemoved())
                        .build()));
        return streetAddressAdditionalInformationDataList;
    }

    private List<StreetData> getStreetData(StreetAddress streetAddress) {
        List<StreetData> streetDataList = new ArrayList<>();
        List<Street> streets = new ArrayList<>(streetAddress.getAllStreets());
        streets.forEach(street -> streetDataList.add(StreetData.builder()
                .language(street.getLanguage())
                .value(street.getValue())
                .changed(street.getStatusInfo().getChanged())
                .created(street.getStatusInfo().getCreated())
                .fetched(street.getStatusInfo().getFetched())
                .removed(street.getStatusInfo().getRemoved())
                .build()));
        return streetDataList;
    }

    private List<StreetAddressMunicipalityData> getStreetAddressMunicipalityData(StreetAddress streetAddress) {
        List<StreetAddressMunicipalityData> streetAddressMunicipalityDataList = new ArrayList<>();
        List<StreetAddressMunicipality> streetAddressMunicipalities = new ArrayList<>(streetAddress.getAllMunicipalities());
        streetAddressMunicipalities.forEach(streetAddressMunicipality -> streetAddressMunicipalityDataList.add(StreetAddressMunicipalityData.builder()
                .code(streetAddressMunicipality.getCode())
                .streetAddressMunicipalityNames(getStreetAddressMunicipalityNameData(streetAddressMunicipality))
                .changed(streetAddressMunicipality.getStatusInfo().getChanged())
                .created(streetAddressMunicipality.getStatusInfo().getCreated())
                .fetched(streetAddressMunicipality.getStatusInfo().getFetched())
                .removed(streetAddressMunicipality.getStatusInfo().getRemoved())
                .build()));
        return streetAddressMunicipalityDataList;
    }

    private List<StreetAddressMunicipalityNameData> getStreetAddressMunicipalityNameData(StreetAddressMunicipality streetAddressMunicipality) {
        List<StreetAddressMunicipalityNameData> streetAddressMunicipalityNameDataList = new ArrayList<>();
        List<StreetAddressMunicipalityName> streetAddressMunicipalityNames = new ArrayList<>(streetAddressMunicipality.getAllMunicipalityNames());
        streetAddressMunicipalityNames.forEach(streetAddressMunicipalityName -> streetAddressMunicipalityNameDataList.add(
                StreetAddressMunicipalityNameData.builder()
                        .language(streetAddressMunicipalityName.getLanguage())
                        .value(streetAddressMunicipalityName.getValue())
                        .changed(streetAddressMunicipalityName.getStatusInfo().getChanged())
                        .created(streetAddressMunicipalityName.getStatusInfo().getCreated())
                        .fetched(streetAddressMunicipalityName.getStatusInfo().getFetched())
                        .removed(streetAddressMunicipalityName.getStatusInfo().getRemoved())
                        .build()));
        return streetAddressMunicipalityNameDataList;
    }

    private List<StreetAddressPostOfficeData> getStreetAddressPostOfficeData(StreetAddress streetAddress) {
        List<StreetAddressPostOfficeData> streetAddressPostOfficeDataList = new ArrayList<>();
        List<StreetAddressPostOffice> streetAddressPostOffices = new ArrayList<>(streetAddress.getAllPostOffices());
        streetAddressPostOffices.forEach(streetAddressPostOffice -> streetAddressPostOfficeDataList.add(StreetAddressPostOfficeData.builder()
                .language(streetAddressPostOffice.getLanguage())
                .value(streetAddressPostOffice.getValue())
                .changed(streetAddressPostOffice.getStatusInfo().getChanged())
                .created(streetAddressPostOffice.getStatusInfo().getCreated())
                .fetched(streetAddressPostOffice.getStatusInfo().getFetched())
                .removed(streetAddressPostOffice.getStatusInfo().getRemoved())
                .build()));
        return streetAddressPostOfficeDataList;
    }

    private List<PostOfficeBoxAddressAdditionalInformationData> getPostOfficeBoxAddressAdditionalInformationData(PostOfficeBoxAddress postOfficeBoxAddress) {
        List<PostOfficeBoxAddressAdditionalInformationData> postOfficeBoxAddressAdditionalInformationDataList = new ArrayList<>();
        List<PostOfficeBoxAddressAdditionalInformation> postOfficeBoxAddressAdditionalInformationList =
                new ArrayList<>(postOfficeBoxAddress.getAllAdditionalInformation());
        postOfficeBoxAddressAdditionalInformationList.forEach(
                postOfficeBoxAddressAdditionalInformation -> postOfficeBoxAddressAdditionalInformationDataList.add(
                        PostOfficeBoxAddressAdditionalInformationData.builder()
                                .language(postOfficeBoxAddressAdditionalInformation.getLanguage())
                                .value(postOfficeBoxAddressAdditionalInformation.getValue())
                                .changed(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getChanged())
                                .created(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getCreated())
                                .fetched(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getFetched())
                                .removed(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getRemoved())
                                .build()));
        return postOfficeBoxAddressAdditionalInformationDataList;
    }

    private List<PostOfficeBoxAddressMunicipalityData> getPostOfficeBoxAddressMunicipalityData(PostOfficeBoxAddress postOfficeBoxAddress) {
        List<PostOfficeBoxAddressMunicipalityData> postOfficeBoxAddressMunicipalityDataList = new ArrayList<>();
        List<PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities = new ArrayList<>(postOfficeBoxAddress.getAllMunicipalities());
        postOfficeBoxAddressMunicipalities.forEach(postOfficeBoxAddressMunicipality -> postOfficeBoxAddressMunicipalityDataList.add(
                        PostOfficeBoxAddressMunicipalityData.builder()
                                .code(postOfficeBoxAddressMunicipality.getCode())
                                .postOfficeBoxAddressMunicipalityNames(getPostOfficeBoxAddressMunicipalityNameData(postOfficeBoxAddressMunicipality))
                                .changed(postOfficeBoxAddressMunicipality.getStatusInfo().getChanged())
                                .created(postOfficeBoxAddressMunicipality.getStatusInfo().getCreated())
                                .fetched(postOfficeBoxAddressMunicipality.getStatusInfo().getFetched())
                                .removed(postOfficeBoxAddressMunicipality.getStatusInfo().getRemoved())
                                .build()));
        return postOfficeBoxAddressMunicipalityDataList;
    }

    private List<PostOfficeBoxAddressMunicipalityNameData> getPostOfficeBoxAddressMunicipalityNameData(
            PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality) {
        List<PostOfficeBoxAddressMunicipalityNameData> postOfficeBoxAddressMunicipalityNameDataList = new ArrayList<>();
        List<PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames =
                new ArrayList<>(postOfficeBoxAddressMunicipality.getAllMunicipalityNames());
        postOfficeBoxAddressMunicipalityNames.forEach(postOfficeBoxAddressMunicipalityName -> postOfficeBoxAddressMunicipalityNameDataList.add(
                PostOfficeBoxAddressMunicipalityNameData.builder()
                        .language(postOfficeBoxAddressMunicipalityName.getLanguage())
                        .value(postOfficeBoxAddressMunicipalityName.getValue())
                        .changed(postOfficeBoxAddressMunicipalityName.getStatusInfo().getChanged())
                        .created(postOfficeBoxAddressMunicipalityName.getStatusInfo().getCreated())
                        .fetched(postOfficeBoxAddressMunicipalityName.getStatusInfo().getFetched())
                        .removed(postOfficeBoxAddressMunicipalityName.getStatusInfo().getRemoved())
                        .build()));
        return postOfficeBoxAddressMunicipalityNameDataList;
    }

    private List<PostOfficeBoxData> getPostOfficeBoxData(PostOfficeBoxAddress postOfficeBoxAddress) {
        List<PostOfficeBoxData> postOfficeBoxDataList = new ArrayList<>();
        List<PostOfficeBox> postOfficeBoxes = new ArrayList<>(postOfficeBoxAddress.getAllPostOfficeBoxes());
        postOfficeBoxes.forEach(postOfficeBox -> postOfficeBoxDataList.add(PostOfficeBoxData.builder()
                        .language(postOfficeBox.getLanguage())
                        .value(postOfficeBox.getValue())
                        .changed(postOfficeBox.getStatusInfo().getChanged())
                        .created(postOfficeBox.getStatusInfo().getCreated())
                        .fetched(postOfficeBox.getStatusInfo().getFetched())
                        .removed(postOfficeBox.getStatusInfo().getRemoved())
                        .build()));
        return postOfficeBoxDataList;
    }

    private List<PostOfficeData> getPostOfficeData(PostOfficeBoxAddress postOfficeBoxAddress) {
        List<PostOfficeData> postOfficeDataList = new ArrayList<>();
        List<PostOffice> postOffices = new ArrayList<>(postOfficeBoxAddress.getAllPostOffices());
        postOffices.forEach(postOffice -> postOfficeDataList.add(PostOfficeData.builder()
                        .language(postOffice.getLanguage())
                        .value(postOffice.getValue())
                        .changed(postOffice.getStatusInfo().getChanged())
                        .created(postOffice.getStatusInfo().getCreated())
                        .fetched(postOffice.getStatusInfo().getFetched())
                        .removed(postOffice.getStatusInfo().getRemoved())
                        .build()));
        return postOfficeDataList;
    }

    private List<EmailData> getEmailData(Iterable<Organization> organizations) {
        List<EmailData> emailDataList = new ArrayList<>();
        List<Email> emails = new ArrayList<>(organizations.iterator().next().getAllEmails());
        emails.forEach(email -> emailDataList.add(EmailData.builder()
                .description(email.getDescription())
                .language(email.getLanguage())
                .value(email.getValue())
                .changed(email.getStatusInfo().getChanged())
                .created(email.getStatusInfo().getCreated())
                .fetched(email.getStatusInfo().getFetched())
                .removed(email.getStatusInfo().getRemoved())
                .build()));
        return emailDataList;
    }

    private List<WebpageData> getWebpageData(Iterable<Organization> organizations) {
        List<WebpageData> webpageDataList = new ArrayList<>();
        List<WebPage> webPages = new ArrayList<>(organizations.iterator().next().getAllWebPages());
        webPages.forEach(webPage -> webpageDataList.add(WebpageData.builder()
                .url(webPage.getUrl())
                .language(webPage.getLanguage())
                .value(webPage.getValue())
                .changed(webPage.getStatusInfo().getChanged())
                .created(webPage.getStatusInfo().getCreated())
                .fetched(webPage.getStatusInfo().getFetched())
                .removed(webPage.getStatusInfo().getRemoved())
                .build()));
        return webpageDataList;
    }

    private List<PhoneNumberData> getPhoneNumberData(Iterable<Organization> organizations) {
        List<PhoneNumberData> phoneNumberDataList = new ArrayList<>();
        List<PhoneNumber> phoneNumbers = new ArrayList<>(organizations.iterator().next().getAllPhoneNumbers());
        phoneNumbers.forEach(phoneNumber -> phoneNumberDataList.add(PhoneNumberData.builder()
                .chargeDescription(phoneNumber.getChargeDescription())
                .additionalInformation(phoneNumber.getAdditionalInformation())
                .isFinnishServiceNumber(phoneNumber.getIsFinnishServiceNumber())
                .language(phoneNumber.getLanguage())
                .number(phoneNumber.getNumber())
                .prefixNumber(phoneNumber.getPrefixNumber())
                .serviceChargeType(phoneNumber.getServiceChargeType())
                .changed(phoneNumber.getStatusInfo().getChanged())
                .created(phoneNumber.getStatusInfo().getCreated())
                .fetched(phoneNumber.getStatusInfo().getFetched())
                .removed(phoneNumber.getStatusInfo().getRemoved())
                .build()));
        return phoneNumberDataList;
    }

    private List<BusinessAddressData> getBusinessAddressData(Iterable<Company> companies) {
        List<BusinessAddressData> businessAddressDataList = new ArrayList<>();
        List<BusinessAddress> businessAddresses = new ArrayList<>(companies.iterator().next().getAllBusinessAddresses());
        businessAddresses.forEach(businessAddress -> businessAddressDataList.add(BusinessAddressData.builder()
                .careOf(businessAddress.getCareOf())
                .street(businessAddress.getStreet())
                .city(businessAddress.getCity())
                .country(businessAddress.getCountry())
                .language(businessAddress.getLanguage())
                .postCode(businessAddress.getPostCode())
                .source(businessAddress.getSource())
                .type(businessAddress.getType())
                .version(businessAddress.getVersion())
                .registrationDate(businessAddress.getRegistrationDate())
                .changed(businessAddress.getStatusInfo().getChanged())
                .created(businessAddress.getStatusInfo().getCreated())
                .fetched(businessAddress.getStatusInfo().getFetched())
                .removed(businessAddress.getStatusInfo().getRemoved())
                .build()));
        return businessAddressDataList;
    }

    private List<BusinessAuxiliaryNameData> getBusinessAuxiliaryNameData(Iterable<Company> companies) {
        List<BusinessAuxiliaryNameData> businessAuxiliaryNameDataList = new ArrayList<>();
        List<BusinessAuxiliaryName> businessAuxiliaryNames = new ArrayList<>(companies.iterator().next().getAllBusinessAuxiliaryNames());
        businessAuxiliaryNames.forEach(businessAuxiliaryName -> businessAuxiliaryNameDataList.add(BusinessAuxiliaryNameData.builder()
                .language(businessAuxiliaryName.getLanguage())
                .source(businessAuxiliaryName.getSource())
                .version(businessAuxiliaryName.getVersion())
                .name(businessAuxiliaryName.getName())
                .ordering(businessAuxiliaryName.getOrdering())
                .endDate(businessAuxiliaryName.getEndDate())
                .registrationDate(businessAuxiliaryName.getRegistrationDate())
                .changed(businessAuxiliaryName.getStatusInfo().getChanged())
                .created(businessAuxiliaryName.getStatusInfo().getCreated())
                .fetched(businessAuxiliaryName.getStatusInfo().getFetched())
                .removed(businessAuxiliaryName.getStatusInfo().getRemoved())
                .build()));
        return businessAuxiliaryNameDataList;
    }

    private List<BusinessIdChangeData> getBusinessIdChangeData(Iterable<Company> companies) {
        List<BusinessIdChangeData> businessIdChangeDataList = new ArrayList<>();
        List<BusinessIdChange> businessIdChanges = new ArrayList<>(companies.iterator().next().getAllBusinessIdChanges());
        businessIdChanges.forEach(businessIdChange -> businessIdChangeDataList.add(BusinessIdChangeData.builder()
                .language(businessIdChange.getLanguage())
                .source(businessIdChange.getSource())
                .description(businessIdChange.getDescription())
                .change(businessIdChange.getChange())
                .oldBusinessId(businessIdChange.getOldBusinessId())
                .newBusinessId(businessIdChange.getNewBusinessId())
                .reason(businessIdChange.getReason())
                .changeDate(businessIdChange.getChangeDate())
                .changed(businessIdChange.getStatusInfo().getChanged())
                .created(businessIdChange.getStatusInfo().getCreated())
                .fetched(businessIdChange.getStatusInfo().getFetched())
                .removed(businessIdChange.getStatusInfo().getRemoved())
                .build()));
        return businessIdChangeDataList;
    }

    private List<BusinessLineData> getBusinessLineData(Iterable<Company> companies) {
        List<BusinessLineData> businessLineDataList = new ArrayList<>();
        List<BusinessLine> businessLines = new ArrayList<>(companies.iterator().next().getAllBusinessLines());
        businessLines.forEach(businessLine -> businessLineDataList.add(BusinessLineData.builder()
                .language(businessLine.getLanguage())
                .source(businessLine.getSource())
                .endDate(businessLine.getEndDate())
                .registrationDate(businessLine.getRegistrationDate())
                .name(businessLine.getName())
                .ordering(businessLine.getOrdering())
                .version(businessLine.getVersion())
                .changed(businessLine.getStatusInfo().getChanged())
                .created(businessLine.getStatusInfo().getCreated())
                .fetched(businessLine.getStatusInfo().getFetched())
                .removed(businessLine.getStatusInfo().getRemoved())
                .build()));
        return businessLineDataList;
    }

    private List<BusinessNameData> getBusinessNameData(Iterable<Company> companies) {
        List<BusinessNameData> businessNameDataList = new ArrayList<>();
        List<BusinessName> businessNames = new ArrayList<>(companies.iterator().next().getAllBusinessNames());
        businessNames.forEach(businessName -> businessNameDataList.add(BusinessNameData.builder()
                .language(businessName.getLanguage())
                .source(businessName.getSource())
                .version(businessName.getVersion())
                .name(businessName.getName())
                .ordering(businessName.getOrdering())
                .endDate(businessName.getEndDate())
                .registrationDate(businessName.getRegistrationDate())
                .changed(businessName.getStatusInfo().getChanged())
                .created(businessName.getStatusInfo().getCreated())
                .fetched(businessName.getStatusInfo().getFetched())
                .removed(businessName.getStatusInfo().getRemoved())
                .build()));
        return businessNameDataList;
    }

    private List<CompanyFormData> getCompanyFormData(Iterable<Company> companies) {
        List<CompanyFormData> companyFormDataList = new ArrayList<>();
        List<CompanyForm> companyForms = new ArrayList<>(companies.iterator().next().getAllCompanyForms());
        companyForms.forEach(companyForm -> companyFormDataList.add(CompanyFormData.builder()
                .language(companyForm.getLanguage())
                .source(companyForm.getSource())
                .version(companyForm.getVersion())
                .name(companyForm.getName())
                .endDate(companyForm.getEndDate())
                .registrationDate(companyForm.getRegistrationDate())
                .type(companyForm.getType())
                .changed(companyForm.getStatusInfo().getChanged())
                .created(companyForm.getStatusInfo().getCreated())
                .fetched(companyForm.getStatusInfo().getFetched())
                .removed(companyForm.getStatusInfo().getRemoved())
                .build()));
        return companyFormDataList;
    }

    private List<ContactDetailData> getContactDetailData(Iterable<Company> companies) {
        List<ContactDetailData> contactDetailDataList = new ArrayList<>();
        List<ContactDetail> contactDetails = new ArrayList<>(companies.iterator().next().getAllContactDetails());
        contactDetails.forEach(contactDetail -> contactDetailDataList.add(ContactDetailData.builder()
                .language(contactDetail.getLanguage())
                .source(contactDetail.getSource())
                .version(contactDetail.getVersion())
                .endDate(contactDetail.getEndDate())
                .registrationDate(contactDetail.getRegistrationDate())
                .type(contactDetail.getType())
                .value(contactDetail.getValue())
                .changed(contactDetail.getStatusInfo().getChanged())
                .created(contactDetail.getStatusInfo().getCreated())
                .fetched(contactDetail.getStatusInfo().getFetched())
                .removed(contactDetail.getStatusInfo().getRemoved())
                .build()));
        return contactDetailDataList;
    }

    private List<LanguageData> getLanguageData(Iterable<Company> companies) {
        List<LanguageData> languageDataList = new ArrayList<>();
        List<Language> languages = new ArrayList<>(companies.iterator().next().getAllLanguages());
        languages.forEach(language -> languageDataList.add(LanguageData.builder()
                .language(language.getLanguage())
                .source(language.getSource())
                .version(language.getVersion())
                .name(language.getName())
                .endDate(language.getEndDate())
                .registrationDate(language.getRegistrationDate())
                .changed(language.getStatusInfo().getChanged())
                .created(language.getStatusInfo().getCreated())
                .fetched(language.getStatusInfo().getFetched())
                .removed(language.getStatusInfo().getRemoved())
                .build()));
        return languageDataList;
    }

    private List<LiquidationData> getLiquidationData(Iterable<Company> companies) {
        List<LiquidationData> liquidationDataList = new ArrayList<>();
        List<Liquidation> liquidations = new ArrayList<>(companies.iterator().next().getAllLiquidations());
        liquidations.forEach(liquidation -> liquidationDataList.add(LiquidationData.builder()
                .language(liquidation.getLanguage())
                .source(liquidation.getSource())
                .version(liquidation.getVersion())
                .name(liquidation.getName())
                .endDate(liquidation.getEndDate())
                .registrationDate(liquidation.getRegistrationDate())
                .changed(liquidation.getStatusInfo().getChanged())
                .created(liquidation.getStatusInfo().getCreated())
                .fetched(liquidation.getStatusInfo().getFetched())
                .removed(liquidation.getStatusInfo().getRemoved())
                .build()));
        return liquidationDataList;
    }

    private List<RegisteredEntryData> getRegisteredEntryData(Iterable<Company> companies) {
        List<RegisteredEntryData> registeredEntryDataList = new ArrayList<>();
        List<RegisteredEntry> registeredEntries = new ArrayList<>(companies.iterator().next().getAllRegisteredEntries());
        registeredEntries.forEach(registeredEntry -> registeredEntryDataList.add(RegisteredEntryData.builder()
                .language(registeredEntry.getLanguage())
                .endDate(registeredEntry.getEndDate())
                .registrationDate(registeredEntry.getRegistrationDate())
                .authority(registeredEntry.getAuthority())
                .description(registeredEntry.getDescription())
                .register(registeredEntry.getRegister())
                .status(registeredEntry.getStatus())
                .changed(registeredEntry.getStatusInfo().getChanged())
                .created(registeredEntry.getStatusInfo().getCreated())
                .fetched(registeredEntry.getStatusInfo().getFetched())
                .removed(registeredEntry.getStatusInfo().getRemoved())
                .build()));
        return registeredEntryDataList;
    }

    private List<RegisteredOfficeData> getRegisteredOfficeData(Iterable<Company> companies) {
        List<RegisteredOfficeData> registeredOfficeDataList = new ArrayList<>();
        List<RegisteredOffice> registeredOffices = new ArrayList<>(companies.iterator().next().getAllRegisteredOffices());
        registeredOffices.forEach(registeredOffice -> registeredOfficeDataList.add(RegisteredOfficeData.builder()
                .language(registeredOffice.getLanguage())
                .endDate(registeredOffice.getEndDate())
                .registrationDate(registeredOffice.getRegistrationDate())
                .name(registeredOffice.getName())
                .ordering(registeredOffice.getOrdering())
                .source(registeredOffice.getSource())
                .version(registeredOffice.getVersion())
                .changed(registeredOffice.getStatusInfo().getChanged())
                .created(registeredOffice.getStatusInfo().getCreated())
                .fetched(registeredOffice.getStatusInfo().getFetched())
                .removed(registeredOffice.getStatusInfo().getRemoved())
                .build()));
        return registeredOfficeDataList;
    }
}
