/**
 * The MIT License
 *
 * Copyright (c) 2023- Nordic Institute for Interoperability Solutions (NIIS) Copyright (c) 2016-2022 Finnish Digital Agency
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence;

import fi.vrk.xroad.catalog.persistence.entity.*;

/**
 * CRUD methods for company related objects. no business logic (e.g. hash calculation),
 * just persistence-related logic.
 * Company entities have time stamps created, updated and deleted.
 */
public interface CompanyService {

    /**
     * @param businessId Only interested in companies with this businessId value
     * @return Iterable of Company entities
     */
    Iterable<Company> getCompanies(String businessId);

    /**
     * Saves given company data. The company can either be a new one, or an update to an existing one.
     * Updates "changed" field based on whether data is different compared to last time.
     * @return saved company
     * @param company the actual company
     */
    Company saveCompany(Company company);

    /**
     * Saves given BusinessName data.
     * @param businessName the BusinessName
     */
    void saveBusinessName(BusinessName businessName);

    /**
     * Saves given BusinessAuxiliaryName data.
     * @param businessAuxiliaryName the BusinessAuxiliaryName
     */
    void saveBusinessAuxiliaryName(BusinessAuxiliaryName businessAuxiliaryName);

    /**
     * Saves given BusinessAddress data.
     * @param businessAddress the BusinessAddress
     */
    void saveBusinessAddress(BusinessAddress businessAddress);

    /**
     * Saves given BusinessIdChange data.
     * @param businessIdChange the BusinessIdChange
     */
    void saveBusinessIdChange(BusinessIdChange businessIdChange);

    /**
     * Saves given BusinessLine data.
     * @param businessLine the BusinessLine
     */
    void saveBusinessLine(BusinessLine businessLine);

    /**
     * Saves given CompanyForm data.
     * @param companyForm the CompanyForm
     */
    void saveCompanyForm(CompanyForm companyForm);

    /**
     * Saves given ContactDetail data.
     * @param contactDetail the ContactDetail
     */
    void saveContactDetail(ContactDetail contactDetail);

    /**
     * Saves given Language data.
     * @param language the Language
     */
    void saveLanguage(Language language);

    /**
     * Saves given Liquidation data.
     * @param liquidation the Liquidation
     */
    void saveLiquidation(Liquidation liquidation);

    /**
     * Saves given RegisteredEntry data.
     * @param registeredEntry the RegisteredEntry
     */
    void saveRegisteredEntry(RegisteredEntry registeredEntry);

    /**
     * Saves given RegisteredOffice data.
     * @param registeredOffice the RegisteredOffice
     */
    void saveRegisteredOffice(RegisteredOffice registeredOffice);

}
