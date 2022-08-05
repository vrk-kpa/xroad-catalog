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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.util.JaxbUtil;
import fi.vrk.xroad.catalog.persistence.entity.Service;
import fi.vrk.xroad.catalog.persistence.entity.Subsystem;
import fi.vrk.xroad.xroad_catalog_lister.*;
import org.springframework.stereotype.Component;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            cm.setChanged(JaxbUtil.toXmlGregorianCalendar(member.getStatusInfo().getChanged()));
            cm.setCreated(JaxbUtil.toXmlGregorianCalendar(member.getStatusInfo().getCreated()));
            cm.setFetched(JaxbUtil.toXmlGregorianCalendar(member.getStatusInfo().getFetched()));
            cm.setRemoved(JaxbUtil.toXmlGregorianCalendar(member.getStatusInfo().getRemoved()));
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
            cs.setChanged(JaxbUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getChanged()));
            cs.setCreated(JaxbUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getCreated()));
            cs.setFetched(JaxbUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getFetched()));
            cs.setRemoved(JaxbUtil.toXmlGregorianCalendar(subsystem.getStatusInfo().getRemoved()));
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
            converted.add(JaxbUtil.convertService(service, onlyActiveChildren));
        }
        return converted;
    }

    public LocalDateTime toLocalDateTime(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
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
            co.setChanged(JaxbUtil.toXmlGregorianCalendar(organization.getStatusInfo().getChanged()));
            co.setCreated(JaxbUtil.toXmlGregorianCalendar(organization.getStatusInfo().getCreated()));
            co.setFetched(JaxbUtil.toXmlGregorianCalendar(organization.getStatusInfo().getFetched()));
            co.setRemoved(JaxbUtil.toXmlGregorianCalendar(organization.getStatusInfo().getRemoved()));
            co.setBusinessCode(organization.getBusinessCode());
            co.setOrganizationType(organization.getOrganizationType());
            co.setGuid(organization.getGuid());
            co.setPublishingStatus(organization.getPublishingStatus());

            co.setOrganizationNames(new OrganizationNameList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationName> organizationNames = organization.getAllOrganizationNames();
            if (organizationNames != null) {
                co.getOrganizationNames().getOrganizationName().addAll(JaxbUtil.convertOrganizationNames(organizationNames));
            }

            co.setOrganizationDescriptions(new OrganizationDescriptionList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription> organizationDescriptions
                    = organization.getAllOrganizationDescriptions();
            if (organizationDescriptions != null) {
                co.getOrganizationDescriptions().getOrganizationDescription().addAll(JaxbUtil.convertOrganizationDescriptions(organizationDescriptions));
            }

            co.setEmails(new EmailList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Email> emails = organization.getAllEmails();
            if (emails != null) {
                co.getEmails().getEmail().addAll(JaxbUtil.convertEmails(emails));
            }

            co.setPhoneNumbers(new PhoneNumberList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PhoneNumber> phoneNumbers = organization.getAllPhoneNumbers();
            if (phoneNumbers != null) {
                co.getPhoneNumbers().getPhoneNumber().addAll(JaxbUtil.convertPhoneNumbers(phoneNumbers));
            }

            co.setWebPages(new WebPageList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.WebPage> webPages = organization.getAllWebPages();
            if (webPages != null) {
                co.getWebPages().getWebPage().addAll(JaxbUtil.convertWebPages(webPages));
            }

            co.setAddresses(new AddressList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Address> addresses = organization.getAllAddresses();
            if (addresses != null) {
                co.getAddresses().getAddress().addAll(JaxbUtil.convertAddresses(addresses));
            }

            converted.add(co);
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param companies Iterable of Company entities
     * @return Collection of Companies (JAXB generated)
     */
    public Collection<Company> convertCompanies(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Company> companies)  {
        List<Company> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Company company: companies) {
            converted.add(JaxbUtil.convertCompany(company));
        }
        return converted;
    }

    /**
     * Convert entities to XML objects
     * @param errorLogEntries Iterable of ErrorLog entities
     * @return Collection of ErrorLog entries (JAXB generated)
     */
    public Collection<ErrorLog> convertErrorLog(Iterable<fi.vrk.xroad.catalog.persistence.entity.ErrorLog> errorLogEntries)  {
        List<ErrorLog> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.ErrorLog errorLog: errorLogEntries) {
            ErrorLog er = new ErrorLog();
            er.setCreated(JaxbUtil.toXmlGregorianCalendar(errorLog.getCreated()));
            er.setMessage(errorLog.getMessage());
            er.setCode(errorLog.getCode());
            er.setXRoadInstance(errorLog.getXRoadInstance());
            er.setMemberClass(errorLog.getMemberClass());
            er.setMemberCode(errorLog.getMemberCode());
            er.setSubsystemCode(errorLog.getSubsystemCode());
            er.setGroupCode(errorLog.getGroupCode());
            er.setServiceCode(errorLog.getServiceCode());
            er.setServiceVersion(errorLog.getServiceVersion());
            er.setSecurityCategoryCode(errorLog.getSecurityCategoryCode());
            er.setServerCode(errorLog.getServerCode());
            converted.add(er);
        }
        return converted;
    }
}
