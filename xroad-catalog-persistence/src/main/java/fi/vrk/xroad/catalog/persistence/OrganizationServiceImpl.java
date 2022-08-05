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

import fi.vrk.xroad.catalog.persistence.entity.*;
import fi.vrk.xroad.catalog.persistence.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation for organizationservice CRUD
 */
@Slf4j
@Component("organizationService")
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

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
    public Iterable<Organization> getOrganizations(String businessCode) {
        return organizationRepository.findAllByBusinessCode(businessCode);
    }

    @Override
    public Optional<Organization> getOrganization(String guid) {
        return organizationRepository.findAnyByOrganizationGuid(guid);
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
            organization.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            organizationName.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            organizationDescription.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            email.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            phoneNumber.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            webPage.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            address.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            streetAddress.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            streetAddressMunicipality.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            streetAddressMunicipalityName.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            postOfficeBoxAddress.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            streetAddressAdditionalInformation.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            streetAddressPostOffice.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            street.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            postOffice.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            postOfficeBox.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            postOfficeBoxAddressAdditionalInformation.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            postOfficeBoxAddressMunicipality.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            postOfficeBoxAddressMunicipalityName.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
        }
        return postOfficeBoxAddressMunicipalityName;
    }
}
