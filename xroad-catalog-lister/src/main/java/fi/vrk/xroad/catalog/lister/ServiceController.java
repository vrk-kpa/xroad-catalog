package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.CatalogService;
import fi.vrk.xroad.catalog.persistence.dto.ListOfServicesRequest;
import fi.vrk.xroad.catalog.persistence.dto.ListOfServicesResponse;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatisticsRequest;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatisticsResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.core.*;

@RestController
@RequestMapping("/api")
public class ServiceController {

    @Value("${xroad-catalog.max-history-length-in-days}")
    private Integer maxHistoryLengthInDays;

    @Autowired
    private CatalogService catalogService;

    @PostMapping(path = "/getServiceStatistics", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> getServiceStatistics(@Valid @RequestBody ServiceStatisticsRequest request) {
        if (request.getHistoryAmountInDays() == null
                || request.getHistoryAmountInDays() < 1 || request.getHistoryAmountInDays() > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        List<ServiceStatistics> serviceStatisticsList = catalogService.getServiceStatistics(request.getHistoryAmountInDays());
        if (serviceStatisticsList != null && !serviceStatisticsList.isEmpty()) {
            return ResponseEntity.ok(ServiceStatisticsResponse.builder().serviceStatisticsList(serviceStatisticsList).build());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(path = "/getServiceStatisticsCSV", consumes = "application/json", produces = "application/octet-stream")
    public ResponseEntity<Resource> getServiceStatisticsCSV(@Valid @RequestBody ServiceStatisticsRequest request) {
        List<ServiceStatistics> serviceStatisticsList = catalogService.getServiceStatistics(request.getHistoryAmountInDays());
        if (serviceStatisticsList != null && !serviceStatisticsList.isEmpty()) {
            try {
                StringWriter sw = new StringWriter();
                CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                        .withHeader("Date", "Number of REST services", "Number of SOAP services", "Total distinct services"));
                serviceStatisticsList.forEach(serviceStatistics -> printCSVRecord(csvPrinter,
                        Arrays.asList(serviceStatistics.getCreated().toString(),
                        serviceStatistics.getNumberOfRestServices().toString(),
                        serviceStatistics.getNumberOfSoapServices().toString(),
                        serviceStatistics.getTotalNumberOfDistinctServices().toString())));
                File file = createCSVFile("ServiceStatistics");
                FileWriter fw = new FileWriter(file);
                fw.write(sw.toString());
                sw.close();
                fw.close();
                csvPrinter.close();
                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                        .body(new ByteArrayResource(Files.readAllBytes(file.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/getListOfServices", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> getListOfServices(@Valid @RequestBody ListOfServicesRequest request) {
        if (request.getHistoryAmountInDays() == null
                || request.getHistoryAmountInDays() < 1 || request.getHistoryAmountInDays() > maxHistoryLengthInDays) {
            return new ResponseEntity<>(
                    "Input parameter historyAmountInDays must be greater "
                            + "than zero and less than the required maximum of " + maxHistoryLengthInDays + " days",
                    HttpStatus.BAD_REQUEST);
        }
        List<MemberDataList> memberDataList = catalogService.getMemberData(request.getHistoryAmountInDays());
        if (memberDataList != null && !memberDataList.isEmpty()) {
            return ResponseEntity.ok(ListOfServicesResponse.builder().memberData(memberDataList).build());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(path = "/getListOfServicesCSV", consumes = "application/json", produces = "application/octet-stream")
    public ResponseEntity<Resource> getListOfServicesCSV(@Valid @RequestBody ListOfServicesRequest request) {
        List<MemberDataList> memberDataList = catalogService.getMemberData(request.getHistoryAmountInDays());
        if (memberDataList != null && !memberDataList.isEmpty()) {
            try {
                StringWriter sw = new StringWriter();
                CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT
                        .withHeader("Date", "XRoad instance", "Member class", "Member code",
                                    "Member name", "Member created", "Subsystem code", "Subsystem created",
                                    "Service code", "Service version", "Service created"));
                printListOfServicesCSV(csvPrinter, memberDataList);
                File file = createCSVFile("ListOfServices");
                FileWriter fw = new FileWriter(file);
                fw.write(sw.toString());
                sw.close();
                fw.close();
                csvPrinter.close();
                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                        .body(new ByteArrayResource(Files.readAllBytes(file.toPath())));
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

    private File createCSVFile(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String fileCreationTime = LocalDateTime.now().format(formatter);
        return new File(fileName + fileCreationTime + ".csv");
    }

    private void printListOfServicesCSV(CSVPrinter csvPrinter, List<MemberDataList> memberDataList) {
        memberDataList.forEach(memberList -> {
            printCSVRecord(csvPrinter, Arrays.asList(memberList.getDate().toString(), "", "", "", "", "", "", "", "", "", ""));
            memberList.getMemberDataList().forEach(memberData -> {
                String memberCreated = memberData.getCreated().toString();
                String xRoadInstance = memberData.getXRoadInstance();
                String memberClass = memberData.getMemberClass();
                String memberCode = memberData.getMemberCode();
                String memberName = memberData.getName();

                if (memberData.getSubsystemList().isEmpty() || memberData.getSubsystemList() == null) {
                    printCSVRecord(csvPrinter, Arrays.asList("", xRoadInstance, memberClass, memberCode, memberName,
                            memberCreated, "", "", "", "", ""));
                }

                memberData.getSubsystemList().forEach(subsystemData -> {

                    if (subsystemData.getServiceList().isEmpty() || subsystemData.getServiceList() == null) {
                        printCSVRecord(csvPrinter, Arrays.asList("", xRoadInstance, memberClass, memberCode, memberName, memberCreated,
                                subsystemData.getSubsystemCode(), subsystemData.getCreated().toString(), "", "", ""));
                    }

                    subsystemData.getServiceList().forEach(serviceData -> printCSVRecord(csvPrinter, Arrays.asList(
                            "", xRoadInstance, memberClass, memberCode, memberName, memberCreated, subsystemData.getSubsystemCode(),
                            subsystemData.getCreated().toString(), serviceData.getServiceCode(), serviceData.getServiceVersion(),
                            serviceData.getCreated().toString())));
                });
            });
        });
    }
}
