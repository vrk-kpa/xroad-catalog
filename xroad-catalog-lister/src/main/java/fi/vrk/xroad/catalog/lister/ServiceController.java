/**
 * The MIT License
 * Copyright (c) 2020, Population Register Centre (VRK)
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

import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.dto.ListOfServicesResponse;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatisticsResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
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
    private SharedParamsParser sharedParamsParser;


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
                        .withHeader("Date", "Number of REST services", "Number of SOAP services", "Number of other services", "Total distinct services"));
                serviceStatisticsList.forEach(serviceStatistics -> printCSVRecord(csvPrinter,
                        Arrays.asList(serviceStatistics.getCreated().toString(),
                                serviceStatistics.getNumberOfRestServices().toString(),
                                serviceStatistics.getNumberOfSoapServices().toString(),
                                serviceStatistics.getNumberOfOtherServices().toString(),
                                serviceStatistics.getTotalNumberOfDistinctServices().toString())));
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
        List<SecurityServerInfo> securityServerList = getSecurityServerData();
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
        List<SecurityServerInfo> securityServerList = getSecurityServerData();
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

    private List<SecurityServerInfo> getSecurityServerData() {
        List<SecurityServerInfo> securityServerList = new ArrayList<>();
        try {
            Set<SecurityServerInfo> securityServerInfos = sharedParamsParser.parse(sharedParamsFile);
            if (securityServerInfos.iterator().hasNext()) {
                securityServerList = new ArrayList<>(securityServerInfos);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return securityServerList;
    }
}
