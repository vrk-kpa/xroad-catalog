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
package fi.vrk.xroad.catalog.lister;

import fi.vrk.xroad.catalog.lister.util.JaxbOrganizationUtil;
import fi.vrk.xroad.catalog.lister.util.JaxbServiceUtil;
import fi.vrk.xroad.xroad_catalog_lister.AddressList;
import fi.vrk.xroad.xroad_catalog_lister.Company;
import fi.vrk.xroad.xroad_catalog_lister.EmailList;
import fi.vrk.xroad.xroad_catalog_lister.Organization;
import fi.vrk.xroad.xroad_catalog_lister.OrganizationDescriptionList;
import fi.vrk.xroad.xroad_catalog_lister.OrganizationNameList;
import fi.vrk.xroad.xroad_catalog_lister.PhoneNumberList;
import fi.vrk.xroad.xroad_catalog_lister.WebPageList;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JaxbOrganizationConverter implements JaxbOrganizationConversion {


    @Override
    public Collection<Organization> convertOrganizations(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Organization> organizations)  {
        List<Organization> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Organization organization: organizations) {
            Organization co = new Organization();
            co.setChanged(JaxbServiceUtil.toXmlGregorianCalendar(organization.getStatusInfo().getChanged()));
            co.setCreated(JaxbServiceUtil.toXmlGregorianCalendar(organization.getStatusInfo().getCreated()));
            co.setFetched(JaxbServiceUtil.toXmlGregorianCalendar(organization.getStatusInfo().getFetched()));
            co.setRemoved(JaxbServiceUtil.toXmlGregorianCalendar(organization.getStatusInfo().getRemoved()));
            co.setBusinessCode(organization.getBusinessCode());
            co.setOrganizationType(organization.getOrganizationType());
            co.setGuid(organization.getGuid());
            co.setPublishingStatus(organization.getPublishingStatus());

            co.setOrganizationNames(new OrganizationNameList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationName> organizationNames = organization.getAllOrganizationNames();
            if (organizationNames != null) {
                co.getOrganizationNames().getOrganizationName().addAll(JaxbOrganizationUtil.convertOrganizationNames(organizationNames));
            }

            co.setOrganizationDescriptions(new OrganizationDescriptionList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.OrganizationDescription> organizationDescriptions
                    = organization.getAllOrganizationDescriptions();
            if (organizationDescriptions != null) {
                co.getOrganizationDescriptions().getOrganizationDescription().addAll(JaxbOrganizationUtil.convertOrganizationDescriptions(organizationDescriptions));
            }

            co.setEmails(new EmailList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Email> emails = organization.getAllEmails();
            if (emails != null) {
                co.getEmails().getEmail().addAll(JaxbOrganizationUtil.convertEmails(emails));
            }

            co.setPhoneNumbers(new PhoneNumberList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.PhoneNumber> phoneNumbers = organization.getAllPhoneNumbers();
            if (phoneNumbers != null) {
                co.getPhoneNumbers().getPhoneNumber().addAll(JaxbOrganizationUtil.convertPhoneNumbers(phoneNumbers));
            }

            co.setWebPages(new WebPageList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.WebPage> webPages = organization.getAllWebPages();
            if (webPages != null) {
                co.getWebPages().getWebPage().addAll(JaxbOrganizationUtil.convertWebPages(webPages));
            }

            co.setAddresses(new AddressList());
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Address> addresses = organization.getAllAddresses();
            if (addresses != null) {
                co.getAddresses().getAddress().addAll(JaxbOrganizationUtil.convertAddresses(addresses));
            }

            converted.add(co);
        }
        return converted;
    }

    @Override
    public Collection<Company> convertCompanies(
            Iterable<fi.vrk.xroad.catalog.persistence.entity.Company> companies)  {
        List<Company> converted = new ArrayList<>();
        for (fi.vrk.xroad.catalog.persistence.entity.Company company: companies) {
            converted.add(JaxbOrganizationUtil.convertCompany(company));
        }
        return converted;
    }

}
