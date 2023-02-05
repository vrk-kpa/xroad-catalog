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

import fi.vrk.xroad.catalog.lister.util.ServiceUtil;
import fi.vrk.xroad.catalog.persistence.dto.DescriptorInfo;
import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatisticsResponse;
import fi.vrk.xroad.catalog.persistence.dto.EndpointData;
import fi.vrk.xroad.catalog.persistence.dto.ErrorLogResponse;
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
import fi.vrk.xroad.catalog.persistence.entity.ErrorLog;
import fi.vrk.xroad.catalog.persistence.entity.Rest;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
@Profile({"default", "fi"})
public class ServiceController implements ServiceOperations {

    @Value("${xroad-catalog.shared-params-file}")
    private String sharedParamsFile;

    private static final String CSV_DATE_HEADER = "Date";
    private static final String CSV_NUMBER_OF_REST_SERVICES_HEADER = "Number of REST services";
    private static final String CSV_NUMBER_OF_SOAP_SERVICES_HEADER = "Number of SOAP services";
    private static final String CSV_NUMBER_OF_OPENAPI_SERVICES_HEADER = "Number of OpenApi services";
    private static final String SERVICE_STATISTICS_REPORT_NAME = "service_statistics_";

    private static final String CSV_XROAD_INSTANCE_HEADER = "XRoad instance";
    private static final String CSV_MEMBER_CLASS_HEADER = "Member class";
    private static final String CSV_MEMBER_CODE_HEADER = "Member code";
    private static final String CSV_MEMBER_NAME_HEADER = "Member name";
    private static final String CSV_MEMBER_CREATED_HEADER = "Member created";
    private static final String CSV_SUBSYSTEM_CODE_HEADER = "Subsystem code";
    private static final String CSV_SUBSYSTEM_CREATED_HEADER = "Subsystem created";
    private static final String CSV_SUBSYSTEM_ACTIVE_HEADER = "Subsystem active";
    private static final String CSV_SERVICE_CODE_HEADER = "Service code";
    private static final String CSV_SERVICE_VERSION_HEADER = "Service version";
    private static final String CSV_SERVICE_CREATED_HEADER = "Service created";
    private static final String CSV_SERVICE_ACTIVE_HEADER = "Service active";

    private static final String LIST_OF_SERVICES_REPORT_NAME = "list_of_services_";
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private SharedParamsParser sharedParamsParser;

    @Override
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
        XRoadData xRoadData = XRoadData.builder().xRoadInstance(xRoadInstance).memberClass(memberClass).memberCode(memberCode).subsystemCode(subsystemCode).build();
        Page<ErrorLog> errors = catalogService.getErrors(xRoadData, Integer.valueOf(page), Integer.valueOf(limit), startDateTime, endDateTime);
        return ResponseEntity.ok(ErrorLogResponse.builder().pageNumber(page).pageSize(limit).numberOfPages(errors.getTotalPages()).errorLogList(errors.getContent()).build());
    }

    @Override
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
        return ResponseEntity.ok(DistinctServiceStatisticsResponse.builder().distinctServiceStatisticsList(serviceStatisticsList).build());
    }

    @Override
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
        return ResponseEntity.ok(ServiceStatisticsResponse.builder().serviceStatisticsList(serviceStatisticsList).build());
    }

    @Override
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
        try {
            StringWriter sw = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.Builder.create().setDelimiter(",").setHeader(CSV_DATE_HEADER,
                    CSV_NUMBER_OF_REST_SERVICES_HEADER,
                    CSV_NUMBER_OF_SOAP_SERVICES_HEADER,
                    CSV_NUMBER_OF_OPENAPI_SERVICES_HEADER).build());
            if (serviceStatisticsList != null) {
                serviceStatisticsList.forEach(serviceStatistics -> ServiceUtil.printCSVRecord(csvPrinter,
                        Arrays.asList(serviceStatistics.getCreated().toString(),
                                serviceStatistics.getNumberOfRestServices().toString(),
                                serviceStatistics.getNumberOfSoapServices().toString(),
                                serviceStatistics.getNumberOfOpenApiServices().toString())));
            }
            String reportName = SERVICE_STATISTICS_REPORT_NAME + LocalDateTime.now();
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

    @Override
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
        return ResponseEntity.ok(ListOfServicesResponse.builder().memberData(memberDataList).securityServerData(securityServerList).build());
    }

    @Override
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
        try {
            StringWriter sw = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.Builder.create().setDelimiter(",").setHeader(
                            CSV_DATE_HEADER,
                            CSV_XROAD_INSTANCE_HEADER,
                            CSV_MEMBER_CLASS_HEADER,
                            CSV_MEMBER_CODE_HEADER,
                            CSV_MEMBER_NAME_HEADER,
                            CSV_MEMBER_CREATED_HEADER,
                            CSV_SUBSYSTEM_CODE_HEADER,
                            CSV_SUBSYSTEM_CREATED_HEADER,
                            CSV_SUBSYSTEM_ACTIVE_HEADER,
                            CSV_SERVICE_CODE_HEADER,
                            CSV_SERVICE_VERSION_HEADER,
                            CSV_SERVICE_CREATED_HEADER,
                            CSV_SERVICE_ACTIVE_HEADER).build());
            if (memberDataList != null) {
                ServiceUtil.printListOfServicesCSV(csvPrinter, memberDataList, securityServerList);
            }
            String reportName = LIST_OF_SERVICES_REPORT_NAME + LocalDateTime.now();
            sw.close();
            csvPrinter.close();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + reportName + ".csv")
                    .contentType(org.springframework.http.MediaType.valueOf(MediaType.TEXT_PLAIN))
                    .body(new ByteArrayResource(sw.toString().getBytes()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<SecurityServerDataList> listSecurityServers() {
        return ResponseEntity.ok(ServiceUtil.getSecurityServerDataList(sharedParamsParser, sharedParamsFile));
    }

    @Override
    public ResponseEntity<List<DescriptorInfo>> listDescriptors() {
        return ResponseEntity.ok(ServiceUtil.getDescriptorInfoList(sharedParamsParser, sharedParamsFile));
    }

    @Override
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

    @Override
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
