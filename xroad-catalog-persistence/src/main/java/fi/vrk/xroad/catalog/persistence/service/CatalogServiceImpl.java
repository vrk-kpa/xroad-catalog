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
package fi.vrk.xroad.catalog.persistence.service;

import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

/**
 * Implementation for catalogservice CRUD
 */
@Slf4j
@Component("catalogService")
@Transactional
public class CatalogServiceImpl implements CatalogService {

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
    OrganizationNameRepository organizationNameRepository;

    @Autowired
    OrganizationDescriptionRepository organizationDescriptionRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    PhoneNumberRepository phoneNumberRepository;

    @Autowired
    WebPageRepository webpageRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    StreetAddressRepository streetAddressRepository;

    @Autowired
    PostOfficeBoxAddressRepository postOfficeBoxAddressRepository;

    @Autowired
    StreetAddressMunicipalityRepository streetAddressMunicipalityRepository;

    @Autowired
    PostOfficeBoxAddressMunicipalityRepository postOfficeBoxAddressMunicipalityRepository;

    @Autowired
    StreetAddressMunicipalityNameRepository streetAddressMunicipalityNameRepository;

    @Autowired
    PostOfficeBoxAddressMunicipalityNameRepository postOfficeBoxAddressMunicipalityNameRepository;

    @Autowired
    StreetAddressAdditionalInformationRepository streetAddressAdditionalInformationRepository;

    @Autowired
    PostOfficeBoxAddressAdditionalInformationRepository postOfficeBoxAddressAdditionalInformationRepository;

    @Autowired
    StreetAddressPostOfficeRepository streetAddressPostOfficeRepository;

    @Autowired
    PostOfficeRepository postOfficeRepository;

    @Autowired
    PostOfficeBoxRepository postOfficeBoxRepository;

    @Autowired
    StreetRepository streetRepository;

    @Override
    public Iterable<Member> getActiveMembers() {
        return memberRepository.findAllActive();
    }

    @Override
    public Iterable<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Iterable<Member> getActiveMembers(LocalDateTime changedAfter) {
        return memberRepository.findActiveChangedSince(changedAfter);
    }

