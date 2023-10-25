/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2016-2023 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.lister.util;

import fi.vrk.xroad.xroad_catalog_lister.Address;
import fi.vrk.xroad.xroad_catalog_lister.BusinessAddress;
import fi.vrk.xroad.xroad_catalog_lister.BusinessAddressList;
import fi.vrk.xroad.xroad_catalog_lister.BusinessAuxiliaryName;
import fi.vrk.xroad.xroad_catalog_lister.BusinessAuxiliaryNameList;
import fi.vrk.xroad.xroad_catalog_lister.BusinessIdChange;
import fi.vrk.xroad.xroad_catalog_lister.BusinessIdChangeList;
import fi.vrk.xroad.xroad_catalog_lister.BusinessLine;
import fi.vrk.xroad.xroad_catalog_lister.BusinessLineList;
import fi.vrk.xroad.xroad_catalog_lister.BusinessName;
import fi.vrk.xroad.xroad_catalog_lister.BusinessNameList;
import fi.vrk.xroad.xroad_catalog_lister.Company;
import fi.vrk.xroad.xroad_catalog_lister.CompanyForm;
import fi.vrk.xroad.xroad_catalog_lister.CompanyFormList;
import fi.vrk.xroad.xroad_catalog_lister.ContactDetail;
import fi.vrk.xroad.xroad_catalog_lister.ContactDetailList;
import fi.vrk.xroad.xroad_catalog_lister.Email;
import fi.vrk.xroad.xroad_catalog_lister.Language;
import fi.vrk.xroad.xroad_catalog_lister.LanguageList;
import fi.vrk.xroad.xroad_catalog_lister.Liquidation;
import fi.vrk.xroad.xroad_catalog_lister.LiquidationList;
import fi.vrk.xroad.xroad_catalog_lister.OrganizationDescription;
import fi.vrk.xroad.xroad_catalog_lister.OrganizationName;
import fi.vrk.xroad.xroad_catalog_lister.PhoneNumber;
import fi.vrk.xroad.xroad_catalog_lister.PostOffice;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBox;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddress;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressAdditionalInformation;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressAdditionalInformationList;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressList;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipality;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipalityList;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipalityName;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipalityNameList;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxList;
import fi.vrk.xroad.xroad_catalog_lister.PostOfficeList;
import fi.vrk.xroad.xroad_catalog_lister.RegisteredEntry;
import fi.vrk.xroad.xroad_catalog_lister.RegisteredEntryList;
import fi.vrk.xroad.xroad_catalog_lister.RegisteredOffice;
import fi.vrk.xroad.xroad_catalog_lister.RegisteredOfficeList;
import fi.vrk.xroad.xroad_catalog_lister.Street;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddress;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressAdditionalInformation;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressAdditionalInformationList;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressList;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipality;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipalityList;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipalityName;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipalityNameList;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressPostOffice;
import fi.vrk.xroad.xroad_catalog_lister.StreetAddressPostOfficeList;
import fi.vrk.xroad.xroad_catalog_lister.StreetList;
import fi.vrk.xroad.xroad_catalog_lister.WebPage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JaxbOrganizationUtil {

    private JaxbOrganizationUtil() {

    }

    public static Collection<OrganizationName> convertOrganizationNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationName> organizationNames) {
        List<OrganizationName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.OrganizationName organizationName: organizationNames) {
            OrganizationName co = new OrganizationName();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(organizationName.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(organizationName.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(organizationName.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(organizationName.getStatusInfo().getRemoved()));
            co.setLanguage(organizationName.getLanguage());
            co.setType(organizationName.getType());
            co.setValue(organizationName.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<OrganizationDescription> convertOrganizationDescriptions(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription> organizationDescriptions) {
        List<OrganizationDescription> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription organizationDescription: organizationDescriptions) {
            OrganizationDescription co = new OrganizationDescription();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(organizationDescription.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(organizationDescription.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(organizationDescription.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(organizationDescription.getStatusInfo().getRemoved()));
            co.setLanguage(organizationDescription.getLanguage());
            co.setType(organizationDescription.getType());
            co.setValue(organizationDescription.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<Email> convertEmails(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Email> emails) {
        List<Email> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Email email: emails) {
            Email co = new Email();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(email.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(email.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(email.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(email.getStatusInfo().getRemoved()));
            co.setLanguage(email.getLanguage());
            co.setDescription(email.getDescription());
            co.setValue(email.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<PhoneNumber> convertPhoneNumbers(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PhoneNumber> phoneNumbers) {
        List<PhoneNumber> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PhoneNumber phoneNumber: phoneNumbers) {
            PhoneNumber co = new PhoneNumber();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(phoneNumber.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(phoneNumber.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(phoneNumber.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(phoneNumber.getStatusInfo().getRemoved()));
            co.setLanguage(phoneNumber.getLanguage());
            co.setAdditionalInformation(phoneNumber.getAdditionalInformation());
            co.setChargeDescription(phoneNumber.getChargeDescription());
            co.setNumber(phoneNumber.getNumber());
            co.setIsFinnishServiceNumber(phoneNumber.getIsFinnishServiceNumber());
            co.setPrefixNumber(phoneNumber.getPrefixNumber());
            co.setServiceChargeType(phoneNumber.getServiceChargeType());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<WebPage> convertWebPages(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.WebPage> webPages) {
        List<WebPage> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.WebPage webPage: webPages) {
            WebPage co = new WebPage();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(webPage.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(webPage.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(webPage.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(webPage.getStatusInfo().getRemoved()));
            co.setLanguage(webPage.getLanguage());
            co.setUrl(webPage.getUrl());
            co.setValue(webPage.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<Address> convertAddresses(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Address> addresses) {
        List<Address> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Address address: addresses) {
            Address co = new Address();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(address.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(address.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(address.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(address.getStatusInfo().getRemoved()));
            co.setCountry(address.getCountry());
            co.setSubType(address.getSubType());
            co.setType(address.getType());

            co.setStreetAddresses(new StreetAddressList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddress> streetAddresses = address.getAllStreetAddresses();
            if (streetAddresses != null) {
                co.getStreetAddresses().getStreetAddress().addAll(convertStreetAddresses(streetAddresses));
            }

            co.setPostOfficeBoxAddresses(new PostOfficeBoxAddressList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress> postOfficeBoxAddresses
                    = address.getAllPostOfficeBoxAddresses();
            if (postOfficeBoxAddresses != null) {
                co.getPostOfficeBoxAddresses().getPostOfficeBoxAddress().addAll(convertPostOfficeBoxAddresses(postOfficeBoxAddresses));
            }

            converted.add(co);
        }
        return converted;
    }

    public static Collection<StreetAddress> convertStreetAddresses(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddress> streetAddresses) {
        List<StreetAddress> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddress streetAddress: streetAddresses) {
            StreetAddress co = new StreetAddress();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(streetAddress.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(streetAddress.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(streetAddress.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(streetAddress.getStatusInfo().getRemoved()));
            co.setPostalCode(streetAddress.getPostalCode());
            co.setLatitude(streetAddress.getLatitude());
            co.setLongitude(streetAddress.getLongitude());
            co.setCoordinateState(streetAddress.getCoordinateState());

            co.setStreets(new StreetList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Street> streets = streetAddress.getAllStreets();
            if (streets != null) {
                co.getStreets().getStreet().addAll(convertStreets(streets));
            }

            co.setPostOffices(new StreetAddressPostOfficeList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice> streetAddressPostOffices = streetAddress.getAllPostOffices();
            if (streetAddressPostOffices != null) {
                co.getPostOffices().getStreetAddressPostOffice().addAll(convertStreetAddressPostOffices(streetAddressPostOffices));
            }

            co.setMunicipalities(new StreetAddressMunicipalityList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality> streetAddressMunicipalities = streetAddress.getAllMunicipalities();
            if (streetAddressMunicipalities != null) {
                co.getMunicipalities().getStreetAddressMunicipality().addAll(convertStreetAddressMunicipalities(streetAddressMunicipalities));
            }

            co.setAdditionalInformation(new StreetAddressAdditionalInformationList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation> streetAddressAdditionalInformation
                    = streetAddress.getAllAdditionalInformation();
            if (streetAddressAdditionalInformation != null) {
                co.getAdditionalInformation().getStreetAddressAdditionalInformation()
                        .addAll(convertStreetAddressAdditionalInformation(streetAddressAdditionalInformation));
            }

            converted.add(co);
        }
        return converted;
    }

    public static Collection<Street> convertStreets(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Street> streets) {
        List<Street> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Street street: streets) {
            Street co = new Street();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(street.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(street.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(street.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(street.getStatusInfo().getRemoved()));
            co.setLanguage(street.getLanguage());
            co.setValue(street.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<StreetAddressPostOffice> convertStreetAddressPostOffices(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice> streetAddressPostOffices) {
        List<StreetAddressPostOffice> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice streetAddressPostOffice: streetAddressPostOffices) {
            StreetAddressPostOffice co = new StreetAddressPostOffice();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getRemoved()));
            co.setLanguage(streetAddressPostOffice.getLanguage());
            co.setValue(streetAddressPostOffice.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<StreetAddressMunicipality> convertStreetAddressMunicipalities(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality> streetAddressMunicipalities) {
        List<StreetAddressMunicipality> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality streetAddressMunicipality: streetAddressMunicipalities) {
            StreetAddressMunicipality co = new StreetAddressMunicipality();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getRemoved()));
            co.setCode(streetAddressMunicipality.getCode());

            co.setStreetAddressMunicipalityNames(new StreetAddressMunicipalityNameList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName> streetAddressMunicipalityNames
                    = streetAddressMunicipality.getAllMunicipalityNames();
            if (streetAddressMunicipalityNames != null) {
                co.getStreetAddressMunicipalityNames().getStreetAddressMunicipalityName()
                        .addAll(convertStreetAddressMunicipalityNames(streetAddressMunicipalityNames));
            }

            converted.add(co);
        }
        return converted;
    }

    public static Collection<StreetAddressMunicipalityName> convertStreetAddressMunicipalityNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName> streetAddressMunicipalityNames) {
        List<StreetAddressMunicipalityName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName streetAddressMunicipalityName: streetAddressMunicipalityNames) {
            StreetAddressMunicipalityName co = new StreetAddressMunicipalityName();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getRemoved()));
            co.setLanguage(streetAddressMunicipalityName.getLanguage());
            co.setValue(streetAddressMunicipalityName.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<StreetAddressAdditionalInformation> convertStreetAddressAdditionalInformation(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation> streetAddressAdditionalInformationList) {
        List<StreetAddressAdditionalInformation> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation streetAddressAdditionalInformation: streetAddressAdditionalInformationList) {
            StreetAddressAdditionalInformation co = new StreetAddressAdditionalInformation();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getRemoved()));
            co.setLanguage(streetAddressAdditionalInformation.getLanguage());
            co.setValue(streetAddressAdditionalInformation.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<PostOfficeBoxAddress> convertPostOfficeBoxAddresses(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress> postOfficeBoxAddresses) {
        List<PostOfficeBoxAddress> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress postOfficeBoxAddress: postOfficeBoxAddresses) {
            PostOfficeBoxAddress co = new PostOfficeBoxAddress();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getRemoved()));
            co.setPostalCode(postOfficeBoxAddress.getPostalCode());
            co.setPostalCode(postOfficeBoxAddress.getPostalCode());

            co.setAdditionalInformation(new PostOfficeBoxAddressAdditionalInformationList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation> postOfficeBoxAddressAdditionalInformation
                    = postOfficeBoxAddress.getAllAdditionalInformation();
            if (postOfficeBoxAddressAdditionalInformation != null) {
                co.getAdditionalInformation().getPostOfficeBoxAddressAdditionalInformation()
                        .addAll(convertPostOfficeBoxAddressAdditionalInformation(postOfficeBoxAddressAdditionalInformation));
            }

            co.setPostOfficeBoxAddressMunicipalities(new PostOfficeBoxAddressMunicipalityList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities
                    = postOfficeBoxAddress.getAllMunicipalities();
            if (postOfficeBoxAddressMunicipalities != null) {
                co.getPostOfficeBoxAddressMunicipalities().getPostOfficeBoxAddressMunicipality()
                        .addAll(convertPostOfficeBoxAddressMunicipalities(postOfficeBoxAddressMunicipalities));
            }

            co.setPostOffices(new PostOfficeList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOffice> postOffices = postOfficeBoxAddress.getAllPostOffices();
            if (postOffices != null) {
                co.getPostOffices().getPostOffice().addAll(convertPostOffices(postOffices));
            }

            co.setPostOfficeBoxes(new PostOfficeBoxList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox> postOfficeBoxes = postOfficeBoxAddress.getAllPostOfficeBoxes();
            if (postOfficeBoxes != null) {
                co.getPostOfficeBoxes().getPostOfficeBox().addAll(convertPostOfficeBoxes(postOfficeBoxes));
            }

            converted.add(co);
        }
        return converted;
    }

    public static Collection<PostOfficeBoxAddressAdditionalInformation> convertPostOfficeBoxAddressAdditionalInformation(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation> postOfficeBoxAddressAdditionalInformationList) {
        List<PostOfficeBoxAddressAdditionalInformation> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation postOfficeBoxAddressAdditionalInformation:
                postOfficeBoxAddressAdditionalInformationList) {
            PostOfficeBoxAddressAdditionalInformation co
                    = new PostOfficeBoxAddressAdditionalInformation();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getRemoved()));
            co.setLanguage(postOfficeBoxAddressAdditionalInformation.getLanguage());
            co.setValue(postOfficeBoxAddressAdditionalInformation.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<PostOfficeBoxAddressMunicipality> convertPostOfficeBoxAddressMunicipalities(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities) {
        List<PostOfficeBoxAddressMunicipality> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality: postOfficeBoxAddressMunicipalities) {
            PostOfficeBoxAddressMunicipality co = new PostOfficeBoxAddressMunicipality();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getRemoved()));
            co.setCode(postOfficeBoxAddressMunicipality.getCode());

            co.setPostOfficeBoxAddressMunicipalityNames(new PostOfficeBoxAddressMunicipalityNameList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames
                    = postOfficeBoxAddressMunicipality.getAllMunicipalityNames();
            if (postOfficeBoxAddressMunicipalityNames != null) {
                co.getPostOfficeBoxAddressMunicipalityNames().getPostOfficeBoxAddressMunicipalityName()
                        .addAll(convertPostOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipalityNames));
            }

            converted.add(co);
        }
        return converted;
    }

    public static Collection<PostOfficeBoxAddressMunicipalityName> convertPostOfficeBoxAddressMunicipalityNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames) {
        List<PostOfficeBoxAddressMunicipalityName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName: postOfficeBoxAddressMunicipalityNames) {
            PostOfficeBoxAddressMunicipalityName co = new PostOfficeBoxAddressMunicipalityName();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getRemoved()));
            co.setLanguage(postOfficeBoxAddressMunicipalityName.getLanguage());
            co.setValue(postOfficeBoxAddressMunicipalityName.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<PostOffice> convertPostOffices(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOffice> postOffices) {
        List<PostOffice> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOffice postOffice: postOffices) {
            PostOffice co = new PostOffice();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(postOffice.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(postOffice.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(postOffice.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(postOffice.getStatusInfo().getRemoved()));
            co.setLanguage(postOffice.getLanguage());
            co.setValue(postOffice.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<PostOfficeBox> convertPostOfficeBoxes(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox> postOfficeBoxes) {
        List<PostOfficeBox> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox postOfficeBox: postOfficeBoxes) {
            PostOfficeBox co = new PostOfficeBox();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getRemoved()));
            co.setLanguage(postOfficeBox.getLanguage());
            co.setValue(postOfficeBox.getValue());
            converted.add(co);
        }
        return converted;
    }

    public static Company convertCompany(fi.vrk.xroad.catalog.persistence.entity.Company company) {
        Company co = new Company();
        co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(company.getStatusInfo().getChanged()));
        co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(company.getStatusInfo().getCreated()));
        co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(company.getStatusInfo().getFetched()));
        co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(company.getStatusInfo().getRemoved()));
        co.setBusinessId(company.getBusinessId());
        co.setCompanyForm(company.getCompanyForm());
        co.setDetailsUri(company.getDetailsUri());
        co.setName(company.getName());
        co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(company.getRegistrationDate()));

        co.setBusinessAddresses(new BusinessAddressList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessAddress> businessAddresses = company.getAllBusinessAddresses();
        if (businessAddresses != null) {
            co.getBusinessAddresses().getBusinessAddress().addAll(convertBusinessAddresses(businessAddresses));
        }

        co.setBusinessAuxiliaryNames(new BusinessAuxiliaryNameList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessAuxiliaryName> businessAuxiliaryNames = company.getAllBusinessAuxiliaryNames();
        if (businessAuxiliaryNames != null) {
            co.getBusinessAuxiliaryNames().getBusinessAuxiliaryName().addAll(convertBusinessAuxiliaryNames(businessAuxiliaryNames));
        }

        co.setBusinessIdChanges(new BusinessIdChangeList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessIdChange> businessIdChanges = company.getAllBusinessIdChanges();
        if (businessIdChanges != null) {
            co.getBusinessIdChanges().getBusinessIdChange().addAll(convertBusinessIdChanges(businessIdChanges));
        }

        co.setBusinessLines(new BusinessLineList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessLine> businessLines = company.getAllBusinessLines();
        if (businessLines != null) {
            co.getBusinessLines().getBusinessLine().addAll(convertBusinessLines(businessLines));
        }

        co.setBusinessNames(new BusinessNameList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessName> businessNames = company.getAllBusinessNames();
        if (businessNames != null) {
            co.getBusinessNames().getBusinessName().addAll(convertBusinessNames(businessNames));
        }

        co.setCompanyForms(new CompanyFormList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.CompanyForm> companyForms = company.getAllCompanyForms();
        if (companyForms != null) {
            co.getCompanyForms().getCompanyForm().addAll(convertCompanyForms(companyForms));
        }

        co.setContactDetails(new ContactDetailList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.ContactDetail> contactDetails = company.getAllContactDetails();
        if (contactDetails != null) {
            co.getContactDetails().getContactDetail().addAll(convertContactDetails(contactDetails));
        }

        co.setLanguages(new LanguageList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Language> languages = company.getAllLanguages();
        if (languages != null) {
            co.getLanguages().getLanguage().addAll(convertLanguages(languages));
        }

        co.setLiquidations(new LiquidationList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.Liquidation> liquidations = company.getAllLiquidations();
        if (liquidations != null) {
            co.getLiquidations().getLiquidation().addAll(convertLiquidations(liquidations));
        }

        co.setRegisteredEntries(new RegisteredEntryList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.RegisteredEntry> registeredEntries = company.getAllRegisteredEntries();
        if (registeredEntries != null) {
            co.getRegisteredEntries().getRegisteredEntry().addAll(convertRegisteredEntries(registeredEntries));
        }

        co.setRegisteredOffices(new RegisteredOfficeList());
        Iterable<fi.vrk.xroad.catalog.persistence.entity.RegisteredOffice> registeredOffices = company.getAllRegisteredOffices();
        if (registeredOffices != null) {
            co.getRegisteredOffices().getRegisteredOffice().addAll(convertRegisteredOffices(registeredOffices));
        }
        return co;
    }

    public static Collection<BusinessAddress> convertBusinessAddresses(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessAddress> businessAddresses) {
        List<BusinessAddress> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.BusinessAddress businessAddress: businessAddresses) {
            BusinessAddress co = new BusinessAddress();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(businessAddress.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(businessAddress.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(businessAddress.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(businessAddress.getStatusInfo().getRemoved()));
            co.setCareOf(businessAddress.getCareOf());
            co.setCity(businessAddress.getCity());
            co.setCountry(businessAddress.getCountry());
            co.setLanguage(businessAddress.getLanguage());
            co.setPostCode(businessAddress.getPostCode());
            co.setSource(businessAddress.getSource());
            co.setStreet(businessAddress.getStreet());
            co.setType(businessAddress.getType());
            co.setVersion(businessAddress.getVersion());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(businessAddress.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(businessAddress.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<BusinessAuxiliaryName> convertBusinessAuxiliaryNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessAuxiliaryName> businessAuxiliaryNames) {
        List<BusinessAuxiliaryName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.BusinessAuxiliaryName businessAuxiliaryName: businessAuxiliaryNames) {
            BusinessAuxiliaryName co = new BusinessAuxiliaryName();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(businessAuxiliaryName.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(businessAuxiliaryName.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(businessAuxiliaryName.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(businessAuxiliaryName.getStatusInfo().getRemoved()));
            co.setLanguage(businessAuxiliaryName.getLanguage());
            co.setName(businessAuxiliaryName.getName());
            co.setOrdering(businessAuxiliaryName.getOrdering());
            co.setSource(businessAuxiliaryName.getSource());
            co.setVersion(businessAuxiliaryName.getVersion());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(businessAuxiliaryName.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(businessAuxiliaryName.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<BusinessIdChange> convertBusinessIdChanges(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessIdChange> businessIdChanges) {
        List<BusinessIdChange> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.BusinessIdChange businessIdChange: businessIdChanges) {
            BusinessIdChange co = new BusinessIdChange();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(businessIdChange.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(businessIdChange.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(businessIdChange.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(businessIdChange.getStatusInfo().getRemoved()));
            co.setLanguage(businessIdChange.getLanguage());
            co.setSource(businessIdChange.getSource());
            co.setChange(businessIdChange.getChange());
            co.setChangeDate(businessIdChange.getChangeDate());
            co.setDescription(businessIdChange.getDescription());
            co.setReason(businessIdChange.getReason());
            co.setOldBusinessId(businessIdChange.getOldBusinessId());
            co.setNewBusinessId(businessIdChange.getNewBusinessId());
            converted.add(co);
        }
        return converted;
    }

    public static Collection<BusinessLine> convertBusinessLines(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessLine> businessLines) {
        List<BusinessLine> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.BusinessLine businessLine: businessLines) {
            BusinessLine co = new BusinessLine();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(businessLine.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(businessLine.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(businessLine.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(businessLine.getStatusInfo().getRemoved()));
            co.setLanguage(businessLine.getLanguage());
            co.setSource(businessLine.getSource());
            co.setName(businessLine.getName());
            co.setOrdering(businessLine.getOrdering());
            co.setVersion(businessLine.getVersion());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(businessLine.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(businessLine.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<BusinessName> convertBusinessNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.BusinessName> businessNames) {
        List<BusinessName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.BusinessName businessName: businessNames) {
            BusinessName co = new BusinessName();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(businessName.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(businessName.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(businessName.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(businessName.getStatusInfo().getRemoved()));
            co.setLanguage(businessName.getLanguage());
            co.setSource(businessName.getSource());
            co.setName(businessName.getName());
            co.setOrdering(businessName.getOrdering());
            co.setVersion(businessName.getVersion());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(businessName.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(businessName.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<CompanyForm> convertCompanyForms(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.CompanyForm> companyForms) {
        List<CompanyForm> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.CompanyForm companyForm: companyForms) {
            CompanyForm co = new CompanyForm();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(companyForm.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(companyForm.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(companyForm.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(companyForm.getStatusInfo().getRemoved()));
            co.setLanguage(companyForm.getLanguage());
            co.setSource(companyForm.getSource());
            co.setName(companyForm.getName());
            co.setType(companyForm.getType());
            co.setVersion(companyForm.getVersion());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(companyForm.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(companyForm.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<ContactDetail> convertContactDetails(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.ContactDetail> contactDetails) {
        List<ContactDetail> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.ContactDetail contactDetail: contactDetails) {
            ContactDetail co = new ContactDetail();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(contactDetail.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(contactDetail.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(contactDetail.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(contactDetail.getStatusInfo().getRemoved()));
            co.setLanguage(contactDetail.getLanguage());
            co.setSource(contactDetail.getSource());
            co.setValue(contactDetail.getValue());
            co.setType(contactDetail.getType());
            co.setVersion(contactDetail.getVersion());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(contactDetail.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(contactDetail.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<Language> convertLanguages(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Language> languages) {
        List<Language> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Language language: languages) {
            Language co = new Language();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(language.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(language.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(language.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(language.getStatusInfo().getRemoved()));
            co.setLanguage(language.getLanguage());
            co.setSource(language.getSource());
            co.setName(language.getName());
            co.setVersion(language.getVersion());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(language.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(language.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<Liquidation> convertLiquidations(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Liquidation> liquidations) {
        List<Liquidation> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Liquidation liquidation: liquidations) {
            Liquidation co = new Liquidation();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(liquidation.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(liquidation.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(liquidation.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(liquidation.getStatusInfo().getRemoved()));
            co.setLanguage(liquidation.getLanguage());
            co.setSource(liquidation.getSource());
            co.setName(liquidation.getName());
            co.setVersion(liquidation.getVersion());
            co.setType(liquidation.getType());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(liquidation.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(liquidation.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<RegisteredEntry> convertRegisteredEntries(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.RegisteredEntry> registeredEntries) {
        List<RegisteredEntry> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.RegisteredEntry registeredEntry: registeredEntries) {
            RegisteredEntry co = new RegisteredEntry();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(registeredEntry.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(registeredEntry.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(registeredEntry.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(registeredEntry.getStatusInfo().getRemoved()));
            co.setLanguage(registeredEntry.getLanguage());
            co.setAuthority(registeredEntry.getAuthority());
            co.setDescription(registeredEntry.getDescription());
            co.setRegister(registeredEntry.getRegister());
            co.setStatus(registeredEntry.getStatus());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(registeredEntry.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(registeredEntry.getEndDate()));
            converted.add(co);
        }
        return converted;
    }

    public static Collection<RegisteredOffice> convertRegisteredOffices(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.RegisteredOffice> registeredOffices) {
        List<RegisteredOffice> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.RegisteredOffice registeredOffice: registeredOffices) {
            RegisteredOffice co = new RegisteredOffice();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(registeredOffice.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(registeredOffice.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(registeredOffice.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(registeredOffice.getStatusInfo().getRemoved()));
            co.setLanguage(registeredOffice.getLanguage());
            co.setSource(registeredOffice.getSource());
            co.setOrdering(registeredOffice.getOrdering());
            co.setVersion(registeredOffice.getVersion());
            co.setName(registeredOffice.getName());
            co.setRegistrationDate(JaxbServiceUtil.toXmlGregorianCalendar(registeredOffice.getRegistrationDate()));
            co.setEndDate(JaxbServiceUtil.toXmlGregorianCalendar(registeredOffice.getEndDate()));
            converted.add(co);
        }
        return converted;
    }
}
