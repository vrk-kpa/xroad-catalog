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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.persistence.entity.OpenApi;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.catalog.persistence.entity.Wsdl;
import fi.vrk.xroad.xroad_catalog_lister.*;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Utility for converting JAXB classes & entities
 */
@Component
public class JaxbConverter {

    /**
     * Convert entities to XML objects
     * @param members Iterable of Member entities
     * @param onlyActiveChildren if true, convert only active subsystems
     * @return Collection of Members (JAXB generated)
     */
    public Collection<Member> convertMembers(Iterable<fi.vrk.xroad.catalog.persistence.entity.Member> members,
                                             boolean onlyActiveChildren)  {
        List<Member> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Member member: members) {
            Member cm = new Member();
            cm.setChanged(toXmlGregorianCalendar(member.getStatusInfo().getChanged()));
            cm.setCreated(toXmlGregorianCalendar(member.getStatusInfo().getCreated()));
            cm.setFetched(toXmlGregorianCalendar(member.getStatusInfo().getFetched()));
            cm.setRemoved(toXmlGregorianCalendar(member.getStatusInfo().getRemoved()));
            cm.setMemberCode(member.getMemberCode());
            cm.setMemberClass(member.getMemberClass());
            cm.setName(member.getName());
            cm.setXRoadInstance(member.getXRoadInstance());
            cm.setSubsystems(new SubsystemList());
            Iterable<Subsystem> subsystems;
            if (onlyActiveChildren) {
                subsystems = member.getActiveSubsystems();
            } else {
                subsystems = member.getAllSubsystems();
            }
            cm.getSubsystems().getSubsystem().addAll(convertSubsystems(subsystems, onlyActiveChildren));
            converted.add(cm);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param subsystems Iterable of Subsystem entities
     * @param onlyActiveChildren if true, convert only active subsystems
     * @return collection of XML objects
     */
    public Collection<fi.vrk.xroad.xroad_catalog_lister.Subsystem> convertSubsystems(Iterable<Subsystem> subsystems,
                                                                                     boolean onlyActiveChildren) {
        List<fi.vrk.xroad.xroad_catalog_lister.Subsystem> converted = new ArrayList<>();
        for (Subsystem subsystem: subsystems) {
            fi.vrk.xroad.xroad_catalog_lister.Subsystem cs = new fi.vrk.xroad.xroad_catalog_lister.Subsystem();
            cs.setChanged(toXmlGregorianCalendar(subsystem.getStatusInfo().getChanged()));
            cs.setCreated(toXmlGregorianCalendar(subsystem.getStatusInfo().getCreated()));
            cs.setFetched(toXmlGregorianCalendar(subsystem.getStatusInfo().getFetched()));
            cs.setRemoved(toXmlGregorianCalendar(subsystem.getStatusInfo().getRemoved()));
            cs.setSubsystemCode(subsystem.getSubsystemCode());
            cs.setServices(new ServiceList());
            Iterable<Service> services;
            if (onlyActiveChildren) {
                services = subsystem.getActiveServices();
            } else {
                services = subsystem.getAllServices();
            }
            cs.getServices().getService().addAll(convertServices(services, onlyActiveChildren));
            converted.add(cs);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param services Iterable of Service entities
     * @param onlyActiveChildren if true, convert only active subsystems
     * @return collection of XML objects
     */
    public Collection<fi.vrk.xroad.xroad_catalog_lister.Service> convertServices(Iterable<Service> services,
                                                                                 boolean onlyActiveChildren) {
        List<fi.vrk.xroad.xroad_catalog_lister.Service> converted = new ArrayList<>();
        for (Service service: services) {
            fi.vrk.xroad.xroad_catalog_lister.Service cs = new fi.vrk.xroad.xroad_catalog_lister.Service();
            cs.setChanged(toXmlGregorianCalendar(service.getStatusInfo().getChanged()));
            cs.setCreated(toXmlGregorianCalendar(service.getStatusInfo().getCreated()));
            cs.setFetched(toXmlGregorianCalendar(service.getStatusInfo().getFetched()));
            cs.setRemoved(toXmlGregorianCalendar(service.getStatusInfo().getRemoved()));
            cs.setServiceCode(service.getServiceCode());
            cs.setServiceVersion(service.getServiceVersion());
            Wsdl wsdl = null;
            if (onlyActiveChildren) {
                if (service.getWsdl() != null && !service.getWsdl().getStatusInfo().isRemoved()) {
                    wsdl = service.getWsdl();
                }
            } else {
                wsdl = service.getWsdl();
            }
            if (wsdl != null) {
                cs.setWsdl(convertWsdl(service.getWsdl()));
            }
            OpenApi openApi = null;
            if (onlyActiveChildren) {
                if (service.getOpenApi() != null && !service.getOpenApi().getStatusInfo().isRemoved()) {
                    openApi = service.getOpenApi();
                }
            } else {
                openApi = service.getOpenApi();
            }
            if (openApi != null) {
                cs.setOpenapi(convertOpenApi(service.getOpenApi()));
            }
            converted.add(cs);
        }
        return converted;
    }

    private WSDL convertWsdl(Wsdl wsdl) {
        WSDL cw = new WSDL();
        cw.setChanged(toXmlGregorianCalendar(wsdl.getStatusInfo().getChanged()));
        cw.setCreated(toXmlGregorianCalendar(wsdl.getStatusInfo().getCreated()));
        cw.setFetched(toXmlGregorianCalendar(wsdl.getStatusInfo().getFetched()));
        cw.setRemoved(toXmlGregorianCalendar(wsdl.getStatusInfo().getRemoved()));
        cw.setExternalId(wsdl.getExternalId());
        return cw;
    }

    private OPENAPI convertOpenApi(OpenApi openApi) {
        OPENAPI cw = new OPENAPI();
        cw.setChanged(toXmlGregorianCalendar(openApi.getStatusInfo().getChanged()));
        cw.setCreated(toXmlGregorianCalendar(openApi.getStatusInfo().getCreated()));
        cw.setFetched(toXmlGregorianCalendar(openApi.getStatusInfo().getFetched()));
        cw.setRemoved(toXmlGregorianCalendar(openApi.getStatusInfo().getRemoved()));
        cw.setExternalId(openApi.getExternalId());
        return cw;
    }

    public LocalDateTime toLocalDateTime(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    protected XMLGregorianCalendar toXmlGregorianCalendar(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            GregorianCalendar cal = GregorianCalendar.from(localDateTime.atZone(ZoneId.systemDefault()));
            XMLGregorianCalendar xc = null;
            try {
                xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            } catch (DatatypeConfigurationException e) {
                throw new CatalogListerRuntimeException("Cannot instantiate DatatypeFactory", e);
            }
            return xc;
        }
    }

    /**
     * Convert entities to XML objects
     * @param organizations Iterable of Organization entities
     * @return Collection of Organizations (JAXB generated)
     */
    public Collection<Organization> convertOrganizations(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Organization> organizations)  {
        List<Organization> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Organization organization: organizations) {
            Organization co = new Organization();
            co.setChanged(toXmlGregorianCalendar(organization.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(organization.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(organization.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(organization.getStatusInfo().getRemoved()));
            co.setBusinessCode(organization.getBusinessCode());
            co.setOrganizationType(organization.getOrganizationType());
            co.setGuid(organization.getGuid());
            co.setPublishingStatus(organization.getPublishingStatus());

            co.setOrganizationNames(new OrganizationNameList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationName> organizationNames = organization.getAllOrganizationNames();
            co.getOrganizationNames().getOrganizationName().addAll(convertOrganizationNames(organizationNames));

            co.setOrganizationDescriptions(new OrganizationDescriptionList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription> organizationDescriptions
                    = organization.getAllOrganizationDescriptions();
            co.getOrganizationDescriptions().getOrganizationDescription().addAll(convertOrganizationDescriptions(organizationDescriptions));

            co.setEmails(new EmailList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Email> emails = organization.getAllEmails();
            co.getEmails().getEmail().addAll(convertEmails(emails));

            co.setPhoneNumbers(new PhoneNumberList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PhoneNumber> phoneNumbers = organization.getAllPhoneNumbers();
            co.getPhoneNumbers().getPhoneNumber().addAll(convertPhoneNumbers(phoneNumbers));

            co.setWebPages(new WebPageList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.WebPage> webPages = organization.getAllWebPages();
            co.getWebPages().getWebPage().addAll(convertWebPages(webPages));

            co.setAddresses(new AddressList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Address> addresses = organization.getAllAddresses();
            co.getAddresses().getAddress().addAll(convertAddresses(addresses));

            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param organizationNames Iterable of OrganizationName entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.OrganizationName> convertOrganizationNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationName> organizationNames) {
        List<fi.vrk.xroad.xroad_catalog_lister.OrganizationName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.OrganizationName organizationName: organizationNames) {
            fi.vrk.xroad.xroad_catalog_lister.OrganizationName co = new fi.vrk.xroad.xroad_catalog_lister.OrganizationName();
            co.setChanged(toXmlGregorianCalendar(organizationName.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(organizationName.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(organizationName.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(organizationName.getStatusInfo().getRemoved()));
            co.setLanguage(organizationName.getLanguage());
            co.setType(organizationName.getType());
            co.setValue(organizationName.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param organizationDescriptions Iterable of OrganizationDescription entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.OrganizationDescription> convertOrganizationDescriptions(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription> organizationDescriptions) {
        List<fi.vrk.xroad.xroad_catalog_lister.OrganizationDescription> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription organizationDescription: organizationDescriptions) {
            fi.vrk.xroad.xroad_catalog_lister.OrganizationDescription co = new fi.vrk.xroad.xroad_catalog_lister.OrganizationDescription();
            co.setChanged(toXmlGregorianCalendar(organizationDescription.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(organizationDescription.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(organizationDescription.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(organizationDescription.getStatusInfo().getRemoved()));
            co.setLanguage(organizationDescription.getLanguage());
            co.setType(organizationDescription.getType());
            co.setValue(organizationDescription.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param emails Iterable of Email entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.Email> convertEmails(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Email> emails) {
        List<fi.vrk.xroad.xroad_catalog_lister.Email> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Email email: emails) {
            fi.vrk.xroad.xroad_catalog_lister.Email co = new fi.vrk.xroad.xroad_catalog_lister.Email();
            co.setChanged(toXmlGregorianCalendar(email.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(email.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(email.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(email.getStatusInfo().getRemoved()));
            co.setLanguage(email.getLanguage());
            co.setDescription(email.getDescription());
            co.setValue(email.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param phoneNumbers Iterable of PhoneNumber entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.PhoneNumber> convertPhoneNumbers(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PhoneNumber> phoneNumbers) {
        List<fi.vrk.xroad.xroad_catalog_lister.PhoneNumber> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PhoneNumber phoneNumber: phoneNumbers) {
            fi.vrk.xroad.xroad_catalog_lister.PhoneNumber co = new fi.vrk.xroad.xroad_catalog_lister.PhoneNumber();
            co.setChanged(toXmlGregorianCalendar(phoneNumber.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(phoneNumber.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(phoneNumber.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(phoneNumber.getStatusInfo().getRemoved()));
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

    /**
     * Convert entities to XML objects
     * @param webPages Iterable of WebPage entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.WebPage> convertWebPages(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.WebPage> webPages) {
        List<fi.vrk.xroad.xroad_catalog_lister.WebPage> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.WebPage webPage: webPages) {
            fi.vrk.xroad.xroad_catalog_lister.WebPage co = new fi.vrk.xroad.xroad_catalog_lister.WebPage();
            co.setChanged(toXmlGregorianCalendar(webPage.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(webPage.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(webPage.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(webPage.getStatusInfo().getRemoved()));
            co.setLanguage(webPage.getLanguage());
            co.setUrl(webPage.getUrl());
            co.setValue(webPage.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param addresses Iterable of Address entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.Address> convertAddresses(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Address> addresses) {
        List<fi.vrk.xroad.xroad_catalog_lister.Address> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Address address: addresses) {
            fi.vrk.xroad.xroad_catalog_lister.Address co = new fi.vrk.xroad.xroad_catalog_lister.Address();
            co.setChanged(toXmlGregorianCalendar(address.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(address.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(address.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(address.getStatusInfo().getRemoved()));
            co.setCountry(address.getCountry());
            co.setSubType(address.getSubType());
            co.setType(address.getType());

            co.setStreetAddresses(new StreetAddressList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddress> streetAddresses = address.getAllStreetAddresses();
            co.getStreetAddresses().getStreetAddress().addAll(convertStreetAddresses(streetAddresses));

            co.setPostOfficeBoxAddresses(new PostOfficeBoxAddressList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress> postOfficeBoxAddresses
                    = address.getAllPostOfficeBoxAddresses();
            co.getPostOfficeBoxAddresses().getPostOfficeBoxAddress().addAll(convertPostOfficeBoxAddresses(postOfficeBoxAddresses));

            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param streetAddresses Iterable of StreetAddress entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.StreetAddress> convertStreetAddresses(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddress> streetAddresses) {
        List<fi.vrk.xroad.xroad_catalog_lister.StreetAddress> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddress streetAddress: streetAddresses) {
            fi.vrk.xroad.xroad_catalog_lister.StreetAddress co = new fi.vrk.xroad.xroad_catalog_lister.StreetAddress();
            co.setChanged(toXmlGregorianCalendar(streetAddress.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(streetAddress.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(streetAddress.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(streetAddress.getStatusInfo().getRemoved()));
            co.setPostalCode(streetAddress.getPostalCode());
            co.setLatitude(streetAddress.getLatitude());
            co.setLongitude(streetAddress.getLongitude());
            co.setCoordinateState(streetAddress.getCoordinateState());

            co.setStreets(new StreetList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Street> streets = streetAddress.getAllStreets();
            co.getStreets().getStreet().addAll(convertStreets(streets));

            co.setPostOffices(new StreetAddressPostOfficeList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice> streetAddressPostOffices = streetAddress.getAllPostOffices();
            co.getPostOffices().getStreetAddressPostOffice().addAll(convertStreetAddressPostOffices(streetAddressPostOffices));

            co.setMunicipalities(new StreetAddressMunicipalityList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality> streetAddressMunicipalities = streetAddress.getAllMunicipalities();
            co.getMunicipalities().getStreetAddressMunicipality().addAll(convertStreetAddressMunicipalities(streetAddressMunicipalities));

            co.setAdditionalInformation(new StreetAddressAdditionalInformationList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation> streetAddressAdditionalInformation
                    = streetAddress.getAllAdditionalInformation();
            co.getAdditionalInformation().getStreetAddressAdditionalInformation()
                    .addAll(convertStreetAddressAdditionalInformation(streetAddressAdditionalInformation));

            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param streets Iterable of Streets entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.Street> convertStreets(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Street> streets) {
        List<fi.vrk.xroad.xroad_catalog_lister.Street> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Street street: streets) {
            fi.vrk.xroad.xroad_catalog_lister.Street co = new fi.vrk.xroad.xroad_catalog_lister.Street();
            co.setChanged(toXmlGregorianCalendar(street.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(street.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(street.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(street.getStatusInfo().getRemoved()));
            co.setLanguage(street.getLanguage());
            co.setValue(street.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param streetAddressPostOffices Iterable of StreetAddressPostOffice entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.StreetAddressPostOffice> convertStreetAddressPostOffices(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice> streetAddressPostOffices) {
        List<fi.vrk.xroad.xroad_catalog_lister.StreetAddressPostOffice> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressPostOffice streetAddressPostOffice: streetAddressPostOffices) {
            fi.vrk.xroad.xroad_catalog_lister.StreetAddressPostOffice co = new fi.vrk.xroad.xroad_catalog_lister.StreetAddressPostOffice();
            co.setChanged(toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(streetAddressPostOffice.getStatusInfo().getRemoved()));
            co.setLanguage(streetAddressPostOffice.getLanguage());
            co.setValue(streetAddressPostOffice.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param streetAddressMunicipalities Iterable of StreetAddressMunicipality entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipality> convertStreetAddressMunicipalities(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality> streetAddressMunicipalities) {
        List<fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipality> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipality streetAddressMunicipality: streetAddressMunicipalities) {
            fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipality co = new fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipality();
            co.setChanged(toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(streetAddressMunicipality.getStatusInfo().getRemoved()));
            co.setCode(streetAddressMunicipality.getCode());

            co.setStreetAddressMunicipalityNames(new StreetAddressMunicipalityNameList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName> streetAddressMunicipalityNames
                    = streetAddressMunicipality.getAllMunicipalityNames();
            co.getStreetAddressMunicipalityNames().getStreetAddressMunicipalityName()
                    .addAll(convertStreetAddressMunicipalityNames(streetAddressMunicipalityNames));

            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param streetAddressMunicipalityNames Iterable of StreetAddressMunicipalityName entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipalityName> convertStreetAddressMunicipalityNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName> streetAddressMunicipalityNames) {
        List<fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipalityName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressMunicipalityName streetAddressMunicipalityName: streetAddressMunicipalityNames) {
            fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipalityName co = new fi.vrk.xroad.xroad_catalog_lister.StreetAddressMunicipalityName();
            co.setChanged(toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(streetAddressMunicipalityName.getStatusInfo().getRemoved()));
            co.setLanguage(streetAddressMunicipalityName.getLanguage());
            co.setValue(streetAddressMunicipalityName.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param streetAddressAdditionalInformationList Iterable of StreetAddressAdditionalInformation entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.StreetAddressAdditionalInformation> convertStreetAddressAdditionalInformation(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation> streetAddressAdditionalInformationList) {
        List<fi.vrk.xroad.xroad_catalog_lister.StreetAddressAdditionalInformation> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.StreetAddressAdditionalInformation streetAddressAdditionalInformation: streetAddressAdditionalInformationList) {
            fi.vrk.xroad.xroad_catalog_lister.StreetAddressAdditionalInformation co = new fi.vrk.xroad.xroad_catalog_lister.StreetAddressAdditionalInformation();
            co.setChanged(toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(streetAddressAdditionalInformation.getStatusInfo().getRemoved()));
            co.setLanguage(streetAddressAdditionalInformation.getLanguage());
            co.setValue(streetAddressAdditionalInformation.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param postOfficeBoxAddresses Iterable of PostOfficeBoxAddresses entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddress> convertPostOfficeBoxAddresses(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress> postOfficeBoxAddresses) {
        List<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddress> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddress postOfficeBoxAddress: postOfficeBoxAddresses) {
            fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddress co = new fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddress();
            co.setChanged(toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(postOfficeBoxAddress.getStatusInfo().getRemoved()));
            co.setPostalCode(postOfficeBoxAddress.getPostalCode());
            co.setPostalCode(postOfficeBoxAddress.getPostalCode());

            co.setAdditionalInformation(new PostOfficeBoxAddressAdditionalInformationList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation> postOfficeBoxAddressAdditionalInformation
                    = postOfficeBoxAddress.getAllAdditionalInformation();
            co.getAdditionalInformation().getPostOfficeBoxAddressAdditionalInformation()
                    .addAll(convertPostOfficeBoxAddressAdditionalInformation(postOfficeBoxAddressAdditionalInformation));

            co.setPostOfficeBoxAddressMunicipalities(new PostOfficeBoxAddressMunicipalityList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities
                    = postOfficeBoxAddress.getAllMunicipalities();
            co.getPostOfficeBoxAddressMunicipalities().getPostOfficeBoxAddressMunicipality()
                    .addAll(convertPostOfficeBoxAddressMunicipalities(postOfficeBoxAddressMunicipalities));

            co.setPostOffices(new PostOfficeList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOffice> postOffices = postOfficeBoxAddress.getAllPostOffices();
            co.getPostOffices().getPostOffice().addAll(convertPostOffices(postOffices));

            co.setPostOfficeBoxes(new PostOfficeBoxList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox> postOfficeBoxes = postOfficeBoxAddress.getAllPostOfficeBoxes();
            co.getPostOfficeBoxes().getPostOfficeBox().addAll(convertPostOfficeBoxes(postOfficeBoxes));


            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param postOfficeBoxAddressAdditionalInformationList Iterable of PostOfficeBoxAddressAdditionalInformation entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressAdditionalInformation> convertPostOfficeBoxAddressAdditionalInformation(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation> postOfficeBoxAddressAdditionalInformationList) {
        List<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressAdditionalInformation> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressAdditionalInformation postOfficeBoxAddressAdditionalInformation:
                postOfficeBoxAddressAdditionalInformationList) {
            fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressAdditionalInformation co
                    = new fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressAdditionalInformation();
            co.setChanged(toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(postOfficeBoxAddressAdditionalInformation.getStatusInfo().getRemoved()));
            co.setLanguage(postOfficeBoxAddressAdditionalInformation.getLanguage());
            co.setValue(postOfficeBoxAddressAdditionalInformation.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param postOfficeBoxAddressMunicipalities Iterable of PostOfficeBoxAddressMunicipality entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipality> convertPostOfficeBoxAddressMunicipalities(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality> postOfficeBoxAddressMunicipalities) {
        List<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipality> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipality postOfficeBoxAddressMunicipality: postOfficeBoxAddressMunicipalities) {
            fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipality co = new fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipality();
            co.setChanged(toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(postOfficeBoxAddressMunicipality.getStatusInfo().getRemoved()));
            co.setCode(postOfficeBoxAddressMunicipality.getCode());

            co.setPostOfficeBoxAddressMunicipalityNames(new PostOfficeBoxAddressMunicipalityNameList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames
                    = postOfficeBoxAddressMunicipality.getAllMunicipalityNames();
            co.getPostOfficeBoxAddressMunicipalityNames().getPostOfficeBoxAddressMunicipalityName()
                    .addAll(convertPostOfficeBoxAddressMunicipalityNames(postOfficeBoxAddressMunicipalityNames));

            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param postOfficeBoxAddressMunicipalityNames Iterable of PostOfficeBoxAddressMunicipalityName entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipalityName> convertPostOfficeBoxAddressMunicipalityNames(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName> postOfficeBoxAddressMunicipalityNames) {
        List<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipalityName> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBoxAddressMunicipalityName postOfficeBoxAddressMunicipalityName: postOfficeBoxAddressMunicipalityNames) {
            fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipalityName co = new fi.vrk.xroad.xroad_catalog_lister.PostOfficeBoxAddressMunicipalityName();
            co.setChanged(toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(postOfficeBoxAddressMunicipalityName.getStatusInfo().getRemoved()));
            co.setLanguage(postOfficeBoxAddressMunicipalityName.getLanguage());
            co.setValue(postOfficeBoxAddressMunicipalityName.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param postOffices Iterable of PostOffice entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.PostOffice> convertPostOffices(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOffice> postOffices) {
        List<fi.vrk.xroad.xroad_catalog_lister.PostOffice> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOffice postOffice: postOffices) {
            fi.vrk.xroad.xroad_catalog_lister.PostOffice co = new fi.vrk.xroad.xroad_catalog_lister.PostOffice();
            co.setChanged(toXmlGregorianCalendar(postOffice.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(postOffice.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(postOffice.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(postOffice.getStatusInfo().getRemoved()));
            co.setLanguage(postOffice.getLanguage());
            co.setValue(postOffice.getValue());
            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param postOfficeBoxes Iterable of PostOfficeBox entities
     * @return collection of XML objects
     */
    private Collection<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBox> convertPostOfficeBoxes(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox> postOfficeBoxes) {
        List<fi.vrk.xroad.xroad_catalog_lister.PostOfficeBox> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.PostOfficeBox postOfficeBox: postOfficeBoxes) {
            fi.vrk.xroad.xroad_catalog_lister.PostOfficeBox co = new fi.vrk.xroad.xroad_catalog_lister.PostOfficeBox();
            co.setChanged(toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getChanged()));
            co.setCreated(toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getCreated()));
            co.setFetched(toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getFetched()));
            co.setRemoved(toXmlGregorianCalendar(postOfficeBox.getStatusInfo().getRemoved()));
            co.setLanguage(postOfficeBox.getLanguage());
            co.setValue(postOfficeBox.getValue());
            converted.add(co);
        }
        return converted;
    }


}