    @Override
    public Iterable<Member> getAllMembers(LocalDateTime changedAfter) {
        return memberRepository.findAllChangedSince(changedAfter);
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
    public Service getService(String serviceCode, String subsystemCode) {
        return serviceRepository.findActiveByServiceAndSubsystem(serviceCode, subsystemCode);
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

    @Override
    public void saveServices(SubsystemId subsystemId, Collection<Service> services) {
        assert subsystemId != null;
        Subsystem oldSubsystem = subsystemRepository.findActiveByNaturalKey(subsystemId.getXRoadInstance(),
                subsystemId.getMemberClass(), subsystemId.getMemberCode(),
                subsystemId.getSubsystemCode());
        if (oldSubsystem == null) {
            throw new IllegalStateException("subsystem " + subsystemId + " not found!");
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
            throw new IllegalStateException("service " + serviceId + " not found!");
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
            throw new IllegalStateException("service " + serviceId + " not found!");
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
    public Organization saveOrganization(Organization newValue) {
        Optional<Organization> foundValue = Optional.ofNullable(organizationRepository
                .findByBusinessCode(newValue.getBusinessCode()));
        if (foundValue.isPresent()) {
            Organization oldValue = foundValue.get();
            StatusInfo statusInfo = oldValue.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldValue.equals(newValue)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            newValue.setStatusInfo(statusInfo);
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            newValue.setStatusInfo(statusInfo);
        }
        return organizationRepository.save(newValue);
    }

    @Override
    public void saveOrganizationName(OrganizationName newValue) {
        Optional<List<OrganizationName>> foundList = organizationNameRepository
                .findAnyByOrganizationId(newValue.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            newValue.setStatusInfo(statusInfo);
        }
        organizationNameRepository.save(newValue);
    }

    @Override
    public void saveOrganizationDescription(OrganizationDescription newValue) {
        Optional<List<OrganizationDescription>> foundList = organizationDescriptionRepository
                .findAnyByOrganizationId(newValue.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            newValue.setStatusInfo(statusInfo);
        }
        organizationDescriptionRepository.save(newValue);
    }

    @Override
    public void saveEmail(Email newValue) {
        Optional<List<Email>> foundList = emailRepository
                .findAnyByOrganizationId(newValue.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            newValue.setStatusInfo(statusInfo);
        }
        emailRepository.save(newValue);
    }

    @Override
    public void savePhoneNumber(PhoneNumber newValue) {
        Optional<List<PhoneNumber>> foundList = phoneNumberRepository
                .findAnyByOrganizationId(newValue.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            newValue.setStatusInfo(statusInfo);
        }
        phoneNumberRepository.save(newValue);
    }

    @Override
    public void saveWebPage(WebPage newValue) {
        Optional<List<WebPage>> foundList = webpageRepository
                .findAnyByOrganizationId(newValue.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            newValue.setStatusInfo(statusInfo);
        }
        webpageRepository.save(newValue);
    }

    @Override
    public Address saveAddress(Address newValue) {
        List<Address> foundList = addressRepository
                .findAnyByOrganizationId(newValue.getOrganization().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return addressRepository.save(newValue);
    }

    @Override
    public StreetAddress saveStreetAddress(StreetAddress newValue) {
        Optional<StreetAddress> foundValue = Optional.ofNullable(streetAddressRepository
                .findByAddressId(newValue.getAddress().getId()));
        if (foundValue.isPresent()) {
            StreetAddress oldValue = foundValue.get();
            StatusInfo statusInfo = oldValue.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldValue.equals(newValue)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            newValue.setStatusInfo(statusInfo);
        }
        return streetAddressRepository.save(newValue);
    }

    @Override
    public PostOfficeBoxAddress savePostOfficeBoxAddress(PostOfficeBoxAddress newValue) {
        Optional<PostOfficeBoxAddress> foundValue = Optional.ofNullable(postOfficeBoxAddressRepository
                .findByAddressId(newValue.getAddress().getId()));
        if (foundValue.isPresent()) {
            PostOfficeBoxAddress oldValue = foundValue.get();
            StatusInfo statusInfo = oldValue.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldValue.equals(newValue)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            newValue.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressRepository.save(newValue);
    }

    @Override
    public StreetAddressMunicipality saveStreetAddressMunicipality(StreetAddressMunicipality newValue) {
        Optional<StreetAddressMunicipality> foundValue =
                Optional.ofNullable(streetAddressMunicipalityRepository
                        .findByStreetAddressId(newValue.getStreetAddress().getId()));
        if (foundValue.isPresent()) {
            StreetAddressMunicipality oldValue = foundValue.get();
            StatusInfo statusInfo = oldValue.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldValue.equals(newValue)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            newValue.setStatusInfo(statusInfo);
        }
        return streetAddressMunicipalityRepository.save(newValue);
    }

    @Override
    public PostOfficeBoxAddressMunicipality savePostOfficeBoxAddressMunicipality(PostOfficeBoxAddressMunicipality newValue) {
        Optional<PostOfficeBoxAddressMunicipality> foundValue =
                Optional.ofNullable(postOfficeBoxAddressMunicipalityRepository
                        .findByPostOfficeBoxAddressId(newValue.getPostOfficeBoxAddress().getId()));
        if (foundValue.isPresent()) {
            PostOfficeBoxAddressMunicipality oldValue = foundValue.get();
            StatusInfo statusInfo = oldValue.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldValue.equals(newValue)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            newValue.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressMunicipalityRepository.save(newValue);
    }

    @Override
    public StreetAddressMunicipalityName saveStreetAddressMunicipalityName(StreetAddressMunicipalityName newValue) {
        List<StreetAddressMunicipalityName> foundList = streetAddressMunicipalityNameRepository
                .findAnyByStreetAddressMunicipalityId(newValue.getStreetAddressMunicipality().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return streetAddressMunicipalityNameRepository.save(newValue);
    }

    @Override
    public PostOfficeBoxAddressMunicipalityName savePostOfficeBoxAddressMunicipalityName(PostOfficeBoxAddressMunicipalityName newValue) {
        List<PostOfficeBoxAddressMunicipalityName> foundList = postOfficeBoxAddressMunicipalityNameRepository
                .findAnyByPostOfficeBoxAddressMunicipalityId(newValue.getPostOfficeBoxAddressMunicipality().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return postOfficeBoxAddressMunicipalityNameRepository.save(newValue);
    }

    @Override
    public StreetAddressAdditionalInformation saveStreetAddressAdditionalInformation(StreetAddressAdditionalInformation newValue) {
        List<StreetAddressAdditionalInformation> foundList = streetAddressAdditionalInformationRepository
                .findAnyByStreetAddressId(newValue.getStreetAddress().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return streetAddressAdditionalInformationRepository.save(newValue);
    }

    @Override
    public PostOfficeBoxAddressAdditionalInformation savePostOfficeBoxStreetAddressAdditionalInformation(
            PostOfficeBoxAddressAdditionalInformation newValue) {
        List<PostOfficeBoxAddressAdditionalInformation> foundList =
                postOfficeBoxAddressAdditionalInformationRepository
                        .findAnyByPostOfficeBoxAddressId(newValue.getPostOfficeBoxAddress().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return postOfficeBoxAddressAdditionalInformationRepository.save(newValue);
    }

    @Override
    public StreetAddressPostOffice saveStreetAddressPostOffice(StreetAddressPostOffice newValue) {
        List<StreetAddressPostOffice> foundList = streetAddressPostOfficeRepository
                .findAnyByStreetAddressId(newValue.getStreetAddress().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return streetAddressPostOfficeRepository.save(newValue);
    }

    @Override
    public PostOffice savePostOffice(PostOffice newValue) {
        List<PostOffice> foundList = postOfficeRepository
                .findAnyByPostOfficeBoxAddressId(newValue.getPostOfficeBoxAddress().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return postOfficeRepository.save(newValue);
    }

    @Override
    public PostOfficeBox savePostOfficeBox(PostOfficeBox newValue) {
        List<PostOfficeBox> foundList = postOfficeBoxRepository
                .findAnyByPostOfficeBoxAddressId(newValue.getPostOfficeBoxAddress().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return postOfficeBoxRepository.save(newValue);
    }

    @Override
    public Street saveStreet(Street newValue) {
        List<Street> foundList = streetRepository
                .findAnyByStreetAddressId(newValue.getStreetAddress().getId());
        if (!foundList.isEmpty()) {
            foundList.forEach(oldValue -> {
                StatusInfo statusInfo = oldValue.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldValue.equals(newValue)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                newValue.setStatusInfo(statusInfo);
            });
        }
        return streetRepository.save(newValue);
    }

}
