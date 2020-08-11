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
package fi.vrk.xroad.catalog.persistence;

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

    @Autowired
    BusinessAddressRepository businessAddressRepository;

    @Autowired
    BusinessAuxiliaryNameRepository businessAuxiliaryNameRepository;

    @Autowired
    BusinessIdChangeRepository businessIdChangeRepository;

    @Autowired
    BusinessLineRepository businessLineRepository;

    @Autowired
    BusinessNameRepository businessNameRepository;

    @Autowired
    CompanyFormRepository companyFormRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ContactDetailRepository contactDetailRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    LiquidationRepository liquidationRepository;

    @Autowired
    RegisteredEntryRepository registeredEntryRepository;

    @Autowired
    RegisteredOfficeRepository registeredOfficeRepository;

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
    public Iterable<Company> getCompanies(String businessId) {
        return companyRepository.findAllByBusinessId(businessId);
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
        if (serviceVersion == null || serviceVersion.isEmpty()) {
            return serviceRepository.findAllByMemberServiceAndSubsystemVersionNull(xRoadInstance,
                    memberClass, memberCode, serviceCode, subsystemCode);
        }
        return serviceRepository.findAllByMemberServiceAndSubsystemAndVersion(xRoadInstance,
                memberClass, memberCode, serviceCode, subsystemCode, serviceVersion);
    }

    @Override
    public Optional<Organization> getOrganization(String guid) {
        return organizationRepository.findAnyByOrganizationGuid(guid);
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
    @Transactional
    public Organization saveOrganization(Organization organization) {
        Optional<Organization> foundOrganization = organizationRepository.findAnyByOrganizationGuid(organization.getGuid());
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
        organizationNameRepository.save(updateOrganizationNameData(organizationName));
    }

    @Override
    public void saveOrganizationDescription(OrganizationDescription organizationDescription) {
        organizationDescriptionRepository.save(updateOrganizationDescriptionData(organizationDescription));
    }

    @Override
    public void saveEmail(Email email) {
        emailRepository.save(updateEmailData(email));
    }

    @Override
    public void savePhoneNumber(PhoneNumber phoneNumber) {
        phoneNumberRepository.save(updatePhoneNumberData(phoneNumber));
    }

    @Override
    public void saveWebPage(WebPage webPage) {
        webpageRepository.save(updateWebPageData(webPage));
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(updateAddressData(address));
    }

    @Override
    public StreetAddress saveStreetAddress(StreetAddress streetAddress) {
        return streetAddressRepository.save(updateStreetAddressData(streetAddress));
    }

    @Override
    public PostOfficeBoxAddress savePostOfficeBoxAddress(PostOfficeBoxAddress postOfficeBoxAddress) {
        return postOfficeBoxAddressRepository.save(updatePostOfficeBoxAddressData(postOfficeBoxAddress));
    }

    @Override
    public StreetAddressMunicipality saveStreetAddressMunicipality(StreetAddressMunicipality streetAddressMunicipality) {
        return streetAddressMunicipalityRepository.save(updateStreetAddressMunicipalityData(streetAddressMunicipality));
    }

    @Override
    public StreetAddressMunicipalityName saveStreetAddressMunicipalityName(
            StreetAddressMunicipalityName streetAddressMunicipalityName) {
        return streetAddressMunicipalityNameRepository.save(updateStreetAddressMunicipalityNameData(streetAddressMunicipalityName));
    }

    @Override
    public StreetAddressAdditionalInformation saveStreetAddressAdditionalInformation(
            StreetAddressAdditionalInformation streetAddressAdditionalInformation) {
        return streetAddressAdditionalInformationRepository.save(updateStreetAddressAdditionalInformationData(streetAddressAdditionalInformation));
    }

    @Override
    public StreetAddressPostOffice saveStreetAddressPostOffice(StreetAddressPostOffice streetAddressPostOffice) {
        return streetAddressPostOfficeRepository.save(updateStreetAddressPostOfficeData(streetAddressPostOffice));
    }

    @Override
    public Street saveStreet(Street street) {
        return streetRepository.save(updateStreetAddressStreetData(street));
    }

    @Override
    public PostOfficeBoxAddressMunicipality savePostOfficeBoxAddressMunicipality(PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality) {
        return postOfficeBoxAddressMunicipalityRepository.save(updatePostOfficeBoxAddressMunicipalityData(postOfficeBoxAddressMunicipality));
    }

    @Override
    public PostOfficeBoxAddressMunicipalityName savePostOfficeBoxAddressMunicipalityName(
            PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName) {
        return postOfficeBoxAddressMunicipalityNameRepository.save(updatePostOfficeBoxAddressMunicipalityNameData(postOfficeBoxAddressMunicipalityName));
    }

    @Override
    public PostOfficeBoxAddressAdditionalInformation savePostOfficeBoxAddressAdditionalInformation(
            PostOfficeBoxAddressAdditionalInformation postOfficeBoxAddressAdditionalInformation) {
        return postOfficeBoxAddressAdditionalInformationRepository.save(
                updatePostOfficeBoxAddressAdditionalInformationData(postOfficeBoxAddressAdditionalInformation));
    }

    @Override
    public PostOffice savePostOffice(PostOffice postOffice) {
        return postOfficeRepository.save(updatePostOfficeData(postOffice));
    }

    @Override
    public PostOfficeBox savePostOfficeBox(PostOfficeBox postOfficeBox) {
        return postOfficeBoxRepository.save(updatePostOfficeBoxData(postOfficeBox));
    }

    @Override
    public Company saveCompany(Company company) {
        Optional<Company> foundCompany = companyRepository.findAny(company.getBusinessId(), company.getCompanyForm(), company.getName());
        if (foundCompany.isPresent()) {
            Company oldCompany = foundCompany.get();
            StatusInfo statusInfo = oldCompany.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldCompany.equals(company)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            company.setStatusInfo(statusInfo);
            company.setId(oldCompany.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            company.setStatusInfo(statusInfo);
        }
        return companyRepository.save(company);
    }

    @Override
    public void saveBusinessName(BusinessName businessName) {
        businessNameRepository.save(updateBusinessNameData(businessName));
    }

    @Override
    public void saveBusinessAuxiliaryName(BusinessAuxiliaryName businessAuxiliaryName) {
        businessAuxiliaryNameRepository.save(updateBusinessAuxiliaryNameData(businessAuxiliaryName));
    }

    @Override
    public void saveBusinessAddress(BusinessAddress businessAddress) {
        businessAddressRepository.save(updateBusinessAddressData(businessAddress));
    }

    @Override
    public void saveBusinessIdChange(BusinessIdChange businessIdChange) {
        businessIdChangeRepository.save(updateBusinessIdChangeData(businessIdChange));
    }

    @Override
    public void saveBusinessLine(BusinessLine businessLine) {
        businessLineRepository.save(updateBusinessLineData(businessLine));
    }

    @Override
    public void saveCompanyForm(CompanyForm companyForm) {
        companyFormRepository.save(updateCompanyFormData(companyForm));
    }

    @Override
    public void saveContactDetail(ContactDetail contactDetail) {
        contactDetailRepository.save(updateContactDetailData(contactDetail));
    }

    @Override
    public void saveLanguage(Language language) {
        languageRepository.save(updateLanguageData(language));
    }

    @Override
    public void saveLiquidation(Liquidation liquidation) {
        liquidationRepository.save(updateLiquidationData(liquidation));
    }

    @Override
    public void saveRegisteredEntry(RegisteredEntry registeredEntry) {
        registeredEntryRepository.save(updateRegisteredEntryData(registeredEntry));
    }

    @Override
    public void saveRegisteredOffice(RegisteredOffice registeredOffice) {
        registeredOfficeRepository.save(updateRegisteredOfficeData(registeredOffice));
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

    private OrganizationName updateOrganizationNameData(OrganizationName organizationName) {
        Optional<OrganizationName> foundOrganizationName = organizationNameRepository
                .findAny(organizationName.getOrganization().getId(), organizationName.getLanguage(), organizationName.getType());
        if (foundOrganizationName.isPresent()) {
            OrganizationName oldOrganizationName = foundOrganizationName.get();
            StatusInfo statusInfo = oldOrganizationName.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldOrganizationName.equals(organizationName)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            organizationName.setStatusInfo(statusInfo);
            organizationName.setId(oldOrganizationName.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            organizationName.setStatusInfo(statusInfo);
        }
        return organizationName;
    }

    private OrganizationDescription updateOrganizationDescriptionData(OrganizationDescription organizationDescription) {
        Optional<OrganizationDescription> foundOrganizationDescription = organizationDescriptionRepository
                .findAny(organizationDescription.getOrganization().getId(), organizationDescription.getLanguage(), organizationDescription.getType());
        if (foundOrganizationDescription.isPresent()) {
            OrganizationDescription oldOrganizationDescription = foundOrganizationDescription.get();
            StatusInfo statusInfo = oldOrganizationDescription.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldOrganizationDescription.equals(organizationDescription)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            organizationDescription.setStatusInfo(statusInfo);
            organizationDescription.setId(oldOrganizationDescription.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            organizationDescription.setStatusInfo(statusInfo);
        }
        return organizationDescription;
    }

    private Email updateEmailData(Email email) {
        Optional<Email> foundEmail = emailRepository.findAny(email.getOrganization().getId(),
                email.getLanguage(), email.getValue(), email.getDescription());
        if (foundEmail.isPresent()) {
            Email oldEmail = foundEmail.get();
            StatusInfo statusInfo = oldEmail.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldEmail.equals(email)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            email.setStatusInfo(statusInfo);
            email.setId(oldEmail.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            email.setStatusInfo(statusInfo);
        }
        return email;
    }

    private PhoneNumber updatePhoneNumberData(PhoneNumber phoneNumber) {
        Optional<PhoneNumber> foundPhoneNumber = phoneNumberRepository.findAny(phoneNumber.getOrganization().getId(),
                phoneNumber.getNumber(), phoneNumber.getAdditionalInformation(), phoneNumber.getLanguage());
        if (foundPhoneNumber.isPresent()) {
            PhoneNumber oldPhoneNumber = foundPhoneNumber.get();
            StatusInfo statusInfo = oldPhoneNumber.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldPhoneNumber.equals(phoneNumber)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            phoneNumber.setStatusInfo(statusInfo);
            phoneNumber.setId(oldPhoneNumber.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            phoneNumber.setStatusInfo(statusInfo);
        }
        return phoneNumber;
    }

    private WebPage updateWebPageData(WebPage webPage) {
        Optional<WebPage> foundWebPage = webpageRepository.findAny(webPage.getOrganization().getId(), webPage.getLanguage(), webPage.getUrl());
        if (foundWebPage.isPresent()) {
            WebPage oldWebPage = foundWebPage.get();
            StatusInfo statusInfo = oldWebPage.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldWebPage.equals(webPage)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            webPage.setStatusInfo(statusInfo);
            webPage.setId(oldWebPage.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            webPage.setStatusInfo(statusInfo);
        }
        return webPage;
    }

    private Address updateAddressData(Address address) {
        Optional<Address> foundAddress = addressRepository.findAny(address.getOrganization().getId(), address.getType(), address.getSubType());
        if (foundAddress.isPresent()) {
            Address oldAddress = foundAddress.get();
            StatusInfo statusInfo = oldAddress.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldAddress.equals(address)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            address.setStatusInfo(statusInfo);
            address.setId(oldAddress.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            address.setStatusInfo(statusInfo);
        }
        return address;
    }

    private StreetAddress updateStreetAddressData(StreetAddress streetAddress) {
        Optional<StreetAddress> foundStreetAddress = Optional.ofNullable(streetAddressRepository.findByAddressId(streetAddress.getAddress().getId()));
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
        return streetAddress;
    }

    private StreetAddressMunicipality updateStreetAddressMunicipalityData(StreetAddressMunicipality streetAddressMunicipality) {
        Optional<StreetAddressMunicipality> foundStreetAddressMunicipality =
                Optional.ofNullable(streetAddressMunicipalityRepository.findByStreetAddressId(streetAddressMunicipality.getStreetAddress().getId()));
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
        return streetAddressMunicipality;
    }

    private StreetAddressMunicipalityName updateStreetAddressMunicipalityNameData(
            StreetAddressMunicipalityName streetAddressMunicipalityName) {
        Optional<StreetAddressMunicipalityName> foundStreetAddressMunicipalityName = streetAddressMunicipalityNameRepository
                .findAny(streetAddressMunicipalityName.getStreetAddressMunicipality().getId(), streetAddressMunicipalityName.getLanguage());
        if (foundStreetAddressMunicipalityName.isPresent()) {
            StreetAddressMunicipalityName oldStreetAddressMunicipalityName = foundStreetAddressMunicipalityName.get();
            StatusInfo statusInfo = oldStreetAddressMunicipalityName.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldStreetAddressMunicipalityName.equals(streetAddressMunicipalityName)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            streetAddressMunicipalityName.setStatusInfo(statusInfo);
            streetAddressMunicipalityName.setId(oldStreetAddressMunicipalityName.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddressMunicipalityName.setStatusInfo(statusInfo);
        }
        return streetAddressMunicipalityName;
    }

    private PostOfficeBoxAddress updatePostOfficeBoxAddressData(PostOfficeBoxAddress postOfficeBoxAddress) {
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
        return postOfficeBoxAddress;
    }

    private StreetAddressAdditionalInformation updateStreetAddressAdditionalInformationData(
            StreetAddressAdditionalInformation streetAddressAdditionalInformation) {
        Optional<StreetAddressAdditionalInformation> foundStreetAddressAdditionalInformation = streetAddressAdditionalInformationRepository
                .findAny(streetAddressAdditionalInformation.getStreetAddress().getId(), streetAddressAdditionalInformation.getLanguage());
        if (foundStreetAddressAdditionalInformation.isPresent()) {
            StreetAddressAdditionalInformation oldStreetAddressAdditionalInformation = foundStreetAddressAdditionalInformation.get();
            StatusInfo statusInfo = oldStreetAddressAdditionalInformation.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldStreetAddressAdditionalInformation.equals(streetAddressAdditionalInformation)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            streetAddressAdditionalInformation.setStatusInfo(statusInfo);
            streetAddressAdditionalInformation.setId(oldStreetAddressAdditionalInformation.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddressAdditionalInformation.setStatusInfo(statusInfo);
        }
        return streetAddressAdditionalInformation;
    }

    private StreetAddressPostOffice updateStreetAddressPostOfficeData(
            StreetAddressPostOffice streetAddressPostOffice) {
        Optional<StreetAddressPostOffice> foundStreetAddressPostOffice = streetAddressPostOfficeRepository
                .findAny(streetAddressPostOffice.getStreetAddress().getId(), streetAddressPostOffice.getLanguage());
        if (foundStreetAddressPostOffice.isPresent()) {
            StreetAddressPostOffice oldStreetAddressPostOffice = foundStreetAddressPostOffice.get();
            StatusInfo statusInfo = oldStreetAddressPostOffice.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldStreetAddressPostOffice.equals(streetAddressPostOffice)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            streetAddressPostOffice.setStatusInfo(statusInfo);
            streetAddressPostOffice.setId(oldStreetAddressPostOffice.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            streetAddressPostOffice.setStatusInfo(statusInfo);
        }
        return streetAddressPostOffice;
    }

    private Street updateStreetAddressStreetData(Street street) {
        Optional<Street> foundStreet = streetRepository
                .findAny(street.getStreetAddress().getId(), street.getLanguage());
        if (foundStreet.isPresent()) {
            Street oldStreet = foundStreet.get();
            StatusInfo statusInfo = oldStreet.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldStreet.equals(street)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            street.setStatusInfo(statusInfo);
            street.setId(oldStreet.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            street.setStatusInfo(statusInfo);
        }
        return street;
    }

    private PostOffice updatePostOfficeData(PostOffice postOffice) {
        Optional<PostOffice> foundPostOffice = postOfficeRepository
                .findAny(postOffice.getPostOfficeBoxAddress().getId(), postOffice.getLanguage());
        if (foundPostOffice.isPresent()) {
            PostOffice oldPostOffice = foundPostOffice.get();
            StatusInfo statusInfo = oldPostOffice.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldPostOffice.equals(postOffice)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            postOffice.setStatusInfo(statusInfo);
            postOffice.setId(oldPostOffice.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOffice.setStatusInfo(statusInfo);
        }
        return postOffice;
    }

    private PostOfficeBox updatePostOfficeBoxData(PostOfficeBox postOfficeBox) {
        Optional<PostOfficeBox> foundPostOfficeBox = postOfficeBoxRepository
                .findAny(postOfficeBox.getPostOfficeBoxAddress().getId(), postOfficeBox.getLanguage());
        if (foundPostOfficeBox.isPresent()) {
            PostOfficeBox oldPostOfficeBox = foundPostOfficeBox.get();
            StatusInfo statusInfo = oldPostOfficeBox.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldPostOfficeBox.equals(postOfficeBox)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            postOfficeBox.setStatusInfo(statusInfo);
            postOfficeBox.setId(oldPostOfficeBox.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBox.setStatusInfo(statusInfo);
        }
        return postOfficeBox;
    }

    private PostOfficeBoxAddressAdditionalInformation updatePostOfficeBoxAddressAdditionalInformationData(
            PostOfficeBoxAddressAdditionalInformation postOfficeBoxAddressAdditionalInformation) {
        Optional<PostOfficeBoxAddressAdditionalInformation> foundPostOfficeBoxAddressAdditionalInformation
                = postOfficeBoxAddressAdditionalInformationRepository.findAny(
                postOfficeBoxAddressAdditionalInformation.getPostOfficeBoxAddress().getId(),
                postOfficeBoxAddressAdditionalInformation.getLanguage());
        if (foundPostOfficeBoxAddressAdditionalInformation.isPresent()) {
            PostOfficeBoxAddressAdditionalInformation oldPostOfficeBoxAddressAdditionalInformation
                    = foundPostOfficeBoxAddressAdditionalInformation.get();
            StatusInfo statusInfo = oldPostOfficeBoxAddressAdditionalInformation.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldPostOfficeBoxAddressAdditionalInformation.equals(postOfficeBoxAddressAdditionalInformation)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            postOfficeBoxAddressAdditionalInformation.setStatusInfo(statusInfo);
            postOfficeBoxAddressAdditionalInformation.setId(oldPostOfficeBoxAddressAdditionalInformation.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBoxAddressAdditionalInformation.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressAdditionalInformation;
    }

    private PostOfficeBoxAddressMunicipality updatePostOfficeBoxAddressMunicipalityData(
            PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality) {
        Optional<PostOfficeBoxAddressMunicipality> foundPostOfficeBoxAddressMunicipality =
                Optional.ofNullable(postOfficeBoxAddressMunicipalityRepository
                        .findByPostOfficeBoxAddressId(postOfficeBoxAddressMunicipality.getPostOfficeBoxAddress().getId()));
        if (foundPostOfficeBoxAddressMunicipality.isPresent()) {
            PostOfficeBoxAddressMunicipality oldPostOfficeBoxAddressMunicipality = foundPostOfficeBoxAddressMunicipality.get();
            StatusInfo statusInfo = oldPostOfficeBoxAddressMunicipality.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldPostOfficeBoxAddressMunicipality.equals(postOfficeBoxAddressMunicipality)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            postOfficeBoxAddressMunicipality.setStatusInfo(statusInfo);
            postOfficeBoxAddressMunicipality.setId(oldPostOfficeBoxAddressMunicipality.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBoxAddressMunicipality.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressMunicipality;
    }

    private PostOfficeBoxAddressMunicipalityName updatePostOfficeBoxAddressMunicipalityNameData(
            PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName) {
        Optional<PostOfficeBoxAddressMunicipalityName> foundPostOfficeBoxAddressMunicipalityName
                = postOfficeBoxAddressMunicipalityNameRepository.findAny(
                postOfficeBoxAddressMunicipalityName.getPostOfficeBoxAddressMunicipality().getId(),
                postOfficeBoxAddressMunicipalityName.getLanguage());
        if (foundPostOfficeBoxAddressMunicipalityName.isPresent()) {
            PostOfficeBoxAddressMunicipalityName oldPostOfficeBoxAddressMunicipalityName
                    = foundPostOfficeBoxAddressMunicipalityName.get();
            StatusInfo statusInfo = oldPostOfficeBoxAddressMunicipalityName.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldPostOfficeBoxAddressMunicipalityName.equals(postOfficeBoxAddressMunicipalityName)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            postOfficeBoxAddressMunicipalityName.setStatusInfo(statusInfo);
            postOfficeBoxAddressMunicipalityName.setId(oldPostOfficeBoxAddressMunicipalityName.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            postOfficeBoxAddressMunicipalityName.setStatusInfo(statusInfo);
        }
        return postOfficeBoxAddressMunicipalityName;
    }

    private BusinessName updateBusinessNameData(BusinessName businessName) {
        Optional<BusinessName> foundBusinessName = businessNameRepository.findAny(
                businessName.getCompany().getId(),
                businessName.getLanguage(),
                businessName.getSource(),
                businessName.getOrdering(),
                businessName.getVersion());
        if (foundBusinessName.isPresent()) {
            BusinessName oldBusinessName = foundBusinessName.get();
            StatusInfo statusInfo = oldBusinessName.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldBusinessName.equals(businessName)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            businessName.setStatusInfo(statusInfo);
            businessName.setId(oldBusinessName.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            businessName.setStatusInfo(statusInfo);
        }
        return businessName;
    }

    private BusinessAuxiliaryName updateBusinessAuxiliaryNameData(BusinessAuxiliaryName businessAuxiliaryName) {
        Optional<BusinessAuxiliaryName> foundBusinessAuxiliaryName = businessAuxiliaryNameRepository.findAny(
                businessAuxiliaryName.getCompany().getId(),
                businessAuxiliaryName.getLanguage(),
                businessAuxiliaryName.getSource(),
                businessAuxiliaryName.getOrdering(),
                businessAuxiliaryName.getVersion());
        if (foundBusinessAuxiliaryName.isPresent()) {
            BusinessAuxiliaryName oldBusinessAuxiliaryName = foundBusinessAuxiliaryName.get();
            StatusInfo statusInfo = oldBusinessAuxiliaryName.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldBusinessAuxiliaryName.equals(businessAuxiliaryName)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            businessAuxiliaryName.setStatusInfo(statusInfo);
            businessAuxiliaryName.setId(oldBusinessAuxiliaryName.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            businessAuxiliaryName.setStatusInfo(statusInfo);
        }
        return businessAuxiliaryName;
    }

    private BusinessAddress updateBusinessAddressData(BusinessAddress businessAddress) {
        Optional<BusinessAddress> foundAddress = businessAddressRepository.findAny(
                businessAddress.getCompany().getId(),
                businessAddress.getLanguage(),
                businessAddress.getType(),
                businessAddress.getSource(),
                businessAddress.getVersion());
        if (foundAddress.isPresent()) {
            BusinessAddress oldBusinessAddress = foundAddress.get();
            StatusInfo statusInfo = oldBusinessAddress.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldBusinessAddress.equals(businessAddress)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            businessAddress.setStatusInfo(statusInfo);
            businessAddress.setId(oldBusinessAddress.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            businessAddress.setStatusInfo(statusInfo);
        }
        return businessAddress;
    }

    private BusinessIdChange updateBusinessIdChangeData(BusinessIdChange businessIdChange) {
        Optional<BusinessIdChange> foundBusinessIdChange = businessIdChangeRepository.findAny(
                businessIdChange.getCompany().getId(),
                businessIdChange.getLanguage(),
                businessIdChange.getSource(),
                businessIdChange.getChange(),
                businessIdChange.getOldBusinessId(),
                businessIdChange.getNewBusinessId());
        if (foundBusinessIdChange.isPresent()) {
            BusinessIdChange oldBusinessIdChange = foundBusinessIdChange.get();
            StatusInfo statusInfo = oldBusinessIdChange.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldBusinessIdChange.equals(businessIdChange)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            businessIdChange.setStatusInfo(statusInfo);
            businessIdChange.setId(oldBusinessIdChange.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            businessIdChange.setStatusInfo(statusInfo);
        }
        return businessIdChange;
    }

    private BusinessLine updateBusinessLineData(BusinessLine businessLine) {
        Optional<BusinessLine> foundBusinessLine = businessLineRepository.findAny(
                businessLine.getCompany().getId(),
                businessLine.getLanguage(),
                businessLine.getSource(),
                businessLine.getOrdering(),
                businessLine.getVersion());
        if (foundBusinessLine.isPresent()) {
            BusinessLine oldBusinessLine = foundBusinessLine.get();
            StatusInfo statusInfo = oldBusinessLine.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldBusinessLine.equals(businessLine)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            businessLine.setStatusInfo(statusInfo);
            businessLine.setId(oldBusinessLine.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            businessLine.setStatusInfo(statusInfo);
        }
        return businessLine;
    }

    private CompanyForm updateCompanyFormData(CompanyForm companyForm) {
        Optional<CompanyForm> foundCompanyForm = companyFormRepository.findAny(
                companyForm.getCompany().getId(),
                companyForm.getLanguage(),
                companyForm.getSource(),
                companyForm.getType(),
                companyForm.getVersion());
        if (foundCompanyForm.isPresent()) {
            CompanyForm oldCompanyForm = foundCompanyForm.get();
            StatusInfo statusInfo = oldCompanyForm.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldCompanyForm.equals(companyForm)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            companyForm.setStatusInfo(statusInfo);
            companyForm.setId(oldCompanyForm.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            companyForm.setStatusInfo(statusInfo);
        }
        return companyForm;
    }

    private ContactDetail updateContactDetailData(ContactDetail contactDetail) {
        Optional<ContactDetail> foundContactDetail = contactDetailRepository.findAny(
                contactDetail.getCompany().getId(),
                contactDetail.getLanguage(),
                contactDetail.getSource(),
                contactDetail.getType(),
                contactDetail.getVersion());
        if (foundContactDetail.isPresent()) {
            ContactDetail oldContactDetail = foundContactDetail.get();
            StatusInfo statusInfo = oldContactDetail.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldContactDetail.equals(contactDetail)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            contactDetail.setStatusInfo(statusInfo);
            contactDetail.setId(oldContactDetail.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            contactDetail.setStatusInfo(statusInfo);
        }
        return contactDetail;
    }

    private Language updateLanguageData(Language language) {
        Optional<Language> foundLanguage = languageRepository.findAny(
                language.getCompany().getId(),
                language.getLanguage(),
                language.getSource(),
                language.getVersion());
        if (foundLanguage.isPresent()) {
            Language oldLanguage = foundLanguage.get();
            StatusInfo statusInfo = oldLanguage.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldLanguage.equals(language)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            language.setStatusInfo(statusInfo);
            language.setId(oldLanguage.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            language.setStatusInfo(statusInfo);
        }
        return language;
    }

    private Liquidation updateLiquidationData(Liquidation liquidation) {
        Optional<Liquidation> foundLiquidation = liquidationRepository.findAny(
                liquidation.getCompany().getId(),
                liquidation.getLanguage(),
                liquidation.getSource(),
                liquidation.getType(),
                liquidation.getVersion());
        if (foundLiquidation.isPresent()) {
            Liquidation oldLiquidation = foundLiquidation.get();
            StatusInfo statusInfo = oldLiquidation.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldLiquidation.equals(liquidation)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            liquidation.setStatusInfo(statusInfo);
            liquidation.setId(oldLiquidation.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            liquidation.setStatusInfo(statusInfo);
        }
        return liquidation;
    }

    private RegisteredEntry updateRegisteredEntryData(RegisteredEntry registeredEntry) {
        Optional<RegisteredEntry> foundRegisteredEntry = registeredEntryRepository.findAny(
                registeredEntry.getCompany().getId(),
                registeredEntry.getLanguage(),
                registeredEntry.getAuthority(),
                registeredEntry.getRegister(),
                registeredEntry.getStatus(),
                registeredEntry.getDescription());
        if (foundRegisteredEntry.isPresent()) {
            RegisteredEntry oldRegisteredEntry = foundRegisteredEntry.get();
            StatusInfo statusInfo = oldRegisteredEntry.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldRegisteredEntry.equals(registeredEntry)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            registeredEntry.setStatusInfo(statusInfo);
            registeredEntry.setId(oldRegisteredEntry.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            registeredEntry.setStatusInfo(statusInfo);
        }
        return registeredEntry;
    }

    private RegisteredOffice updateRegisteredOfficeData(RegisteredOffice registeredOffice) {
        Optional<RegisteredOffice> foundRegisteredOffice = registeredOfficeRepository.findAny(
                registeredOffice.getCompany().getId(),
                registeredOffice.getLanguage(),
                registeredOffice.getOrdering(),
                registeredOffice.getVersion(),
                registeredOffice.getSource());
        if (foundRegisteredOffice.isPresent()) {
            RegisteredOffice oldRegisteredOffice = foundRegisteredOffice.get();
            StatusInfo statusInfo = oldRegisteredOffice.getStatusInfo();
            statusInfo.setFetched(LocalDateTime.now());
            if (!oldRegisteredOffice.equals(registeredOffice)) {
                statusInfo.setChanged(LocalDateTime.now());
            }
            registeredOffice.setStatusInfo(statusInfo);
            registeredOffice.setId(oldRegisteredOffice.getId());
        } else {
            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setCreated(LocalDateTime.now());
            statusInfo.setChanged(LocalDateTime.now());
            statusInfo.setFetched(LocalDateTime.now());
            registeredOffice.setStatusInfo(statusInfo);
        }
        return registeredOffice;
    }

}
