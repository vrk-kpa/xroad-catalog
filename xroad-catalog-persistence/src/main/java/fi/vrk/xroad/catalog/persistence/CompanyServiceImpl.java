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
 * Implementation for companyservice CRUD
 */
@Slf4j
@Component("companyService")
@Transactional
public class CompanyServiceImpl implements CompanyService {

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
    public Iterable<Company> getCompanies(String businessId) {
        return companyRepository.findAllByBusinessId(businessId);
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
            company.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            businessName.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            businessAuxiliaryName.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            businessAddress.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            businessIdChange.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            businessLine.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            companyForm.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            contactDetail.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            language.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            liquidation.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            registeredEntry.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
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
            registeredOffice.setStatusInfo(new StatusInfo(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null));
        }
        return registeredOffice;
    }
}
