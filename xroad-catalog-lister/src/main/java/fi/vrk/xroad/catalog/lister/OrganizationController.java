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

import fi.vrk.xroad.catalog.lister.util.JaxbServiceUtil;
import fi.vrk.xroad.catalog.lister.util.OrganizationUtil;
import fi.vrk.xroad.catalog.lister.util.ServiceUtil;
import fi.vrk.xroad.catalog.persistence.CompanyService;
import fi.vrk.xroad.catalog.persistence.OrganizationService;
import fi.vrk.xroad.catalog.persistence.dto.CompanyData;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationChanged;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationDTO;
import fi.vrk.xroad.catalog.persistence.dto.OrganizationData;
import fi.vrk.xroad.catalog.persistence.entity.Company;
import fi.vrk.xroad.catalog.persistence.entity.Organization;
import fi.vrk.xroad.xroad_catalog_lister.ChangedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@PropertySource("classpath:lister.properties")
@Profile("fi")
public class OrganizationController implements OrganizationOperations {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private JaxbCompanyService jaxbCompanyService;

    @Autowired
    private JaxbOrganizationService jaxbOrganizationService;


    @Override
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable String businessCode) {
        OrganizationDTO organizationDTO = null;
        Iterable<Company> companies = companyService.getCompanies(businessCode);
        if (companies.iterator().hasNext()) {
            organizationDTO = OrganizationDTO.builder().companyData(CompanyData.builder()
                    .businessCode(businessCode)
                    .changed(companies.iterator().next().getStatusInfo().getChanged())
                    .created(companies.iterator().next().getStatusInfo().getCreated())
                    .fetched(companies.iterator().next().getStatusInfo().getFetched())
                    .removed(companies.iterator().next().getStatusInfo().getRemoved())
                    .registrationDate(companies.iterator().next().getRegistrationDate())
                    .companyForm(companies.iterator().next().getCompanyForm())
                    .detailsUri(companies.iterator().next().getDetailsUri())
                    .name(companies.iterator().next().getName())
                    .businessAddresses(OrganizationUtil.getBusinessAddressData(companies))
                    .businessAuxiliaryNames(OrganizationUtil.getBusinessAuxiliaryNameData(companies))
                    .businessIdChanges(OrganizationUtil.getBusinessIdChangeData(companies))
                    .businessLines(OrganizationUtil.getBusinessLineData(companies))
                    .businessNames(OrganizationUtil.getBusinessNameData(companies))
                    .companyForms(OrganizationUtil.getCompanyFormData(companies))
                    .contactDetails(OrganizationUtil.getContactDetailData(companies))
                    .languages(OrganizationUtil.getLanguageData(companies))
                    .liquidations(OrganizationUtil.getLiquidationData(companies))
                    .registeredEntries(OrganizationUtil.getRegisteredEntryData(companies))
                    .registeredOffices(OrganizationUtil.getRegisteredOfficeData(companies))
                    .build())
                    .organizationData(null).build();
        } else {
            Iterable<Organization> organizations = organizationService.getOrganizations(businessCode);
            if (organizations.iterator().hasNext()) {
                organizationDTO = OrganizationDTO.builder().organizationData(OrganizationData.builder()
                        .businessCode(businessCode)
                        .changed(organizations.iterator().next().getStatusInfo().getChanged())
                        .created(organizations.iterator().next().getStatusInfo().getCreated())
                        .fetched(organizations.iterator().next().getStatusInfo().getFetched())
                        .removed(organizations.iterator().next().getStatusInfo().getRemoved())
                        .guid(organizations.iterator().next().getGuid())
                        .organizationType(organizations.iterator().next().getOrganizationType())
                        .publishingStatus(organizations.iterator().next().getPublishingStatus())
                        .organizationNames(OrganizationUtil.getOrganizationNameData(organizations))
                        .organizationDescriptions(OrganizationUtil.getOrganizationDescriptionData(organizations))
                        .addresses(OrganizationUtil.getAddressData(organizations))
                        .emails(OrganizationUtil.getEmailData(organizations))
                        .webPages(OrganizationUtil.getWebpageData(organizations))
                        .phoneNumbers(OrganizationUtil.getPhoneNumberData(organizations))
                        .build()).companyData(null).build();
            }
        }

        return organizationDTO != null ? ResponseEntity.ok(organizationDTO) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<OrganizationChanged> getOrganizationChanges(@PathVariable String businessCode,
                                                    @RequestParam(required = false) String startDate,
                                                    @RequestParam(required = false) String endDate) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = ServiceUtil.convertStringToLocalDateTime(startDate);
            endDateTime = ServiceUtil.convertStringToLocalDateTime(endDate);
        } catch(CatalogListerRuntimeException e) {
            return ResponseEntity.badRequest().build();
        }

        OrganizationChanged organizationChanged = null;
        Iterable<ChangedValue> changedValues = null;
        Iterable<Company> companies = companyService.getCompanies(businessCode);
        if (companies.iterator().hasNext()) {
            changedValues = jaxbCompanyService.getChangedCompanyValues(companies.iterator().next().getBusinessId(),
                    JaxbServiceUtil.toXmlGregorianCalendar(startDateTime),
                    JaxbServiceUtil.toXmlGregorianCalendar(endDateTime));
        } else {
            Iterable<Organization> organizations = organizationService.getOrganizations(businessCode);
            if (organizations.iterator().hasNext()) {
                changedValues = jaxbOrganizationService.getChangedOrganizationValues(organizations.iterator().next().getGuid(),
                        JaxbServiceUtil.toXmlGregorianCalendar(startDateTime),
                        JaxbServiceUtil.toXmlGregorianCalendar(endDateTime));
            }
        }
        if (changedValues == null) {
            return ResponseEntity.ok(OrganizationChanged.builder().changed(false).changedValueList(new ArrayList<>()).build());
        }
        if (changedValues.iterator().hasNext()) {
            List<fi.vrk.xroad.catalog.persistence.dto.ChangedValue> changedValueList = new ArrayList<>();
            changedValues.forEach(changedValue -> changedValueList.add(fi.vrk.xroad.catalog.persistence.dto.ChangedValue.builder()
                    .name(changedValue.getName())
                    .build()));
            organizationChanged = OrganizationChanged.builder().changed(true).changedValueList(changedValueList).build();
        } else {
            organizationChanged = OrganizationChanged.builder().changed(false).changedValueList(new ArrayList<>()).build();
        }
        return ResponseEntity.ok(organizationChanged);
    }

}
