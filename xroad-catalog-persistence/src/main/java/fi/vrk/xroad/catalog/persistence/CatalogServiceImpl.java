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
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.dto.DistinctServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.LastCollectionData;
import fi.vrk.xroad.catalog.persistence.dto.MemberData;
import fi.vrk.xroad.catalog.persistence.dto.MemberDataList;
import fi.vrk.xroad.catalog.persistence.dto.ServiceData;
import fi.vrk.xroad.catalog.persistence.dto.ServiceStatistics;
import fi.vrk.xroad.catalog.persistence.dto.SubsystemData;
import fi.vrk.xroad.catalog.persistence.dto.XRoadData;
import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Implementation for catalogservice CRUD
 */
@Slf4j
@Component("catalogService")
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private static final String NOT_FOUND = " not found!";
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SubsystemRepository subsystemRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    OpenApiRepository openApiRepository;

    @Autowired
    WsdlRepository wsdlRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ErrorLogRepository errorLogRepository;

    @Override
    public Iterable<Member> getActiveMembers() {
        return memberRepository.findAllActive();
    }

    @Override
    public Iterable<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Iterable<Member> getActiveMembers(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return memberRepository.findActiveChangedBetween(startDateTime, endDateTime);
    }

    @Override
    public Iterable<Member> getAllMembers(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return memberRepository.findAllChangedBetween(startDateTime, endDateTime);
    }

    @Override
    public Member getMember(String xRoadInstance, String memberClass, String memberCode) {
        return memberRepository.findActiveByNaturalKey(xRoadInstance, memberClass, memberCode);
    }

    @Override
    public Wsdl getWsdl(String externalId) {
        List<Wsdl> matches = wsdlRepository.findAnyByExternalId(externalId);
        if (matches.size() > 1) {
            throw new IllegalStateException("multiple matches found to " + externalId + ": " + matches);
        } else if (matches.size() == 1) {
            return matches.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public OpenApi getOpenApi(String externalId) {
        List<OpenApi> matches = openApiRepository.findAnyByExternalId(externalId);
        if (matches.size() > 1) {
            throw new IllegalStateException("multiple matches found to " + externalId + ": " + matches);
        } else if (matches.size() == 1) {
            return matches.iterator().next();
        } else {
            return null;
        }
    }

    @Override
    public Service getService(String xRoadInstance,
                              String memberClass,
                              String memberCode,
                              String serviceCode,
                              String subsystemCode,
                              String serviceVersion) {
        if (serviceVersion == null) {
            return serviceRepository.findAllByMemberServiceAndSubsystemVersionNull(xRoadInstance,
                    memberClass, memberCode, serviceCode, subsystemCode);
        }
        return serviceRepository.findAllByMemberServiceAndSubsystemAndVersion(xRoadInstance,
                memberClass, memberCode, serviceCode, subsystemCode, serviceVersion);
    }

    @Override
    public List<ServiceStatistics> getServiceStatistics(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<ServiceStatistics> serviceStatisticsList = new ArrayList<>();
        List<Service> services = serviceRepository.findAllActive();
        LocalDateTime dateInPast = startDateTime;
        while (isDateBetweenDates(dateInPast, startDateTime, endDateTime)) {
            ServiceStatistics serviceStatistics = createServiceStatistics(services, dateInPast, startDateTime, endDateTime);
            serviceStatisticsList.add(serviceStatistics);
            dateInPast = dateInPast.plusDays(1);
        }
        return serviceStatisticsList;
    }

    private ServiceStatistics createServiceStatistics(List<Service> services,
                                                      LocalDateTime dateInPast,
                                                      LocalDateTime startDateTime,
                                                      LocalDateTime endDateTime) {
        AtomicLong numberOfSoapServices = new AtomicLong();
        AtomicLong numberOfRestServices = new AtomicLong();
        AtomicLong numberOfOpenApiServices = new AtomicLong();

        services.forEach(service -> {
            LocalDateTime creationDate = service.getStatusInfo().getCreated();
            if (isDateBetweenDates(creationDate, startDateTime, endDateTime)) {
                if (service.hasOpenApi()) {
                    numberOfOpenApiServices.getAndIncrement();
                }
                else if (service.hasWsdl()) {
                    numberOfSoapServices.getAndIncrement();
                }
                else {
                    numberOfRestServices.getAndIncrement();
                }
            }
        });

        ServiceStatistics serviceStatistics = ServiceStatistics.builder()
                .created(dateInPast)
                .numberOfRestServices(numberOfRestServices.longValue())
                .numberOfSoapServices(numberOfSoapServices.longValue())
                .numberOfOpenApiServices(numberOfOpenApiServices.longValue()).build();
        return serviceStatistics;
    }

    @Override
    public Page<ErrorLog> getErrors(XRoadData xRoadData,
                                    int page,
                                    int limit,
                                    LocalDateTime startDateTime,
                                    LocalDateTime endDateTime) {
        Page<ErrorLog> errorLogList;
        String xRoadInstance = xRoadData.getXRoadInstance();
        String memberClass = xRoadData.getMemberClass();
        String memberCode = xRoadData.getMemberCode();
        String subsystemCode = xRoadData.getSubsystemCode();

        if (xRoadInstance != null) {
            if (memberClass != null) {
                if (memberCode != null) {
                    if (subsystemCode != null) {
                        errorLogList = errorLogRepository.findAnyByAllParameters(startDateTime,
                                endDateTime,
                                xRoadInstance,
                                memberClass,
                                memberCode,
                                subsystemCode,
                                new PageRequest(page, limit));
                    } else {
                        errorLogList = errorLogRepository.findAnyByMemberCode(startDateTime,
                                endDateTime,
                                xRoadInstance,
                                memberClass,
                                memberCode,
                                new PageRequest(page, limit));
                    }
                } else {
                    errorLogList = errorLogRepository.findAnyByMemberClass(startDateTime,
                            endDateTime,
                            xRoadInstance,
                            memberClass,
                            new PageRequest(page, limit));
                }
            } else {
                errorLogList = errorLogRepository.findAnyByInstance(startDateTime,
                        endDateTime,
                        xRoadInstance,
                        new PageRequest(page, limit));
            }
        } else {
            errorLogList = errorLogRepository.findAnyByCreated(startDateTime,
                    endDateTime,
                    new PageRequest(page, limit));
        }

        return errorLogList;
    }

    @Override
    public List<DistinctServiceStatistics> getDistinctServiceStatistics(LocalDateTime startDateTime,
                                                                        LocalDateTime endDateTime) {
        List<DistinctServiceStatistics> serviceStatisticsList = new ArrayList<>();
        List<Service> services = serviceRepository.findAllActive();
        LocalDateTime dateInPast = startDateTime;
        while (isDateBetweenDates(dateInPast, startDateTime, endDateTime)) {
            AtomicLong totalDistinctServices = new AtomicLong();
            List<Service> servicesBetweenDates = services.stream()
                    .filter(p -> isDateBetweenDates(p.getStatusInfo().getCreated(), startDateTime, endDateTime))
                    .collect(Collectors.toList());
            if (!servicesBetweenDates.isEmpty()) {
                totalDistinctServices.set(servicesBetweenDates.stream().map(Service::getServiceCode).collect(Collectors.toList())
                    .stream().distinct().collect(Collectors.toList()).size());

                DistinctServiceStatistics serviceStatistics = DistinctServiceStatistics.builder()
                        .created(dateInPast)
                        .numberOfDistinctServices(totalDistinctServices.longValue()).build();

                serviceStatisticsList.add(serviceStatistics);
            }

            dateInPast = dateInPast.plusDays(1);
        }
        return serviceStatisticsList;
    }

    @Override
    public List<MemberDataList> getMemberData(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<MemberDataList> listOfMemberDataLists = new ArrayList<>();
        Set<Member> members = memberRepository.findAll();
        LocalDateTime dateInPast = startDateTime;
        while (isDateBetweenDates(dateInPast, startDateTime, endDateTime)) {
            List<MemberData> memberDataList = new ArrayList<>();
            members.forEach(member -> {
                LocalDateTime creationDate = member.getStatusInfo().getCreated();
                if (isDateBetweenDates(creationDate, startDateTime, endDateTime)) {
                    AtomicReference<Boolean> isProvider = new AtomicReference<>();
                    isProvider.set(Boolean.FALSE);
                    Set<Subsystem> subsystems = member.getAllSubsystems();
                    List<SubsystemData> subsystemDataList = new ArrayList<>();
                    subsystems.forEach(subsystem -> {
                        List<ServiceData> serviceDataList = new ArrayList<>();
                        Set<Service> services = subsystem.getAllServices();
                        services.forEach(service -> {
                            ServiceData serviceData = ServiceData.builder()
                                    .created(service.getStatusInfo().getCreated())
                                    .serviceCode(service.getServiceCode())
                                    .active(!service.getStatusInfo().isRemoved())
                                    .serviceVersion(service.getServiceVersion()).build();
                            serviceDataList.add(serviceData);
                            if (service.hasWsdl() || service.hasOpenApi()) {
                                isProvider.set(Boolean.TRUE);
                            }
                        });
                        SubsystemData subsystemData = SubsystemData.builder()
                                .created(subsystem.getStatusInfo().getCreated())
                                .subsystemCode(subsystem.getSubsystemCode())
                                .active(!subsystem.getStatusInfo().isRemoved())
                                .serviceList(serviceDataList).build();
                        subsystemDataList.add(subsystemData);
                    });

                    MemberData memberData = MemberData.builder()
                            .created(creationDate)
                            .provider(isProvider.get())
                            .memberClass(member.getMemberClass())
                            .memberCode(member.getMemberCode())
                            .name(member.getName())
                            .xRoadInstance(member.getXRoadInstance())
                            .subsystemList(subsystemDataList).build();
                    memberDataList.add(memberData);
                }
            });
            listOfMemberDataLists.add(MemberDataList.builder().date(dateInPast).memberDataList(memberDataList).build());
            dateInPast = dateInPast.plusDays(1);
        }

        return listOfMemberDataLists;
    }

    @Override
    public Iterable<ErrorLog> getErrorLog(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return errorLogRepository.findAny(startDateTime, endDateTime);
    }

    @Override
    public void saveAllMembersAndSubsystems(Collection<Member> members) {
        LocalDateTime now = LocalDateTime.now();
        // process members
        Map<MemberId, Member> unprocessedOldMembers = new HashMap<>();
        StreamSupport.stream(memberRepository.findAll().spliterator(), false)
                .forEach(member -> unprocessedOldMembers.put(member.createKey(), member));

        for (Member member: members) {
            Member oldMember = unprocessedOldMembers.get(member.createKey());
            if (oldMember == null) {
                // brand new item
                member.getStatusInfo().setTimestampsForNew(now);
                for (Subsystem subsystem: member.getAllSubsystems()) {
                    subsystem.getStatusInfo().setTimestampsForNew(now);
                    subsystem.setMember(member);
                }
                member = memberRepository.save(member);
            } else {
                handleOldMember(now, member, oldMember);

                member = memberRepository.save(oldMember);
            }
            unprocessedOldMembers.remove(member.createKey());
        }
        // now unprocessedOldMembers should all be removed (either already removed, or will be now)
        removeUnprocessedOldMembers(now, unprocessedOldMembers);
    }

    @Override
    public void saveServices(SubsystemId subsystemId, Collection<Service> services) {
        if (subsystemId == null) {
            throw new IllegalStateException("subsystem " + subsystemId + NOT_FOUND);
        }
        Subsystem oldSubsystem = subsystemRepository.findActiveByNaturalKey(subsystemId.getXRoadInstance(),
                subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                subsystemId.getSubsystemCode());
        if (oldSubsystem == null) {
            throw new IllegalStateException("subsystem " + subsystemId + NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();

        Map<ServiceId, Service> unprocessedOldServices = new HashMap<>();
        oldSubsystem.getAllServices().stream().forEach(s -> unprocessedOldServices.put(s.createKey(), s));

        for (Service service: services) {
            Service oldService = unprocessedOldServices.get(service.createKey());
            if (oldService == null) {
                // brand new item, add it
                service.getStatusInfo().setTimestampsForNew(now);
                service.setSubsystem(oldSubsystem);
                oldSubsystem.getAllServices().add(service);
            } else {
                oldService.getStatusInfo().setTimestampsForFetched(now);
            }
            unprocessedOldServices.remove(service.createKey());
        }

        // now unprocessedOldServices should all be removed (either already removed, or will be now)
        for (Service oldToRemove: unprocessedOldServices.values()) {
            StatusInfo status = oldToRemove.getStatusInfo();
            if (!status.isRemoved()) {
                status.setTimestampsForRemoved(now);
            }
        }

    }

    @Override
    public void saveWsdl(SubsystemId subsystemId, ServiceId serviceId, String wsdlString) {
        Assert.notNull(subsystemId, "subsystemId is required");
        Assert.notNull(serviceId, "serviceId is required");
        Service oldService;
        // bit ugly this one, would be a little cleaner if
        // https://jira.spring.io/browse/DATAJPA-209 was resolved
        if (serviceId.getServiceVersion() == null) {
            oldService = serviceRepository.findActiveNullVersionByNaturalKey(
                    subsystemId.getXRoadInstance(),
                    subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                    subsystemId.getSubsystemCode(), serviceId.getServiceCode());
        } else {
            oldService = serviceRepository.findActiveByNaturalKey(subsystemId.getXRoadInstance(),
                    subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                    subsystemId.getSubsystemCode(), serviceId.getServiceCode(),
                    serviceId.getServiceVersion());
        }
        if (oldService == null) {
            throw new IllegalStateException("service " + serviceId + NOT_FOUND);
        }
        LocalDateTime now = LocalDateTime.now();
        Wsdl wsdl = new Wsdl();
        wsdl.setData(wsdlString);
        Wsdl oldWsdl = oldService.getWsdl();
        if (oldWsdl == null) {
            wsdl.initializeExternalId();
            wsdl.getStatusInfo().setTimestampsForNew(now);
            oldService.setWsdl(wsdl);
            wsdl.setService(oldService);
            wsdlRepository.save(wsdl);
        } else {
            if (oldWsdl.getStatusInfo().isRemoved()) {
                // resurrect
                oldWsdl.setData(wsdl.getData());
                oldWsdl.getStatusInfo().setChanged(now);
                oldWsdl.getStatusInfo().setRemoved(null);
                oldWsdl.getStatusInfo().setFetched(now);
            } else {
                // update existing
                boolean wsdlChanged = !oldWsdl.getData().equals(wsdl.getData());
                if (wsdlChanged) {
                    oldWsdl.getStatusInfo().setChanged(now);
                    oldWsdl.setData(wsdl.getData());
                }
                oldWsdl.getStatusInfo().setFetched(now);
            }
        }
    }

    @Override
    public void saveOpenApi(SubsystemId subsystemId, ServiceId serviceId, String openApiString) {
        Assert.notNull(subsystemId, "subsystemId is required");
        Assert.notNull(serviceId, "serviceId is required");
        Service oldService;
        // bit ugly this one, would be a little cleaner if
        // https://jira.spring.io/browse/DATAJPA-209 was resolved
        if (serviceId.getServiceVersion() == null) {
            oldService = serviceRepository.findActiveNullVersionByNaturalKey(
                    subsystemId.getXRoadInstance(),
                    subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                    subsystemId.getSubsystemCode(), serviceId.getServiceCode());
        } else {
            oldService = serviceRepository.findActiveByNaturalKey(subsystemId.getXRoadInstance(),
                    subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                    subsystemId.getSubsystemCode(), serviceId.getServiceCode(),
                    serviceId.getServiceVersion());
        }
        if (oldService == null) {
            throw new IllegalStateException("service " + serviceId + NOT_FOUND);
        }
        LocalDateTime now = LocalDateTime.now();
        OpenApi openApi = new OpenApi();
        openApi.setData(openApiString);
        OpenApi oldOpenApi = oldService.getOpenApi();
        if (oldOpenApi == null) {
            openApi.initializeExternalId();
            openApi.getStatusInfo().setTimestampsForNew(now);
            oldService.setOpenApi(openApi);
            openApi.setService(oldService);
            openApiRepository.save(openApi);
        } else {
            if (oldOpenApi.getStatusInfo().isRemoved()) {
                // resurrect
                oldOpenApi.setData(openApi.getData());
                oldOpenApi.getStatusInfo().setChanged(now);
                oldOpenApi.getStatusInfo().setRemoved(null);
                oldOpenApi.getStatusInfo().setFetched(now);
            } else {
                // update existing
                boolean wsdlChanged = !oldOpenApi.getData().equals(openApi.getData());
                if (wsdlChanged) {
                    oldOpenApi.getStatusInfo().setChanged(now);
                    oldOpenApi.setData(openApi.getData());
                }
                oldOpenApi.getStatusInfo().setFetched(now);
            }
        }
    }

    @Override
    public ErrorLog saveErrorLog(ErrorLog errorLog) {
        return errorLogRepository.save(errorLog);
    }

    @Override
    public void deleteOldErrorLogEntries(Integer daysBefore) {
        LocalDateTime oldDate = LocalDateTime.now().minusDays(daysBefore);
        errorLogRepository.deleteEntriesOlderThan(oldDate);
    }

    @Override
    public Boolean checkDatabaseConnection() {
        return Integer.valueOf(1).equals(memberRepository.checkConnection());
    }

    @Override
    public LastCollectionData getLastCollectionData() {
        return LastCollectionData.builder()
                .companiesLastFetched(companyRepository.findLatestFetched())
                .membersLastFetched(memberRepository.findLatestFetched())
                .openapisLastFetched(openApiRepository.findLatestFetched())
                .organizationsLastFetched(organizationRepository.findLatestFetched())
                .servicesLastFetched(serviceRepository.findLatestFetched())
                .subsystemsLastFetched(subsystemRepository.findLatestFetched())
                .wsdlsLastFetched(wsdlRepository.findLatestFetched()).build();
    }

    private void handleOldMember(LocalDateTime now, Member member, Member oldMember) {
        oldMember.updateWithDataFrom(member, now);
        // process subsystems for the old member
        Map<SubsystemId, Subsystem> unprocessedOldSubsystems = new HashMap<>();
        for (Subsystem subsystem: oldMember.getAllSubsystems()) {
            unprocessedOldSubsystems.put(subsystem.createKey(), subsystem);
        }
        for (Subsystem subsystem: member.getAllSubsystems()) {
            Subsystem oldSubsystem = unprocessedOldSubsystems.get(subsystem.createKey());
            if (oldSubsystem == null) {
                // brand new item, add it
                subsystem.getStatusInfo().setTimestampsForNew(now);
                subsystem.setMember(oldMember);
                oldMember.getAllSubsystems().add(subsystem);
            } else {
                oldSubsystem.getStatusInfo().setTimestampsForFetched(now);
            }
            unprocessedOldSubsystems.remove(subsystem.createKey());
        }
        // remaining old subsystems - that were not included in member.subsystems -
        // are removed (if not already)
        for (Subsystem oldToRemove: unprocessedOldSubsystems.values()) {
            StatusInfo status = oldToRemove.getStatusInfo();
            if (!status.isRemoved()) {
                status.setTimestampsForRemoved(now);
            }
        }
    }

    private void removeUnprocessedOldMembers(LocalDateTime now, Map<MemberId, Member> unprocessedOldMembers) {
        for (Member oldToRemove: unprocessedOldMembers.values()) {
            StatusInfo status = oldToRemove.getStatusInfo();
            if (!status.isRemoved()) {
                status.setTimestampsForRemoved(now);
            }
            for (Subsystem subsystem: oldToRemove.getAllSubsystems()) {
                if (!subsystem.getStatusInfo().isRemoved()) {
                    subsystem.getStatusInfo().setTimestampsForRemoved(now);
                }
            }
        }
    }

    private boolean isDateBetweenDates(LocalDateTime dateToBeChecked,
                                       LocalDateTime startDate,
                                       LocalDateTime endDate) {
        return (dateToBeChecked.isAfter(startDate) || dateToBeChecked.isEqual(startDate))
                && (dateToBeChecked.isBefore(endDate) || dateToBeChecked.isEqual(endDate));
    }
}
