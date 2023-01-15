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
package fi.vrk.xroad.catalog.lister.util;

import fi.vrk.xroad.catalog.lister.CatalogListerRuntimeException;
import fi.vrk.xroad.catalog.lister.SharedParamsParser;
import fi.vrk.xroad.catalog.persistence.dto.DescriptorInfo;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerDataList;
import fi.vrk.xroad.catalog.persistence.dto.SecurityServerInfo;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import org.apache.commons.csv.CSVPrinter;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;

public class ServiceUtil {

    private ServiceUtil() {

    }

    public static LocalDateTime convertStringToLocalDateTime(String dateStr) {
        LocalDateTime dateTime;
        try {
            dateTime = (dateStr == null)
                    ? LocalDateTime.now().with(LocalTime.MIDNIGHT)
                    : LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new CatalogListerRuntimeException("Exception parsing date parameter: " + e.getMessage());
        }
        return dateTime;
    }

    public static void printCSVRecord(CSVPrinter csvPrinter, List<String> data) {
        try {
            csvPrinter.printRecord(data);
        } catch (IOException e) {
            throw new CatalogListerRuntimeException("Exception printing CSV record: " + e.getMessage());
        }
    }

    public static void printListOfServicesCSV(CSVPrinter csvPrinter, List<MemberDataList> memberDataList, List<SecurityServerInfo> securityServerList) {
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

    public static List<SecurityServerInfo> getSecurityServerInfoList(SharedParamsParser sharedParamsParser,
                                                                     String sharedParamsFile) {
        List<SecurityServerInfo> securityServerList = new ArrayList<>();
        try {
            Set<SecurityServerInfo> securityServerInfos = sharedParamsParser.parseInfo(sharedParamsFile);
            if (securityServerInfos.iterator().hasNext()) {
                securityServerList = new ArrayList<>(securityServerInfos);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new CatalogListerRuntimeException("Exception when parsing sharedParams file: " + e);
        }
        return securityServerList;
    }

    public static SecurityServerDataList getSecurityServerDataList(SharedParamsParser sharedParamsParser,
                                                                   String sharedParamsFile) {
        SecurityServerDataList securityServerDataList;
        try {
            securityServerDataList = sharedParamsParser.parseDetails(sharedParamsFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new CatalogListerRuntimeException("Exception when parsing security server data from sharedParams file: " + e);
        }
        return securityServerDataList;
    }

    public static List<DescriptorInfo> getDescriptorInfoList(SharedParamsParser sharedParamsParser,
                                                             String sharedParamsFile) {
        List<DescriptorInfo> descriptorInfoList;
        try {
            descriptorInfoList = sharedParamsParser.parseDescriptorInfo(sharedParamsFile);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new CatalogListerRuntimeException("Exception when parsing descriptor info from sharedParams file: " + e);
        }
        return descriptorInfoList;
    }
}
