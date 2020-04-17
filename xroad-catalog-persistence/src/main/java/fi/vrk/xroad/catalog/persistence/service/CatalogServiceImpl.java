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

    private int orgCount = 0;

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
    public Iterable<Organization> getOrganizations(String businessCode) {
        return organizationRepository.findAllByBusinessCode(businessCode);
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
    public Organization saveOrganization(Organization organization) {
        Optional<Organization> foundOrganization = Optional.ofNullable(organizationRepository
                .findAnyByOrganizationGuid(organization.getGuid()));
        if (foundOrganization.isPresent()) {
            Organization oldOrganization = foundOrganization.get();
            StatusInfo statusInfo = oldOrganization.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldOrganization.equals(organization)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            organization.setStatusInfo(statusInfo);
            organization.setId(oldOrganization.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            organization.setStatusInfo(statusInfo);
        }
        return organizationRepository.save(organization);
    }

    @Override
    public void saveOrganizationName(OrganizationName organizationName) {
        Optional<List<OrganizationName>> foundList = organizationNameRepository
                .findAnyByOrganizationId(organizationName.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldOrganizationName -> {
                StatusInfo statusInfo = oldOrganizationName.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldOrganizationName.equals(organizationName)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                organizationName.setStatusInfo(statusInfo);
                organizationName.setId(oldOrganizationName.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            organizationName.setStatusInfo(statusInfo);
        }
        organizationNameRepository.save(organizationName);
    }

    @Override
    public void saveOrganizationDescription(OrganizationDescription organizationDescription) {
        Optional<List<OrganizationDescription>> foundList = organizationDescriptionRepository
                .findAnyByOrganizationId(organizationDescription.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldOrganizationDescription -> {
                StatusInfo statusInfo = oldOrganizationDescription.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldOrganizationDescription.equals(organizationDescription)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                organizationDescription.setStatusInfo(statusInfo);
                organizationDescription.setId(oldOrganizationDescription.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            organizationDescription.setStatusInfo(statusInfo);
        }
        organizationDescriptionRepository.save(organizationDescription);
    }

    @Override
    public void saveEmail(Email email) {
        Optional<List<Email>> foundList = emailRepository
                .findAnyByOrganizationId(email.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldEmail -> {
                StatusInfo statusInfo = oldEmail.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldEmail.equals(email)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                email.setStatusInfo(statusInfo);
                email.setId(oldEmail.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            email.setStatusInfo(statusInfo);
        }
        emailRepository.save(email);
    }

    @Override
    public void savePhoneNumber(PhoneNumber phoneNumber) {
        Optional<List<PhoneNumber>> foundList = phoneNumberRepository
                .findAnyByOrganizationId(phoneNumber.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldPhoneNumber -> {
                StatusInfo statusInfo = oldPhoneNumber.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldPhoneNumber.equals(phoneNumber)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                phoneNumber.setStatusInfo(statusInfo);
                phoneNumber.setId(oldPhoneNumber.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            phoneNumber.setStatusInfo(statusInfo);
        }
        phoneNumberRepository.save(phoneNumber);
    }

    @Override
    public void saveWebPage(WebPage webPage) {
        Optional<List<WebPage>> foundList = webpageRepository
                .findAnyByOrganizationId(webPage.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldWebPage -> {
                StatusInfo statusInfo = oldWebPage.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldWebPage.equals(webPage)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                webPage.setStatusInfo(statusInfo);
                webPage.setId(oldWebPage.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            webPage.setStatusInfo(statusInfo);
        }
        webpageRepository.save(webPage);
    }

    @Override
    public Address saveAddress(Address address) {
        Optional<List<Address>> foundList = addressRepository
                .findAnyByOrganizationId(address.getOrganization().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldAddress -> {
                StatusInfo statusInfo = oldAddress.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldAddress.equals(address)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                address.setStatusInfo(statusInfo);
                address.setId(oldAddress.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            address.setStatusInfo(statusInfo);
        }
        return addressRepository.save(address);
    }

    @Override
    public StreetAddress saveStreetAddress(StreetAddress streetAddress) {
        Optional<StreetAddress> foundStreetAddress = Optional.ofNullable(streetAddressRepository
                .findByAddressId(streetAddress.getAddress().getId()));
        if (foundStreetAddress.isPresent()) {
            StreetAddress oldStreetAddress = foundStreetAddress.get();
            StatusInfo statusInfo = oldStreetAddress.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldStreetAddress.equals(streetAddress)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            streetAddress.setStatusInfo(statusInfo);
            streetAddress.setId(oldStreetAddress.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddress.setStatusInfo(statusInfo);
        }
        return streetAddressRepository.save(streetAddress);
    }

    @Override
    public PostOfficeBoxAddress savePostOfficeBoxAddress(PostOfficeBoxAddress postOfficeBoxAddress) {
        Optional<PostOfficeBoxAddress> foundPostOfficeBoxAddress = Optional.ofNullable(postOfficeBoxAddressRepository
                .findByAddressId(postOfficeBoxAddress.getAddress().getId()));
        if (foundPostOfficeBoxAddress.isPresent()) {
            PostOfficeBoxAddress oldPostOfficeBoxAddress = foundPostOfficeBoxAddress.get();
            StatusInfo statusInfo = oldPostOfficeBoxAddress.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldPostOfficeBoxAddress.equals(postOfficeBoxAddress)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            postOfficeBoxAddress.setStatusInfo(statusInfo);
            postOfficeBoxAddress.setId(oldPostOfficeBoxAddress.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBoxAddress.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressRepository.save(postOfficeBoxAddress);
    }

    @Override
    public StreetAddressMunicipality saveStreetAddressMunicipality(StreetAddressMunicipality streetAddressMunicipality) {
        Optional<StreetAddressMunicipality> foundStreetAddressMunicipality =
                Optional.ofNullable(streetAddressMunicipalityRepository
                        .findByStreetAddressId(streetAddressMunicipality.getStreetAddress().getId()));
        if (foundStreetAddressMunicipality.isPresent()) {
            StreetAddressMunicipality oldStreetAddressMunicipality = foundStreetAddressMunicipality.get();
            StatusInfo statusInfo = oldStreetAddressMunicipality.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldStreetAddressMunicipality.equals(streetAddressMunicipality)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            streetAddressMunicipality.setStatusInfo(statusInfo);
            streetAddressMunicipality.setId(oldStreetAddressMunicipality.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddressMunicipality.setStatusInfo(statusInfo);
        }
        return streetAddressMunicipalityRepository.save(streetAddressMunicipality);
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
    public StreetAddressMunicipalityName saveStreetAddressMunicipalityName(
            StreetAddressMunicipalityName streetAddressMunicipalityName) {
        Optional<List<StreetAddressMunicipalityName>> foundList = streetAddressMunicipalityNameRepository
                .findAnyByStreetAddressMunicipalityId(streetAddressMunicipalityName.getStreetAddressMunicipality().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldStreetAddressMunicipalityName -> {
                StatusInfo statusInfo = oldStreetAddressMunicipalityName.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldStreetAddressMunicipalityName.equals(streetAddressMunicipalityName)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                streetAddressMunicipalityName.setStatusInfo(statusInfo);
                streetAddressMunicipalityName.setId(oldStreetAddressMunicipalityName.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddressMunicipalityName.setStatusInfo(statusInfo);
        }
        return streetAddressMunicipalityNameRepository.save(streetAddressMunicipalityName);
    }

    @Override
    public PostOfficeBoxAddressMunicipalityName savePostOfficeBoxAddressMunicipalityName(
            PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName) {
        Optional<List<PostOfficeBoxAddressMunicipalityName>> foundList =
                postOfficeBoxAddressMunicipalityNameRepository
                .findAnyByPostOfficeBoxAddressMunicipalityId(postOfficeBoxAddressMunicipalityName.getPostOfficeBoxAddressMunicipality().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldPostOfficeBoxAddressMunicipalityName -> {
                StatusInfo statusInfo = oldPostOfficeBoxAddressMunicipalityName.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldPostOfficeBoxAddressMunicipalityName.equals(postOfficeBoxAddressMunicipalityName)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                postOfficeBoxAddressMunicipalityName.setStatusInfo(statusInfo);
                postOfficeBoxAddressMunicipalityName.setId(oldPostOfficeBoxAddressMunicipalityName.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBoxAddressMunicipalityName.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressMunicipalityNameRepository.save(postOfficeBoxAddressMunicipalityName);
    }

    @Override
    public StreetAddressAdditionalInformation saveStreetAddressAdditionalInformation(
            StreetAddressAdditionalInformation streetAddressAdditionalInformation) {
        Optional<List<StreetAddressAdditionalInformation>> foundList = streetAddressAdditionalInformationRepository
                .findAnyByStreetAddressId(streetAddressAdditionalInformation.getStreetAddress().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldStreetAddressAdditionalInformation -> {
                StatusInfo statusInfo = oldStreetAddressAdditionalInformation.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldStreetAddressAdditionalInformation.equals(streetAddressAdditionalInformation)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                streetAddressAdditionalInformation.setStatusInfo(statusInfo);
                streetAddressAdditionalInformation.setId(oldStreetAddressAdditionalInformation.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddressAdditionalInformation.setStatusInfo(statusInfo);
        }
        return streetAddressAdditionalInformationRepository.save(streetAddressAdditionalInformation);
    }

    @Override
    public PostOfficeBoxAddressAdditionalInformation savePostOfficeBoxAddressAdditionalInformation(
            PostOfficeBoxAddressAdditionalInformation postOfficeBoxAddressAdditionalInformation) {
        Optional<List<PostOfficeBoxAddressAdditionalInformation>> foundList =
                postOfficeBoxAddressAdditionalInformationRepository
                        .findAnyByPostOfficeBoxAddressId(postOfficeBoxAddressAdditionalInformation.getPostOfficeBoxAddress().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldPostOfficeBoxAddressAdditionalInformation -> {
                StatusInfo statusInfo = oldPostOfficeBoxAddressAdditionalInformation.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldPostOfficeBoxAddressAdditionalInformation.equals(postOfficeBoxAddressAdditionalInformation)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                postOfficeBoxAddressAdditionalInformation.setStatusInfo(statusInfo);
                postOfficeBoxAddressAdditionalInformation.setId(oldPostOfficeBoxAddressAdditionalInformation.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBoxAddressAdditionalInformation.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressAdditionalInformationRepository.save(postOfficeBoxAddressAdditionalInformation);
    }

    @Override
    public StreetAddressPostOffice saveStreetAddressPostOffice(StreetAddressPostOffice streetAddressPostOffice) {
        Optional<List<StreetAddressPostOffice>> foundList = streetAddressPostOfficeRepository
                .findAnyByStreetAddressId(streetAddressPostOffice.getStreetAddress().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldStreetAddressPostOffice -> {
                StatusInfo statusInfo = oldStreetAddressPostOffice.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldStreetAddressPostOffice.equals(streetAddressPostOffice)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                streetAddressPostOffice.setStatusInfo(statusInfo);
                streetAddressPostOffice.setId(oldStreetAddressPostOffice.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddressPostOffice.setStatusInfo(statusInfo);
        }
        return streetAddressPostOfficeRepository.save(streetAddressPostOffice);
    }

    @Override
    public PostOffice savePostOffice(PostOffice postOffice) {
        Optional<List<PostOffice>> foundList = postOfficeRepository
                .findAnyByPostOfficeBoxAddressId(postOffice.getPostOfficeBoxAddress().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldPostOffice -> {
                StatusInfo statusInfo = oldPostOffice.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldPostOffice.equals(postOffice)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                postOffice.setStatusInfo(statusInfo);
                postOffice.setId(oldPostOffice.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOffice.setStatusInfo(statusInfo);
        }
        return postOfficeRepository.save(postOffice);
    }

    @Override
    public PostOfficeBox savePostOfficeBox(PostOfficeBox postOfficeBox) {
        Optional<List<PostOfficeBox>> foundList = postOfficeBoxRepository
                .findAnyByPostOfficeBoxAddressId(postOfficeBox.getPostOfficeBoxAddress().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldPostOfficeBox -> {
                StatusInfo statusInfo = oldPostOfficeBox.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldPostOfficeBox.equals(postOfficeBox)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                postOfficeBox.setStatusInfo(statusInfo);
                postOfficeBox.setId(oldPostOfficeBox.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBox.setStatusInfo(statusInfo);
        }
        return postOfficeBoxRepository.save(postOfficeBox);
    }

    @Override
    public Street saveStreet(Street street) {
        Optional<List<Street>> foundList = streetRepository
                .findAnyByStreetAddressId(street.getStreetAddress().getId());
        if (foundList.isPresent() && !foundList.get().isEmpty()) {
            foundList.get().forEach(oldStreet -> {
                StatusInfo statusInfo = oldStreet.getStatusInfo();
                statusInfo.setFetched(LocalDateTime.now());
                if (!oldStreet.equals(street)) {
                    statusInfo.setChanged(LocalDateTime.now());
                }
                street.setStatusInfo(statusInfo);
                street.setId(oldStreet.getId());
            });
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            street.setStatusInfo(statusInfo);
        }
        return streetRepository.save(street);
    }

}
